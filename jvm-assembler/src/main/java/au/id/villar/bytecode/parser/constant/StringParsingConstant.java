package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.attribute.Attribute;
import au.id.villar.bytecode.constant.StringConstant;

import java.util.List;

public final class StringParsingConstant extends IndexToUtf8ParsingConstant implements RuntimeParsingConstant<StringConstant> {

    public StringParsingConstant(int stringIndex) {
        super(stringIndex);
    }

    public int getStringIndex() {
        return utf8Index;
    }

    StringParsingConstant() {}

    @Override
    public StringConstant toConstant(ParsingConstantPool constantPool, List<Attribute> attributes, int mayor) {
        return new StringConstant(constantPool.getStringFromUtf8(utf8Index));
    }

    @Override
    public String toString() {
        return "StringConstant{" + utf8Index + '}';
    }
}
