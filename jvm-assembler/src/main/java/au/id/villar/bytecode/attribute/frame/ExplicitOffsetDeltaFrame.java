package au.id.villar.bytecode.attribute.frame;

public abstract class ExplicitOffsetDeltaFrame extends StackMapFrame {

    private int offsetDelta;

    ExplicitOffsetDeltaFrame(int offsetDelta) {
        this.offsetDelta = offsetDelta;
    }

    public int getOffsetDelta() {
        return offsetDelta;
    }

}
