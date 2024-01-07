package au.id.villar.bytecode.constant;

public abstract sealed class Constant
        permits ClassConstant, DoubleConstant, DynamicConstant, FloatConstant, IntegerConstant, LongConstant,
            MemberRefConstant, MethodHandleConstant, MethodTypeConstant, StringConstant {

    public abstract boolean isLoadable();

    public abstract String toAssemblyDefinition(String identifier);
}
