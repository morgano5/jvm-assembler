package au.id.villar.bytecode.constant;

public final class ClassConstant extends Constant {

    private final String value;

    public ClassConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
