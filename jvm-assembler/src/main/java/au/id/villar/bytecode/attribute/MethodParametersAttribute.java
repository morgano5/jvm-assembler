package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.AccessFlags;
import au.id.villar.bytecode.parser.constant.ParsingConstant;
import au.id.villar.bytecode.parser.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MethodParametersAttribute extends ListAttribute<MethodParametersAttribute.ParameterInfo> {

    public static class ParameterInfo {
        public final String name;
        public final AccessFlags accessFlags;

        public ParameterInfo(String name, AccessFlags accessFlags) {
            this.name = name;
            this.accessFlags = accessFlags;
        }

        @Override
        public String toString() {
            return "ParameterInfo{" +
                    "name='" + name + '\'' +
                    ", accessFlags=" + accessFlags +
                    '}';
        }
    }

    public MethodParametersAttribute(List<ParameterInfo> list) {
        this.list = Collections.unmodifiableList(new ArrayList<>(list));
    }

    MethodParametersAttribute() {
    }

    @Override
    ParameterInfo parseElement(BytesReader bytesReader, ParsingConstantPool constantPool) throws IOException {
        return new ParameterInfo(ParsingConstant.toString(bytesReader.readShort(), constantPool),
                new AccessFlags((short)bytesReader.readShort(), MethodParametersAttribute.class));
    }
}
