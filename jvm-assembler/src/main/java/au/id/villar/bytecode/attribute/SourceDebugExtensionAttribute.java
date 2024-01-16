package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public class SourceDebugExtensionAttribute extends StringAttribute {

    public SourceDebugExtensionAttribute(String value) {
        super(value);
    }

    SourceDebugExtensionAttribute() {}

    @Override
    public void parseBody(int nameIndex, int length, BytesReader bytesReader, ConstantPool constantPool,
            AttributeGenerator generator) throws IOException {
        this.nameIndex = nameIndex;
        value = bytesReader.readModUtf8(length);
    }
}
