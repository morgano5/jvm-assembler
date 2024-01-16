package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.util.BytesReader;
import au.id.villar.bytecode.util.BytesWriter;

import java.io.IOException;

public class ConstantValueAttribute extends Attribute {

    private Integer valueIndex;

    public ConstantValueAttribute(Integer valueIndex) {
        this.valueIndex = valueIndex;
    }

    ConstantValueAttribute() {}

    public Integer getValueIndex() {
        return valueIndex;
    }

    @Override
    public void parseBody(int nameIndex, int length, BytesReader bytesReader, ConstantPool constantPool,
            AttributeGenerator generator)
            throws IOException {
        this.nameIndex = nameIndex;
        valueIndex = bytesReader.readShort();
    }

    @Override
    public void write(BytesWriter bytesWriter) throws IOException {
        bytesWriter.writeShort(nameIndex);
        bytesWriter.writeInt(2);
        bytesWriter.writeShort(valueIndex);
    }
}
