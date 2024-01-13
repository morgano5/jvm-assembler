package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public interface AttributeGenerator {

    <T extends Attribute> T readAttribute(Class<T> type, int length, BytesReader bytesReader,
        ParsingConstantPool constantPool, AttributeGenerator generator) throws IOException;

    GenericAttribute readGenericAttribute(String name, int length, BytesReader bytesReader,
        ParsingConstantPool constantPool, AttributeGenerator generator) throws IOException;
}
