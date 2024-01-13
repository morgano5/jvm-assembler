package au.id.villar.bytecode.constant;

public abstract sealed class ValueConstant
        extends Constant
        permits Utf8Constant, DoubleConstant, FloatConstant, LongConstant,
        IntegerConstant {

    public abstract String toStringValue();
}
