package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.parser.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public class SourceDebugExtensionAttribute extends StringAttribute {

    public SourceDebugExtensionAttribute(String value) {
        super(value);
    }

    SourceDebugExtensionAttribute() {
    }

    @Override
    public void parseBody(int length, BytesReader bytesReader, ParsingConstantPool constantPool) throws IOException {
        value = bytesReader.readUTF8String(length);
    }
}
