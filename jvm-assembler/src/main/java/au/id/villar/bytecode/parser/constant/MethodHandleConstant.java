package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public class MethodHandleConstant extends Constant {

	private MethodHandleReferenceKind referenceKind;
	private int referenceIndex;

	public MethodHandleConstant(MethodHandleReferenceKind referenceKind, int referenceIndex) {
		this.referenceKind = referenceKind;
		this.referenceIndex = referenceIndex;
	}

	public MethodHandleReferenceKind getReferenceKind() {
		return referenceKind;
	}

	public int getReferenceIndex() {
		return referenceIndex;
	}

	MethodHandleConstant() {}

	@Override
	void parseBody(BytesReader bytesReader) throws IOException {
		int kind = bytesReader.readByte();
		referenceIndex = bytesReader.readShort();
		for(MethodHandleReferenceKind potentialReferenceKind: MethodHandleReferenceKind.values()) {
			if(potentialReferenceKind.KIND == kind) {
				referenceKind = potentialReferenceKind;
				return;
			}
		}
		throw new IOException("Reference Kind not known: " + kind);
	}

	@Override
	public String toString() {
		return "MethodHandleConstant{" +
				"referenceKind=" + referenceKind +
				", referenceIndex=" + referenceIndex +
				'}';
	}
}
