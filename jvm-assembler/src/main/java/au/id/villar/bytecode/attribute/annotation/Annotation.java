package au.id.villar.bytecode.attribute.annotation;

import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Annotation {

	private String type;
	private List<ElementValuePair> values;

	public Annotation(String type, List<ElementValuePair> values) {
		this.type = type;
		this.values = Collections.unmodifiableList(new ArrayList<>(values));
	}

	private Annotation() {}

	public String getType() {
		return type;
	}

	public List<ElementValuePair> getValues() {
		return values;
	}

	public static Annotation readAnnotation(BytesReader bytesReader, Map<Integer, Constant> constantPool)
			throws IOException {
		Annotation annotation = new Annotation();
		annotation.type = Constant.toString(bytesReader.readShort(), constantPool);
		int numElementValuePairs = bytesReader.readShort();
		List<ElementValuePair> list = new ArrayList<>(numElementValuePairs);
		annotation.values = Collections.unmodifiableList(list);
		for(int count = 0; count < numElementValuePairs; count++) {
			list.add(ElementValuePair.readElementValuePair(bytesReader, constantPool));
		}
		return annotation;
	}

	@Override
	public String toString() {
		return "Annotation{" +
				"type='" + type + '\'' +
				", values=" + values +
				'}';
	}
}
