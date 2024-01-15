package au.id.villar.bytecode;

import au.id.villar.bytecode.attribute.Attribute;

import java.util.List;

class Member {

    private final AccessFlags accessFlags;
    private final Integer nameIndex;
    private final Integer descriptorIndex;
    private final List<Attribute> attributes;

    public Member(AccessFlags accessFlags, Integer nameIndex, Integer descriptorIndex, List<Attribute> attributes) {
        this.accessFlags = accessFlags;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributes = List.copyOf(attributes);
    }

    public AccessFlags getAccessFlags() {
        return accessFlags;
    }

    public Integer getNameIndex() {
        return nameIndex;
    }

    public Integer getDescriptorIndex() {
        return descriptorIndex;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "accessFlags=" + accessFlags +
                ", name='" + nameIndex + '\'' +
                ", descriptor='" + descriptorIndex + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
