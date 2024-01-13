package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.attribute.frame.StackMapFrame;
import au.id.villar.bytecode.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StackMapTableAttribute extends ListAttribute<StackMapFrame> {

    public StackMapTableAttribute(List<StackMapFrame> table) {
        this.list = Collections.unmodifiableList(new ArrayList<>(table));
    }

    StackMapTableAttribute() {}

    @Override
    StackMapFrame parseElement(BytesReader bytesReader, ParsingConstantPool constantPool) throws IOException {
        return StackMapFrame.readStackMapFrame(bytesReader, constantPool);
    }
}
