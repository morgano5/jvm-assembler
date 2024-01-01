package au.id.villar.bytecode.attribute.frame.type;

import au.id.villar.bytecode.parser.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public abstract class VerificationTypeInfo {

    public static VerificationTypeInfo readVerificationTypeInfo(BytesReader bytesReader,
            ParsingConstantPool constantPool) throws IOException {

        VerificationTypeInfo typeInfo;
        int tag = bytesReader.readByte();

        switch(tag) {
            case 0: typeInfo = new TopVerificationTypeInfo(); break;
            case 1: typeInfo = new IntegerVerificationTypeInfo(); break;
            case 2: typeInfo = new FloatVerificationTypeInfo(); break;
            case 3: typeInfo = new DoubleVerificationTypeInfo(); break;
            case 4: typeInfo = new LongVerificationTypeInfo(); break;
            case 5: typeInfo = new NullVerificationTypeInfo(); break;
            case 6: typeInfo = new UninitializedThisVerificationTypeInfo(); break;
            case 7: typeInfo = new ObjectVerificationTypeInfo(); break;
            case 8: typeInfo = new UninitializedVerificationTypeInfo(); break;
            default: throw new IOException("Unexpected verification type info tag: " + tag);
        }

        typeInfo.parseBody(bytesReader, constantPool);
        return typeInfo;
    }

    VerificationTypeInfo() {}

    abstract void parseBody(BytesReader bytesReader, ParsingConstantPool constantPool) throws IOException;
}

