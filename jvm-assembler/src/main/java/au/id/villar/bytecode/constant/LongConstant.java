package au.id.villar.bytecode.constant;

import au.id.villar.bytecode.util.BytesReader;
import au.id.villar.bytecode.util.BytesWriter;

import java.io.IOException;

public final class LongConstant extends Constant implements LoadableConstant {

    private long value;

    public LongConstant(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    LongConstant() {}

    @Override
    void parseBody(BytesReader bytesReader) throws IOException {
        value = bytesReader.readLong();
    }

    @Override
    public String toString() {
        return "LongConstant{" + value + '}';
    }

    @Override
    public void write(BytesWriter bytesWriter) throws IOException {
        bytesWriter.writeByte(5);
        bytesWriter.writeLong(value);
    }
}
