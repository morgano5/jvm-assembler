package au.id.villar.bytecode.constant;

public final class MethodRefConstant extends MemberRefConstant {

    public MethodRefConstant(String className, String methodName, String descriptor) {
        super(className, methodName, descriptor);
    }

    public String getMethodName() {
        return memberName;
    }

    @Override
    public String toAssemblyDefinition(String identifier) {
        return String.format("d_method %s \"%s\" \"%s\" \"%s\"", identifier, memberName, getDescriptor(),
                getClassName());
    }

    @Override
    public String toString() {
        return "MethodRefConstant{" +
                "className='" + getClassName() + '\'' +
                ", methodName='" + memberName + '\'' +
                ", descriptor='" + getDescriptor() + '\'' +
                '}';
    }
}
