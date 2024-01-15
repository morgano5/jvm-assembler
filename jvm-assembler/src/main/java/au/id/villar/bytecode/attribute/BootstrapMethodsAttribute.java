package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.constant.FieldRefConstant;
import au.id.villar.bytecode.constant.InterfaceMethodRefConstant;
import au.id.villar.bytecode.constant.LoadableConstant;
import au.id.villar.bytecode.constant.MemberRefConstant;
import au.id.villar.bytecode.constant.MethodHandleConstant;
import au.id.villar.bytecode.constant.MethodHandleReferenceKind;
import au.id.villar.bytecode.constant.MethodRefConstant;
import au.id.villar.bytecode.constant.NameAndTypeConstant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BootstrapMethodsAttribute extends ListAttribute<BootstrapMethodsAttribute.BootstrapMethod> {


    public record BootstrapMethod(MethodHandleReferenceKind referenceKind, MemberReference memberReference,
        List<LoadableConstant> bootstrapArguments) {

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
        List<LoadableConstant> argumentList = new ArrayList<>(numArgs);
        while(numArgs-- > 0) {
            LoadableConstant argType = constantPool.getLoadable(bytesReader.readShort());
            argumentList.add(argType);
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
