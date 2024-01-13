package au.id.villar.bytecode.constant;

public final class ClassConstant extends IndexToUtf8Constant {

    public ClassConstant(int nameIndex) {
        super(nameIndex);
    }

    public int getNameIndex() {
        return utf8Index;
    }

    ClassConstant() {}

    @Override
    public String toString() {
        return "ClassConstant{" + utf8Index + '}';
    }
}
