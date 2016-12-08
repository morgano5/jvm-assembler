package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public class StringConstant extends Constant {

	private int stringIndex;

	public StringConstant(int stringIndex) {
		this.stringIndex = stringIndex;
	}

	public int getStringIndex() {
		return stringIndex;
	}

	StringConstant() {}

	@Override
	void parseBody(BytesReader bytesReader) throws IOException {
		stringIndex = bytesReader.readShort();
	}

	@Override
	public String toString() {
		return "StringConstant{" + stringIndex + '}';
	}
}
