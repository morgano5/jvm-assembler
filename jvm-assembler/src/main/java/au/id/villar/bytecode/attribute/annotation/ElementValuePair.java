package au.id.villar.bytecode.attribute.annotation;

import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.Map;

public final class ElementValuePair {
	private String name;
	private ElementValue value;

	public ElementValuePair(String name, ElementValue value) {
		this.name = name;
		this.value = value;
	}

	private ElementValuePair() {}

	public String getName() {
		return name;
	}

	public ElementValue getValue() {
		return value;
	}

	static ElementValuePair readElementValuePair(BytesReader bytesReader, Map<Integer, Constant> constantPool)
			throws IOException {
		ElementValuePair elementValuePair = new ElementValuePair();
		elementValuePair.name = Constant.toString(bytesReader.readShort(), constantPool);
		elementValuePair.value = ElementValue.readElementValue(bytesReader, constantPool);
		return elementValuePair;
	}

	@Override
	public String toString() {
		return "ElementValuePair{" +
				"name='" + name + '\'' +
				", value=" + value +
				'}';
	}
}
