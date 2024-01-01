package au.id.villar.bytecode.attribute.frame.type;

import au.id.villar.bytecode.parser.constant.ParsingConstant;
import au.id.villar.bytecode.parser.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public class ObjectVerificationTypeInfo extends VerificationTypeInfo {

    private String className;

    public ObjectVerificationTypeInfo(String className) {
        this.className = className;
    }

    ObjectVerificationTypeInfo() {}

    public String getClassName() {
        return className;
    }

    @Override
    void parseBody(BytesReader bytesReader, ParsingConstantPool constantPool) throws IOException {
        int poolIndex = bytesReader.readShort();
        className = ParsingConstant.toString(poolIndex, constantPool);
    }

    @Override
    public String toString() {
        return "ObjectVerificationTypeInfo{" +
                "className='" + className + '\'' +
                '}';
    }
}
