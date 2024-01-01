package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.attribute.Attribute;
import au.id.villar.bytecode.constant.DynamicConstant;

import java.util.List;

public final class DynamicParsingConstant extends AbstractDynamicParsingConstant implements Loadable<DynamicConstant> {

    public DynamicParsingConstant(int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
        super(bootstrapMethodAttrIndex, nameAndTypeIndex);
    }

    DynamicParsingConstant() {}

    @Override
    public DynamicConstant toConstant(ParsingConstantPool constantPool, List<Attribute> attributes, int mayor) {

        NameAndTypeParsingConstant nameAndTypeConstant =
                constantPool.get(nameAndTypeIndex, NameAndTypeParsingConstant.class);
        String name = constantPool.getStringFromUtf8(nameAndTypeConstant.getNameIndex());
        String type = constantPool.getStringFromUtf8(nameAndTypeConstant.getDescriptorIndex());

//        return null;
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public String toString() {
        return "DynamicConstant{" +
                "bootstrapMethodAttrIndex=" + bootstrapMethodAttrIndex +
                ", nameAndTypeIndex=" + nameAndTypeIndex +
                '}';
    }
}
