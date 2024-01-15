package au.id.villar.bytecode.constant;

public sealed interface LoadableConstant
        permits DynamicConstant, MethodHandleConstant, MethodTypeConstant, ClassConstant, StringConstant,
            DoubleConstant, FloatConstant, LongConstant, IntegerConstant {

}
