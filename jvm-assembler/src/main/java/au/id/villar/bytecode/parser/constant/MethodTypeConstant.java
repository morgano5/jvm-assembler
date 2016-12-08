package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public class MethodTypeConstant extends Constant {

	private int descriptorIndex;

	public MethodTypeConstant(int descriptorIndex) {
		this.descriptorIndex = descriptorIndex;
	}

	public int getDescriptorIndex() {
		return descriptorIndex;
	}

	MethodTypeConstant() {}

	@Override
	void parseBody(BytesReader bytesReader) throws IOException {
		descriptorIndex = bytesReader.readShort();
	}

	@Override
	public String toString() {
		return "MethodTypeConstant{" + descriptorIndex + '}';
	}
}
