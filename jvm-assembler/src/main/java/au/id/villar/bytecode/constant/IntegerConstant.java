package au.id.villar.bytecode.constant;

import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public final class IntegerConstant extends ValueConstant {

    private int value;

    public IntegerConstant(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    IntegerConstant() {}

    @Override
    void parseBody(BytesReader bytesReader) throws IOException {
        value = bytesReader.readInt();
    }

    @Override
    public String toStringValue() {
        return String.valueOf(getValue());
    }

    @Override
    public String toString() {
        return "IntegerConstant{" + value + '}';
    }
}
