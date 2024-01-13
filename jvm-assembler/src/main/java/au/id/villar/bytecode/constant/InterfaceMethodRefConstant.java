package au.id.villar.bytecode.constant;

public final class InterfaceMethodRefConstant extends MemberRefConstant {

    public InterfaceMethodRefConstant(int classIndex, int nameAndTypeIndex) {
        super(classIndex, nameAndTypeIndex);
    }

    InterfaceMethodRefConstant() {}

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "InterfaceMethodRefConstant{" +
                "classIndex=" + classIndex +
                ", nameAndTypeIndex=" + nameAndTypeIndex +
                '}';
    }
}
