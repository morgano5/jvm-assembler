package au.id.villar.bytecode.attribute.frame.type;

import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.Map;

public abstract class EmptyVerificationTypeInfo extends VerificationTypeInfo {

	@Override
	void parseBody(BytesReader bytesReader, Map<Integer, Constant> constantPool) throws IOException {
		// DO nothing
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{}";
	}
}
