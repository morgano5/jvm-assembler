package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LineNumberTableAttribute extends ListAttribute<LineNumberTableAttribute.LineNumber> {

	public static class LineNumber {
		public final int startPc;
		public final int lineNumber;

		public LineNumber(int startPc, int lineNumber) {
			this.startPc = startPc;
			this.lineNumber = lineNumber;
		}

		@Override
		public String toString() {
			return "LineNumber{" +
					"startPc=" + startPc +
					", lineNumber=" + lineNumber +
					'}';
		}
	}

	public LineNumberTableAttribute(List<LineNumber> list) {
		this.list = Collections.unmodifiableList(new ArrayList<>(list));
	}

	LineNumberTableAttribute() {
	}

	@Override
	LineNumber parseElement(BytesReader bytesReader, Map<Integer, Constant> constantPool) throws IOException {
		return new LineNumber(bytesReader.readShort(), bytesReader.readShort());
	}
}
