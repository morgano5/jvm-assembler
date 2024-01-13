package au.id.villar.bytecode.attribute.frame;

import au.id.villar.bytecode.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;

public abstract class StackMapFrame {

    public static StackMapFrame readStackMapFrame(BytesReader bytesReader, ParsingConstantPool constantPool)
            throws IOException {

        StackMapFrame mapFrame;
        int tag = bytesReader.readByte();
        if(tag >= 0 && tag <= 63) {
            mapFrame = new SameFrame(tag);
        } else if(tag >= 64 && tag <= 127) {
            mapFrame = new SameLocalsOneStackItemFrame(tag - 64);
        } else if(tag == 247) {
            mapFrame = new SameLocalsOneStackItemFrameExtended(bytesReader.readShort());
        } else if(tag >= 248 && tag <= 250) {
            mapFrame = new ChopFrame(bytesReader.readShort(), 251 - tag);
        } else if(tag == 251) {
            mapFrame = new SameFrameExtended(bytesReader.readShort());
        } else if(tag >= 252 && tag <= 254) {
            mapFrame = new AppendFrame(bytesReader.readShort(), tag - 251);
        } else if(tag == 255) {
            mapFrame = new FullFrame(bytesReader.readShort());
        } else {
            throw new IOException("unexpected stack map frame tag: " + tag);
        }

        mapFrame.parseBody(bytesReader, constantPool);
        return mapFrame;
    }

    StackMapFrame() {}

    abstract public int getOffsetDelta();

    abstract void parseBody(BytesReader bytesReader, ParsingConstantPool constantPool) throws IOException;

}
