package au.id.villar.bytecode.constant;

public final class DynamicConstant extends AbstractDynamicConstant implements LoadableConstant {

    public DynamicConstant(int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
        super(bootstrapMethodAttrIndex, nameAndTypeIndex);
    }

    DynamicConstant() {}

    @Override
    public String toString() {
        return "DynamicConstant{" +
                "bootstrapMethodAttrIndex=" + bootstrapMethodAttrIndex +
                ", nameAndTypeIndex=" + nameAndTypeIndex +
                '}';
    }

    @Override
    protected byte getRawTag() {
        return 17;
    }
}
