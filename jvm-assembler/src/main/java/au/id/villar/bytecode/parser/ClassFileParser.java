package au.id.villar.bytecode.parser;

import au.id.villar.bytecode.Class;
import au.id.villar.bytecode.constant.Constant;
import au.id.villar.bytecode.constant.DoubleConstant;
import au.id.villar.bytecode.constant.LongConstant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ClassFileParser {

    public static void parse(InputStream bytecode, ClassFileHandler handler) throws IOException {
        ClassFileParser parser = new ClassFileParser(bytecode, handler);
        parser.parse();
    }

    public static Class parseToClass(InputStream stream) throws IOException {
        Class aClass = new Class();
        ClassFileParser.parse(stream, new ParserClassFileHandler(aClass));
        return aClass;
    }

    private final InputStream bytecode;
    private final ClassFileHandler handler;
    private final BytesReader bytesReader;
    private final RestrictedInputStream attributeInfo = new RestrictedInputStream();

    private ClassFileParser(InputStream bytecode, ClassFileHandler handler) {
        Objects.requireNonNull(bytecode);
        Objects.requireNonNull(handler);

        this.bytecode = bytecode;
        this.handler = handler;
        this.bytesReader = new BytesReader(this.bytecode);
    }

    private void parse() throws IOException {

        if (bytesReader.readInt() != 0xCAFEBABE) {
            throw new IOException("Not a stream containing a class file");
        }

        boolean keepParsing = readMayorAndMinor();

        if (keepParsing) {
            keepParsing = readConstantPool();
        }

        if (keepParsing) {
            keepParsing = readAccessAndClassAndSuper();
        }

        if (keepParsing) {
            keepParsing = readInterfaces();
        }

        if (keepParsing) {
            keepParsing = readFields();
        }

        if (keepParsing) {
            keepParsing = readMethods();
        }

        if (keepParsing) {
            keepParsing = readAttributes();
        }

        if (keepParsing) {
            handler.end();
        }
    }

    private boolean readMayorAndMinor() throws IOException {
        boolean keepParsing = handler.number(bytesReader.readShort(), ClassFileHandler.NumberType.MINOR);
        if (keepParsing) {
            keepParsing = handler.number(bytesReader.readShort(), ClassFileHandler.NumberType.MAYOR);
        }
        return keepParsing;
    }

    private boolean readAccessAndClassAndSuper() throws IOException {
        boolean keepParsing = handler.number(bytesReader.readShort(), ClassFileHandler.NumberType.ACCESS_FLAGS);
        if (keepParsing) {
            keepParsing = handler.number(bytesReader.readShort(), ClassFileHandler.NumberType.THIS_INDEX);
        }
        if (keepParsing) {
            keepParsing = handler.number(bytesReader.readShort(), ClassFileHandler.NumberType.SUPER_INDEX);
        }
        return keepParsing;
    }

    private boolean readConstantPool() throws IOException {
        int size = bytesReader.readShort() - 1;
        boolean keepParsing = handler.number(size, ClassFileHandler.NumberType.CONSTANT_POOL);
        int index = 1;
        while(index <= size && keepParsing) {
            Constant constant = Constant.readConstant(bytesReader);
            keepParsing = handler.constant(constant, index++);
            if(constant.getClass() == LongConstant.class || constant.getClass() == DoubleConstant.class) {
                index++;
            }
        }
        return keepParsing;
    }

    private boolean readInterfaces() throws IOException {
        int size = bytesReader.readShort();
        boolean keepParsing = handler.number(size, ClassFileHandler.NumberType.INTERFACES);
        for(int index = 0; index < size && keepParsing; index++) {
            keepParsing = handler.interfaceIndex(bytesReader.readShort());
        }
        return keepParsing;
    }

    private boolean readFields() throws IOException {
        int size = bytesReader.readShort();
        boolean keepParsing = handler.number(size, ClassFileHandler.NumberType.FIELDS);
        for(int index = 0; index < size && keepParsing; index++) {
            keepParsing = handler.field(bytesReader.readShort(), bytesReader.readShort(), bytesReader.readShort());
            if (keepParsing) {
                readAttributes();
            }
        }
        return keepParsing;
    }

    private boolean readMethods() throws IOException {
        int size = bytesReader.readShort();
        boolean keepParsing = handler.number(size, ClassFileHandler.NumberType.METHODS);
        for(int index = 0; index < size && keepParsing; index++) {
            keepParsing = handler.method(bytesReader.readShort(), bytesReader.readShort(), bytesReader.readShort());
            if (keepParsing) {
                readAttributes();
            }
        }
        return keepParsing;
    }

    private boolean readAttributes() throws IOException {
        int size = bytesReader.readShort();
        boolean keepParsing = handler.number(size, ClassFileHandler.NumberType.ATTRIBUTES);
        for(int index = 0; index < size && keepParsing; index++) {
            int nameIndex = bytesReader.readShort();
            int infoSize = bytesReader.readInt();
            keepParsing = handler.attribute(nameIndex, infoSize, attributeInfo.reuse(infoSize, bytecode));
            if (keepParsing) {
                attributeInfo.consumeRemaining();
            }
        }
        return keepParsing;
    }

}
