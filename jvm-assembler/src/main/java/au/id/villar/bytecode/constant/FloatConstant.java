package au.id.villar.bytecode.constant;

import au.id.villar.bytecode.util.BytesReader;
import au.id.villar.bytecode.util.BytesWriter;

import java.io.IOException;

public final class FloatConstant extends Constant implements LoadableConstant {

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
    public String toString() {
        return "FloatConstant{" + value + '}';
    }

    @Override
    public void write(BytesWriter bytesWriter) throws IOException {
        bytesWriter.writeByte(4);
        bytesWriter.writeFloat(value);
    }
}
