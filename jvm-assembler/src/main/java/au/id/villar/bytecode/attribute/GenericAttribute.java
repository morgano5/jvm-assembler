package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.Arrays;

public class GenericAttribute extends Attribute {

    private String name;
    private byte[] data;

    public GenericAttribute(String name, byte[] data) {
        this(name);
        this.data = data;
    }

    GenericAttribute(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public byte[] getData() {
        // TODO find a method to make that byte[] read-only
        return data;
    }

    @Override
    public void parseBody(int length, BytesReader bytesReader, ParsingConstantPool constantPool,
            AttributeGenerator generator) throws IOException {
        if(length > 0) {
            data = new byte[length];
            bytesReader.readMinimum(data, length);
        }
    }

    @Override
    public String toString() {
        return "GenericAttribute{" +
                "name='" + name + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
