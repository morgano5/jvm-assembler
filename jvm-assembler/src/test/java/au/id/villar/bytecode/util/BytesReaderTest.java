package au.id.villar.bytecode.util;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BytesReaderTest {

    @Test
    void shouldReadInteger() throws IOException {
        BytesReader reader = createStream(1, 2, 3, 4, 5, 6, 7, 8);

        assertEquals(0x01020304, reader.readInt(), "Should read integer");
        assertEquals(0x05060708, reader.readInt(), "Should read integer");
    }

    @Test
    void shouldThrowIoExceptionWithExplanatoryMessage() throws IOException {
        BytesReader reader = createStream();

        IOException e = assertThrows(IOException.class, reader::readInt,
                "Should throw IOException then there are no more bytes");

        assertEquals("Unexpected end of stream", e.getMessage(), "Should give an explanatory message");
    }

    @Test
    void shouldReadModifiedUtf() throws IOException {

        BytesReader reader = createStream(0x00, 0x02, 0xC0, 0x80,   // \0   (0x00)
                0x00, 0x01, 0x58,                                   // 'X'  (0x88)
                0x00, 0x02, 0xC3, 0xB1,                             // 'ñ'  (0xF1)
                0x00, 0x03, 0xE0, 0xA4, 0xB9,                       // 'ह'  (0x939)
                0x00, 0x06, 0xED, 0xA1, 0x80, 0xED, 0xBD, 0x88);    // '𐍈'  (0x10348)

        assertEquals("\0", reader.readModUtf8(), "Should read char 0 of modified UTF8");
        assertEquals("X", reader.readModUtf8(), "Should read range 1 to 0x7F of modified UTF8");
        assertEquals("ñ", reader.readModUtf8(), "Should read range 0x80 to 0x7FF of modified UTF8");
        assertEquals("ह", reader.readModUtf8(), "Should read range 0x800 to 0xFFFF of modified UTF8");
        assertEquals("𐍈", reader.readModUtf8(), "Should read range 0x10000 to 0x10FFFF of modified UTF8");
    }

    private BytesReader createStream(int ... bytes) {
        int size = bytes.length;
        byte[] buffer = new byte[size];
        for (int x = 0; x < size; x++) {
            buffer[x] = (byte)bytes[x];
        }
        return new BytesReader(new ByteArrayInputStream(buffer));
    }
}