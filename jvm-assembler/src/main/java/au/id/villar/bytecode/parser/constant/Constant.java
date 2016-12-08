package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.Map;

public abstract class Constant {

	public static Constant readConstant(BytesReader bytesReader) throws IOException {

		Constant constant;
		int tag = bytesReader.readByte();

		switch(tag) {
			case 1: constant = new Utf8Constant(); break;

			case 3: constant = new IntegerConstant(); break;
			case 4: constant = new FloatConstant(); break;
			case 5: constant = new LongConstant(); break;
			case 6: constant = new DoubleConstant(); break;
			case 7: constant = new ClassConstant(); break;
			case 8: constant = new StringConstant(); break;
			case 9: constant = new FieldRefConstant(); break;
			case 10: constant = new MethodRefConstant(); break;
			case 11: constant = new InterfaceMethodRefConstant(); break;
			case 12: constant = new NameAndTypeConstant(); break;


			case 15: constant = new MethodHandleConstant(); break;
			case 16: constant = new MethodTypeConstant(); break;

			case 18: constant = new InvokeDynamicConstant(); break;
			default: throw new IOException("Unexpected constant tag: " + tag);
		}

		constant.parseBody(bytesReader);
		return constant;
	}

	Constant() {}

	public String toStringValue() {
		throw new RuntimeException("Unexpected constant type: " + getClass().getSimpleName());
	}

	public static String toString(int index, Map<Integer, Constant> constantPool) {

		Constant constant = constantPool.get(index);

		if(constant instanceof ClassConstant) {
			constant = constantPool.get(((ClassConstant) constant).getNameIndex());
		}
		if(constant instanceof StringConstant) {
			constant = constantPool.get(((StringConstant) constant).getStringIndex());
		}

		return constant.toStringValue();
	}

	abstract void parseBody(BytesReader bytesReader) throws IOException;

}
