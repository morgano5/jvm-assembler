package au.id.villar.bytecode.attribute.annotation;

import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ArrayElementValue extends ElementValue {

	private List<ElementValue> values;

	public ArrayElementValue(List<ElementValue> values) {
		this.values = Collections.unmodifiableList(new ArrayList<>(values));
	}

	ArrayElementValue() {}

	public List<ElementValue> getValues() {
		return values;
	}

	@Override
	void parseBody(BytesReader bytesReader, Map<Integer, Constant> constantPool) throws IOException {
		int size = bytesReader.readShort();
		List<ElementValue> list = new ArrayList<>(size);
		values = Collections.unmodifiableList(list);
		for(int count = 0; count < size; count++) {
			list.add(ElementValue.readElementValue(bytesReader, constantPool));
		}
	}

	@Override
	public String toString() {
		return "ArrayElementValue{" +
				"values=" + values +
				'}';
	}
}
