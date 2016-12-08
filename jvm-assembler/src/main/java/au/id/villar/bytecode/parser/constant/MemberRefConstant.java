package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public abstract class MemberRefConstant extends Constant {

	private int classIndex;
	private int nameAndTypeIndex;

	MemberRefConstant(int classIndex, int nameAndTypeIndex) {
		this.classIndex = classIndex;
		this.nameAndTypeIndex = nameAndTypeIndex;
	}

	public int getClassIndex() {
		return classIndex;
	}

	public int getNameAndTypeIndex() {
		return nameAndTypeIndex;
	}

	MemberRefConstant() {}

	@Override
	void parseBody(BytesReader bytesReader) throws IOException {
		classIndex = bytesReader.readShort();
		nameAndTypeIndex = bytesReader.readShort();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{" +
				"classIndex=" + classIndex +
				", nameAndTypeIndex=" + nameAndTypeIndex +
				'}';
	}

}
