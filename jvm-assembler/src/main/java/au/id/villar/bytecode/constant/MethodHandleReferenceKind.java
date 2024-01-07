package au.id.villar.bytecode.constant;

public enum MethodHandleReferenceKind {
    GET_FIELD(1),
    GET_STATIC(2),
    PUT_FIELD(3),
    PUT_STATIC(4),
    INVOKE_VIRTUAL(5),
    INVOKE_STATIC(6),
    INVOKE_SPECIAL(7),
    NEW_INVOKE_SPECIAL(8),
    INVOKE_INTERFACE(9);

    public final int KIND;

    MethodHandleReferenceKind(int kind) {
        this.KIND = kind;
    }
}
