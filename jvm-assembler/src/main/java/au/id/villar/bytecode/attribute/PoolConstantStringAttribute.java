package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.Map;

abstract class PoolConstantStringAttribute extends StringAttribute {

	PoolConstantStringAttribute(String value) {
		super(value);
	}

	PoolConstantStringAttribute() {}

	@Override
	public void parseBody(int length, BytesReader bytesReader, Map<Integer, Constant> constantPool) throws IOException {
		value = Constant.toString(bytesReader.readShort(), constantPool);
	}

}
