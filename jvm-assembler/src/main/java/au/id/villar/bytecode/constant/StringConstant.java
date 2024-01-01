package au.id.villar.bytecode.constant;

public final class StringConstant extends Constant {

    private final String value;

    public StringConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
