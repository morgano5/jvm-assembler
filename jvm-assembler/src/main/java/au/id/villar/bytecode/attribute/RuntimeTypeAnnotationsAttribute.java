package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.attribute.annotation.TypeAnnotation;
import au.id.villar.bytecode.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class RuntimeTypeAnnotationsAttribute extends ListAttribute<TypeAnnotation> {

    public RuntimeTypeAnnotationsAttribute(List<TypeAnnotation> annotations) {
        this.list = Collections.unmodifiableList(new ArrayList<>(annotations));
    }

    RuntimeTypeAnnotationsAttribute() {}

    @Override
    TypeAnnotation parseElement(BytesReader bytesReader, ParsingConstantPool constantPool) throws IOException {
        return TypeAnnotation.readTypeAnnotation(bytesReader, constantPool);
    }
}
