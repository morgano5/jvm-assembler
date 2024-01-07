package au.id.villar.bytecode.constant;

public abstract sealed class MemberRefConstant
        extends Constant
        permits FieldRefConstant, MethodRefConstant, InterfaceMethodRefConstant {

    private final String className;
    private final String descriptor;
    protected final String memberName;

    protected MemberRefConstant(String className, String memberName, String descriptor) {
        this.className = className;
        this.memberName = memberName;
        this.descriptor = descriptor;
    }

    public String getClassName() {
        return className;
    }

    public String getDescriptor() {
        return descriptor;
    }

    @Override
    public boolean isLoadable() {
        return false;
    }

}
