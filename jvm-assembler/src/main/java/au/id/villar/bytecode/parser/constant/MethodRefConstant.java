package au.id.villar.bytecode.parser.constant;

public class MethodRefConstant extends MemberRefConstant {

	public MethodRefConstant(int classIndex, int nameAndTypeIndex) {
		super(classIndex, nameAndTypeIndex);
	}

	MethodRefConstant() {}

}
