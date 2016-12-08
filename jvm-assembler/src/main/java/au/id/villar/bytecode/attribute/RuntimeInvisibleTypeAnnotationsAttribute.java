package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.attribute.annotation.TypeAnnotation;

import java.util.List;

public class RuntimeInvisibleTypeAnnotationsAttribute extends RuntimeTypeAnnotationsAttribute {

	public RuntimeInvisibleTypeAnnotationsAttribute(List<TypeAnnotation> annotations) {
		super(annotations);
	}

	RuntimeInvisibleTypeAnnotationsAttribute() {}
}
