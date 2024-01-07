package au.id.villar.bytecode.parser;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public interface CodeHandler {

    enum ArrayType {
        BOOLEAN(4),
        CHAR(5),
        FLOAT(6),
        DOUBLE(7),
        BYTE(8),
        SHORT(9),
        INT(10),
        LONG(11);

        private final int type;

        ArrayType(int type) {
            this.type = type;
        }

        static ArrayType toArrayType(int type) {
            for (ArrayType e : ArrayType.values()) {
                if (e.type == type) {
                    return e;
                }
            }
            throw new IllegalArgumentException("Unknown ArrayType: " + type);
        }
    }

    default void operation(int offset, int opcode) throws IOException {};

    default void operationImmediateByte(int offset, int opcode, byte operand) throws IOException {};

    default void operationSignedShortOperand(int offset, int opcode, short operand) throws IOException {};

    default void operationConstantPoolIndex(int offset, int opcode, int poolIndex) throws IOException {};

    default void operationByteIndex(int offset, int opcode, int index) throws IOException {};

    default void operationArrayType(int offset, int opcode, ArrayType arrayType) throws IOException {};

    default void operationIntIncrement(int offset, int opcode, int index, int value) throws IOException {};

    default void operationTableswitch(int offset, int opcode, int defaultOffset, int low, int high,
            Iterator<Integer> offsets) throws IOException {};

    default void operationLookupswitch(int offset, int opcode, int defaultOffset, int numPairs,
            Iterator<Map.Entry<Integer, Integer>> offsets) throws IOException {};

    default void operationWideByteIndex(int offset, int opcode, int index) throws IOException {};

    default void operationWideIntIncrement(int offset, int opcode, int index, int value) throws IOException {};

    default void operationMultiANewArray(int offset, int opcode, int poolIndex, int dimensions) throws IOException {};

    default void operationBranching(int offset, int opcode, short branchingOffset) throws IOException {};

    default void operationLongBranching(int offset, int opcode, int branchingOffset) throws IOException {};

    default void operationInvokeDynamic(int offset, int opcode, int poolIndex) throws IOException {};

    default void operationInvokeInterface(int offset, int opcode, int poolIndex, int count) throws IOException {};

}
