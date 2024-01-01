package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.attribute.annotation.Annotation;

import java.util.List;

public class RuntimeVisibleAnnotationsAttribute extends RuntimeAnnotationsAttribute {

	public RuntimeVisibleAnnotationsAttribute(List<Annotation> annotations) {
		super(annotations);
	}

	RuntimeVisibleAnnotationsAttribute() {}

}
