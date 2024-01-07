package au.id.villar.bytecode.constant;

public final class FieldRefConstant extends MemberRefConstant {

    public FieldRefConstant(String className, String fieldName, String descriptor) {
        super(className, fieldName, descriptor);
    }

    public String getFieldName() {
        return memberName;
    }

    @Override
    public String toAssemblyDefinition(String identifier) {
        return String.format("d_field %s \"%s\" \"%s\" \"%s\"", identifier, memberName, getDescriptor(),
                getClassName());
    }

    @Override
    public String toString() {
        return "FieldRefConstant{" +
                "className='" + getClassName() + '\'' +
                ", fieldName='" + memberName + '\'' +
                ", descriptor='" + getDescriptor() + '\'' +
                '}';
    }
}
