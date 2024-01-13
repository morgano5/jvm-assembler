package au.id.villar.bytecode.attribute.annotation;

import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public class AnnotationElementValue extends ElementValue {

    private Annotation value;

    public AnnotationElementValue(Annotation value) {
        this.value = value;
    }

    AnnotationElementValue() {}

    public Annotation getValue() {
        return value;
    }

    @Override
    void parseBody(BytesReader bytesReader, ConstantPool constantPool) throws IOException {
        value = Annotation.readAnnotation(bytesReader, constantPool);
    }

    @Override
    public String toString() {
        return "AnnotationElementValue{" +
                "value=" + value +
                '}';
    }
}
