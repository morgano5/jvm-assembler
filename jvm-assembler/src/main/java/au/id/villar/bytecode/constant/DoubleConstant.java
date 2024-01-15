package au.id.villar.bytecode.constant;

import au.id.villar.bytecode.util.BytesReader;
import au.id.villar.bytecode.util.BytesWriter;

import java.io.IOException;

public final class DoubleConstant extends Constant implements LoadableConstant {

    private double value;

    public DoubleConstant(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    DoubleConstant() {}

    @Override
    void parseBody(BytesReader bytesReader) throws IOException {
        value = bytesReader.readDouble();
    }

    @Override
    public String toString() {
        return "DoubleConstant{" + value + '}';
    }

    @Override
    public void write(BytesWriter bytesWriter) throws IOException {
        bytesWriter.writeByte(6);
        bytesWriter.writeDouble(value);
    }
}
