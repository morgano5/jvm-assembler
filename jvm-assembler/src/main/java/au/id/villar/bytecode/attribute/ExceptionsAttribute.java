package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.constant.Constant;
import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExceptionsAttribute extends ListAttribute<String> {

    public ExceptionsAttribute(List<String> exceptions) {
        this.list = Collections.unmodifiableList(new ArrayList<>(exceptions));
    }

    ExceptionsAttribute() {}

    @Override
    String parseElement(BytesReader bytesReader, ConstantPool constantPool) throws IOException {
        return Constant.toString(bytesReader.readShort(), constantPool);
    }
}
