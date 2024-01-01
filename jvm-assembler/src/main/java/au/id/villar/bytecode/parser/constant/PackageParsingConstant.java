package au.id.villar.bytecode.parser.constant;

public final class PackageParsingConstant extends IndexToUtf8ParsingConstant {

    public PackageParsingConstant(int descriptorIndex) {
        super(descriptorIndex);
    }

    public int getNameIndex() {
        return utf8Index;
    }

    PackageParsingConstant() {}

    @Override
    public String toString() {
        return "PackageConstant{" + utf8Index + '}';
    }
}
