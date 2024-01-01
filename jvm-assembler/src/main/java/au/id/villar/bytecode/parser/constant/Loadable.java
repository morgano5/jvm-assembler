package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.attribute.Attribute;
import au.id.villar.bytecode.constant.Constant;

import java.util.List;

public interface Loadable<T extends Constant> {

    T toConstant(ParsingConstantPool constantPool, List<Attribute> attributes, int mayor);

}
