package au.id.villar.bytecode.attribute.frame;

import au.id.villar.bytecode.attribute.frame.type.VerificationTypeInfo;
import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FullFrame extends ExplicitOffsetDeltaFrame {

    private List<VerificationTypeInfo> locals;
    private List<VerificationTypeInfo> stackItems;

    public FullFrame(int offsetDelta, List<VerificationTypeInfo> locals, List<VerificationTypeInfo> stackItems) {
        super(offsetDelta);
        this.locals = Collections.unmodifiableList(new ArrayList<>(locals));
        this.stackItems = Collections.unmodifiableList(new ArrayList<>(stackItems));
    }

    FullFrame(int offsetDelta) {
        super(offsetDelta);
    }

    public List<VerificationTypeInfo> getLocals() {
        return locals;
    }

    public List<VerificationTypeInfo> getStackItems() {
        return stackItems;
    }

    @Override
    void parseBody(BytesReader bytesReader, ConstantPool constantPool) throws IOException {
        int aux;
        aux = bytesReader.readShort();
        locals = new ArrayList<>(aux);
        while(aux-- > 0) {
            locals.add(VerificationTypeInfo.readVerificationTypeInfo(bytesReader, constantPool));
        }
        aux = bytesReader.readShort();
        stackItems = new ArrayList<>(aux);
        while(aux-- > 0) {
            stackItems.add(VerificationTypeInfo.readVerificationTypeInfo(bytesReader, constantPool));
        }
        locals = Collections.unmodifiableList(locals);
        stackItems = Collections.unmodifiableList(stackItems);
    }

    @Override
    public String toString() {
        return "FullFrame{" +
                "offsetDelta=" + getOffsetDelta() +
                ", locals=" + locals +
                ", stackItems=" + stackItems +
                '}';
    }
}
