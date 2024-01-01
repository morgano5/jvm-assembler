package au.id.villar.bytecode;

import au.id.villar.bytecode.attribute.Attribute;

import java.util.List;

public class Field extends Member {

    public Field(AccessFlags accessFlags, String name, String descriptor, List<Attribute> attributes) {
        super(accessFlags, name, descriptor, attributes);
    }

}
