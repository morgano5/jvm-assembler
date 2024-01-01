package au.id.villar.bytecode.constant;

public final class IntegerConstant extends Constant {

    private final int value;

    public IntegerConstant(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
