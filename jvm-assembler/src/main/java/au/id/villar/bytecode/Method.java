package au.id.villar.bytecode;

import au.id.villar.bytecode.attribute.Attribute;

import java.util.List;

public class Method extends Member {

    public Method(AccessFlags accessFlags, String name, String descriptor, List<Attribute> attributes) {
        super(accessFlags, name, descriptor, attributes);
    }

}
