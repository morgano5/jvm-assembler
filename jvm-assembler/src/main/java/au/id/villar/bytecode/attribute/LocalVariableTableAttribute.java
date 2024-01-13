package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.constant.Constant;
import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalVariableTableAttribute extends ListAttribute<LocalVariableTableAttribute.LocalVariableInfo> {

    public static class LocalVariableInfo {
        public final int startPc;
        public final int length;
        public final String name;
        public final String descriptor;
        public final int index;

        public LocalVariableInfo(int startPc, int length, String name, String descriptor, int index) {
            this.startPc = startPc;
            this.length = length;
            this.name = name;
            this.descriptor = descriptor;
            this.index = index;
        }

        @Override
        public String toString() {
            return "LocalVariableInfo{" +
                    "startPc=" + startPc +
                    ", length=" + length +
                    ", name='" + name + '\'' +
                    ", descriptor='" + descriptor + '\'' +
                    ", index=" + index +
                    '}';
        }
    }

    public LocalVariableTableAttribute(List<LocalVariableInfo> list) {
        this.list = Collections.unmodifiableList(new ArrayList<>(list));
    }

    LocalVariableTableAttribute() {
    }

    @Override
    LocalVariableInfo parseElement(BytesReader bytesReader, ConstantPool constantPool) throws IOException {
        return new LocalVariableInfo(bytesReader.readShort(),
                bytesReader.readShort(),
                Constant.toString(bytesReader.readShort(), constantPool),
                Constant.toString(bytesReader.readShort(), constantPool),
                bytesReader.readShort()
        );
    }
}
