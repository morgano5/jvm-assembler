package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.parser.constant.*;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BootstrapMethodsAttribute extends ListAttribute<BootstrapMethodsAttribute.BootstrapMethod> {


	public static enum BootstrapArgumentType {
		STRING, CLASS, INTEGER, LONG, FLOAT, DOUBLE, METHOD_TYPE, METHOD_HANDLE
	}

	public static abstract class BootstrapArgument {
		public final BootstrapArgumentType type;

		BootstrapArgument(BootstrapArgumentType type) {
			this.type = type;
		}
	}

	public static class SingleBootstrapArgument extends BootstrapArgument {
		public final String value;

		public SingleBootstrapArgument(BootstrapArgumentType type, String value) {
			super(type);
			this.value = value;
		}

		@Override
		public String toString() {
			return "SingleBootstrapArgument{" +
					"type=" + type +
					", value='" + value + '\'' +
					'}';
		}
	}

	public static class MethodHandleBootstrapArgument extends BootstrapArgument {
		public final MethodHandleReferenceKind referenceKind;
		public final MemberReference memberReference;

		public MethodHandleBootstrapArgument(MethodHandleReferenceKind referenceKind, MemberReference memberReference) {
			super(BootstrapArgumentType.METHOD_HANDLE);
			this.referenceKind = referenceKind;
			this.memberReference = memberReference;
		}

		@Override
		public String toString() {
			return "MethodHandleBootstrapArgument{" +
					"type=" + type +
					", referenceKind=" + referenceKind +
					", memberReference=" + memberReference +
					'}';
		}
	}

	public static class BootstrapMethod {

		public final MethodHandleReferenceKind referenceKind;
		public final MemberReference memberReference;
		public final List<BootstrapArgument> bootstrapArguments;

		public BootstrapMethod(MethodHandleReferenceKind referenceKind, MemberReference memberReference,
				List<BootstrapArgument> bootstrapArguments) {
			this.referenceKind = referenceKind;
			this.memberReference = memberReference;
			this.bootstrapArguments = Collections.unmodifiableList(new ArrayList<>(bootstrapArguments));
		}

		@Override
		public String toString() {
			return "BootstrapMethod{" +
					"referenceKind=" + referenceKind +
					", memberReference=" + memberReference +
					", bootstrapArguments=" + bootstrapArguments +
					'}';
		}

	}

	public BootstrapMethodsAttribute(List<BootstrapMethod> list) {
		this.list = Collections.unmodifiableList(new ArrayList<>(list));
	}

	BootstrapMethodsAttribute() {
	}

	@Override
	BootstrapMethod parseElement(BytesReader bytesReader, Map<Integer, Constant> constantPool) throws IOException {
		MethodHandleConstant methodHandle = (MethodHandleConstant)constantPool.get(bytesReader.readShort());
		int numArgs = bytesReader.readShort();
		List<BootstrapArgument> argumentList = new ArrayList<>(numArgs);
		while(numArgs-- > 0) {
			Constant argType = constantPool.get(bytesReader.readShort());
			if(argType instanceof StringConstant) {
				argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.STRING, argType.toStringValue()));
			} else if(argType instanceof ClassConstant) {
				argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.CLASS, argType.toStringValue()));
			} else if(argType instanceof DoubleConstant) {
				argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.DOUBLE, argType.toStringValue()));
			} else if(argType instanceof FloatConstant) {
				argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.FLOAT, argType.toStringValue()));
			} else if(argType instanceof LongConstant) {
				argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.LONG, argType.toStringValue()));
			} else if(argType instanceof IntegerConstant) {
				argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.INTEGER, argType.toStringValue()));
			} else if(argType instanceof MethodTypeConstant) {
				argumentList.add(new SingleBootstrapArgument(BootstrapArgumentType.METHOD_TYPE, argType.toStringValue()));
			} else if(argType instanceof MethodHandleConstant) {
				MethodHandleConstant methodHandleArg = (MethodHandleConstant)argType;
				MethodHandleReferenceKind referenceKind = methodHandleArg.getReferenceKind();
				MemberReference reference = translate(methodHandleArg, constantPool);
				argumentList.add(new MethodHandleBootstrapArgument(referenceKind, reference));
			} else {
				throw new IOException("Constant type not allowed here: "+ argType);
			}
		}
		return new BootstrapMethod(methodHandle.getReferenceKind(), translate(methodHandle, constantPool),
				Collections.unmodifiableList(argumentList));
	}

	private MemberReference translate(MethodHandleConstant constant, Map<Integer, Constant> constantPool)
			throws IOException {
		MemberRefConstant reference = (MemberRefConstant)constantPool.get(constant.getReferenceIndex());
		NameAndTypeConstant nameAndType = (NameAndTypeConstant)constantPool.get(reference.getNameAndTypeIndex());
		MemberReference.Type type;
		if(reference instanceof FieldRefConstant) {
			type = MemberReference.Type.FIELD;
		} else if (reference instanceof MethodRefConstant) {
			type = MemberReference.Type.METHOD;
		} else if (reference instanceof InterfaceMethodRefConstant) {
			type = MemberReference.Type.INTERFACE_METHOD;
		} else {
			throw new IOException("unexpected constant type: " + reference);
		}

		return new MemberReference(type, Constant.toString(reference.getClassIndex(), constantPool),
				Constant.toString(nameAndType.getNameIndex(), constantPool),
				Constant.toString(nameAndType.getDescriptorIndex(), constantPool));
	}
}
