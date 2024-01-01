package au.id.villar.bytecode.parser;

import au.id.villar.bytecode.Class;
import au.id.villar.bytecode.parser.constant.ParsingConstant;
import au.id.villar.bytecode.parser.constant.DoubleParsingConstant;
import au.id.villar.bytecode.parser.constant.LongParsingConstant;
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

        handler.number(bytesReader.readShort(), ClassFileHandler.NumberType.MINOR);
        handler.number(bytesReader.readShort(), ClassFileHandler.NumberType.MAYOR);

        readConstantPool();

        handler.number(bytesReader.readShort(), ClassFileHandler.NumberType.ACCESS_FLAGS);
        handler.number(bytesReader.readShort(), ClassFileHandler.NumberType.THIS_INDEX);
        handler.number(bytesReader.readShort(), ClassFileHandler.NumberType.SUPER_INDEX);

        readInterfaces();

        readFields();

        readMethods();

        readAttributes();

        handler.end();
    }

    private void readConstantPool() throws IOException {
        int size = bytesReader.readShort() - 1;
        handler.number(size, ClassFileHandler.NumberType.CONSTANT_POOL);
        int index = 1;
        while(index <= size) {
            ParsingConstant constant = ParsingConstant.readConstant(bytesReader);
            handler.constant(constant, index++);
            if(constant.getClass() == LongParsingConstant.class || constant.getClass() == DoubleParsingConstant.class) {
                index++;
            }
        }
    }

    private void readInterfaces() throws IOException {
        int size = bytesReader.readShort();
        handler.number(size, ClassFileHandler.NumberType.INTERFACES);
        for(int index = 0; index < size; index++) {
            handler.interfaceIndex(bytesReader.readShort());
        }
    }

    private void readFields() throws IOException {
        int size = bytesReader.readShort();
        handler.number(size, ClassFileHandler.NumberType.FIELDS);
        for(int index = 0; index < size; index++) {
            handler.field(bytesReader.readShort(), bytesReader.readShort(), bytesReader.readShort());
            readAttributes();
        }
    }

    private void readMethods() throws IOException {
        int size = bytesReader.readShort();
        handler.number(size, ClassFileHandler.NumberType.METHODS);
        for(int index = 0; index < size; index++) {
            handler.method(bytesReader.readShort(), bytesReader.readShort(), bytesReader.readShort());
            readAttributes();
        }
    }

    private void readAttributes() throws IOException {
        int size = bytesReader.readShort();
        handler.number(size, ClassFileHandler.NumberType.ATTRIBUTES);
        for(int index = 0; index < size; index++) {
            int nameIndex = bytesReader.readShort();
            int infoSize = bytesReader.readInt();
            handler.attribute(nameIndex, infoSize, attributeInfo.reuse(infoSize, bytecode));
            attributeInfo.consumeRemaining();
        }
    }

}
