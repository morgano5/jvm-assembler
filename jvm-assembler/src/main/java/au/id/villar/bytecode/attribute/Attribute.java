package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.parser.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public abstract class Attribute {

    public static Attribute readAttribute(String name, int length, BytesReader bytesReader,
            ParsingConstantPool constantPool) throws IOException {

        Attribute attribute = switch (name) {
            case "Code" -> new CodeAttribute();
            case "ConstantValue" -> new ConstantValueAttribute();
            case "Signature" -> new SignatureAttribute();
            case "SourceFile" -> new SourceFileAttribute();
            case "Synthetic" -> new SyntheticAttribute();
            case "Deprecated" -> new DeprecatedAttribute();
            case "Exceptions" -> new ExceptionsAttribute();
            case "RuntimeVisibleAnnotations" -> new RuntimeVisibleAnnotationsAttribute();
            case "RuntimeInvisibleAnnotations" -> new RuntimeInvisibleAnnotationsAttribute();
            case "RuntimeVisibleParameterAnnotations" -> new RuntimeVisibleParameterAnnotationsAttribute();
            case "RuntimeInvisibleParameterAnnotations" -> new RuntimeInvisibleParameterAnnotationsAttribute();
            case "RuntimeVisibleTypeAnnotations" -> new RuntimeVisibleTypeAnnotationsAttribute();
            case "RuntimeInvisibleTypeAnnotations" -> new RuntimeInvisibleTypeAnnotationsAttribute();
            case "StackMapTable" -> new StackMapTableAttribute();
            case "InnerClasses" -> new InnerClassesAttribute();
            case "EnclosingMethod" -> new EnclosingMethodAttribute();
            case "SourceDebugExtension" -> new SourceDebugExtensionAttribute();
            case "LineNumberTable" -> new LineNumberTableAttribute();
            case "LocalVariableTable" -> new LocalVariableTableAttribute();
            case "LocalVariableTypeTable" -> new LocalVariableTypeTableAttribute();
            case "AnnotationDefault" -> new AnnotationDefaultAttribute();
            case "BootstrapMethods" -> new BootstrapMethodsAttribute();
            case "MethodParameters" -> new MethodParametersAttribute();
            default -> new GenericAttribute(name); // TODO : should we throw an "Unknown type" instead?
        };

        attribute.parseBody(length, bytesReader, constantPool);
        return attribute;
    }

    Attribute() {}

    public abstract void parseBody(int length, BytesReader bytesReader, ParsingConstantPool constantPool)
            throws IOException;

}
