package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.parser.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public abstract class Attribute {

    public static Attribute readAttribute(String name, int length, BytesReader bytesReader,
            ParsingConstantPool constantPool, AttributeGenerator generator) throws IOException {

        Class<? extends Attribute> attributeClass = switch (name) {
            case "Code" -> CodeAttribute.class;
            case "ConstantValue" -> ConstantValueAttribute.class;
            case "Signature" -> SignatureAttribute.class;
            case "SourceFile" -> SourceFileAttribute.class;
            case "Synthetic" -> SyntheticAttribute.class;
            case "Deprecated" -> DeprecatedAttribute.class;
            case "Exceptions" -> ExceptionsAttribute.class;
            case "RuntimeVisibleAnnotations" -> RuntimeVisibleAnnotationsAttribute.class;
            case "RuntimeInvisibleAnnotations" -> RuntimeInvisibleAnnotationsAttribute.class;
            case "RuntimeVisibleParameterAnnotations" -> RuntimeVisibleParameterAnnotationsAttribute.class;
            case "RuntimeInvisibleParameterAnnotations" -> RuntimeInvisibleParameterAnnotationsAttribute.class;
            case "RuntimeVisibleTypeAnnotations" -> RuntimeVisibleTypeAnnotationsAttribute.class;
            case "RuntimeInvisibleTypeAnnotations" -> RuntimeInvisibleTypeAnnotationsAttribute.class;
            case "StackMapTable" -> StackMapTableAttribute.class;
            case "InnerClasses" -> InnerClassesAttribute.class;
            case "EnclosingMethod" -> EnclosingMethodAttribute.class;
            case "SourceDebugExtension" -> SourceDebugExtensionAttribute.class;
            case "LineNumberTable" -> LineNumberTableAttribute.class;
            case "LocalVariableTable" -> LocalVariableTableAttribute.class;
            case "LocalVariableTypeTable" -> LocalVariableTypeTableAttribute.class;
            case "AnnotationDefault" -> AnnotationDefaultAttribute.class;
            case "BootstrapMethods" -> BootstrapMethodsAttribute.class;
            case "MethodParameters" -> MethodParametersAttribute.class;
            default -> GenericAttribute.class;
        };

        return attributeClass == GenericAttribute.class
                ? generator.readGenericAttribute(name, length, bytesReader, constantPool, generator)
                : generator.readAttribute(attributeClass, length, bytesReader, constantPool, generator);
    }

    Attribute() {}

    public abstract void parseBody(int length, BytesReader bytesReader, ParsingConstantPool constantPool,
            AttributeGenerator generator) throws IOException;

}
