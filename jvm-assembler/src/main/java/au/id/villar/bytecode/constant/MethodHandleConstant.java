package au.id.villar.bytecode.constant;

public final class MethodHandleConstant extends Constant {

    public enum MemberType {
        FIELD, METHOD, INTERFACE_METHOD
    }

    private final MethodHandleReferenceKind kind;
    private final MemberType memberType;
    private final String className;
    private final String memberName;
    private final String descriptor;

    public MethodHandleConstant(MethodHandleReferenceKind kind, MemberType memberType, String className,
            String memberName, String descriptor) {
        this.kind = kind;
        this.memberType = memberType;
        this.className = className;
        this.memberName = memberName;
        this.descriptor = descriptor;
    }

    public MethodHandleReferenceKind getKind() {
        return kind;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public String getClassName() {
        return className;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getDescriptor() {
        return descriptor;
    }
}
