package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.attribute.Attribute;
import au.id.villar.bytecode.constant.MethodTypeConstant;

import java.util.List;

public final class MethodTypeParsingConstant
        extends IndexToUtf8ParsingConstant
        implements RuntimeParsingConstant<MethodTypeConstant> {

    public MethodTypeParsingConstant(int descriptorIndex) {
        super(descriptorIndex);
    }

    public int getDescriptorIndex() {
        return utf8Index;
    }

    MethodTypeParsingConstant() {}

    @Override
    public MethodTypeConstant toConstant(ParsingConstantPool constantPool, List<Attribute> attributes, int mayor) {
        return new MethodTypeConstant(constantPool.getStringFromUtf8(utf8Index));
    }

    @Override
    public String toString() {
        return "MethodTypeConstant{" + utf8Index + '}';
    }
}
