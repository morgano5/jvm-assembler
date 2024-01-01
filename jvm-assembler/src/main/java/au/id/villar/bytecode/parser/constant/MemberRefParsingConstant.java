package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.constant.MethodHandleConstant.MemberType;
import au.id.villar.bytecode.constant.MethodHandleReferenceKind;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public abstract sealed class MemberRefParsingConstant
        extends ParsingConstant
        permits FieldRefParsingConstant, InterfaceMethodRefParsingConstant, MethodRefParsingConstant {

    protected int classIndex;
    protected int nameAndTypeIndex;

    MemberRefParsingConstant(int classIndex, int nameAndTypeIndex) {
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    public int getClassIndex() {
        return classIndex;
    }

    public int getNameAndTypeIndex() {
        return nameAndTypeIndex;
    }

    MemberRefParsingConstant() {}

    abstract MemberType calculateMemberType(MethodHandleReferenceKind referenceKind, int mayorVersion);

    @Override
    void parseBody(BytesReader bytesReader) throws IOException {
        classIndex = bytesReader.readShort();
        nameAndTypeIndex = bytesReader.readShort();
    }

    @Override
    public abstract String toString();

}
