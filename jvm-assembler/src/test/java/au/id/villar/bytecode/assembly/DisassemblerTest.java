package au.id.villar.bytecode.assembly;

import au.id.villar.bytecode.Class;
import au.id.villar.bytecode.parser.ClassFileParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

class DisassemblerTest {

    @Test
    void shouldDisassemble() throws IOException {
        try (InputStream bytecode = ClassLoader.getSystemResourceAsStream("class/BlockScope.class")) {
            Class aClass = ClassFileParser.parseToClass(bytecode);

            Writer writer = new StringWriter();
            Disassembler.toAssembly(aClass, writer);
            System.out.println("==================\n" + writer);

        }
    }
}
