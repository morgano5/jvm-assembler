package au.id.villar.bytecode.constant;

public final class ClassConstant extends Constant {

    private final String value;

    public ClassConstant(String value) {
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
        return String.format("d_class %s \"%s\"", identifier, value);
    }

    @Override
    public String toString() {
        return "ClassConstant{" +
                "value='" + value + '\'' +
                '}';
    }
}
