package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public class InvokeDynamicConstant extends Constant {

	private int bootstrapMethodAttrIndex;
	private int nameAndTypeIndex;

	public InvokeDynamicConstant(int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
		this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
		this.nameAndTypeIndex = nameAndTypeIndex;
	}

	public int getBootstrapMethodAttrIndex() {
		return bootstrapMethodAttrIndex;
	}

	public int getNameAndTypeIndex() {
		return nameAndTypeIndex;
	}

	InvokeDynamicConstant() {}

	@Override
	void parseBody(BytesReader bytesReader) throws IOException {
		bootstrapMethodAttrIndex = bytesReader.readShort();
		nameAndTypeIndex = bytesReader.readShort();
	}

	@Override
	public String toString() {
		return "InvokeDynamicConstant{" +
				"bootstrapMethodAttrIndex=" + bootstrapMethodAttrIndex +
				", nameAndTypeIndex=" + nameAndTypeIndex +
				'}';
	}
}
