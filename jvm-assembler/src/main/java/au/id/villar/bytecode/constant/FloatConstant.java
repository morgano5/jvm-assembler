package au.id.villar.bytecode.constant;

public final class FloatConstant extends Constant {

    private final float value;

    public FloatConstant(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public boolean isLoadable() {
        return true;
    }

    @Override
    public String toAssemblyDefinition(String identifier) {
        return String.format("d_float %s %e", identifier, value);
    }

    @Override
    public String toString() {
        return "FloatConstant{" +
                "value=" + value +
                '}';
    }
}
