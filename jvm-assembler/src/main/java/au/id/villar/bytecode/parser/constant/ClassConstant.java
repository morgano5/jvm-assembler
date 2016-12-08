package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public class ClassConstant extends Constant {

	private int nameIndex;

	public ClassConstant(int nameIndex) {
		this.nameIndex = nameIndex;
	}

	public int getNameIndex() {
		return nameIndex;
	}

	ClassConstant() {}

	@Override
	void parseBody(BytesReader bytesReader) throws IOException {
		nameIndex = bytesReader.readShort();
	}

	@Override
	public String toString() {
		return "ClassConstant{" + nameIndex + '}';
	}
}
