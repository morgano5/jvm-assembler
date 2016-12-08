package au.id.villar.bytecode.attribute.annotation;

import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.Map;

public abstract class ElementValue {


	public static ElementValue readElementValue(BytesReader bytesReader, Map<Integer, Constant> constantPool)
			throws IOException {

		ElementValue elementValue;
		int tag = bytesReader.readByte();
		switch(tag) {
			case 'B':case 'C':case 'D':case 'F':case 'I':case 'J':case 'S':case 'Z':case 's':case 'c':
				elementValue = new SimpleElementValue();
				break;
			case '[':
				elementValue = new ArrayElementValue();
				break;
			case 'e':
				elementValue = new EnumConstElementValue();
				break;
			case '@':
				elementValue = new AnnotationElementValue();
				break;
			default:
				throw new IOException("elementValue tag not known: " + tag);
		}
		elementValue.parseBody(bytesReader, constantPool);
		return elementValue;
	}

	ElementValue() {}

	abstract void parseBody(BytesReader bytesReader, Map<Integer, Constant> constantPool) throws IOException;

}
