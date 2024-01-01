package au.id.villar.bytecode.parser.constant;

public final class InvokeDynamicParsingConstant extends AbstractDynamicParsingConstant {

    public InvokeDynamicParsingConstant(int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
        super(bootstrapMethodAttrIndex, nameAndTypeIndex);
    }

    InvokeDynamicParsingConstant() {}

    @Override
    public String toString() {
        return "InvokeDynamicConstant{" +
                "bootstrapMethodAttrIndex=" + bootstrapMethodAttrIndex +
                ", nameAndTypeIndex=" + nameAndTypeIndex +
                '}';
    }
}
