package au.id.villar.bytecode.attribute;

abstract class StringAttribute extends Attribute {

    protected String value;

    StringAttribute(String value) {
        this.value = value;
    }

    StringAttribute() {}

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{value=" + value + '}';
    }

}
