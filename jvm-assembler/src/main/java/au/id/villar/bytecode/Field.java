package au.id.villar.bytecode;

import au.id.villar.bytecode.attribute.Attribute;

import java.util.List;

public class Field extends Member {

    public Field(AccessFlags accessFlags, Integer nameIndex, Integer descriptorIndex, List<Attribute> attributes) {
        super(accessFlags, nameIndex, descriptorIndex, attributes);
    }

}
