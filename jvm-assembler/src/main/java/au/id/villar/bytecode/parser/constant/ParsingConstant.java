package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public abstract sealed class ParsingConstant
        permits AbstractDynamicParsingConstant,
        IndexToUtf8ParsingConstant,
        MemberRefParsingConstant,
        MethodHandleParsingConstant,
        NameAndTypeParsingConstant,
        ValueParsingConstant {

    public static ParsingConstant readConstant(BytesReader bytesReader) throws IOException {

        ParsingConstant constant;
        int tag = bytesReader.readByte();

        constant = switch (tag) {
            case 1 -> new Utf8ParsingConstant();
            // case 2 -> Not used
            case 3 -> new IntegerParsingConstant();
            case 4 -> new FloatParsingConstant();
            case 5 -> new LongParsingConstant();
            case 6 -> new DoubleParsingConstant();
            case 7 -> new ClassParsingConstant();
            case 8 -> new StringParsingConstant();
            case 9 -> new FieldRefParsingConstant();
            case 10 -> new MethodRefParsingConstant();
            case 11 -> new InterfaceMethodRefParsingConstant();
            case 12 -> new NameAndTypeParsingConstant();
            // case 13 -> Not used
            // case 14 -> Not used
            case 15 -> new MethodHandleParsingConstant();
            case 16 -> new MethodTypeParsingConstant();
            case 17 -> new DynamicParsingConstant();
            case 18 -> new InvokeDynamicParsingConstant();
            case 19 -> new ModuleParsingConstant();
            case 20 -> new PackageParsingConstant();
            default -> throw new IOException("Unexpected constant tag: " + tag);
        };

        constant.parseBody(bytesReader);
        return constant;
    }

    @Deprecated
    public static String toString(int index, ParsingConstantPool constantPool) {

        final ParsingConstant constant = constantPool.get(index);

        if (constant instanceof IndexToUtf8ParsingConstant con) {
            return constantPool.getStringFromUtf8(con.utf8Index);
        }
        if (constant instanceof ValueParsingConstant con) {
            return con.toStringValue();
        }

        throw new RuntimeException("Unexpected constant type: " + constant.getClass().getSimpleName());
    }

    abstract void parseBody(BytesReader bytesReader) throws IOException;

}
