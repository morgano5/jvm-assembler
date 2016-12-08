package au.id.villar.bytecode.parser;

import au.id.villar.bytecode.Class;
import au.id.villar.bytecode.parser.constant.ClassConstant;
import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.parser.constant.Utf8Constant;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.Assert.*;

public class ClassFileParserTest {

	@Test
	public void basicTest() throws IOException {
		ClassFileParser parser = new ClassFileParser();
		try(InputStream bytecode = ClassLoader.getSystemResourceAsStream("class/FareRulesRequest.class")) {
			parser.setBytecodeStream(bytecode);
			parser.setHandler(new ClassFileHandler() {

				private Map<Integer, Constant> constants;

				@Override
				public void number(int number, NumberType numberType) {

					ClassConstant classConstant;
					String className;

					switch(numberType) {
						case CONSTANT_POOL:
							constants = new HashMap<>(number);
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
				}

				@Override
				public void constant(Constant constant, int index) {
					constants.put(index, constant);
					System.out.println("CONSTANT (" + index + "): " + constant);
				}

				@Override
				public void interfaceIndex(int constantPoolIndex) {
					ClassConstant classConstant = (ClassConstant)constants.get(constantPoolIndex);
					String className = ((Utf8Constant)constants.get(classConstant.getNameIndex())).getValue();
					System.out.println("INTERFACE: " + className);
				}

				@Override
				public void field(int accessFlags, int nameIndex, int descriptorIndex) {
					String name = ((Utf8Constant)constants.get(nameIndex)).getValue();
					String type = ((Utf8Constant)constants.get(descriptorIndex)).getValue();
					System.out.println("FIELD: accessFlags=" + accessFlags + ", name=" + name + ", type=" + type);
				}

				@Override
				public void method(int accessFlags, int nameIndex, int descriptorIndex) {
					String name = ((Utf8Constant)constants.get(nameIndex)).getValue();
					String descriptor = ((Utf8Constant)constants.get(descriptorIndex)).getValue();
					System.out.println("METHOD: accessFlags=" + accessFlags + ", name=" + name
							+ ", descriptor=" + descriptor);
				}

				@Override
				public void attribute(int nameIndex, int length, InputStream info) {
					byte[] data = new byte[length];
					try {
						info.read(data);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					String name = ((Utf8Constant)constants.get(nameIndex)).getValue();
					System.out.println("ATTRIBUTE: name=" + name + ", length=" + length
							+ ", data=" + Arrays.toString(data));
				}
			});

			parser.parse();
			assertEquals(-1, bytecode.read());
		}

	}

	@Test
	public void softBasicTest() throws IOException {
		try(InputStream bytecode = ClassLoader.getSystemResourceAsStream("class/AnnotatableTypeSystem.class")) {
			Class aClass = Class.build(bytecode);
			assertEquals("org/eclipse/jdt/internal/compiler/lookup/AnnotatableTypeSystem", aClass.getName());
			assertEquals("org/eclipse/jdt/internal/compiler/lookup/TypeSystem", aClass.getSuperClass());
			assertEquals(50, aClass.getMayor());


			System.out.println(">>> " + aClass.getConstants().size());
			java.util.Map<Integer, Constant> constants = aClass.getConstants();
			for (java.util.Map.Entry<Integer, Constant> entry : constants.entrySet()) {
				System.out.format(">> %3d: %s%n", entry.getKey(), entry.getValue().toString());
			}
		}

//		try(FileInputStream bytecode = new FileInputStream("/home/villarr/Desktop/MyBean.class")) {
//			Class aClass = Class.build(bytecode);
//		}
	}
}
