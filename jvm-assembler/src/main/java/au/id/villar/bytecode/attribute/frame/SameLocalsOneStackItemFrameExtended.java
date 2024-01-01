package au.id.villar.bytecode.attribute.frame;

import au.id.villar.bytecode.attribute.frame.type.VerificationTypeInfo;

public class SameLocalsOneStackItemFrameExtended extends SameLocalsOneStackItemFrame {

    public SameLocalsOneStackItemFrameExtended(int offsetDelta, VerificationTypeInfo typeInfo) {
        super(offsetDelta, typeInfo);
    }

    SameLocalsOneStackItemFrameExtended(int offsetDelta) {
        super(offsetDelta);
    }

}
