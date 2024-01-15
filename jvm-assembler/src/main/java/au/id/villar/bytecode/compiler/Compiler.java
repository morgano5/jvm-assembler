package au.id.villar.bytecode.compiler;

import au.id.villar.bytecode.ClassFile;
import au.id.villar.bytecode.Field;
import au.id.villar.bytecode.Method;
import au.id.villar.bytecode.attribute.Attribute;
import au.id.villar.bytecode.constant.Constant;
import au.id.villar.bytecode.constant.DoubleConstant;
import au.id.villar.bytecode.constant.LongConstant;
import au.id.villar.bytecode.util.BytesWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class Compiler {

    public static void compile(ClassFile classFile, OutputStream output) throws IOException {
        Compiler compiler = new Compiler(classFile, output);
        compiler.compile();
    }

    private final BytesWriter bytesWriter;
    private final ClassFile classFile;

    private Compiler(ClassFile classFile, OutputStream output) {
        this.classFile = classFile;
        this.bytesWriter = new BytesWriter(output);
    }

    private void compile() throws IOException {
        writeMagicAndVersion();
        writeConstantPool();
        writeAccessAndNameAndInheritance();
        writeFields();
        writeMethods();
        writeAttributes(classFile.getAttributes());
    }

    private void writeMagicAndVersion() throws IOException {
        bytesWriter.writeInt(0xCAFEBABE);
        bytesWriter.writeShort(classFile.getMinor());
        bytesWriter.writeShort(classFile.getMayor());
    }

    private void writeConstantPool() throws IOException {
        List<Integer> indexes = classFile.getConstants().getOrderedIndexes();
        int lastIndex = indexes.get(indexes.size() - 1);
        Constant lastConstant = classFile.getConstants().get(lastIndex);
        boolean lastIsDoubleSize = lastConstant instanceof LongConstant || lastConstant instanceof DoubleConstant;
        int size = lastIndex + (lastIsDoubleSize? 1 : 0);
        bytesWriter.writeShort(size);
        for (Integer index : indexes) {
            classFile.getConstants().get(index).write(bytesWriter);
        }
    }

    private void writeAccessAndNameAndInheritance() throws IOException {
        bytesWriter.writeShort(classFile.getAccessFlags().getFlags());
        bytesWriter.writeShort(classFile.getNameIndex());
        bytesWriter.writeShort(classFile.getSuperClassIndex());
        bytesWriter.writeShort(classFile.getInterfaceIndexes().size());
        for (Integer index : classFile.getInterfaceIndexes()) {
            bytesWriter.writeShort(index);
        }
    }

    private void writeFields() throws IOException {
        for (Field field : classFile.getFields()) {
            bytesWriter.writeShort(field.getAccessFlags().getFlags());
            bytesWriter.writeShort(field.getNameIndex());
            bytesWriter.writeShort(field.getDescriptorIndex());
            writeAttributes(field.getAttributes());
        }
    }

    private void writeMethods() throws IOException {
        for (Method method : classFile.getMethods()) {
            bytesWriter.writeShort(method.getAccessFlags().getFlags());
            bytesWriter.writeShort(method.getNameIndex());
            bytesWriter.writeShort(method.getDescriptorIndex());
            writeAttributes(method.getAttributes());
        }
    }

    private void writeAttributes(List<Attribute> attributes) throws IOException {
        bytesWriter.writeShort(attributes.size());
        for (Attribute attribute : attributes) {
            attribute.write(bytesWriter);
        }
    }
}
