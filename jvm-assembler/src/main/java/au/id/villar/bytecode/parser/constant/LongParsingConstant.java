package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.attribute.Attribute;
import au.id.villar.bytecode.constant.LongConstant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.List;

public final class LongParsingConstant extends ValueParsingConstant implements Loadable<LongConstant> {

    private long value;

    public LongParsingConstant(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    LongParsingConstant() {}

    @Override
    void parseBody(BytesReader bytesReader) throws IOException {
        value = bytesReader.readLong();
    }

    @Override
    public String toStringValue() {
        return String.valueOf(getValue());
    }

    @Override
    public LongConstant toConstant(ParsingConstantPool constantPool, List<Attribute> attributes, int mayor) {
        return new LongConstant(value);
    }

    @Override
    public String toString() {
        return "LongConstant{" + value + '}';
    }
}
