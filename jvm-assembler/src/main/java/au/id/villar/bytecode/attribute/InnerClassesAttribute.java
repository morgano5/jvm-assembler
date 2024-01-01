package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.AccessFlags;
import au.id.villar.bytecode.parser.constant.ParsingConstant;
import au.id.villar.bytecode.parser.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InnerClassesAttribute extends ListAttribute<InnerClassesAttribute.ClassInfo> {

    public static class ClassInfo {
        public final String innerClass;
        public final String outerClass;
        public final String innerName;
        public final AccessFlags accessFlags;

        public ClassInfo(String innerClass, String outerClass, String innerName, AccessFlags accessFlags) {
            this.innerClass = innerClass;
            this.outerClass = outerClass;
            this.innerName = innerName;
            this.accessFlags = accessFlags;
        }

        @Override
        public String toString() {
            return "ClassInfo{" +
                    "innerClass='" + innerClass + '\'' +
                    ", outerClass='" + outerClass + '\'' +
                    ", innerName='" + innerName + '\'' +
                    ", accessFlags=" + accessFlags +
                    '}';
        }
    }

    public InnerClassesAttribute(List<ClassInfo> innerClasses) {
        this.list = Collections.unmodifiableList(new ArrayList<>(innerClasses));
    }

    InnerClassesAttribute() {}

    @Override
    ClassInfo parseElement(BytesReader bytesReader, ParsingConstantPool constantPool) throws IOException {
        int innerIndex = bytesReader.readShort();
        int outerIndex = bytesReader.readShort();
        int nameIndex = bytesReader.readShort();
        AccessFlags flags = new AccessFlags((short)bytesReader.readShort(), false);
        String innerClass = innerIndex != 0? ParsingConstant.toString(innerIndex, constantPool): null;
        String outerClass = outerIndex != 0? ParsingConstant.toString(outerIndex, constantPool): null;
        String nameClass = nameIndex != 0? ParsingConstant.toString(nameIndex, constantPool): null;
        return new ClassInfo(innerClass, outerClass, nameClass, flags);
    }
}
