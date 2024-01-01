package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.attribute.Attribute;
import au.id.villar.bytecode.constant.ClassConstant;

import java.util.List;

public final class ClassParsingConstant extends IndexToUtf8ParsingConstant implements Loadable<ClassConstant> {

    public ClassParsingConstant(int nameIndex) {
        super(nameIndex);
    }

    public int getNameIndex() {
        return utf8Index;
    }

    ClassParsingConstant() {}

    @Override
    public ClassConstant toConstant(ParsingConstantPool constantPool, List<Attribute> attributes, int mayor) {
        return new ClassConstant(constantPool.getStringFromUtf8(utf8Index));
    }

    @Override
    public String toString() {
        return "ClassConstant{" + utf8Index + '}';
    }
}
