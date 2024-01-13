package au.id.villar.bytecode.attribute.frame.type;

import au.id.villar.bytecode.constant.Constant;
import au.id.villar.bytecode.constant.ConstantPool;
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
    void parseBody(BytesReader bytesReader, ConstantPool constantPool) throws IOException {
        int poolIndex = bytesReader.readShort();
        className = Constant.toString(poolIndex, constantPool);
    }

    @Override
    public String toString() {
        return "ObjectVerificationTypeInfo{" +
                "className='" + className + '\'' +
                '}';
    }
}
