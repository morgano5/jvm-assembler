package au.id.villar.bytecode.attribute.frame.type;

import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.Map;

public class UninitializedVerificationTypeInfo extends VerificationTypeInfo {

	int offset;

	@Override
	void parseBody(BytesReader bytesReader, Map<Integer, Constant> constantPool) throws IOException {
		offset = bytesReader.readShort();
	}

	public int getOffset() {
		return offset;
	}

	@Override
	public String toString() {
		return "UninitializedVerificationTypeInfo{" +
				"offset=" + offset +
				'}';
	}
}
