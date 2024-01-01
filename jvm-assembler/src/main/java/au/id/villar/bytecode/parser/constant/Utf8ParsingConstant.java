package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public final class Utf8ParsingConstant extends ValueParsingConstant {

    private String value;

    public Utf8ParsingConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    Utf8ParsingConstant() {}

    @Override
    void parseBody(BytesReader bytesReader) throws IOException {
        value = bytesReader.readUTF8String();
    }

    @Override
    public String toStringValue() {
        return getValue();
    }

    @Override
    public String toString() {
        return "Utf8Constant{'" + value + "'}";
    }
}
