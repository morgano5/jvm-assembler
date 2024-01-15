package au.id.villar.bytecode.constant;

import au.id.villar.bytecode.util.BytesReader;
import au.id.villar.bytecode.util.BytesWriter;

import java.io.IOException;

public final class IntegerConstant extends Constant implements LoadableConstant {

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
    public String toString() {
        return "IntegerConstant{" + value + '}';
    }

    @Override
    public void write(BytesWriter bytesWriter) throws IOException {
        bytesWriter.writeByte(3);
        bytesWriter.writeInt(value);
    }
}
