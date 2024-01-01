package au.id.villar.bytecode.constant;

public final class LongConstant extends Constant {

    private final long value;

    public LongConstant(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
