package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public class LongConstant extends Constant {

	private long value;

	public LongConstant(long value) {
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	LongConstant() {}

	@Override
	void parseBody(BytesReader bytesReader) throws IOException {
		value = bytesReader.readLong();
	}

	@Override
	public String toStringValue() {
		return String.valueOf(getValue());
	}

	@Override
	public String toString() {
		return "LongConstant{" + value + '}';
	}
}
