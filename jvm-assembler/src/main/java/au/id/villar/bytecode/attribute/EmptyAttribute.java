package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.parser.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

class EmptyAttribute extends Attribute {

    @Override
    public void parseBody(int length, BytesReader bytesReader, ParsingConstantPool constantPool) throws IOException {
        // DO nothing
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{}";
    }
}
