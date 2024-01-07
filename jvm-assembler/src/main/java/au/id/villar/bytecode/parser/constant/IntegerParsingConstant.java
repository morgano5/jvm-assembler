package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.attribute.Attribute;
import au.id.villar.bytecode.constant.IntegerConstant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.List;

public final class IntegerParsingConstant extends ValueParsingConstant implements RuntimeParsingConstant<IntegerConstant> {

    private int value;

    public IntegerParsingConstant(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    IntegerParsingConstant() {}

    @Override
    void parseBody(BytesReader bytesReader) throws IOException {
        value = bytesReader.readInt();
    }

    @Override
    public String toStringValue() {
        return String.valueOf(getValue());
    }

    @Override
    public IntegerConstant toConstant(ParsingConstantPool constantPool, List<Attribute> attributes, int mayor) {
        return new IntegerConstant(value);
    }

    @Override
    public String toString() {
        return "IntegerConstant{" + value + '}';
    }
}
