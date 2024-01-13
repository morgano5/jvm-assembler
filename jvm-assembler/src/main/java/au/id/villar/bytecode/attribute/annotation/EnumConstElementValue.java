package au.id.villar.bytecode.attribute.annotation;

import au.id.villar.bytecode.constant.Constant;
import au.id.villar.bytecode.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public class EnumConstElementValue extends ElementValue {

    private String type;
    private String name;

    public EnumConstElementValue(String type, String name) {
        this.type = type;
        this.name = name;
    }

    EnumConstElementValue() {}

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    void parseBody(BytesReader bytesReader, ParsingConstantPool constantPool) throws IOException {
        type = Constant.toString(bytesReader.readShort(), constantPool);
        name = Constant.toString(bytesReader.readShort(), constantPool);
    }

    @Override
    public String toString() {
        return "EnumConstElementValue{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
