package au.id.villar.bytecode.constant;

public final class MethodTypeConstant
        extends IndexToUtf8Constant {

    public MethodTypeConstant(int descriptorIndex) {
        super(descriptorIndex);
    }

    public int getDescriptorIndex() {
        return utf8Index;
    }

    MethodTypeConstant() {}

    @Override
    public String toString() {
        return "MethodTypeConstant{" + utf8Index + '}';
    }
}
