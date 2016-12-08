package au.id.villar.bytecode.attribute.frame;

import au.id.villar.bytecode.attribute.frame.type.VerificationTypeInfo;
import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AppendFrame extends ExplicitOffsetDeltaFrame {

	private List<VerificationTypeInfo> typeInfos;

	public AppendFrame(int offsetDelta, List<VerificationTypeInfo> typeInfos) {
		super(offsetDelta);
		this.typeInfos = Collections.unmodifiableList(new ArrayList<>(typeInfos));
	}

	AppendFrame(int offsetDelta, int numTypeInfos) {
		super(offsetDelta);
		typeInfos = new ArrayList<>(numTypeInfos);
		while (numTypeInfos-- > 0) {
			typeInfos.add(null);
		}
	}

	public List<VerificationTypeInfo> getTypeInfos() {
		return typeInfos;
	}

	@Override
	void parseBody(BytesReader bytesReader, Map<Integer, Constant> constantPool) throws IOException {
		for(int index = 0; index < typeInfos.size(); index++) {
			typeInfos.set(index, VerificationTypeInfo.readVerificationTypeInfo(bytesReader, constantPool));
		}
		typeInfos = Collections.unmodifiableList(typeInfos);
	}

	@Override
	public String toString() {
		return "AppendFrame{" +
				"offsetDelta=" + getOffsetDelta() +
				", typeInfos=" + typeInfos +
				'}';
	}
}
