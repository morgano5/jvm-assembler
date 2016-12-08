package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.attribute.annotation.TypeAnnotation;

import java.util.List;

public class RuntimeVisibleTypeAnnotationsAttribute extends RuntimeTypeAnnotationsAttribute {

	public RuntimeVisibleTypeAnnotationsAttribute(List<TypeAnnotation> annotations) {
		super(annotations);
	}

	RuntimeVisibleTypeAnnotationsAttribute() {}

}
