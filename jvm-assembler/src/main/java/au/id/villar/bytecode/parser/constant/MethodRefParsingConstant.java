package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.attribute.Attribute;
import au.id.villar.bytecode.constant.MethodHandleConstant;
import au.id.villar.bytecode.constant.MethodHandleReferenceKind;
import au.id.villar.bytecode.constant.MethodRefConstant;

import java.util.List;
import java.util.Set;

import static au.id.villar.bytecode.constant.MethodHandleReferenceKind.INVOKE_SPECIAL;
import static au.id.villar.bytecode.constant.MethodHandleReferenceKind.INVOKE_STATIC;
import static au.id.villar.bytecode.constant.MethodHandleReferenceKind.INVOKE_VIRTUAL;
import static au.id.villar.bytecode.constant.MethodHandleReferenceKind.NEW_INVOKE_SPECIAL;

public final class MethodRefParsingConstant extends MemberRefParsingConstant<MethodRefConstant> {

    private static final Set<MethodHandleReferenceKind> METHOD_KIND =
            Set.of(INVOKE_VIRTUAL, INVOKE_STATIC, INVOKE_SPECIAL, NEW_INVOKE_SPECIAL);

    public MethodRefParsingConstant(int classIndex, int nameAndTypeIndex) {
        super(classIndex, nameAndTypeIndex);
    }

    MethodRefParsingConstant() {}

    @Override
    MethodHandleConstant.MemberType calculateMemberType(MethodHandleReferenceKind referenceKind, int mayorVersion) {
        if (!METHOD_KIND.contains(referenceKind)) {
            throw new IllegalStateException("Mismatch between kind " + referenceKind + " and method reference");
        }
        return MethodHandleConstant.MemberType.METHOD;
    }

    @Override
    protected MethodRefConstant createRefConstant(String className, String memberName, String descriptor) {
        return new MethodRefConstant(className, memberName, descriptor);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "MethodRefConstant{" +
                "classIndex=" + classIndex +
                ", nameAndTypeIndex=" + nameAndTypeIndex +
                '}';
    }
}
