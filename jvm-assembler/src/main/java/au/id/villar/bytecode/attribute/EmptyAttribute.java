package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

class EmptyAttribute extends Attribute {

    @Override
    public void parseBody(int nameIndex, int length, BytesReader bytesReader, ConstantPool constantPool,
            AttributeGenerator generator) throws IOException {
        this.nameIndex = nameIndex;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{}";
    }
}
