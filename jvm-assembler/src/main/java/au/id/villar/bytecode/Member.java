package au.id.villar.bytecode;

import au.id.villar.bytecode.attribute.Attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Member {

	private final AccessFlags accessFlags;
	private final String name;
	private final String descriptor;
	private final List<Attribute> attributes;

	public Member(AccessFlags accessFlags, String name, String descriptor, List<Attribute> attributes) {
		this.accessFlags = accessFlags;
		this.name = name;
		this.descriptor = descriptor;
		this.attributes = Collections.unmodifiableList(new ArrayList<>(attributes));
	}

	public AccessFlags getAccessFlags() {
		return accessFlags;
	}

	public String getName() {
		return name;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{" +
				"accessFlags=" + accessFlags +
				", name='" + name + '\'' +
				", descriptor='" + descriptor + '\'' +
				", attributes=" + attributes +
				'}';
	}
}
