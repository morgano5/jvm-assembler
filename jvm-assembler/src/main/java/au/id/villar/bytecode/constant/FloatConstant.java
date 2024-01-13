package au.id.villar.bytecode.constant;

import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public final class FloatConstant extends ValueConstant {

    private float value;

    public FloatConstant(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    FloatConstant() {}

    @Override
    void parseBody(BytesReader bytesReader) throws IOException {
        value = bytesReader.readFloat();
    }

    @Override
    public String toStringValue() {
        return String.valueOf(getValue());
    }

    @Override
    public String toString() {
        return "FloatConstant{" + value + '}';
    }
}
