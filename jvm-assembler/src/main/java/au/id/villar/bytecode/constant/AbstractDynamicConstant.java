package au.id.villar.bytecode.constant;

import au.id.villar.bytecode.util.BytesReader;
import au.id.villar.bytecode.util.BytesWriter;

import java.io.IOException;

public abstract sealed class AbstractDynamicConstant
        extends Constant
        permits InvokeDynamicConstant, DynamicConstant {

    protected int bootstrapMethodAttrIndex;
    protected int nameAndTypeIndex;

    public AbstractDynamicConstant(int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
        this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    public int getBootstrapMethodAttrIndex() {
        return bootstrapMethodAttrIndex;
    }

    public int getNameAndTypeIndex() {
        return nameAndTypeIndex;
    }

    AbstractDynamicConstant() {}

    @Override
    public void write(BytesWriter bytesWriter) throws IOException {
        bytesWriter.writeByte(getRawTag());
        bytesWriter.writeShort(bootstrapMethodAttrIndex);
        bytesWriter.writeShort(nameAndTypeIndex);
    }

    @Override
    void parseBody(BytesReader bytesReader) throws IOException {
        bootstrapMethodAttrIndex = bytesReader.readShort();
        nameAndTypeIndex = bytesReader.readShort();
    }

    protected abstract byte getRawTag();
}
