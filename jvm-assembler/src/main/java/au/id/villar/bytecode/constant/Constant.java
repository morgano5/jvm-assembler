package au.id.villar.bytecode.constant;

public abstract sealed class Constant
        permits ClassConstant, DoubleConstant, DynamicConstant, FloatConstant, IntegerConstant, LongConstant,
            MethodHandleConstant, MethodTypeConstant, StringConstant {

}
