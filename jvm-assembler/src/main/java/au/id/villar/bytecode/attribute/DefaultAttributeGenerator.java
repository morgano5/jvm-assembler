package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public class DefaultAttributeGenerator implements AttributeGenerator {

    @Override
    public <T extends Attribute> T readAttribute(Class<T> type, int length, BytesReader bytesReader,
            ConstantPool constantPool, AttributeGenerator generator) throws IOException {

        final Attribute attribute;
        if (type == CodeAttribute.class) {
            attribute = new CodeAttribute();
        } else if (type == ConstantValueAttribute.class) {
            attribute = new ConstantValueAttribute();
        } else if (type == SignatureAttribute.class) {
            attribute = new SignatureAttribute();
        } else if (type == SourceFileAttribute.class) {
            attribute = new SourceFileAttribute();
        } else if (type == SyntheticAttribute.class) {
            attribute = new SyntheticAttribute();
        } else if (type == DeprecatedAttribute.class) {
            attribute = new DeprecatedAttribute();
        } else if (type == ExceptionsAttribute.class) {
            attribute = new ExceptionsAttribute();
        } else if (type == RuntimeVisibleAnnotationsAttribute.class) {
            attribute = new RuntimeVisibleAnnotationsAttribute();
        } else if (type == RuntimeInvisibleAnnotationsAttribute.class) {
            attribute = new RuntimeInvisibleAnnotationsAttribute();
        } else if (type == RuntimeVisibleParameterAnnotationsAttribute.class) {
            attribute = new RuntimeVisibleParameterAnnotationsAttribute();
        } else if (type == RuntimeInvisibleParameterAnnotationsAttribute.class) {
            attribute = new RuntimeInvisibleParameterAnnotationsAttribute();
        } else if (type == RuntimeVisibleTypeAnnotationsAttribute.class) {
            attribute = new RuntimeVisibleTypeAnnotationsAttribute();
        } else if (type == RuntimeInvisibleTypeAnnotationsAttribute.class) {
            attribute = new RuntimeInvisibleTypeAnnotationsAttribute();
        } else if (type == StackMapTableAttribute.class) {
            attribute = new StackMapTableAttribute();
        } else if (type == InnerClassesAttribute.class) {
            attribute = new InnerClassesAttribute();
        } else if (type == EnclosingMethodAttribute.class) {
            attribute = new EnclosingMethodAttribute();
        } else if (type == SourceDebugExtensionAttribute.class) {
            attribute = new SourceDebugExtensionAttribute();
        } else if (type == LineNumberTableAttribute.class) {
            attribute = new LineNumberTableAttribute();
        } else if (type == LocalVariableTableAttribute.class) {
            attribute = new LocalVariableTableAttribute();
        } else if (type == LocalVariableTypeTableAttribute.class) {
            attribute = new LocalVariableTypeTableAttribute();
        } else if (type == AnnotationDefaultAttribute.class) {
            attribute = new AnnotationDefaultAttribute();
        } else if (type == BootstrapMethodsAttribute.class) {
            attribute = new BootstrapMethodsAttribute();
        } else if (type == MethodParametersAttribute.class) {
            attribute = new MethodParametersAttribute();
        } else {
            throw new IllegalStateException("Unknown attribute type: " + type.getName());
        }

        attribute.parseBody(length, bytesReader, constantPool, generator);
        return type.cast(attribute);
    }

    @Override
    public GenericAttribute readGenericAttribute(String name, int length, BytesReader bytesReader,
            ConstantPool constantPool, AttributeGenerator generator) throws IOException {
        GenericAttribute attribute = new GenericAttribute(name);
        attribute.parseBody(length, bytesReader, constantPool, generator);
        return attribute;
    }
}
