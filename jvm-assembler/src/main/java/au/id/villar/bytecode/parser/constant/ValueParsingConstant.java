package au.id.villar.bytecode.parser.constant;

public abstract sealed class ValueParsingConstant
        extends ParsingConstant
        permits Utf8ParsingConstant, DoubleParsingConstant, FloatParsingConstant, LongParsingConstant,
        IntegerParsingConstant {

    public abstract String toStringValue();
}
