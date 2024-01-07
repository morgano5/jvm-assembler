package au.id.villar.bytecode.constant;

public final class StringConstant extends Constant {

    private final String value;

    public StringConstant(String value) {
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
        StringBuilder formatted = new StringBuilder(value);
        replace(formatted, "\\", "\\\\");
        replace(formatted, "\b", "\\b");
        replace(formatted, "\t", "\\t");
        replace(formatted, "\n", "\\n");
        replace(formatted, "\f", "\\f");
        replace(formatted, "\r", "\\r");
        replace(formatted, "\"", "\\\"");
        return String.format("d_string %s \"%s\"", identifier, formatted);
    }

    @Override
    public String toString() {
        return "StringConstant{" +
                "value='" + value + '\'' +
                '}';
    }

    private void replace(StringBuilder text, String oldText, String newText) {
        int fromIndex = 0;
        while ((fromIndex = text.indexOf(oldText, fromIndex)) >= 0) {
            text.replace(fromIndex, oldText.length() + fromIndex, newText);
            fromIndex += newText.length();
        }
    }
}
