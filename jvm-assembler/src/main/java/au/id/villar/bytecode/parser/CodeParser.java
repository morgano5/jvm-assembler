package au.id.villar.bytecode.parser;

import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class CodeParser {

    public static void parse(InputStream bytecode, int bytecodeLength, CodeHandler handler) throws IOException {
        try {
            CodeParser parser = new CodeParser(bytecode, bytecodeLength, handler);
            parser.parse();
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    private final CodeHandler handler;
    private final int codeLength;
    private final BytesReader bytesReader;

    private CodeParser(InputStream bytecode, int bytecodeLength, CodeHandler handler) {
        this.handler = handler;
        this.codeLength = bytecodeLength;
        this.bytesReader = new BytesReader(bytecode);
    }

    private void parse() throws IOException {
        int opcode;
        int offset = 0;

        while (offset < codeLength) {
            opcode = bytesReader.readByte();
            offset += readRestOfOperation(offset, opcode);
        }
    }

    private int readRestOfOperation(int offset, int opcode) throws IOException {

        switch(opcode) {
            case 0x10:
                handler.operationImmediateByte(offset, opcode, (byte)bytesReader.readByte());
                return 2;
            case 0x11:
                handler.operationSignedShortOperand(offset, opcode, (short)bytesReader.readShort());
                return 3;
            case 0x12:
                handler.operationConstantPoolIndex(offset, opcode, bytesReader.readByte());
                return 2;
            case 0x13, 0x14, 0xB2, 0xB3, 0xB4, 0xB5, 0xB6, 0xB7, 0xB8, 0xBB, 0xBD, 0xC0, 0xC1:
                handler.operationConstantPoolIndex(offset, opcode, bytesReader.readShort());
                return 3;
            case 0x15, 0x16, 0x17, 0x18, 0x19, 0x36, 0x37, 0x38, 0x39, 0x3A, 0xA9:
                handler.operationByteIndex(offset, opcode, bytesReader.readByte());
                return 2;
            case 0xBC:
                handler.operationArrayType(offset, opcode, CodeHandler.ArrayType.toArrayType(bytesReader.readByte()));
                return 2;
            case 0x84:
                handler.operationIntIncrement(offset, opcode, bytesReader.readByte(), (byte)bytesReader.readByte());
                return 3;
            case 0xAA: {
                final int pad = readToPad(offset + 1);
                final int defaultOffset = bytesReader.readInt();
                final int low = bytesReader.readInt();
                final int high = bytesReader.readInt();
                final int numOffsets = high - low + 1;
                final Iterator<Integer> iterator = new InternalIntIterator(numOffsets, bytesReader);
                handler.operationTableswitch(offset, opcode, defaultOffset, low, high, iterator);
                iterator.forEachRemaining(i -> {
                });
                return pad + 13 + numOffsets * 4;
            }
            case 0xAB: {
                final int pad = readToPad(offset + 1);
                final int defaultOffset = bytesReader.readInt();
                final int numPairs = bytesReader.readInt();
                final Iterator<Map.Entry<Integer, Integer>> iterator = new InternalMapIterator(numPairs, bytesReader);
                handler.operationLookupswitch(offset, opcode, defaultOffset, numPairs, iterator);
                iterator.forEachRemaining(i -> {
                });
                return pad + 9 + numPairs * 8;
            }
            case 0xC4: {
                final int IINC_OPCODE = 0x84;
                final int widenOpcode = bytesReader.readByte();
                final int index = bytesReader.readShort();
                if (widenOpcode == IINC_OPCODE) {
                    final int immediateValue = bytesReader.readShort();
                    handler.operationWideIntIncrement(offset, opcode, index, immediateValue);
                    return 6;
                } else {
                    handler.operationWideByteIndex(offset, widenOpcode, index);
                    return 4;
                }
            }
            case 0xC5:
                handler.operationMultiANewArray(offset, opcode, bytesReader.readShort(), bytesReader.readByte());
                return 4;
            case 0xC6, 0xC7, 0x99, 0x9A, 0x9B, 0x9C, 0x9D, 0x9E, 0x9F, 0xA0, 0xA1, 0xA2, 0xA3, 0xA4, 0xA5, 0xA6, 0xA7,
                    0xA8:
                handler.operationBranching(offset, opcode, (short)bytesReader.readShort());
                return 3;
            case 0xC8, 0xC9:
                handler.operationLongBranching(offset, opcode, bytesReader.readShort());
                return 5;
            case 0xB9:
                handler.operationInvokeInterface(offset, opcode, bytesReader.readShort(), bytesReader.readByte());
                bytesReader.readByte();
                return 5;
            case 0xBA:
                handler.operationInvokeDynamic(offset, opcode, bytesReader.readShort());
                bytesReader.readShort();
                return 5;
            default:
                handler.operation(offset, opcode);
                return 1;
        }
    }

    private int readToPad(int currentOffset) throws IOException {
        final int pad = (4 - (currentOffset % 4)) % 4;

        currentOffset = pad;
        while (currentOffset-- > 0) {
            bytesReader.readByte();
        }
        return pad;
    }

    private static class InternalIntIterator implements Iterator<Integer> {

        private int remaining;
        private final BytesReader reader;

        public InternalIntIterator(int remaining, BytesReader reader) {
            this.remaining = remaining;
            this.reader = reader;
        }

        @Override
        public boolean hasNext() {
            return remaining > 0;
        }

        @Override
        public Integer next() {
            try {
                if (remaining == 0) {
                    throw new NoSuchElementException();
                }
                remaining--;
                return reader.readInt();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    private static class InternalMapIterator implements Iterator<Map.Entry<Integer, Integer>> {

        private int remaining;
        private final BytesReader reader;

        public InternalMapIterator(int remaining, BytesReader reader) {
            this.remaining = remaining;
            this.reader = reader;
        }

        @Override
        public boolean hasNext() {
            return remaining > 0;
        }

        @Override
        public Map.Entry<Integer, Integer> next() {
            try {
                if (remaining == 0) {
                    throw new NoSuchElementException();
                }
                remaining--;
                return new InternalMapEntry(reader.readInt(), reader.readInt());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    private static class InternalMapEntry implements Map.Entry<Integer, Integer> {

        private final Integer key;
        private final Integer value;

        public InternalMapEntry(Integer key, Integer value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Integer getKey() {
            return key;
        }

        @Override
        public Integer getValue() {
            return value;
        }

        @Override
        public Integer setValue(Integer value) {
            throw new UnsupportedOperationException();
        }
    }
}
