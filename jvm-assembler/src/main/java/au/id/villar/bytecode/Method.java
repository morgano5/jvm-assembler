package au.id.villar.bytecode;

import au.id.villar.bytecode.attribute.Attribute;

import java.util.List;

public class Method extends Member {

    public Method(AccessFlags accessFlags, Integer nameIndex, Integer descriptorIndex, List<Attribute> attributes) {
        super(accessFlags, nameIndex, descriptorIndex, attributes);
    }

}
