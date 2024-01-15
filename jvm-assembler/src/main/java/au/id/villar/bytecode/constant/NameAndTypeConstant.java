package au.id.villar.bytecode.constant;

import au.id.villar.bytecode.util.BytesReader;
import au.id.villar.bytecode.util.BytesWriter;

import java.io.IOException;

public final class NameAndTypeConstant extends Constant {

    private int nameIndex;
    private int descriptorIndex;

    public NameAndTypeConstant(int nameIndex, int descriptorIndex) {
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public int getDescriptorIndex() {
        return descriptorIndex;
    }

    NameAndTypeConstant() {}

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

    @Override
    public void write(BytesWriter bytesWriter) throws IOException {
        bytesWriter.writeByte(12);
        bytesWriter.writeShort(nameIndex);
        bytesWriter.writeShort(descriptorIndex);
    }
}
