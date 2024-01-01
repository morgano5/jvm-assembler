package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.Map;

public abstract sealed class IndexToUtf8ParsingConstant
        extends ParsingConstant
        permits ClassParsingConstant, MethodTypeParsingConstant, ModuleParsingConstant, PackageParsingConstant,
            StringParsingConstant {

    protected int utf8Index;

    IndexToUtf8ParsingConstant(int utf8Index) {
        this.utf8Index = utf8Index;
    }

    IndexToUtf8ParsingConstant() {}

    @Override
    void parseBody(BytesReader bytesReader) throws IOException {
        utf8Index = bytesReader.readShort();
    }
}
