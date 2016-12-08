package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.parser.constant.NameAndTypeConstant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.Map;

public class EnclosingMethodAttribute extends Attribute {

	private MemberReference method;

	public EnclosingMethodAttribute(MemberReference method) {
		this.method = method;
	}

	EnclosingMethodAttribute() {}

	public MemberReference getMethod() {
		return method;
	}

	@Override
	public void parseBody(int length, BytesReader bytesReader, Map<Integer, Constant> constantPool) throws IOException {
		String className = Constant.toString(bytesReader.readShort(), constantPool);
		NameAndTypeConstant methodInfo = (NameAndTypeConstant)constantPool.get(bytesReader.readShort());
		String methodName = Constant.toString(methodInfo.getNameIndex(), constantPool);
		String methodDescriptor = Constant.toString(methodInfo.getDescriptorIndex(), constantPool);
		method = new MemberReference(MemberReference.Type.METHOD, className, methodName, methodDescriptor);
	}

	@Override
	public String toString() {
		return "EnclosingMethodAttribute{" +
				"method=" + method +
				'}';
	}
}
