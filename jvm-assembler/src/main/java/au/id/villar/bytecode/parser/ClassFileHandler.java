package au.id.villar.bytecode.parser;

import au.id.villar.bytecode.parser.constant.Constant;

import java.io.InputStream;

public class ClassFileHandler {

	public enum NumberType {
		MAYOR,
		MINOR,

		ACCESS_FLAGS,
		THIS_INDEX,
		SUPER_INDEX,

		CONSTANT_POOL,
		INTERFACES,
		FIELDS,
		METHODS,
		ATTRIBUTES
	}

	public void number(int number, NumberType numberType) {}

	public void constant(Constant constant, int index) {}

	public void interfaceIndex(int constantPoolIndex) {}

	public void field(int accessFlags, int nameIndex, int descriptorIndex) {}

	public void method(int accessFlags, int nameIndex, int descriptorIndex) {}

	public void attribute(int nameIndex, int length, InputStream info) {}

}
