package au.id.villar.bytecode.constant;

import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public abstract sealed class IndexToUtf8Constant
        extends Constant
        permits ClassConstant, MethodTypeConstant, ModuleConstant, PackageConstant,
        StringConstant {

    protected int utf8Index;

    IndexToUtf8Constant(int utf8Index) {
        this.utf8Index = utf8Index;
    }

    IndexToUtf8Constant() {}

    @Override
    void parseBody(BytesReader bytesReader) throws IOException {
        utf8Index = bytesReader.readShort();
    }
}
