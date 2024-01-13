package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.constant.ClassConstant;
import au.id.villar.bytecode.constant.DynamicConstant;
import au.id.villar.bytecode.constant.Constant;
import au.id.villar.bytecode.constant.DoubleConstant;
import au.id.villar.bytecode.constant.FieldRefConstant;
import au.id.villar.bytecode.constant.FloatConstant;
import au.id.villar.bytecode.constant.IntegerConstant;
import au.id.villar.bytecode.constant.InterfaceMethodRefConstant;
import au.id.villar.bytecode.constant.LongConstant;
import au.id.villar.bytecode.constant.MemberRefConstant;
import au.id.villar.bytecode.constant.MethodHandleConstant;
import au.id.villar.bytecode.constant.MethodHandleReferenceKind;
import au.id.villar.bytecode.constant.MethodRefConstant;
import au.id.villar.bytecode.constant.MethodTypeConstant;
import au.id.villar.bytecode.constant.NameAndTypeConstant;
import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.constant.StringConstant;
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
    BootstrapMethod parseElement(BytesReader bytesReader, ConstantPool constantPool) throws IOException {
        MethodHandleConstant methodHandle = (MethodHandleConstant)constantPool.get(bytesReader.readShort());
        int numArgs = bytesReader.readShort();
        List<BootstrapArgument> argumentList = new ArrayList<>(numArgs);
        while(numArgs-- > 0) {
            Constant argType = constantPool.get(bytesReader.readShort());
            if(argType instanceof StringConstant con) {
                argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.STRING,
                        constantPool.getStringFromUtf8(con.getStringIndex())));
            } else if(argType instanceof ClassConstant con) {
                argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.CLASS,
                        constantPool.getStringFromUtf8(con.getNameIndex())));
            } else if(argType instanceof DoubleConstant con) {
                argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.DOUBLE, con.toStringValue()));
            } else if(argType instanceof FloatConstant con) {
                argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.FLOAT, con.toStringValue()));
            } else if(argType instanceof LongConstant con) {
                argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.LONG, con.toStringValue()));
            } else if(argType instanceof IntegerConstant con) {
                argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.INTEGER, con.toStringValue()));
            } else if(argType instanceof MethodTypeConstant con) {
                argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.METHOD_TYPE,
                        constantPool.getStringFromUtf8(con.getDescriptorIndex())));
            } else if(argType instanceof MethodHandleConstant con) {
                MethodHandleReferenceKind referenceKind = con.getReferenceKind();
                MemberReference reference = translate(con, constantPool);
                argumentList.add(new MethodHandleBootstrapArgument(referenceKind, reference));
            } else if(argType instanceof DynamicConstant con) {
                // TODO
                throw new RuntimeException("Section not implemented yet");
            } else {
                throw new IOException("Constant type not allowed here: "+ argType);
            }
        }
        return new BootstrapMethod(methodHandle.getReferenceKind(), translate(methodHandle, constantPool),
                Collections.unmodifiableList(argumentList));
    }

    private MemberReference translate(MethodHandleConstant constant, ConstantPool constantPool)
            throws IOException {
        MemberRefConstant reference = (MemberRefConstant)constantPool.get(constant.getReferenceIndex());
        NameAndTypeConstant nameAndType = (NameAndTypeConstant)constantPool.get(reference.getNameAndTypeIndex());
        MemberReference.Type type;
        if(reference instanceof FieldRefConstant) {
            type = MemberReference.Type.FIELD;
        } else if (reference instanceof MethodRefConstant) {
            type = MemberReference.Type.METHOD;
        } else if (reference instanceof InterfaceMethodRefConstant) {
            type = MemberReference.Type.INTERFACE_METHOD;
        } else {
            throw new IOException("unexpected constant type: " + reference);
        }

        return new MemberReference(type, constantPool.getClassName(reference.getClassIndex()),
                constantPool.getStringFromUtf8(nameAndType.getNameIndex()),
                constantPool.getStringFromUtf8(nameAndType.getDescriptorIndex()));
    }
}
