package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.parser.constant.ParsingConstant;
import au.id.villar.bytecode.parser.constant.NameAndTypeParsingConstant;
import au.id.villar.bytecode.parser.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.Map;

public class EnclosingMethodAttribute extends Attribute {

    private MemberReference method;

    public EnclosingMethodAttribute(MemberReference method) {
        this.method = method;
    }

    EnclosingMethodAttribute() {}

    public MemberReference getMethod() {
        return method;
    }

    @Override
    public void parseBody(int length, BytesReader bytesReader, ParsingConstantPool constantPool) throws IOException {
        String className = ParsingConstant.toString(bytesReader.readShort(), constantPool);
        NameAndTypeParsingConstant methodInfo = (NameAndTypeParsingConstant)constantPool.get(bytesReader.readShort());
        String methodName = ParsingConstant.toString(methodInfo.getNameIndex(), constantPool);
        String methodDescriptor = ParsingConstant.toString(methodInfo.getDescriptorIndex(), constantPool);
        method = new MemberReference(MemberReference.Type.METHOD, className, methodName, methodDescriptor);
    }

    @Override
    public String toString() {
        return "EnclosingMethodAttribute{" +
                "method=" + method +
                '}';
    }
}
