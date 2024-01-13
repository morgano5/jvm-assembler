package au.id.villar.bytecode.attribute.frame.type;

import au.id.villar.bytecode.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public abstract class EmptyVerificationTypeInfo extends VerificationTypeInfo {

    @Override
    void parseBody(BytesReader bytesReader, ParsingConstantPool constantPool) throws IOException {
        // DO nothing
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{}";
    }
}
