package au.id.villar.bytecode.parser.constant;

public class InterfaceMethodRefConstant extends MemberRefConstant {

	public InterfaceMethodRefConstant(int classIndex, int nameAndTypeIndex) {
		super(classIndex, nameAndTypeIndex);
	}

	InterfaceMethodRefConstant() {}

}
