package au.id.villar.bytecode.constant;

public final class ModuleConstant extends IndexToUtf8Constant {

    public ModuleConstant(int descriptorIndex) {
        super(descriptorIndex);
    }

    public int getNameIndex() {
        return utf8Index;
    }

    ModuleConstant() {}

    @Override
    public String toString() {
        return "ModuleConstant{" + utf8Index + '}';
    }

    @Override
    protected byte getRawTag() {
        return 19;
    }
}
