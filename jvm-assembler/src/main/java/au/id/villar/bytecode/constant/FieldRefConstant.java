package au.id.villar.bytecode.constant;

public final class FieldRefConstant extends MemberRefConstant {

    public FieldRefConstant(int classIndex, int nameAndTypeIndex) {
        super(classIndex, nameAndTypeIndex);
    }

    FieldRefConstant() {}

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "FieldRefConstant{" +
                "classIndex=" + classIndex +
                ", nameAndTypeIndex=" + nameAndTypeIndex +
                '}';
    }

    @Override
    protected byte getRawTag() {
        return 9;
    }
}
