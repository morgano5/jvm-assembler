package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.util.BytesReader;
import au.id.villar.bytecode.util.BytesWriter;

import java.io.IOException;

public abstract class Attribute {

    protected Integer nameIndex;

    Attribute() {}

    public static Attribute readAttribute(int nameIndex, int length, BytesReader bytesReader,
            ConstantPool constantPool, AttributeGenerator generator) throws IOException {

        String name = constantPool.getStringFromUtf8(nameIndex);
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

        return generator.readAttribute(attributeClass, nameIndex, length, bytesReader, constantPool, generator);
    }

    public Integer getNameIndex() {
        return nameIndex;
    }

    public abstract void parseBody(int nameIndex, int length, BytesReader bytesReader, ConstantPool constantPool,
            AttributeGenerator generator) throws IOException;

    //public abstract void write(BytesWriter bytesWriter) throws IOException;
    public void write(BytesWriter bytesWriter) throws IOException {
        //.. .. ..
    }
}
