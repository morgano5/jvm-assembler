package au.id.villar.bytecode.parser.constant;

public class FieldRefConstant extends MemberRefConstant {

	public FieldRefConstant(int classIndex, int nameAndTypeIndex) {
		super(classIndex, nameAndTypeIndex);
	}

	FieldRefConstant() {}
}
