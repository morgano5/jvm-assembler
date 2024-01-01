package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.attribute.annotation.Annotation;

import java.util.List;

public class RuntimeInvisibleAnnotationsAttribute extends RuntimeAnnotationsAttribute {

    public RuntimeInvisibleAnnotationsAttribute(List<Annotation> annotations) {
        super(annotations);
    }

    RuntimeInvisibleAnnotationsAttribute() {}

}
