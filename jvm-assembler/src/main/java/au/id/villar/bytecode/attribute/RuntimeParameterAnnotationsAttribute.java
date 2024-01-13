package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.attribute.annotation.Annotation;
import au.id.villar.bytecode.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class RuntimeParameterAnnotationsAttribute extends ListAttribute<List<Annotation>> {

    protected RuntimeParameterAnnotationsAttribute(List<List<Annotation>> parameterAnnotations) {
        List<List<Annotation>> list = new ArrayList<>();
        for(List<Annotation> subList: parameterAnnotations) {
            list.add(Collections.unmodifiableList(new ArrayList<>(subList)));
        }
        this.list = Collections.unmodifiableList(list);
    }

    RuntimeParameterAnnotationsAttribute() {}

    @Override
    List<Annotation> parseElement(BytesReader bytesReader, ParsingConstantPool constantPool) throws IOException {
        int numAnnotations = bytesReader.readShort();
        List<Annotation> subList = new ArrayList<>(numAnnotations);
        for (int subCount = 0; subCount < numAnnotations; subCount++) {
            subList.add(Annotation.readAnnotation(bytesReader, constantPool));
        }
        return Collections.unmodifiableList(subList);
    }
}
