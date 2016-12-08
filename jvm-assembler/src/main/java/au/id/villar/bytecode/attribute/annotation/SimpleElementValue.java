package au.id.villar.bytecode.attribute.annotation;

import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.Map;

public class SimpleElementValue extends ElementValue {

	private String value;

	public SimpleElementValue(String value) {
		this.value = value;
	}

	SimpleElementValue() {}

	public String getValue() {
		return value;
	}

	@Override
	void parseBody(BytesReader bytesReader, Map<Integer, Constant> constantPool) throws IOException {
		value = Constant.toString(bytesReader.readShort(), constantPool);
	}

	@Override
	public String toString() {
		return "SimpleElementValue{" +
				"value='" + value + '\'' +
				'}';
	}
}
