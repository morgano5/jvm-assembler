package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.attribute.Attribute;
import au.id.villar.bytecode.constant.FloatConstant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.List;

public final class FloatParsingConstant extends ValueParsingConstant implements RuntimeParsingConstant<FloatConstant> {

    private float value;

    public FloatParsingConstant(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    FloatParsingConstant() {}

    @Override
    void parseBody(BytesReader bytesReader) throws IOException {
        value = bytesReader.readFloat();
    }

    @Override
    public String toStringValue() {
        return String.valueOf(getValue());
    }

    @Override
    public FloatConstant toConstant(ParsingConstantPool constantPool, List<Attribute> attributes, int mayor) {
        return new FloatConstant(value);
    }

    @Override
    public String toString() {
        return "FloatConstant{" + value + '}';
    }
}
