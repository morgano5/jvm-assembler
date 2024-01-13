package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.attribute.annotation.Annotation;
import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class RuntimeAnnotationsAttribute extends ListAttribute<Annotation> {

    public RuntimeAnnotationsAttribute(List<Annotation> annotations) {
        this.list = Collections.unmodifiableList(new ArrayList<>(annotations));
    }

    RuntimeAnnotationsAttribute() {}

    @Override
    Annotation parseElement(BytesReader bytesReader, ConstantPool constantPool) throws IOException {
        return Annotation.readAnnotation(bytesReader, constantPool);
    }
}
