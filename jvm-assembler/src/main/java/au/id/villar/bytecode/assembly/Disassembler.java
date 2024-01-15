package au.id.villar.bytecode.assembly;

import au.id.villar.bytecode.AccessFlags;
import au.id.villar.bytecode.ClassFile;
import au.id.villar.bytecode.Field;
import au.id.villar.bytecode.Method;
import au.id.villar.bytecode.attribute.Attribute;
import au.id.villar.bytecode.attribute.CodeAttribute;
import au.id.villar.bytecode.compiler.CodeHandler;
import au.id.villar.bytecode.compiler.CodeParser;
import au.id.villar.bytecode.constant.ClassConstant;
import au.id.villar.bytecode.constant.DoubleConstant;
import au.id.villar.bytecode.constant.DynamicConstant;
import au.id.villar.bytecode.constant.FieldRefConstant;
import au.id.villar.bytecode.constant.FloatConstant;
import au.id.villar.bytecode.constant.IntegerConstant;
import au.id.villar.bytecode.constant.InterfaceMethodRefConstant;
import au.id.villar.bytecode.constant.InvokeDynamicConstant;
import au.id.villar.bytecode.constant.LongConstant;
import au.id.villar.bytecode.constant.MethodHandleConstant;
import au.id.villar.bytecode.constant.MethodRefConstant;
import au.id.villar.bytecode.constant.MethodTypeConstant;
import au.id.villar.bytecode.constant.ModuleConstant;
import au.id.villar.bytecode.constant.NameAndTypeConstant;
import au.id.villar.bytecode.constant.PackageConstant;
import au.id.villar.bytecode.constant.Constant;
import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.constant.StringConstant;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Disassembler {

    private static final String INDENTATION = "    ";
    private static final String OBJECT_CLASS_NAME = "java/lang/Object";

    private final ClassFile classFile;
    private final Writer writer;

    private Disassembler(ClassFile classFile, Writer writer) {
        this.classFile = classFile;
        this.writer = writer;
    }

    public static void toAssembly(ClassFile classFile, Writer writer) throws IOException {
        Disassembler disassembler = new Disassembler(classFile, writer);
        disassembler.toAssembly();
    }

    private void toAssembly() throws IOException {

        try {
            writeClassData(classFile);

            for (Field field : classFile.getFields()) {
                writeField(field);
            }

            for (Method method : classFile.getMethods()) {
                writeMethod(method, classFile.getConstants());
            }
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }

    }

    private void writeClassData(ClassFile classFile) {
        write("CLASS \"%s\"%n", classFile.getName());
        write("%s.version %d %d%n", INDENTATION, classFile.getMayor(), classFile.getMinor());
        write("%s.access", INDENTATION);
        writeFlags(classFile.getAccessFlags());
        write("%n");
        if (!OBJECT_CLASS_NAME.equals(classFile.getSuperClass())) {
            write("%s.super \"%s\"%n", INDENTATION, classFile.getSuperClass());
        }
        for (Integer iface : classFile.getInterfaceIndexes()) {
            write("%s.implements \"%s\"%n", INDENTATION, classFile.getConstants().getClassName(iface));
        }
    }

    private void writeField(Field field) {
        write("%nFIELD \"%s\"%n", classFile.getConstants().getStringFromUtf8(field.getNameIndex()));
        write("%s.access", INDENTATION);
        writeFlags(field.getAccessFlags());
        write("%n");
        write("%s.descriptor \"%s\"%n", INDENTATION,
                classFile.getConstants().getStringFromUtf8(field.getDescriptorIndex()));
    }

    private void writeMethod(Method method, ConstantPool constants) throws IOException {
        write("%nMETHOD \"%s\"%n", classFile.getConstants().getStringFromUtf8(method.getNameIndex()));
        write("%s.access", INDENTATION);
        writeFlags(method.getAccessFlags());
        write("%n");
        write("%s.descriptor \"%s\"%n", INDENTATION,
                classFile.getConstants().getStringFromUtf8(method.getDescriptorIndex()));

        for (Attribute attribute : method.getAttributes()) {
            if (attribute instanceof CodeAttribute codeAttribute) {
                ConstantsAndLabels constantsAndLabels = calculateBranchLabelsAndConstants(codeAttribute, constants);
                writeAssembly(codeAttribute, constantsAndLabels);
            }
        }
    }

    private ConstantsAndLabels calculateBranchLabelsAndConstants(CodeAttribute codeAttribute,
            ConstantPool constants) throws IOException {
        final ConstantsAndLabels constantsAndLabels = new ConstantsAndLabels(new HashMap<>(), new HashMap<>());
        try (InputStream codeStream = codeAttribute.createCodeStream()) {
            CodeParser.parse(codeStream, codeAttribute.getCodeLength(), new CodeHandler() {

                int tagNumber = 1;

                @Override
                public void operationTableswitch(int offset, int opcode, int defaultOffset, int low, int high,
                        Iterator<Integer> offsets) {
                    setLabel(offset + defaultOffset);
                    while (offsets.hasNext()) {
                        setLabel(offset + offsets.next());
                    }
                }

                @Override
                public void operationLookupswitch(int offset, int opcode, int defaultOffset, int numPairs,
                        Iterator<Map.Entry<Integer, Integer>> offsets) {
                    setLabel(offset + defaultOffset);
                    while (offsets.hasNext()) {
                        setLabel(offset + offsets.next().getValue());
                    }
                }

                @Override
                public void operationBranching(int offset, int opcode, short branchingOffset) {
                    setLabel(offset + branchingOffset);
                }

                @Override
                public void operationLongBranching(int offset, int opcode, int branchingOffset) {
                    setLabel(offset + branchingOffset);
                }

                @Override
                public void operationConstantPoolIndex(int offset, int opcode, int poolIndex) {
                    registerConstant(poolIndex);
                };

                @Override
                public void operationMultiANewArray(int offset, int opcode, int poolIndex, int dimensions) {
                    registerConstant(poolIndex);
                };

                @Override
                public void operationInvokeDynamic(int offset, int opcode, int poolIndex) {
                    registerConstant(poolIndex);
                };

                @Override
                public void operationInvokeInterface(int offset, int opcode, int poolIndex, int count) {
                    registerConstant(poolIndex);
                };

                private void registerConstant(int constantIndex) {
                    constantsAndLabels.constants.computeIfAbsent(constantIndex, constants::get);
                }

                private void setLabel(int offset) {
                    constantsAndLabels.labels.computeIfAbsent(offset, o -> "LABEL_" + (tagNumber++));
                }
            });
        }
        return constantsAndLabels;
    }

    private void writeAssembly(CodeAttribute codeAttribute, ConstantsAndLabels constantsAndLabels)
            throws IOException {

        Map<Integer, Constant> constants = constantsAndLabels.constants;
        Map<Integer, String> labels = constantsAndLabels.labels;
        List<Map.Entry<Integer, Constant>> constantEntries = constants.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .toList();

        write("%n%s.constants%n", INDENTATION);
        for (Map.Entry<Integer, Constant> constantEntry : constantEntries) {
            write("%s", INDENTATION);
            writeConstant(constantEntry.getKey(), constantEntry.getValue());
            write("%n");
        }
        write("%n%s.code%n", INDENTATION);
        try (InputStream codeStream = codeAttribute.createCodeStream()) {
            CodeParser.parse(codeStream, codeAttribute.getCodeLength(), new CodeHandler() {

                @Override
                public void operation(int offset, int opcode) {
                    labelAndIndentation(false, offset, opcode);
                    write("%n");
                }

                @Override
                public void operationImmediateByte(int offset, int opcode, byte operand) {
                    labelAndIndentation(false, offset, opcode);
                    write(" %d%n", operand);
                }

                @Override
                public void operationSignedShortOperand(int offset, int opcode, short operand) {
                    labelAndIndentation(false, offset, opcode);
                    write(" %d%n", operand);
                }

                @Override
                public void operationConstantPoolIndex(int offset, int opcode, int poolIndex) {
                    labelAndIndentation(false, offset, opcode);
                    write(" #c%d%n", poolIndex);
                }

                @Override
                public void operationByteIndex(int offset, int opcode, int index) {
                    labelAndIndentation(false, offset, opcode);
                    write(" %d%n", index);
                }

                @Override
                public void operationArrayType(int offset, int opcode, ArrayType arrayType) {
                    labelAndIndentation(false, offset, opcode);
                    write(" %s%n", arrayType.name());
                }

                @Override
                public void operationIntIncrement(int offset, int opcode, int index, int value) {
                    labelAndIndentation(false, offset, opcode);
                    write(" %d %d%n", index, value);
                }

                @Override
                public void operationTableswitch(int offset, int opcode, int defaultOffset, int low, int high,
                        Iterator<Integer> offsets) {
                    labelAndIndentation(false, offset, opcode);
                    write("%n");
                    while (offsets.hasNext()) {
                        String label = labels.getOrDefault(offset + offsets.next(), "[ERROR: Label Not Found]");
                        write("%s    %d, %s%n", INDENTATION, low++, label);
                    }
                    write("%send%n", INDENTATION);
                }

                @Override
                public void operationLookupswitch(int offset, int opcode, int defaultOffset, int numPairs,
                        Iterator<Map.Entry<Integer, Integer>> offsets) {
                    labelAndIndentation(false, offset, opcode);
                    write("%n");
                    while (offsets.hasNext()) {
                        Map.Entry<Integer, Integer> next = offsets.next();
                        String label = labels.getOrDefault(offset + next.getValue(), "[ERROR: Label Not Found]");
                        write("%s    %d, %s%n", INDENTATION, next.getKey(), label);
                    }
                    write("%send%n", INDENTATION);
                }

                @Override
                public void operationWideByteIndex(int offset, int opcode, int index) {
                    labelAndIndentation(true, offset, opcode);
                    write(" %d%n", index);
                }

                @Override
                public void operationWideIntIncrement(int offset, int opcode, int index, int value) {
                    labelAndIndentation(true, offset, opcode);
                    write(" %d %d%n", index, value);
                }

                @Override
                public void operationMultiANewArray(int offset, int opcode, int poolIndex, int dimensions) {
                    labelAndIndentation(false, offset, opcode);
                    write(" #c%d %d%n", poolIndex, dimensions);
                }

                @Override
                public void operationBranching(int offset, int opcode, short branchingOffset) {
                    labelAndIndentation(false, offset, opcode);
                    String label = labels.getOrDefault(offset + branchingOffset, "[ERROR: Label Not Found]");
                    write(" %s%n", label);
                }

                @Override
                public void operationLongBranching(int offset, int opcode, int branchingOffset) {
                    labelAndIndentation(false, offset, opcode);
                    String label = labels.getOrDefault(offset + branchingOffset, "[ERROR: Label Not Found]");
                    write(" %s%n", label);
                }

                @Override
                public void operationInvokeDynamic(int offset, int opcode, int poolIndex) {
                    labelAndIndentation(false, offset, opcode);
                    write(" #c%d%n", poolIndex);
                }

                @Override
                public void operationInvokeInterface(int offset, int opcode, int poolIndex, int count) {
                    labelAndIndentation(false, offset, opcode);
                    write(" #c%d %d%n", poolIndex, count);
                }

                private void labelAndIndentation(boolean wide, int offset, int opcode) {
                    String label = labels.get(offset);
                    if (label != null) {
                        write("%s:%n", label);
                    }
                    write(INDENTATION);
                    if (wide) {
                        write("wide ");
                    }
                    write("%s", toMnemonic(opcode));
                }
            });
        }
    }

    private void writeConstant(Integer index, Constant constant) {
        ConstantPool constantPool = classFile.getConstants();
        if (constant instanceof IntegerConstant c) {
            write("d_int     c%d %d", index, c.getValue());
        } else if (constant instanceof LongConstant c) {
            write("d_long    c%d %d", index, c.getValue());
        } else if (constant instanceof FloatConstant c) {
            write("d_float   c%d %f", index, c.getValue());
        } else if (constant instanceof DoubleConstant c) {
            write("d_double  c%d %f", index, c.getValue());
        } else if (constant instanceof StringConstant c) {
            write("d_string  c%d \"%s\"", index, utf8ConstantWithEscapedChars(c.getStringIndex()));
        } else if (constant instanceof ClassConstant c) {
            write("d_class   c%d \"%s\"", index, utf8ConstantWithEscapedChars(c.getNameIndex()));
        } else if (constant instanceof PackageConstant c) {
            write("d_package c%d \"%s\"", index, utf8ConstantWithEscapedChars(c.getNameIndex()));
        } else if (constant instanceof ModuleConstant c) {
            write("d_module  c%d \"%s\"", index, utf8ConstantWithEscapedChars(c.getNameIndex()));
        } else if (constant instanceof MethodTypeConstant c) {
            write("d_method_type c%d \"%s\"", index, utf8ConstantWithEscapedChars(c.getDescriptorIndex()));
        } else if (constant instanceof FieldRefConstant c) {
            NameAndTypeConstant nameAndType = constantPool
                    .get(c.getNameAndTypeIndex(), NameAndTypeConstant.class);
            String className = constantPool.getClassName(c.getClassIndex());
            String name = constantPool.getStringFromUtf8(nameAndType.getNameIndex());
            String descriptor = constantPool.getStringFromUtf8(nameAndType.getDescriptorIndex());
            write("d_field   c%d \"%s\" \"%s\" \"%s\"", index, name, descriptor, className);
        } else if (constant instanceof MethodRefConstant c) {
            NameAndTypeConstant nameAndType = constantPool
                    .get(c.getNameAndTypeIndex(), NameAndTypeConstant.class);
            String className = constantPool.getClassName(c.getClassIndex());
            String name = constantPool.getStringFromUtf8(nameAndType.getNameIndex());
            String descriptor = constantPool.getStringFromUtf8(nameAndType.getDescriptorIndex());
            write("d_method  c%d \"%s\" \"%s\" \"%s\"", index, name, descriptor, className);
        } else if (constant instanceof InterfaceMethodRefConstant c) {
            NameAndTypeConstant nameAndType = constantPool
                    .get(c.getNameAndTypeIndex(), NameAndTypeConstant.class);
            String className = constantPool.getClassName(c.getClassIndex());
            String name = constantPool.getStringFromUtf8(nameAndType.getNameIndex());
            String descriptor = constantPool.getStringFromUtf8(nameAndType.getDescriptorIndex());
            write("d_imethod c%d \"%s\" \"%s\" \"%s\"", index, name, descriptor, className);
        } else if (constant instanceof MethodHandleConstant c) {
            write("d_method_handle c%d", index); // TODO implement this if needed or remove it
        } else if (constant instanceof DynamicConstant c) {
            write("d_dynamic c%d", index); // TODO implement this if needed or remove it
        } else if (constant instanceof InvokeDynamicConstant c) {
            write("d_invoke_dynamic c%d PENDING", index); // TODO implement this if needed or remove it
        } else {
            throw new IllegalArgumentException("Unknown constant type: " + constant.getClass().getName());
        }
    }

    public String utf8ConstantWithEscapedChars(Integer utf8Index) {
        String string = classFile.getConstants().getStringFromUtf8(utf8Index);
        StringBuilder formatted = new StringBuilder(string);
        replace(formatted, "\\", "\\\\");
        replace(formatted, "\b", "\\b");
        replace(formatted, "\t", "\\t");
        replace(formatted, "\n", "\\n");
        replace(formatted, "\f", "\\f");
        replace(formatted, "\r", "\\r");
        replace(formatted, "\"", "\\\"");
        return formatted.toString();
    }

    private void replace(StringBuilder text, String oldText, String newText) {
        int fromIndex = 0;
        while ((fromIndex = text.indexOf(oldText, fromIndex)) >= 0) {
            text.replace(fromIndex, oldText.length() + fromIndex, newText);
            fromIndex += newText.length();
        }
    }

    private void writeFlags(AccessFlags flags) {
        if(flags.isPublic()) write(" public");
        if(flags.isPrivate()) write(" private");
        if(flags.isProtected()) write(" protected");
        if(flags.isStatic()) write(" static");
        if(flags.isFinal()) write(" final");
        if(flags.isSuper()) write(" super");
        if(flags.isVolatile()) write(" volatile");
        if(flags.isTransient()) write(" transient");
        if(flags.isNative()) write(" Native");
        if(flags.isInterface()) write(" interface");
        if(flags.isAbstract()) write(" abstract");
        if(flags.isStrict()) write(" strict");
        if(flags.isSynthetic()) write(" synthetic");
        if(flags.isAnnotation()) write(" annotation");
        if(flags.isEnum()) write(" enum");
        if(flags.isModule()) write(" module");
        if(flags.isSynchronized()) write(" synchronized");
        if(flags.isBridge()) write(" bridge");
        if(flags.isVargars()) write(" vargars");
        if(flags.isMandated()) write(" mandated");
    }

    private String toMnemonic(int opCode) {

        return switch (opCode) {
            case 0x00 -> "nop";
            case 0x01 -> "aconst_null";
            case 0x02 -> "iconst_m1";
            case 0x03 -> "iconst_0";
            case 0x04 -> "iconst_1";
            case 0x05 -> "iconst_2";
            case 0x06 -> "iconst_3";
            case 0x07 -> "iconst_4";
            case 0x08 -> "iconst_5";
            case 0x09 -> "lconst_0";
            case 0x0A -> "lconst_1";
            case 0x0B -> "fconst_0";
            case 0x0C -> "fconst_1";
            case 0x0D -> "fconst_2";
            case 0x0E -> "dconst_0";
            case 0x0F -> "dconst_1";
            case 0x10 -> "bipush";
            case 0x11 -> "sipush";
            case 0x12 -> "ldc";
            case 0x13 -> "ldc_w";
            case 0x14 -> "ldc2_w";
            case 0x15 -> "iload";
            case 0x16 -> "lload";
            case 0x17 -> "fload";
            case 0x18 -> "dload";
            case 0x19 -> "aload";
            case 0x1A -> "iload_0";
            case 0x1B -> "iload_1";
            case 0x1C -> "iload_2";
            case 0x1D -> "iload_3";
            case 0x1E -> "lload_0";
            case 0x1F -> "lload_1";
            case 0x20 -> "lload_2";
            case 0x21 -> "lload_3";
            case 0x22 -> "fload_0";
            case 0x23 -> "fload_1";
            case 0x24 -> "fload_2";
            case 0x25 -> "fload_3";
            case 0x26 -> "dload_0";
            case 0x27 -> "dload_1";
            case 0x28 -> "dload_2";
            case 0x29 -> "dload_3";
            case 0x2A -> "aload_0";
            case 0x2B -> "aload_1";
            case 0x2C -> "aload_2";
            case 0x2D -> "aload_3";
            case 0x2E -> "iaload";
            case 0x2F -> "laload";
            case 0x30 -> "faload";
            case 0x31 -> "daload";
            case 0x32 -> "aaload";
            case 0x33 -> "baload";
            case 0x34 -> "caload";
            case 0x35 -> "saload";
            case 0x36 -> "istore";
            case 0x37 -> "lstore";
            case 0x38 -> "fstore";
            case 0x39 -> "dstore";
            case 0x3A -> "astore";
            case 0x3B -> "istore_0";
            case 0x3C -> "istore_1";
            case 0x3D -> "istore_2";
            case 0x3E -> "istore_3";
            case 0x3F -> "lstore_0";
            case 0x40 -> "lstore_1";
            case 0x41 -> "lstore_2";
            case 0x42 -> "lstore_3";
            case 0x43 -> "fstore_0";
            case 0x44 -> "fstore_1";
            case 0x45 -> "fstore_2";
            case 0x46 -> "fstore_3";
            case 0x47 -> "dstore_0";
            case 0x48 -> "dstore_1";
            case 0x49 -> "dstore_2";
            case 0x4A -> "dstore_3";
            case 0x4B -> "astore_0";
            case 0x4C -> "astore_1";
            case 0x4D -> "astore_2";
            case 0x4E -> "astore_3";
            case 0x4F -> "iastore";
            case 0x50 -> "lastore";
            case 0x51 -> "fastore";
            case 0x52 -> "dastore";
            case 0x53 -> "aastore";
            case 0x54 -> "bastore";
            case 0x55 -> "castore";
            case 0x56 -> "sastore";
            case 0x57 -> "pop";
            case 0x58 -> "pop2";
            case 0x59 -> "dup";
            case 0x5A -> "dup_x1";
            case 0x5B -> "dup_x2";
            case 0x5C -> "dup2";
            case 0x5D -> "dup2_x1";
            case 0x5E -> "dup2_x2";
            case 0x5F -> "swap";
            case 0x60 -> "iadd";
            case 0x61 -> "ladd";
            case 0x62 -> "fadd";
            case 0x63 -> "dadd";
            case 0x64 -> "isub";
            case 0x65 -> "lsub";
            case 0x66 -> "fsub";
            case 0x67 -> "dsub";
            case 0x68 -> "imul";
            case 0x69 -> "lmul";
            case 0x6A -> "fmul";
            case 0x6B -> "dmul";
            case 0x6C -> "idiv";
            case 0x6D -> "ldiv";
            case 0x6E -> "fdiv";
            case 0x6F -> "ddiv";
            case 0x70 -> "irem";
            case 0x71 -> "lrem";
            case 0x72 -> "frem";
            case 0x73 -> "drem";
            case 0x74 -> "ineg";
            case 0x75 -> "lneg";
            case 0x76 -> "fneg";
            case 0x77 -> "dneg";
            case 0x78 -> "ishl";
            case 0x79 -> "lshl";
            case 0x7A -> "ishr";
            case 0x7B -> "lshr";
            case 0x7C -> "iushr";
            case 0x7D -> "lushr";
            case 0x7E -> "iand";
            case 0x7F -> "land";
            case 0x80 -> "ior";
            case 0x81 -> "lor";
            case 0x82 -> "ixor";
            case 0x83 -> "lxor";
            case 0x84 -> "iinc";
            case 0x85 -> "i2l";
            case 0x86 -> "i2f";
            case 0x87 -> "i2d";
            case 0x88 -> "l2i";
            case 0x89 -> "l2f";
            case 0x8A -> "l2d";
            case 0x8B -> "f2i";
            case 0x8C -> "f2l";
            case 0x8D -> "f2d";
            case 0x8E -> "d2i";
            case 0x8F -> "d2l";
            case 0x90 -> "d2f";
            case 0x91 -> "i2b";
            case 0x92 -> "i2c";
            case 0x93 -> "i2s";
            case 0x94 -> "lcmp";
            case 0x95 -> "fcmpl";
            case 0x96 -> "fcmpg";
            case 0x97 -> "dcmpl";
            case 0x98 -> "dcmpg";
            case 0x99 -> "ifeq";
            case 0x9A -> "ifne";
            case 0x9B -> "iflt";
            case 0x9C -> "ifge";
            case 0x9D -> "ifgt";
            case 0x9E -> "ifle";
            case 0x9F -> "if_icmpeq";
            case 0xA0 -> "if_icmpne";
            case 0xA1 -> "if_icmplt";
            case 0xA2 -> "if_icmpge";
            case 0xA3 -> "if_icmpgt";
            case 0xA4 -> "if_icmple";
            case 0xA5 -> "if_acmpeq";
            case 0xA6 -> "if_acmpne";
            case 0xA7 -> "goto";
            case 0xA8 -> "jsr";
            case 0xA9 -> "ret";
            case 0xAA -> "tableswitch";
            case 0xAB -> "lookupswitch";
            case 0xAC -> "ireturn";
            case 0xAD -> "lreturn";
            case 0xAE -> "freturn";
            case 0xAF -> "dreturn";
            case 0xB0 -> "areturn";
            case 0xB1 -> "return";
            case 0xB2 -> "getstatic";
            case 0xB3 -> "putstatic";
            case 0xB4 -> "getfield";
            case 0xB5 -> "putfield";
            case 0xB6 -> "invokevirtual";
            case 0xB7 -> "invokespecial";
            case 0xB8 -> "invokestatic";
            case 0xB9 -> "invokeinterface";
            case 0xBA -> "invokedynamic";
            case 0xBB -> "new";
            case 0xBC -> "newarray";
            case 0xBD -> "anewarray";
            case 0xBE -> "arraylength";
            case 0xBF -> "athrow";
            case 0xC0 -> "checkcast";
            case 0xC1 -> "instanceof";
            case 0xC2 -> "monitorenter";
            case 0xC3 -> "monitorexit";
            case 0xC4 -> "wide";
            case 0xC5 -> "multianewarray";
            case 0xC6 -> "ifnull";
            case 0xC7 -> "ifnonnull";
            case 0xC8 -> "goto_w";
            case 0xC9 -> "jsr_w";

            case 0xCA -> "breakpoint";
            case 0xFE -> "impdep1";
            case 0xFF -> "impdep2";

            default -> throw new IllegalStateException("Unknown opcode: " + opCode);
        };
    }

    private void write(String format, Object ... args) {
        try {
            writer.write(String.format(format, args));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private record ConstantsAndLabels(Map<Integer, Constant> constants, Map<Integer, String> labels) {}
}
