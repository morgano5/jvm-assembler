package au.id.villar.bytecode.parser.constant;

import au.id.villar.bytecode.constant.FieldRefConstant;
import au.id.villar.bytecode.constant.MethodHandleConstant;
import au.id.villar.bytecode.constant.MethodHandleReferenceKind;

import java.util.Set;

import static au.id.villar.bytecode.constant.MethodHandleReferenceKind.GET_FIELD;
import static au.id.villar.bytecode.constant.MethodHandleReferenceKind.GET_STATIC;
import static au.id.villar.bytecode.constant.MethodHandleReferenceKind.PUT_FIELD;
import static au.id.villar.bytecode.constant.MethodHandleReferenceKind.PUT_STATIC;

public final class FieldRefParsingConstant extends MemberRefParsingConstant<FieldRefConstant> {

    private static final Set<MethodHandleReferenceKind> FIELD_KIND =
            Set.of(GET_FIELD, GET_STATIC, PUT_FIELD, PUT_STATIC);

    public FieldRefParsingConstant(int classIndex, int nameAndTypeIndex) {
        super(classIndex, nameAndTypeIndex);
    }

    FieldRefParsingConstant() {}

    @Override
    MethodHandleConstant.MemberType calculateMemberType(MethodHandleReferenceKind referenceKind, int mayorVersion) {
        if (!FIELD_KIND.contains(referenceKind)) {
            throw new IllegalStateException("Mismatch between kind " + referenceKind + " and field reference");
        }
        return MethodHandleConstant.MemberType.FIELD;
    }

    @Override
    protected FieldRefConstant createRefConstant(String className, String memberName, String descriptor) {
        return new FieldRefConstant(className, memberName, descriptor);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "FieldRefConstant{" +
                "classIndex=" + classIndex +
                ", nameAndTypeIndex=" + nameAndTypeIndex +
                '}';
    }
}
