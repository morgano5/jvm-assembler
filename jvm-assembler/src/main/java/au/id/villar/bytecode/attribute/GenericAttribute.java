package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.Arrays;

public class GenericAttribute extends Attribute {

    private byte[] data;

    public GenericAttribute(byte[] data) {
        this.data = data;
    }

    GenericAttribute() {}

    public byte[] getData() {
        // TODO find a method to make that byte[] read-only
        return data;
    }

    @Override
    public void parseBody(int nameIndex, int length, BytesReader bytesReader, ConstantPool constantPool,
            AttributeGenerator generator) throws IOException {
        this.nameIndex = nameIndex;
        if(length > 0) {
            data = new byte[length];
            bytesReader.readMinimum(data, length);
        }
    }

    @Override
    public String toString() {
        return "GenericAttribute{" +
                "nameIndex='" + nameIndex + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
