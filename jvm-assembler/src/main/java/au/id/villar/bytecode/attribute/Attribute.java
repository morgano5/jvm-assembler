package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.Map;

public abstract class Attribute {

	public static Attribute readAttribute(String name, int length, BytesReader bytesReader,
			Map<Integer, Constant> constantPool) throws IOException {

		Attribute attribute;

		switch (name) {
			case "Code": attribute = new CodeAttribute(); break;
			case "ConstantValue": attribute = new ConstantValueAttribute(); break;
			case "Signature": attribute = new SignatureAttribute(); break;
			case "SourceFile": attribute = new SourceFileAttribute(); break;
			case "Synthetic": attribute = new SyntheticAttribute(); break;
			case "Deprecated": attribute = new DeprecatedAttribute(); break;
			case "Exceptions": attribute = new ExceptionsAttribute(); break;
			case "RuntimeVisibleAnnotations": attribute = new RuntimeVisibleAnnotationsAttribute(); break;
			case "RuntimeInvisibleAnnotations": attribute = new RuntimeInvisibleAnnotationsAttribute(); break;
			case "RuntimeVisibleParameterAnnotations": attribute = new RuntimeVisibleParameterAnnotationsAttribute(); break;
			case "RuntimeInvisibleParameterAnnotations": attribute = new RuntimeInvisibleParameterAnnotationsAttribute(); break;
			case "RuntimeVisibleTypeAnnotations": attribute = new RuntimeVisibleTypeAnnotationsAttribute(); break;
			case "RuntimeInvisibleTypeAnnotations": attribute = new RuntimeInvisibleTypeAnnotationsAttribute(); break;
			case "StackMapTable": attribute = new StackMapTableAttribute(); break;
			case "InnerClasses": attribute = new InnerClassesAttribute(); break;
			case "EnclosingMethod": attribute = new EnclosingMethodAttribute(); break;
			case "SourceDebugExtension": attribute = new SourceDebugExtensionAttribute(); break;
			case "LineNumberTable": attribute = new LineNumberTableAttribute(); break;
			case "LocalVariableTable": attribute = new LocalVariableTableAttribute(); break;
			case "LocalVariableTypeTable": attribute = new LocalVariableTypeTableAttribute(); break;
			case "AnnotationDefault": attribute = new AnnotationDefaultAttribute(); break;
			case "BootstrapMethods": attribute = new BootstrapMethodsAttribute(); break;
			case "MethodParameters": attribute = new MethodParametersAttribute(); break;
			default: attribute = new GenericAttribute(name);
		}
		attribute.parseBody(length, bytesReader, constantPool);
		return attribute;
	}

	Attribute() {}


	public abstract void parseBody(int length, BytesReader bytesReader, Map<Integer, Constant> constantPool)
			throws IOException;

}
