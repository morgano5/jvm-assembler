package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.constant.Constant;
import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalVariableTypeTableAttribute
        extends ListAttribute<LocalVariableTypeTableAttribute.LocalVariableTypeInfo> {

    public static class LocalVariableTypeInfo {
        public final int startPc;
        public final int length;
        public final String name;
        public final String signature;
        public final int index;

        public LocalVariableTypeInfo(int startPc, int length, String name, String signature, int index) {
            this.startPc = startPc;
            this.length = length;
            this.name = name;
            this.signature = signature;
            this.index = index;
        }

        @Override
        public String toString() {
            return "LocalVariableTypeInfo{" +
                    "startPc=" + startPc +
                    ", length=" + length +
                    ", name='" + name + '\'' +
                    ", signature='" + signature + '\'' +
                    ", index=" + index +
                    '}';
        }
    }

    public LocalVariableTypeTableAttribute(List<LocalVariableTypeInfo> list) {
        this.list = Collections.unmodifiableList(new ArrayList<>(list));
    }

    LocalVariableTypeTableAttribute() {
    }

    @Override
    LocalVariableTypeInfo parseElement(BytesReader bytesReader, ConstantPool constantPool)
            throws IOException {
        return new LocalVariableTypeInfo(bytesReader.readShort(),
                bytesReader.readShort(),
                Constant.toString(bytesReader.readShort(), constantPool),
                Constant.toString(bytesReader.readShort(), constantPool),
                bytesReader.readShort()
        );
    }

}
