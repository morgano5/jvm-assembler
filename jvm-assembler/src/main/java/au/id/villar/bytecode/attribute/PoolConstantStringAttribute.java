package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.constant.Constant;
import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

abstract class PoolConstantStringAttribute extends StringAttribute {

    PoolConstantStringAttribute(String value) {
        super(value);
    }

    PoolConstantStringAttribute() {}

    @Override
    public void parseBody(int length, BytesReader bytesReader, ConstantPool constantPool,
            AttributeGenerator generator) throws IOException {
        value = Constant.toString(bytesReader.readShort(), constantPool);
    }

}
