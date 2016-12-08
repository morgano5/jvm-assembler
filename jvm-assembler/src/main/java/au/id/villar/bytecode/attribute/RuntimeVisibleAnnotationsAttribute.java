package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.attribute.annotation.Annotation;
import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RuntimeVisibleAnnotationsAttribute extends RuntimeAnnotationsAttribute {

	public RuntimeVisibleAnnotationsAttribute(List<Annotation> annotations) {
		super(annotations);
	}

	RuntimeVisibleAnnotationsAttribute() {}

}
