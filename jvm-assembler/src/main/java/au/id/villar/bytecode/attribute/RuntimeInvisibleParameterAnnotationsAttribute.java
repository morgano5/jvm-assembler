package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.attribute.annotation.Annotation;

import java.util.List;

public class RuntimeInvisibleParameterAnnotationsAttribute extends RuntimeParameterAnnotationsAttribute {

    public RuntimeInvisibleParameterAnnotationsAttribute(List<List<Annotation>> parameterAnnotations) {
        super(parameterAnnotations);
    }

    public RuntimeInvisibleParameterAnnotationsAttribute() {
    }
}
