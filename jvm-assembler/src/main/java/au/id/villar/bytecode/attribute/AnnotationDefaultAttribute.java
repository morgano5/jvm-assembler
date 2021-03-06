package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class AnnotationDefaultAttribute extends Attribute {

	private byte[] defaultValue;

	public AnnotationDefaultAttribute(byte[] defaultValue) {
		this.defaultValue = new byte[defaultValue.length];
		System.arraycopy(this.defaultValue, 0, defaultValue, 0, defaultValue.length);
	}

	AnnotationDefaultAttribute() {}

	public int getCodeLength() {
		return defaultValue.length;
	}

	public void readDefaultValue(byte[] buffer, int bufferOffset, int codeOffset, int length) {
		System.arraycopy(defaultValue, codeOffset, buffer, bufferOffset, length);
	}

	@Override
	public void parseBody(int length, BytesReader bytesReader, Map<Integer, Constant> constantPool) throws IOException {
		defaultValue = new byte[length];
		bytesReader.readMinimum(defaultValue, length);
	}

	@Override
	public String toString() {
		return "AnnotationDefaultAttribute{" +
				"defaultValue=" + Arrays.toString(defaultValue) +
				'}';
	}
}
