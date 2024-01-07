package au.id.villar.bytecode.constant;

public final class IntegerConstant extends Constant {

    private final int value;

    public IntegerConstant(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean isLoadable() {
        return true;
    }

    @Override
    public String toAssemblyDefinition(String identifier) {
        return String.format("d_int %s %d", identifier, value);
    }

    @Override
    public String toString() {
        return "IntegerConstant{" +
                "value=" + value +
                '}';
    }
}
