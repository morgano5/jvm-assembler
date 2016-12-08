package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.*;

public class CodeAttribute extends Attribute {

	public static class ExceptionInfo {
		public final int startPc;
		public final int endPc;
		public final int handlerPc;
		public final String exception;

		public ExceptionInfo(int startPc, int endPc, int handlerPc, String exception) {
			this.startPc = startPc;
			this.endPc = endPc;
			this.handlerPc = handlerPc;
			this.exception = exception;
		}

		@Override
		public String toString() {
			return "ExceptionInfo{" +
					"startPc=" + startPc +
					", endPc=" + endPc +
					", handlerPc=" + handlerPc +
					", exception='" + exception + '\'' +
					'}';
		}
	}

	private int maxStack;
	private int maxLocals;
	private byte[] code;
	private List<ExceptionInfo> exceptionTable;
	private List<Attribute> attributes;

	public CodeAttribute(int maxStack, int maxLocals, byte[] code, List<ExceptionInfo> exceptionTable,
						 List<Attribute> attributes) {
		this.maxStack = maxStack;
		this.maxLocals = maxLocals;
		this.code = code;
		this.exceptionTable = Collections.unmodifiableList(new ArrayList<>(exceptionTable));
		this.attributes = Collections.unmodifiableList(new ArrayList<>(attributes));
	}

	CodeAttribute() {}

	public int getMaxStack() {
		return maxStack;
	}

	public int getMaxLocals() {
		return maxLocals;
	}

	public int getCodeLength() {
		return code.length;
	}

	public void readCode(byte[] buffer, int bufferOffset, int codeOffset, int length) {
		System.arraycopy(code, codeOffset, buffer, bufferOffset, length);
	}

	public List<ExceptionInfo> getExceptionTable() {
		return exceptionTable;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	@Override
	public void parseBody(int length, BytesReader bytesReader, Map<Integer, Constant> constantPool) throws IOException {
		maxStack = bytesReader.readShort();
		maxLocals = bytesReader.readShort();
		int codeLength = bytesReader.readInt();
		code = new byte[codeLength];
		bytesReader.readMinimum(code, codeLength);
		int tableSize = bytesReader.readShort();
		List<ExceptionInfo> table = new ArrayList<>(tableSize);
		for(int count = 0; count < tableSize; count++) {
			table.add(new ExceptionInfo(bytesReader.readShort(), bytesReader.readShort(),
					bytesReader.readShort(), Constant.toString(bytesReader.readShort(), constantPool)));
		}
		exceptionTable = Collections.unmodifiableList(table);
		int numAttributes = bytesReader.readShort();
		List<Attribute> attributes = new ArrayList<>(numAttributes);
		for(int count = 0; count < numAttributes; count++) {
			String name = Constant.toString(bytesReader.readShort(), constantPool);
			attributes.add(Attribute.readAttribute(name, bytesReader.readInt(), bytesReader, constantPool));
		}
		this.attributes = Collections.unmodifiableList(attributes);
	}

	@Override
	public String toString() {
		return "CodeAttribute{" +
				"maxStack=" + maxStack +
				", maxLocals=" + maxLocals +
				", code=" + Arrays.toString(code) +
				", exceptionTable=" + exceptionTable +
				", attributes=" + attributes +
				'}';
	}
}
