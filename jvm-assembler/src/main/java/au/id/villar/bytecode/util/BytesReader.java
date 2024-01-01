package au.id.villar.bytecode.util;

import java.io.IOException;
import java.io.InputStream;

public class BytesReader {

    private InputStream stream;
    private final byte[] buffer = new byte[8192];

    public BytesReader(InputStream stream) {
        reuse(stream);
    }

    public BytesReader reuse(InputStream stream) {
        this.stream = stream;
        return this;
    }

    public void readMinimum(byte[] buffer, int minimum) throws IOException {
        int read = stream.read(buffer, 0, minimum);
        throwEndOfStreamIfTrue(read < minimum);
    }

    public int readByte() throws IOException {
        readMinimum(buffer, 1);
        return buffer[0] & 0xFF;
    }

    public int readShort() throws IOException {
        readMinimum(buffer, 2);
        return ((buffer[0] & 0xFF) << 8) | (buffer[1] & 0xFF);
    }

    public int readInt() throws IOException {
        readMinimum(buffer, 4);
        return ((buffer[0] & 0xFF) << 24) | ((buffer[1] & 0xFF) << 16) | ((buffer[2] & 0xFF) << 8) | (buffer[3] & 0xFF);
    }

    public float readFloat() throws IOException {
        int raw = readInt();
        return Float.intBitsToFloat(raw);
    }

    public long readLong() throws IOException {
        return (((long)readInt()) << 32) | (readInt() & 0xFFFFFFFFL);
    }

    public double readDouble() throws IOException {
        long raw = readLong();
        return Double.longBitsToDouble(raw);
    }

    public String readUTF8String() throws IOException {
        return readUTF8String(readShort());
    }

    public String readUTF8String(int length) throws IOException {
        int read = stream.read(buffer, 0, length);
        throwEndOfStreamIfTrue(read != length);
        // TODO implement modified UTF8 instead of the standard one
        return new String(buffer, 0, length, "UTF8");
    }

    private void throwEndOfStreamIfTrue(boolean condition) throws IOException {
        if(condition) throw new IOException("unexpected end of stream");
    }

}
