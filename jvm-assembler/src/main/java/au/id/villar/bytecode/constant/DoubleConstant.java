package au.id.villar.bytecode.constant;

public final class DoubleConstant extends Constant {

    private final double value;

    public DoubleConstant(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean isLoadable() {
        return true;
    }

    @Override
    public String toAssemblyDefinition(String identifier) {
        return String.format("d_double %s %e", identifier, value);
    }

    @Override
    public String toString() {
        return "DoubleConstant{" +
                "value=" + value +
                '}';
    }
}
