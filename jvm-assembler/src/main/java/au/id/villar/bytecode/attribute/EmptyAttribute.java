package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.Map;

class EmptyAttribute extends Attribute {

	@Override
	public void parseBody(int length, BytesReader bytesReader, Map<Integer, Constant> constantPool) throws IOException {
		// DO nothing
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{}";
	}
}
