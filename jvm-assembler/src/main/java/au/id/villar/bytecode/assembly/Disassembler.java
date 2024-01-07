package au.id.villar.bytecode.assembly;

import au.id.villar.bytecode.Class;
import au.id.villar.bytecode.Field;
import au.id.villar.bytecode.Method;
import au.id.villar.bytecode.attribute.Attribute;
import au.id.villar.bytecode.attribute.CodeAttribute;
import au.id.villar.bytecode.constant.Constant;
import au.id.villar.bytecode.parser.CodeHandler;
import au.id.villar.bytecode.parser.CodeParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Disassembler {

    public String toAssembly(Class aClass) throws IOException {

        Writer writer = new StringWriter();

        writeClassData(aClass, writer);

        writeConstantSection(aClass.getConstants(), writer);

        for (Field field : aClass.getFields()) {
            writeField(field, writer);
        }

        for (Method method : aClass.getMethods()) {
            writeMethod(method, writer);
        }

        return writer.toString();
    }

    private void writeClassData(Class aClass, Writer writer) throws IOException {
        writer.write(String.format("class \"%s\"%n", aClass.getName()));
        writer.write(String.format("    %s%n", aClass.getAccessFlags().toAssembly(Class.class)));
        if (!aClass.getSuperClass().equals("java/lang/Object")) {
            writer.write(String.format("    super \"%s\"%n", aClass.getSuperClass()));
        }
        for (String iface : aClass.getInterfaces()) {
            writer.write(String.format("    implements \"%s\"%n",iface));
        }
        writer.write(String.format("    version %d %d%n", aClass.getMayor(), aClass.getMinor()));
    }

    private static void writeConstantSection(Map<Integer, Constant> constants, Writer writer) throws IOException {
        writer.write(String.format("constants%n"));

        List<String> definitions = constants.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .map(entry -> entry.getValue().toAssemblyDefinition(String.format("c%05d", entry.getKey())))
                .toList();

        for (String definition : definitions) {
            writer.write(String.format("    %s%n", definition));
        }
    }

    private void writeField(Field field, Writer writer) throws IOException {
        writer.write(String.format("%nfield \"%s\"%n", field.getName()));
        writer.write(String.format("    %s%n", field.getAccessFlags().toAssembly(Field.class)));
        writer.write(String.format("    descriptor \"%s\"%n", field.getDescriptor()));
    }

    private void writeMethod(Method method, Writer writer) throws IOException {
        writer.write(String.format("%nmethod \"%s\"%n", method.getName()));
        writer.write(String.format("    %s%n", method.getAccessFlags().toAssembly(Field.class)));
        writer.write(String.format("    descriptor \"%s\"%n", method.getDescriptor()));

        for (Attribute attribute : method.getAttributes()) {
            if (attribute instanceof CodeAttribute codeAttribute) {
                Map<Integer, String> labels = calculateBranchLabels(codeAttribute);
                writeAssembly(codeAttribute, writer, labels);
            }
        }
    }

    private Map<Integer, String> calculateBranchLabels(CodeAttribute codeAttribute) throws IOException {
        final Map<Integer, String> tags = new HashMap<>();
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
                        Iterator<Map.Entry<Integer, Integer>> offsets) throws IOException {
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

                private void setLabel(int offset) {
                    if (!tags.containsKey(offset)) {
                        tags.put(offset, "LABEL_" + (tagNumber++));
                    }
                }
            });
        }
        return tags;
    }

    private void writeAssembly(CodeAttribute codeAttribute, Writer writer, Map<Integer, String> labels)
            throws IOException {
        writer.write("\n    code\n");
        try (InputStream codeStream = codeAttribute.createCodeStream()) {
            CodeParser.parse(codeStream, codeAttribute.getCodeLength(), new CodeHandler() {

                private static final String INDENTATION = "    ";

                @Override
                public void operation(int offset, int opcode) throws IOException {
                    labelAndIndentation(false, offset, opcode);
                    writer.write(String.format("%n"));
                }

                @Override
                public void operationImmediateByte(int offset, int opcode, byte operand) throws IOException {
                    labelAndIndentation(false, offset, opcode);
                    writer.write(String.format(" %d%n", operand));
                }

                @Override
                public void operationSignedShortOperand(int offset, int opcode, short operand) throws IOException {
                    labelAndIndentation(false, offset, opcode);
                    writer.write(String.format(" %d%n", operand));
                }

                @Override
                public void operationConstantPoolIndex(int offset, int opcode, int poolIndex) throws IOException {
                    labelAndIndentation(false, offset, opcode);
                    writer.write(String.format(" #c%05d%n", poolIndex));
                }

                @Override
                public void operationByteIndex(int offset, int opcode, int index) throws IOException {
                    labelAndIndentation(false, offset, opcode);
                    writer.write(String.format(" %d%n", index));
                }

                @Override
                public void operationArrayType(int offset, int opcode, ArrayType arrayType) throws IOException {
                    labelAndIndentation(false, offset, opcode);
                    writer.write(String.format(" %s%n", arrayType.name()));
                }

                @Override
                public void operationIntIncrement(int offset, int opcode, int index, int value) throws IOException {
                    labelAndIndentation(false, offset, opcode);
                    writer.write(String.format(" %d %d%n", index, value));
                }

                @Override
                public void operationTableswitch(int offset, int opcode, int defaultOffset, int low, int high,
                        Iterator<Integer> offsets) throws IOException {
                    labelAndIndentation(false, offset, opcode);
                    writer.write(String.format("%n"));
                    while (offsets.hasNext()) {
                        String label = labels.getOrDefault(offset + offsets.next(), "[ERROR: Label Not Found]");
                        writer.write(String.format("%s    %d, %s%n", INDENTATION, low++, label));
                    }
                    writer.write(String.format("%send%n", INDENTATION));
                }

                @Override
                public void operationLookupswitch(int offset, int opcode, int defaultOffset, int numPairs,
                        Iterator<Map.Entry<Integer, Integer>> offsets) throws IOException {
                    labelAndIndentation(false, offset, opcode);
                    writer.write(String.format("%n"));
                    while (offsets.hasNext()) {
                        Map.Entry<Integer, Integer> next = offsets.next();
                        String label = labels.getOrDefault(offset + next.getValue(), "[ERROR: Label Not Found]");
                        writer.write(String.format("%s    %d, %s%n", INDENTATION, next.getKey(), label));
                    }
                    writer.write(String.format("%send%n", INDENTATION));
                }

                @Override
                public void operationWideByteIndex(int offset, int opcode, int index) throws IOException {
                    labelAndIndentation(true, offset, opcode);
                    writer.write(String.format(" %d%n", index));
                }

                @Override
                public void operationWideIntIncrement(int offset, int opcode, int index, int value) throws IOException {
                    labelAndIndentation(true, offset, opcode);
                    writer.write(String.format(" %d %d%n", index, value));
                }

                @Override
                public void operationMultiANewArray(int offset, int opcode, int poolIndex, int dimensions)
                        throws IOException {
                    labelAndIndentation(false, offset, opcode);
                    writer.write(String.format(" #c%05d %d%n", poolIndex, dimensions));
                }

                @Override
                public void operationBranching(int offset, int opcode, short branchingOffset) throws IOException {
                    labelAndIndentation(false, offset, opcode);
                    String label = labels.getOrDefault(offset + branchingOffset, "[ERROR: Label Not Found]");
                    writer.write(String.format(" %s%n", label));
                }

                @Override
                public void operationLongBranching(int offset, int opcode, int branchingOffset) throws IOException {
                    labelAndIndentation(false, offset, opcode);
                    String label = labels.getOrDefault(offset + branchingOffset, "[ERROR: Label Not Found]");
                    writer.write(String.format(" %s%n", label));
                }

                @Override
                public void operationInvokeDynamic(int offset, int opcode, int poolIndex) throws IOException {
                    labelAndIndentation(false, offset, opcode);
                    writer.write(String.format(" #c%05d%n", poolIndex));
                }

                @Override
                public void operationInvokeInterface(int offset, int opcode, int poolIndex, int count)
                        throws IOException {
                    labelAndIndentation(false, offset, opcode);
                    writer.write(String.format(" #c%05d %d%n", poolIndex, count));
                }

                private void labelAndIndentation(boolean wide, int offset, int opcode) throws IOException {
                    String label = labels.get(offset);
                    if (label != null) {
                        writer.write(String.format("%s:%n", label));
                    }
                    writer.write(INDENTATION);
                    if (wide) {
                        writer.write("wide ");
                    }
                    writer.write(String.format("%s", toMnemonic(opcode)));
                }
            });
        }
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
}
