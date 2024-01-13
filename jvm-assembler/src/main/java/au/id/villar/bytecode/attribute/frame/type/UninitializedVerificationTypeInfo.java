package au.id.villar.bytecode.attribute.frame.type;

import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public class UninitializedVerificationTypeInfo extends VerificationTypeInfo {

    int offset;

    @Override
    void parseBody(BytesReader bytesReader, ConstantPool constantPool) throws IOException {
        offset = bytesReader.readShort();
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return "UninitializedVerificationTypeInfo{" +
                "offset=" + offset +
                '}';
    }
}
