package au.id.villar.bytecode.parser;

import au.id.villar.bytecode.parser.constant.ParsingConstant;

import java.io.InputStream;

public interface ClassFileHandler {

    enum NumberType {
        MAYOR,
        MINOR,

        ACCESS_FLAGS,
        THIS_INDEX,
        SUPER_INDEX,

        CONSTANT_POOL,
        INTERFACES,
        FIELDS,
        METHODS,
        ATTRIBUTES
    }

    default boolean number(int number, NumberType numberType) { return true; }

    default boolean constant(ParsingConstant constant, int index) { return true; }

    default boolean interfaceIndex(int constantPoolIndex) { return true; }

    default boolean field(int accessFlags, int nameIndex, int descriptorIndex) { return true; }

    default boolean method(int accessFlags, int nameIndex, int descriptorIndex) { return true; }

    default boolean attribute(int nameIndex, int length, InputStream info) { return true; }

    default void end() {}
}
