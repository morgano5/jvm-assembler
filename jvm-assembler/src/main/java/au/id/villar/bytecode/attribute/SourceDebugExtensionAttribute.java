package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.Map;

public class SourceDebugExtensionAttribute extends StringAttribute {

	public SourceDebugExtensionAttribute(String value) {
		super(value);
	}

	SourceDebugExtensionAttribute() {
	}

	@Override
	public void parseBody(int length, BytesReader bytesReader, Map<Integer, Constant> constantPool) throws IOException {
		value = bytesReader.readUTF8String(length);
	}
}
