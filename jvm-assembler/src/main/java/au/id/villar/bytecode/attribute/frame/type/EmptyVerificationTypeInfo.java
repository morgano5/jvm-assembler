package au.id.villar.bytecode.attribute.frame.type;

import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public abstract class EmptyVerificationTypeInfo extends VerificationTypeInfo {

    @Override
    void parseBody(BytesReader bytesReader, ConstantPool constantPool) throws IOException {
        // DO nothing
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{}";
    }
}
