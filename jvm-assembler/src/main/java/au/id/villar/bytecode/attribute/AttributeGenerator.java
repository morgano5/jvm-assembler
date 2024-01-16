package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public interface AttributeGenerator {

    <T extends Attribute> T readAttribute(Class<T> type, Integer nameIndex, int length, BytesReader bytesReader,
        ConstantPool constantPool, AttributeGenerator generator) throws IOException;

}
