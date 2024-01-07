package au.id.villar.bytecode.constant;

public final class MethodTypeConstant extends Constant {

    private final String value;

    public MethodTypeConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean isLoadable() {
        return true;
    }

    @Override
    public String toAssemblyDefinition(String identifier) {
        return String.format("d_methodtype %s \"%s\"", identifier, value);
    }

    @Override
    public String toString() {
        return "MethodTypeConstant{" +
                "value='" + value + '\'' +
                '}';
    }
}
