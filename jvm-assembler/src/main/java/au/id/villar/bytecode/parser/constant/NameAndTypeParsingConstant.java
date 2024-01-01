package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public final class NameAndTypeParsingConstant extends ParsingConstant {

    private int nameIndex;
    private int descriptorIndex;

    public NameAndTypeParsingConstant(int nameIndex, int descriptorIndex) {
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public int getDescriptorIndex() {
        return descriptorIndex;
    }

    NameAndTypeParsingConstant() {}

    @Override
    void parseBody(BytesReader bytesReader) throws IOException {
        nameIndex = bytesReader.readShort();
        descriptorIndex = bytesReader.readShort();
    }

    @Override
    public String toString() {
        return "NameAndTypeConstant{" +
                "nameIndex=" + nameIndex +
                ", descriptorIndex=" + descriptorIndex +
                '}';
    }
}
