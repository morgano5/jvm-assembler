package au.id.villar.bytecode.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BytesWriter {

    private OutputStream stream;

    public BytesWriter(OutputStream stream) {
        reuse(stream);
    }

    public BytesWriter reuse(OutputStream stream) {
        this.stream = stream;
        return this;
    }

    public void writeByte(int value) throws IOException {
        stream.write(value & 0xFF);
    }

    public void writeShort(int value) throws IOException {
        stream.write((value >> 8) & 0xFF);
        stream.write(value & 0xFF);
    }

    public void writeInt(int value) throws IOException {
        stream.write((value >> 24) & 0xFF);
        stream.write((value >> 16) & 0xFF);
        stream.write((value >> 8) & 0xFF);
        stream.write(value & 0xFF);
    }

    public void writeLong(long value) throws IOException {
        writeInt((int)((value >> 32) & 0xFF));
        stream.write((int)value);
    }

    public void writeFloat(float value) throws IOException {
        writeInt(Float.floatToRawIntBits(value));
    }

    public void writeDouble(double value) throws IOException {
        writeLong(Double.doubleToRawLongBits(value));
    }

    public void writeModUtf8(String value) throws IOException {
        byte[] rawString = value.getBytes(StandardCharsets.UTF_8);
        int length = rawString.length;

        for (int index = 0; index < rawString.length; index++) {
            int x = rawString[index] & 0xFF;
            if (x == 0) {
                length++;
            } else if ((x & 0xF0) == 0xF0) {
                length += 2;
                index += 3;
            }
        }

        writeShort(length);

        for (int index = 0; index < rawString.length; index++) {
            int x = rawString[index] & 0xFF;
            if (x == 0) {
                writeShort(0xC080);
            } else if ((x & 0xF0) == 0xF0) {
                x = ((((((x & 0x7) << 6) | (rawString[++index] & 0x3F)) << 6)
                        | (rawString[++index] & 0x3F)) << 6) | (rawString[++index] & 0x3F);
                writeByte(0xED);
                writeByte(0xA0 | ((x >> 16) & 0xF));
                writeByte(0x80 | ((x >> 10) & 0x3F));
                writeByte(0xED);
                writeByte(0xB0 | ((x >> 6) & 0xF));
                writeByte(0x80 | (x & 0x3F));
            } else {
                writeByte(x);
            }
        }
    }
}
