package au.id.villar.bytecode.attribute.frame;

import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public class ChopFrame extends ExplicitOffsetDeltaFrame {

    private int lastChoppedVars;

    public ChopFrame(int offsetDelta, int lastChoppedVars) {
        super(offsetDelta);
        this.lastChoppedVars = lastChoppedVars;
    }

    public int getLastChoppedVars() {
        return lastChoppedVars;
    }

    @Override
    void parseBody(BytesReader bytesReader, ConstantPool constantPool) throws IOException {
        // DO nothing
    }

    @Override
    public String toString() {
        return "ChopFrame{" +
                "offsetDelta=" + getOffsetDelta() +
                ",lastChoppedVars=" + lastChoppedVars +
                '}';
    }
}
