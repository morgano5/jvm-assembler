package au.id.villar.bytecode.constant;

import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public abstract sealed class Constant
        permits AbstractDynamicConstant,
        IndexToUtf8Constant,
        MemberRefConstant,
        MethodHandleConstant,
        NameAndTypeConstant,
        ValueConstant {

    public static Constant readConstant(BytesReader bytesReader) throws IOException {

        Constant constant;
        int tag = bytesReader.readByte();

        constant = switch (tag) {
            case 1 -> new Utf8Constant();
            // case 2 -> Not used
            case 3 -> new IntegerConstant();
            case 4 -> new FloatConstant();
            case 5 -> new LongConstant();
            case 6 -> new DoubleConstant();
            case 7 -> new ClassConstant();
            case 8 -> new StringConstant();
            case 9 -> new FieldRefConstant();
            case 10 -> new MethodRefConstant();
            case 11 -> new InterfaceMethodRefConstant();
            case 12 -> new NameAndTypeConstant();
            // case 13 -> Not used
            // case 14 -> Not used
            case 15 -> new MethodHandleConstant();
            case 16 -> new MethodTypeConstant();
            case 17 -> new DynamicConstant();
            case 18 -> new InvokeDynamicConstant();
            case 19 -> new ModuleConstant();
            case 20 -> new PackageConstant();
            default -> throw new IOException("Unexpected constant tag: " + tag);
        };

        constant.parseBody(bytesReader);
        return constant;
    }

    @Deprecated
    public static String toString(int index, ParsingConstantPool constantPool) {

        final Constant constant = constantPool.get(index);

        if (constant instanceof IndexToUtf8Constant con) {
            return constantPool.getStringFromUtf8(con.utf8Index);
        }
        if (constant instanceof ValueConstant con) {
            return con.toStringValue();
        }

        throw new RuntimeException("Unexpected constant type: " + constant.getClass().getSimpleName());
    }

    abstract void parseBody(BytesReader bytesReader) throws IOException;

}
