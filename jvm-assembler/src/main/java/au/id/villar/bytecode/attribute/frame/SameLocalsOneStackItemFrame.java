package au.id.villar.bytecode.attribute.frame;

import au.id.villar.bytecode.attribute.frame.type.VerificationTypeInfo;
import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.Map;

public class SameLocalsOneStackItemFrame extends ExplicitOffsetDeltaFrame {

	private VerificationTypeInfo typeInfo;

	SameLocalsOneStackItemFrame(int offsetDelta) {
		super(offsetDelta);
	}

	public SameLocalsOneStackItemFrame(int offsetDelta, VerificationTypeInfo typeInfo) {
		super(offsetDelta);
		this.typeInfo = typeInfo;
	}

	public VerificationTypeInfo getTypeInfo() {
		return typeInfo;
	}

	@Override
	void parseBody(BytesReader bytesReader, Map<Integer, Constant> constantPool) throws IOException {
		typeInfo = VerificationTypeInfo.readVerificationTypeInfo(bytesReader, constantPool);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{" +
				"offsetDelta=" + getOffsetDelta() +
				", typeInfo=" + typeInfo +
				'}';
	}
}
