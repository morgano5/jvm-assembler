package au.id.villar.bytecode.compiler;

import java.io.IOException;
import java.io.InputStream;

class InvalidInputStream extends InputStream {

    @Override
    public int read() throws IOException {
        throw new IOException("InputStream not specified");
    }

}
