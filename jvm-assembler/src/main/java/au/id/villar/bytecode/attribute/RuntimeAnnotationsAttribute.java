package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.attribute.annotation.Annotation;
import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

abstract class RuntimeAnnotationsAttribute extends ListAttribute<Annotation> {

	public RuntimeAnnotationsAttribute(List<Annotation> annotations) {
		this.list = Collections.unmodifiableList(new ArrayList<>(annotations));
	}

	RuntimeAnnotationsAttribute() {}

	@Override
	Annotation parseElement(BytesReader bytesReader, Map<Integer, Constant> constantPool) throws IOException {
		return Annotation.readAnnotation(bytesReader, constantPool);
	}
}
