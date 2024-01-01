package au.id.villar.bytecode.parser.constant;

public final class ModuleParsingConstant extends IndexToUtf8ParsingConstant {

    public ModuleParsingConstant(int descriptorIndex) {
        super(descriptorIndex);
    }

    public int getNameIndex() {
        return utf8Index;
    }

    ModuleParsingConstant() {}

    @Override
    public String toString() {
        return "ModuleConstant{" + utf8Index + '}';
    }
}
