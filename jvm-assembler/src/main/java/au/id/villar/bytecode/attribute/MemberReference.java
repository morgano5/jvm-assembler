package au.id.villar.bytecode.attribute;

public class MemberReference {

	public static enum Type {
		FIELD, METHOD, INTERFACE_METHOD
	}

	public final Type type;
	public final String className;
	public final String memberName;
	public final String memberDescriptor;

	public MemberReference(Type type, String className, String memberName, String memberDescriptor) {
		this.type = type;
		this.className = className;
		this.memberName = memberName;
		this.memberDescriptor = memberDescriptor;
	}

	@Override
	public String toString() {
		return "MemberReference{" +
				"type=" + type +
				", className='" + className + '\'' +
				", memberName='" + memberName + '\'' +
				", memberDescriptor='" + memberDescriptor + '\'' +
				'}';
	}
}
