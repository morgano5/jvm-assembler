package au.id.villar.bytecode.attribute;

import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CodeAttribute extends Attribute {

    public static class ExceptionInfo {
        public final int startPc;
        public final int endPc;
        public final int handlerPc;
        public final String exception;

        public ExceptionInfo(int startPc, int endPc, int handlerPc, String exception) {
            this.startPc = startPc;
            this.endPc = endPc;
            this.handlerPc = handlerPc;
            this.exception = exception;
        }

        @Override
        public String toString() {
            return "ExceptionInfo{" +
                    "startPc=" + startPc +
                    ", endPc=" + endPc +
                    ", handlerPc=" + handlerPc +
                    ", exception='" + exception + '\'' +
                    '}';
        }
    }

    private int maxStack;
    private int maxLocals;
    private byte[] code;
    private List<ExceptionInfo> exceptionTable;
    private List<Attribute> attributes;

    public CodeAttribute(int maxStack, int maxLocals, byte[] code, List<ExceptionInfo> exceptionTable,
                         List<Attribute> attributes) {
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
        this.code = code;
        this.exceptionTable = Collections.unmodifiableList(new ArrayList<>(exceptionTable));
        this.attributes = Collections.unmodifiableList(new ArrayList<>(attributes));
    }

    CodeAttribute() {}

    public int getMaxStack() {
        return maxStack;
    }

    public int getMaxLocals() {
        return maxLocals;
    }

    public int getCodeLength() {
        return code.length;
    }

    public InputStream createCodeStream() {
        return new ByteArrayInputStream(code);
    }

    public List<ExceptionInfo> getExceptionTable() {
        return exceptionTable;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    @Override
    public void parseBody(int length, BytesReader bytesReader, ConstantPool constantPool,
            AttributeGenerator generator) throws IOException {
        maxStack = bytesReader.readShort();
        maxLocals = bytesReader.readShort();
        int codeLength = bytesReader.readInt();
        code = new byte[codeLength];
        bytesReader.readMinimum(code, codeLength);
        int tableSize = bytesReader.readShort();
        List<ExceptionInfo> table = new ArrayList<>(tableSize);
        for(int count = 0; count < tableSize; count++) {
            table.add(new ExceptionInfo(bytesReader.readShort(), bytesReader.readShort(),
                    bytesReader.readShort(), constantPool.getClassName(bytesReader.readShort())));
        }
        exceptionTable = Collections.unmodifiableList(table);
        int numAttributes = bytesReader.readShort();
        List<Attribute> attributes = new ArrayList<>(numAttributes);
        for(int count = 0; count < numAttributes; count++) {
            String name = constantPool.getStringFromUtf8(bytesReader.readShort());
            attributes.add(Attribute.readAttribute(name, bytesReader.readInt(), bytesReader, constantPool, generator));
        }
        this.attributes = Collections.unmodifiableList(attributes);
    }

    @Override
    public String toString() {
        return "CodeAttribute{" +
                "maxStack=" + maxStack +
                ", maxLocals=" + maxLocals +
                ", code=" + Arrays.toString(code) +
                ", exceptionTable=" + exceptionTable +
                ", attributes=" + attributes +
                '}';
    }
}
