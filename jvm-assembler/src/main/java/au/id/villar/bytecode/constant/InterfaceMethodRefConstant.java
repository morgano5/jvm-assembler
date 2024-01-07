package au.id.villar.bytecode.constant;

public final class InterfaceMethodRefConstant extends MemberRefConstant {

    public InterfaceMethodRefConstant(String className, String methodName, String descriptor) {
        super(className, methodName, descriptor);
    }

    public String getMethodName() {
        return memberName;
    }

    @Override
    public String toAssemblyDefinition(String identifier) {
        return String.format("d_imethod %s \"%s\" \"%s\" \"%s\"", identifier, memberName, getDescriptor(),
                getClassName());
    }

    @Override
    public String toString() {
        return "InterfaceMethodRefConstant{" +
                "className='" + getClassName() + '\'' +
                ", methodName='" + memberName + '\'' +
                ", descriptor='" + getDescriptor() + '\'' +
                '}';
    }
}
