package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.attribute.Attribute;
import au.id.villar.bytecode.constant.MemberRefConstant;
import au.id.villar.bytecode.constant.MethodHandleConstant.MemberType;
import au.id.villar.bytecode.constant.MethodHandleReferenceKind;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.List;

public abstract sealed class MemberRefParsingConstant<T extends MemberRefConstant>
        extends ParsingConstant
        implements RuntimeParsingConstant<T>
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

    @Override
    public T toConstant(ParsingConstantPool constantPool, List<Attribute> attributes, int mayor) {
        NameAndTypeParsingConstant nameTypeConstant = constantPool.get(getNameAndTypeIndex(),
                NameAndTypeParsingConstant.class);
        String className = constantPool.getClassName(getClassIndex());
        String memberName = constantPool.getStringFromUtf8(nameTypeConstant.getNameIndex());
        String descriptor = constantPool.getStringFromUtf8(nameTypeConstant.getDescriptorIndex());
        return createRefConstant(className, memberName, descriptor);
    }

    MemberRefParsingConstant() {}

    protected abstract T createRefConstant(String className, String memberName, String descriptor);

    abstract MemberType calculateMemberType(MethodHandleReferenceKind referenceKind, int mayorVersion);

    @Override
    void parseBody(BytesReader bytesReader) throws IOException {
        classIndex = bytesReader.readShort();
        nameAndTypeIndex = bytesReader.readShort();
    }

    @Override
    public abstract String toString();

}
