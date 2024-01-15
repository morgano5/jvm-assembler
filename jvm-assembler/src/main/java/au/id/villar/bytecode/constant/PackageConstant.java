package au.id.villar.bytecode.constant;

public final class PackageConstant extends IndexToUtf8Constant {

    public PackageConstant(int descriptorIndex) {
        super(descriptorIndex);
    }

    public int getNameIndex() {
        return utf8Index;
    }

    PackageConstant() {}

    @Override
    public String toString() {
        return "PackageConstant{" + utf8Index + '}';
    }

    @Override
    protected byte getRawTag() {
        return 20;
    }
}
