package au.id.villar.bytecode.assembly;

import au.id.villar.bytecode.Class;
import au.id.villar.bytecode.parser.ClassFileParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

class DisassemblerTest {

    @Test
    void shouldDisassemble() throws IOException {
        try (InputStream bytecode = ClassLoader.getSystemResourceAsStream("class/BlockScope.class")) {
            Class aClass = ClassFileParser.parseToClass(bytecode);

            System.out.println("==================\n" + new Disassembler().toAssembly(aClass));

        }
    }

}