package au.id.villar.bytecode.constant;

public final class FloatConstant extends Constant {

    private final float value;

    public FloatConstant(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }
}
