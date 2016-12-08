package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ExceptionsAttribute extends ListAttribute<String> {

	public ExceptionsAttribute(List<String> exceptions) {
		this.list = Collections.unmodifiableList(new ArrayList<>(exceptions));
	}

	ExceptionsAttribute() {}

	@Override
	String parseElement(BytesReader bytesReader, Map<Integer, Constant> constantPool) throws IOException {
		return Constant.toString(bytesReader.readShort(), constantPool);
	}
}
