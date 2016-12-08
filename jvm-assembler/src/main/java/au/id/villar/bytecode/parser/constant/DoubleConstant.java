package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public class DoubleConstant extends Constant {

	private double value;

	public DoubleConstant(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	DoubleConstant() {}

	@Override
	void parseBody(BytesReader bytesReader) throws IOException {
		value = bytesReader.readDouble();
	}

	@Override
	public String toStringValue() {
		return String.valueOf(getValue());
	}

	@Override
	public String toString() {
		return "DoubleConstant{" + value + '}';
	}
}
