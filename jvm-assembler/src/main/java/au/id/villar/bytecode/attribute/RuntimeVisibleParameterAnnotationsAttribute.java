package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.attribute.annotation.Annotation;

import java.util.List;

public class RuntimeVisibleParameterAnnotationsAttribute extends RuntimeParameterAnnotationsAttribute {

	public RuntimeVisibleParameterAnnotationsAttribute(List<List<Annotation>> parameterAnnotations) {
		super(parameterAnnotations);
	}

	public RuntimeVisibleParameterAnnotationsAttribute() {
	}
}
