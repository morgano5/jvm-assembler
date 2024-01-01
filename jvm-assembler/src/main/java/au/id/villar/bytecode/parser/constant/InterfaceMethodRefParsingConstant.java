package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.constant.MethodHandleConstant;
import au.id.villar.bytecode.constant.MethodHandleReferenceKind;

import java.util.Set;

import static au.id.villar.bytecode.constant.MethodHandleReferenceKind.INVOKE_INTERFACE;
import static au.id.villar.bytecode.constant.MethodHandleReferenceKind.INVOKE_SPECIAL;
import static au.id.villar.bytecode.constant.MethodHandleReferenceKind.INVOKE_STATIC;

public final class InterfaceMethodRefParsingConstant extends MemberRefParsingConstant {

	private static final Set<MethodHandleReferenceKind> INTERFACE_PRE_52_KIND =
			Set.of(INVOKE_INTERFACE);
	private static final Set<MethodHandleReferenceKind> INTERFACE_52_AND_ABOVE_KIND =
			Set.of(INVOKE_STATIC, INVOKE_SPECIAL, INVOKE_INTERFACE);

	public InterfaceMethodRefParsingConstant(int classIndex, int nameAndTypeIndex) {
		super(classIndex, nameAndTypeIndex);
	}

	InterfaceMethodRefParsingConstant() {}

	@Override
	MethodHandleConstant.MemberType calculateMemberType(MethodHandleReferenceKind referenceKind, int mayorVersion) {
		if (mayorVersion < 52 && !INTERFACE_PRE_52_KIND.contains(referenceKind)
				|| mayorVersion >= 52 && !INTERFACE_52_AND_ABOVE_KIND.contains(referenceKind)) {
			throw new IllegalStateException("Mismatch between kind " + referenceKind +
					" and interface method reference");
		}
		return MethodHandleConstant.MemberType.INTERFACE_METHOD;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "InterfaceMethodRefConstant{" +
				"classIndex=" + classIndex +
				", nameAndTypeIndex=" + nameAndTypeIndex +
				'}';
	}
}
