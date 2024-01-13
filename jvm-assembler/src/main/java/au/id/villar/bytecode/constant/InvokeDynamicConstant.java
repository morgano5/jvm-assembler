package au.id.villar.bytecode.constant;

public final class InvokeDynamicConstant extends AbstractDynamicConstant {

    public InvokeDynamicConstant(int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
        super(bootstrapMethodAttrIndex, nameAndTypeIndex);
    }

    InvokeDynamicConstant() {}

    @Override
    public String toString() {
        return "InvokeDynamicConstant{" +
                "bootstrapMethodAttrIndex=" + bootstrapMethodAttrIndex +
                ", nameAndTypeIndex=" + nameAndTypeIndex +
                '}';
    }
}
