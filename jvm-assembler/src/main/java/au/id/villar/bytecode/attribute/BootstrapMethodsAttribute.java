package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.parser.constant.ClassParsingConstant;
import au.id.villar.bytecode.parser.constant.DynamicParsingConstant;
import au.id.villar.bytecode.parser.constant.ParsingConstant;
import au.id.villar.bytecode.parser.constant.DoubleParsingConstant;
import au.id.villar.bytecode.parser.constant.FieldRefParsingConstant;
import au.id.villar.bytecode.parser.constant.FloatParsingConstant;
import au.id.villar.bytecode.parser.constant.IntegerParsingConstant;
import au.id.villar.bytecode.parser.constant.InterfaceMethodRefParsingConstant;
import au.id.villar.bytecode.parser.constant.LongParsingConstant;
import au.id.villar.bytecode.parser.constant.MemberRefParsingConstant;
import au.id.villar.bytecode.parser.constant.MethodHandleParsingConstant;
import au.id.villar.bytecode.constant.MethodHandleReferenceKind;
import au.id.villar.bytecode.parser.constant.MethodRefParsingConstant;
import au.id.villar.bytecode.parser.constant.MethodTypeParsingConstant;
import au.id.villar.bytecode.parser.constant.NameAndTypeParsingConstant;
import au.id.villar.bytecode.parser.constant.ParsingConstantPool;
import au.id.villar.bytecode.parser.constant.StringParsingConstant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BootstrapMethodsAttribute extends ListAttribute<BootstrapMethodsAttribute.BootstrapMethod> {


    public static enum BootstrapArgumentType {
        STRING, CLASS, INTEGER, LONG, FLOAT, DOUBLE, METHOD_TYPE, METHOD_HANDLE
    }

    public static abstract class BootstrapArgument {
        public final BootstrapArgumentType type;

        BootstrapArgument(BootstrapArgumentType type) {
            this.type = type;
        }
    }

    public static class SingleBootstrapArgument extends BootstrapArgument {
        public final String value;

        public SingleBootstrapArgument(BootstrapArgumentType type, String value) {
            super(type);
            this.value = value;
        }

        @Override
        public String toString() {
            return "SingleBootstrapArgument{" +
                    "type=" + type +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    public static class MethodHandleBootstrapArgument extends BootstrapArgument {
        public final MethodHandleReferenceKind referenceKind;
        public final MemberReference memberReference;

        public MethodHandleBootstrapArgument(MethodHandleReferenceKind referenceKind, MemberReference memberReference) {
            super(BootstrapArgumentType.METHOD_HANDLE);
            this.referenceKind = referenceKind;
            this.memberReference = memberReference;
        }

        @Override
        public String toString() {
            return "MethodHandleBootstrapArgument{" +
                    "type=" + type +
                    ", referenceKind=" + referenceKind +
                    ", memberReference=" + memberReference +
                    '}';
        }
    }

    public record BootstrapMethod(MethodHandleReferenceKind referenceKind, MemberReference memberReference,
                                  List<BootstrapArgument> bootstrapArguments) {

        public BootstrapMethod(MethodHandleReferenceKind referenceKind, MemberReference memberReference,
                List<BootstrapArgument> bootstrapArguments) {
            this.referenceKind = referenceKind;
            this.memberReference = memberReference;
            this.bootstrapArguments = List.copyOf(bootstrapArguments);
        }

        @Override
        public String toString() {
            return "BootstrapMethod{" +
                    "referenceKind=" + referenceKind +
                    ", memberReference=" + memberReference +
                    ", bootstrapArguments=" + bootstrapArguments +
                    '}';
        }

    }

    public BootstrapMethodsAttribute(List<BootstrapMethod> list) {
        this.list = List.copyOf(list);
    }

    BootstrapMethodsAttribute() {
    }

    @Override
    BootstrapMethod parseElement(BytesReader bytesReader, ParsingConstantPool constantPool) throws IOException {
        MethodHandleParsingConstant methodHandle = (MethodHandleParsingConstant)constantPool.get(bytesReader.readShort());
        int numArgs = bytesReader.readShort();
        List<BootstrapArgument> argumentList = new ArrayList<>(numArgs);
        while(numArgs-- > 0) {
            ParsingConstant argType = constantPool.get(bytesReader.readShort());
            if(argType instanceof StringParsingConstant con) {
                argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.STRING,
                        constantPool.getStringFromUtf8(con.getStringIndex())));
            } else if(argType instanceof ClassParsingConstant con) {
                argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.CLASS,
                        constantPool.getStringFromUtf8(con.getNameIndex())));
            } else if(argType instanceof DoubleParsingConstant con) {
                argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.DOUBLE, con.toStringValue()));
            } else if(argType instanceof FloatParsingConstant con) {
                argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.FLOAT, con.toStringValue()));
            } else if(argType instanceof LongParsingConstant con) {
                argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.LONG, con.toStringValue()));
            } else if(argType instanceof IntegerParsingConstant con) {
                argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.INTEGER, con.toStringValue()));
            } else if(argType instanceof MethodTypeParsingConstant con) {
                argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.METHOD_TYPE,
                        constantPool.getStringFromUtf8(con.getDescriptorIndex())));
            } else if(argType instanceof MethodHandleParsingConstant con) {
                MethodHandleReferenceKind referenceKind = con.getReferenceKind();
                MemberReference reference = translate(con, constantPool);
                argumentList.add(new MethodHandleBootstrapArgument(referenceKind, reference));
            } else if(argType instanceof DynamicParsingConstant con) {
                // TODO
                throw new RuntimeException("Section not implemented yet");
            } else {
                throw new IOException("Constant type not allowed here: "+ argType);
            }
        }
        return new BootstrapMethod(methodHandle.getReferenceKind(), translate(methodHandle, constantPool),
                Collections.unmodifiableList(argumentList));
    }

    private MemberReference translate(MethodHandleParsingConstant constant, ParsingConstantPool constantPool)
            throws IOException {
        MemberRefParsingConstant reference = (MemberRefParsingConstant)constantPool.get(constant.getReferenceIndex());
        NameAndTypeParsingConstant nameAndType = (NameAndTypeParsingConstant)constantPool.get(reference.getNameAndTypeIndex());
        MemberReference.Type type;
        if(reference instanceof FieldRefParsingConstant) {
            type = MemberReference.Type.FIELD;
        } else if (reference instanceof MethodRefParsingConstant) {
            type = MemberReference.Type.METHOD;
        } else if (reference instanceof InterfaceMethodRefParsingConstant) {
            type = MemberReference.Type.INTERFACE_METHOD;
        } else {
            throw new IOException("unexpected constant type: " + reference);
        }

        return new MemberReference(type, constantPool.getClassName(reference.getClassIndex()),
                constantPool.getStringFromUtf8(nameAndType.getNameIndex()),
                constantPool.getStringFromUtf8(nameAndType.getDescriptorIndex()));
    }
}
