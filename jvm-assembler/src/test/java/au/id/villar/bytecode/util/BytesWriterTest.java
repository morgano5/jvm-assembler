package au.id.villar.bytecode.util;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class BytesWriterTest {

    @Test
    void shouldWriteModifiedUtf() throws IOException {

        ByteArrayOutputStream baos;

        baos = new ByteArrayOutputStream();
        new BytesWriter(baos).writeModUtf8("\0");
        byte[] test0x0 = baos.toByteArray();
        baos = new ByteArrayOutputStream();
        new BytesWriter(baos).writeModUtf8("X");
        byte[] test0x88 = baos.toByteArray();
        baos = new ByteArrayOutputStream();
        new BytesWriter(baos).writeModUtf8("ñ");
        byte[] test0xF1 = baos.toByteArray();
        baos = new ByteArrayOutputStream();
        new BytesWriter(baos).writeModUtf8("ह");
        byte[] test0x939 = baos.toByteArray();
        baos = new ByteArrayOutputStream();
        new BytesWriter(baos).writeModUtf8("𐍈");
        byte[] test0x10348 = baos.toByteArray();

        assertArrayEquals(data(0x00, 0x02, 0xC0, 0x80), test0x0, "Should write char 0 of modified UTF8");
        assertArrayEquals(data(0x00, 0x01, 0x58), test0x88, "Should write range 1 to 0x7F of modified UTF8");
        assertArrayEquals(data(0x00, 0x02, 0xC3, 0xB1), test0xF1, "Should write range 0x80 to 0x7FF of modified UTF8");
        assertArrayEquals(data(0x00, 0x03, 0xE0, 0xA4, 0xB9), test0x939,
                "Should write range 0x800 to 0xFFFF of modified UTF8");
        assertArrayEquals(data(0x00, 0x06, 0xED, 0xA1, 0x80, 0xED, 0xBD, 0x88), test0x10348,
                "Should write range 0x10000 to 0x10FFFF of modified UTF8");
    }

    private byte[] data(int ... data) {
        byte[] r = new byte[data.length];
        int i = 0;
        while (i < r.length) {
            r[i] = (byte)data[i++];
        }
        return r;
    }
}
