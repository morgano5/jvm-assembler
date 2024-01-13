package au.id.villar.bytecode.attribute.frame;

import au.id.villar.bytecode.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public class SameFrame extends ExplicitOffsetDeltaFrame {

    public SameFrame(int offsetDelta) {
        super(offsetDelta);
    }

    @Override
    void parseBody(BytesReader bytesReader, ParsingConstantPool constantPool) throws IOException {
        // DO nothing
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{offsetDelta=" + getOffsetDelta() + "}";
    }

}
