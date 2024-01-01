package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.attribute.Attribute;
import au.id.villar.bytecode.constant.DoubleConstant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.List;

public final class DoubleParsingConstant extends ValueParsingConstant implements Loadable<DoubleConstant> {

    private double value;

    public DoubleParsingConstant(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    DoubleParsingConstant() {}

    @Override
    void parseBody(BytesReader bytesReader) throws IOException {
        value = bytesReader.readDouble();
    }

    @Override
    public String toStringValue() {
        return String.valueOf(getValue());
    }

    @Override
    public DoubleConstant toConstant(ParsingConstantPool constantPool, List<Attribute> attributes, int mayor) {
        return new DoubleConstant(value);
    }

    @Override
    public String toString() {
        return "DoubleConstant{" + value + '}';
    }
}
