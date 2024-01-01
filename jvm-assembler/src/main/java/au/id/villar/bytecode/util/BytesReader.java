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
        if(read < minimum) {
            throw new IOException("Unexpected end of stream");
        }
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
        StringBuilder builder = new StringBuilder(length);

        while (length > 0) {
            readMinimum(buffer, 1);
            length--;
            int ch1 = 0xFF & buffer[0];
            throwUnexpectedByteIfTrue((byte)ch1, ch1 == 0 || ch1 >= 0xF0);
            if (ch1 < 0x80) {
                builder.appendCodePoint(ch1);
            } else if (ch1 < 0xE0) {
                readMinimum(buffer, 1);
                throwUnexpectedByteIfTrue(buffer[0], (buffer[0] & 0xC0) != 0x80);
                length--;
                builder.append((char)(((ch1 & 0x1F) << 6) | (buffer[0] & 0x3F)));
            } else if (ch1 == 0xED) {
                readMinimum(buffer, 5);
                throwUnexpectedByteIfTrue(buffer[0], (buffer[0] & 0xF0) != 0xA0);
                throwUnexpectedByteIfTrue(buffer[1], (buffer[1] & 0xC0) != 0x80);
                throwUnexpectedByteIfTrue(buffer[2], (buffer[2] & 0xFF) != 0xED);
                throwUnexpectedByteIfTrue(buffer[3], (buffer[3] & 0xF0) != 0xB0);
                throwUnexpectedByteIfTrue(buffer[4], (buffer[4] & 0xC0) != 0x80);
                length -= 5;
                builder.appendCodePoint((((buffer[0] & 0xF) << 16) | ((buffer[1] & 0x3F) << 10) |
                        ((buffer[3] & 0xF) << 6) | (buffer[4] & 0x3F)));
            } else {
                readMinimum(buffer, 2);
                throwUnexpectedByteIfTrue(buffer[0], (buffer[0] & 0xC0) != 0x80);
                throwUnexpectedByteIfTrue(buffer[1], (buffer[1] & 0xC0) != 0x80);
                length -= 2;
                builder.appendCodePoint((((ch1 & 0xF) << 12) | ((buffer[0] & 0x3F) << 6) | (buffer[1] & 0x3F)));
            }
        }
        return builder.toString();
    }

    private void throwUnexpectedByteIfTrue(byte octect, boolean condition) throws IOException {
        if (condition) {
            throw new IOException(String.format("Unexpected byte 0x%2X reading a modified-UTF8 string", octect & 0xFF));
        }
    }
}
