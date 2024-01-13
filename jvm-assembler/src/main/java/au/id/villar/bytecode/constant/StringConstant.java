package au.id.villar.bytecode.constant;

public final class StringConstant extends IndexToUtf8Constant {

    public StringConstant(int stringIndex) {
        super(stringIndex);
    }

    public int getStringIndex() {
        return utf8Index;
    }

    StringConstant() {}

    @Override
    public String toString() {
        return "StringConstant{" + utf8Index + '}';
    }
}
