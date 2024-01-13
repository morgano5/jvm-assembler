package au.id.villar.bytecode.constant;

public final class MethodRefConstant extends MemberRefConstant {

    public MethodRefConstant(int classIndex, int nameAndTypeIndex) {
        super(classIndex, nameAndTypeIndex);
    }

    MethodRefConstant() {}

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "MethodRefConstant{" +
                "classIndex=" + classIndex +
                ", nameAndTypeIndex=" + nameAndTypeIndex +
                '}';
    }
}
