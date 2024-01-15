package au.id.villar.bytecode.compiler;

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

    default void operation(int offset, int opcode) {};

    default void operationImmediateByte(int offset, int opcode, byte operand) {};

    default void operationSignedShortOperand(int offset, int opcode, short operand) {};

    default void operationConstantPoolIndex(int offset, int opcode, int poolIndex) {};

    default void operationByteIndex(int offset, int opcode, int index) {};

    default void operationArrayType(int offset, int opcode, ArrayType arrayType) {};

    default void operationIntIncrement(int offset, int opcode, int index, int value) {};

    default void operationTableswitch(int offset, int opcode, int defaultOffset, int low, int high,
            Iterator<Integer> offsets) {};

    default void operationLookupswitch(int offset, int opcode, int defaultOffset, int numPairs,
            Iterator<Map.Entry<Integer, Integer>> offsets) {};

    default void operationWideByteIndex(int offset, int opcode, int index) {};

    default void operationWideIntIncrement(int offset, int opcode, int index, int value) {};

    default void operationMultiANewArray(int offset, int opcode, int poolIndex, int dimensions) {};

    default void operationBranching(int offset, int opcode, short branchingOffset) {};

    default void operationLongBranching(int offset, int opcode, int branchingOffset) {};

    default void operationInvokeDynamic(int offset, int opcode, int poolIndex) {};

    default void operationInvokeInterface(int offset, int opcode, int poolIndex, int count) {};

}
