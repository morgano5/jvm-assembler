package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.parser.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class ListAttribute<T> extends Attribute {

	protected List<T> list;

	public List<T> getList() {
		return list;
	}

	@Override
	public final void parseBody(int length, BytesReader bytesReader, ParsingConstantPool constantPool)
			throws IOException {
		int size = bytesReader.readShort();
		list = new ArrayList<>(size);
		while(size-- > 0) {
			list.add(parseElement(bytesReader, constantPool));
		}
		list = Collections.unmodifiableList(list);
	}

	abstract T parseElement(BytesReader bytesReader, ParsingConstantPool constantPool)
			throws IOException;

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{" +
				"list=" + list +
				'}';
	}
}
