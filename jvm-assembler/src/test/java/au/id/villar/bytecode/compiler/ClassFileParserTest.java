package au.id.villar.bytecode.compiler;

import au.id.villar.bytecode.ClassFile;
import au.id.villar.bytecode.constant.ClassConstant;
import au.id.villar.bytecode.constant.Constant;
import au.id.villar.bytecode.constant.Utf8Constant;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassFileParserTest {

    @Test
    void shouldParseWithNopLoader() throws IOException {
        try (InputStream bytecode = ClassLoader.getSystemResourceAsStream("class/BlockScope.class")) {
            ClassFileParser.parse(bytecode, new ClassFileHandler() { /* Do nothing */ });
        }
    }

//======================================
    @Test
    public void basicTest() throws IOException {
        try (InputStream bytecode = ClassLoader.getSystemResourceAsStream("class/BlockScope.class")) {
            ClassFileParser.parse(bytecode, new ClassFileHandler() {

                private Map<Integer, Constant> constants;

                @Override
                public boolean number(int number, NumberType numberType) {

                    ClassConstant classConstant;
                    String className;

                    switch(numberType) {
                        case CONSTANT_POOL:
                            constants = new HashMap<>(number);
                            System.out.println("CONSTANT POOL SIZE: " + number);
                            break;
                        case THIS_INDEX:
                            classConstant = (ClassConstant)constants.get(number);
                            className = ((Utf8Constant)constants.get(classConstant.getNameIndex())).getValue();
                            System.out.println("THIS: " + className);
                            break;
                        case SUPER_INDEX:
                            classConstant = (ClassConstant)constants.get(number);
                            className = ((Utf8Constant)constants.get(classConstant.getNameIndex())).getValue();
                            System.out.println("SUPER: " + className);
                            break;
                        case ACCESS_FLAGS:
                            System.out.println("NUMBER: " + number + ", TYPE: " + numberType);
                            break;
                        case ATTRIBUTES:
                    }
                    return true;
                }

                @Override
                public boolean constant(Constant constant, int index) {
                    constants.put(index, constant);
                    System.out.println("CONSTANT (" + index + "): " + constant);
                    return true;
                }

                @Override
                public boolean interfaceIndex(int constantPoolIndex) {
                    ClassConstant classConstant = (ClassConstant)constants.get(constantPoolIndex);
                    String className = ((Utf8Constant)constants.get(classConstant.getNameIndex())).getValue();
                    System.out.println("INTERFACE: " + className);
                    return true;
                }

                @Override
                public boolean field(int accessFlags, int nameIndex, int descriptorIndex) {
                    String name = ((Utf8Constant)constants.get(nameIndex)).getValue();
                    String type = ((Utf8Constant)constants.get(descriptorIndex)).getValue();
                    System.out.println("FIELD: accessFlags=" + accessFlags + ", name=" + name + ", type=" + type);
                    return true;
                }

                @Override
                public boolean method(int accessFlags, int nameIndex, int descriptorIndex) {
                    String name = ((Utf8Constant)constants.get(nameIndex)).getValue();
                    String descriptor = ((Utf8Constant)constants.get(descriptorIndex)).getValue();
                    System.out.println("METHOD: accessFlags=" + accessFlags + ", name=" + name
                            + ", descriptor=" + descriptor);
                    return true;
                }

                @Override
                public boolean attribute(int nameIndex, int length, InputStream info) {
                    byte[] data = new byte[length];
                    try {
                        info.read(data);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    String name = ((Utf8Constant)constants.get(nameIndex)).getValue();
                    System.out.println("ATTRIBUTE: name=" + name + ", length=" + length
                            + ", data=" + Arrays.toString(data));
                    return true;
                }
            });

            assertEquals(-1, bytecode.read());
        }

    }

    @Test
    public void softBasicTest() throws IOException {
        try (InputStream bytecode = ClassLoader.getSystemResourceAsStream("class/AnnotatableTypeSystem.class")) {
            ClassFile classFile = ClassFileParser.parseToClass(bytecode);
            assertEquals("org/eclipse/jdt/internal/compiler/lookup/AnnotatableTypeSystem", classFile.getName());
            assertEquals("org/eclipse/jdt/internal/compiler/lookup/TypeSystem", classFile.getSuperClass());
            assertEquals(50, classFile.getMayor());


            System.out.println(">>> " + classFile.getConstants().getOrderedIndexes().size());
            for (Integer index : classFile.getConstants().getOrderedIndexes()) {
                Constant constant = classFile.getConstants().get(index);
                System.out.format(">> %3d: %s%n", index, constant.toString());
            }
        }

//		try(FileInputStream bytecode = new FileInputStream("/home/villarr/Desktop/MyBean.class")) {
//			ClassFile classFile = ClassFile.build(bytecode);
//		}
    }
}
