package au.id.villar.bytecode.constant;

public final class MethodTypeConstant extends Constant {

    private final String value;

    public MethodTypeConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
