package au.id.villar.bytecode.constant;

public final class DoubleConstant extends Constant {

    private final double value;

    public DoubleConstant(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
