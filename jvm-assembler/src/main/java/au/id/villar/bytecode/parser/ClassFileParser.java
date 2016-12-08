package au.id.villar.bytecode.parser;

import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.parser.constant.DoubleConstant;
import au.id.villar.bytecode.parser.constant.LongConstant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.io.InputStream;

public class ClassFileParser {

	private static final InputStream INVALID_INPUT_STREAM = new InvalidInputStream();
	private static final ClassFileHandler DUMMY_HANDLER = new ClassFileHandler();

	private final byte[] buffer = new byte[8192];
	private final RestrictedInputStream attributeInfo = new RestrictedInputStream(0, INVALID_INPUT_STREAM);

	private InputStream bytecode = INVALID_INPUT_STREAM;
	private BytesReader bytesReader = new BytesReader(bytecode);
	private ClassFileHandler handler = DUMMY_HANDLER;

	public void setBytecodeStream(InputStream bytecode) {
		this.bytecode = bytecode != null? bytecode: INVALID_INPUT_STREAM;
		bytesReader = new BytesReader(this.bytecode);
	}

	public void setHandler(ClassFileHandler handler) {
		this.handler = handler != null? handler: DUMMY_HANDLER;
	}

	public void parse() throws IOException {

		int size;
		int index;

		bytesReader.readMinimum(buffer, 4);

		if(buffer[0] != (byte)0xCA || buffer[1] != (byte)0xFE
				|| buffer[2] != (byte)0xBA || buffer[3] != (byte)0xBE) {
			throw new IOException("Not a stream containing a class file");
		}

		handler.number(bytesReader.readShort(), ClassFileHandler.NumberType.MINOR);
		handler.number(bytesReader.readShort(), ClassFileHandler.NumberType.MAYOR);
		size = bytesReader.readShort() - 1;
		handler.number(size, ClassFileHandler.NumberType.CONSTANT_POOL);

		index = 1;
		while(index <= size) {
			Constant constant = Constant.readConstant(bytesReader);
			handler.constant(constant, index++);
			if(constant.getClass() == LongConstant.class || constant.getClass() == DoubleConstant.class) {
				index++; // poor design in the class file format
			}
		}

		handler.number(bytesReader.readShort(), ClassFileHandler.NumberType.ACCESS_FLAGS);
		handler.number(bytesReader.readShort(), ClassFileHandler.NumberType.THIS_INDEX);
		handler.number(bytesReader.readShort(), ClassFileHandler.NumberType.SUPER_INDEX);
		size = bytesReader.readShort();
		handler.number(size, ClassFileHandler.NumberType.INTERFACES);

		for(index = 0; index < size; index++) {
			handler.interfaceIndex(bytesReader.readShort());
		}

		size = bytesReader.readShort();
		handler.number(size, ClassFileHandler.NumberType.FIELDS);
		for(index = 0; index < size; index++) {
			handler.field(bytesReader.readShort(), bytesReader.readShort(), bytesReader.readShort());
			readAttributes();
		}

		size = bytesReader.readShort();
		handler.number(size, ClassFileHandler.NumberType.METHODS);
		for(index = 0; index < size; index++) {
			handler.method(bytesReader.readShort(), bytesReader.readShort(), bytesReader.readShort());
			readAttributes();
		}

		readAttributes();
	}

	BytesReader getBytesReader() {
		return bytesReader;
	}


	private void readAttributes() throws IOException {

		int size = bytesReader.readShort();
		int nameIndex;
		int infoSize;

		handler.number(size, ClassFileHandler.NumberType.ATTRIBUTES);
		for(int index = 0; index < size; index++) {
			nameIndex = bytesReader.readShort();
			infoSize = bytesReader.readInt();
			handler.attribute(nameIndex, infoSize, attributeInfo.reuse(infoSize, bytecode));
			attributeInfo.consumeRemaining();
		}
	}

}
