package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.attribute.Attribute;
import au.id.villar.bytecode.constant.MethodHandleConstant;
import au.id.villar.bytecode.constant.MethodHandleReferenceKind;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.List;

public final class MethodHandleParsingConstant extends ParsingConstant implements Loadable<MethodHandleConstant> {

    private MethodHandleReferenceKind referenceKind;
    private int referenceIndex;

    public MethodHandleParsingConstant(MethodHandleReferenceKind referenceKind, int referenceIndex) {
        this.referenceKind = referenceKind;
        this.referenceIndex = referenceIndex;
    }

    public MethodHandleReferenceKind getReferenceKind() {
        return referenceKind;
    }

    public int getReferenceIndex() {
        return referenceIndex;
    }

    MethodHandleParsingConstant() {}

    @Override
    void parseBody(BytesReader bytesReader) throws IOException {
        int kind = bytesReader.readByte();
        referenceIndex = bytesReader.readShort();
        for(MethodHandleReferenceKind potentialReferenceKind: MethodHandleReferenceKind.values()) {
            if(potentialReferenceKind.KIND == kind) {
                referenceKind = potentialReferenceKind;
                return;
            }
        }
        throw new IOException("Reference Kind not known: " + kind);
    }

    @Override
    public MethodHandleConstant toConstant(ParsingConstantPool constantPool, List<Attribute> attributes, int mayor) {

        MemberRefParsingConstant refConstant = constantPool.get(referenceIndex, MemberRefParsingConstant.class);
        NameAndTypeParsingConstant nameTypeConstant = constantPool.get(refConstant.getNameAndTypeIndex(),
                NameAndTypeParsingConstant.class);
        String className = constantPool.getClassName(refConstant.getClassIndex());
        String memberName = constantPool.getStringFromUtf8(nameTypeConstant.getNameIndex());
        String descriptor = constantPool.getStringFromUtf8(nameTypeConstant.getDescriptorIndex());
        MethodHandleConstant.MemberType type = refConstant.calculateMemberType(referenceKind, mayor);
        return new MethodHandleConstant(referenceKind, type, className, memberName, descriptor);
    }

    @Override
    public String toString() {
        return "MethodHandleConstant{" +
                "referenceKind=" + referenceKind +
                ", referenceIndex=" + referenceIndex +
                '}';
    }
}
