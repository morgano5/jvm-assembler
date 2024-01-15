package au.id.villar.bytecode.constant;

import au.id.villar.bytecode.util.BytesReader;
import au.id.villar.bytecode.util.BytesWriter;

import java.io.IOException;

public abstract sealed class MemberRefConstant
        extends Constant
        permits FieldRefConstant, InterfaceMethodRefConstant, MethodRefConstant {

    protected int classIndex;
    protected int nameAndTypeIndex;

    MemberRefConstant(int classIndex, int nameAndTypeIndex) {
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    public int getClassIndex() {
        return classIndex;
    }

    public int getNameAndTypeIndex() {
        return nameAndTypeIndex;
    }

    MemberRefConstant() {}

    @Override
    void parseBody(BytesReader bytesReader) throws IOException {
        classIndex = bytesReader.readShort();
        nameAndTypeIndex = bytesReader.readShort();
    }

    @Override
    public abstract String toString();

    @Override
    public void write(BytesWriter bytesWriter) throws IOException {
        bytesWriter.writeByte(getRawTag());
        bytesWriter.writeShort(classIndex);
        bytesWriter.writeShort(nameAndTypeIndex);
    }

    protected abstract byte getRawTag();

}
