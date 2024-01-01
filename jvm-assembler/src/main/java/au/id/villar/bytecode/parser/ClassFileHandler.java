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

    default void number(int number, NumberType numberType) {}

    default void constant(ParsingConstant constant, int index) {}

    default void interfaceIndex(int constantPoolIndex) {}

    default void field(int accessFlags, int nameIndex, int descriptorIndex) {}

    default void method(int accessFlags, int nameIndex, int descriptorIndex) {}

    default void attribute(int nameIndex, int length, InputStream info) {}

    default void end() {}
}
