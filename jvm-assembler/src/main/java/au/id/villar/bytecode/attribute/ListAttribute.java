package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

abstract class ListAttribute<T> extends Attribute {

	protected List<T> list;

	public List<T> getList() {
		return list;
	}

	@Override
	public final void parseBody(int length, BytesReader bytesReader, Map<Integer, Constant> constantPool)
			throws IOException {
		int size = bytesReader.readShort();
		list = new ArrayList<>(size);
		while(size-- > 0) {
			list.add(parseElement(bytesReader, constantPool));
		}
		list = Collections.unmodifiableList(list);
	}

	abstract T parseElement(BytesReader bytesReader, Map<Integer, Constant> constantPool)
			throws IOException;

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{" +
				"list=" + list +
				'}';
	}
}
