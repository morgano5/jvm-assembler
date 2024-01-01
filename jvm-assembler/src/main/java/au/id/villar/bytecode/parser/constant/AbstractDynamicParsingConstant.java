package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public abstract sealed class AbstractDynamicParsingConstant
        extends ParsingConstant
        permits InvokeDynamicParsingConstant, DynamicParsingConstant {

    protected int bootstrapMethodAttrIndex;
    protected int nameAndTypeIndex;

    public AbstractDynamicParsingConstant(int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
        this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    public int getBootstrapMethodAttrIndex() {
        return bootstrapMethodAttrIndex;
    }

    public int getNameAndTypeIndex() {
        return nameAndTypeIndex;
    }

    AbstractDynamicParsingConstant() {}

    @Override
    void parseBody(BytesReader bytesReader) throws IOException {
        bootstrapMethodAttrIndex = bytesReader.readShort();
        nameAndTypeIndex = bytesReader.readShort();
    }

    @Override
    public abstract String toString();

}
