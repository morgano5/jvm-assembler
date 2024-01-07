package au.id.villar.bytecode.constant;

public final class LongConstant extends Constant {

    private final long value;

    public LongConstant(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean isLoadable() {
        return true;
    }

    @Override
    public String toAssemblyDefinition(String identifier) {
        return String.format("d_long %s %d", identifier, value);
    }

    @Override
    public String toString() {
        return "LongConstant{" +
                "value=" + value +
                '}';
    }
}
