package au.id.villar.bytecode.assembly;

import au.id.villar.bytecode.ClassFile;
import au.id.villar.bytecode.compiler.ClassFileParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

class DisassemblerTest {

    @Test
    void shouldDisassemble() throws IOException {
        try (InputStream bytecode = ClassLoader.getSystemResourceAsStream("class/BlockScope.class")) {
            ClassFile classFile = ClassFileParser.parseToClass(bytecode);

            Writer writer = new StringWriter();
            Disassembler.toAssembly(classFile, writer);
            System.out.println("==================\n" + writer);

        }
    }
}
