package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.AccessFlags;
import au.id.villar.bytecode.constant.Constant;
import au.id.villar.bytecode.constant.ConstantPool;
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
    ParameterInfo parseElement(BytesReader bytesReader, ConstantPool constantPool) throws IOException {
        return new ParameterInfo(Constant.toString(bytesReader.readShort(), constantPool),
                new AccessFlags((short)bytesReader.readShort(), AccessFlags.FlagsType.METHOD_PARAMETERS));
    }
}
