package au.id.villar.bytecode;

import au.id.villar.bytecode.attribute.InnerClassesAttribute;
import au.id.villar.bytecode.attribute.MethodParametersAttribute;

import java.util.Set;

public class AccessFlags {

    private static final short ACC_PUBLIC =       0x0001; // Declared public; may be accessed from outside its package.
    private static final short ACC_PRIVATE =      0x0002; // Declared private; usable only within the defining class.
    private static final short ACC_PROTECTED =    0x0004; // Declared protected; may be accessed within subclasses.
    private static final short ACC_STATIC =       0x0008; // Declared static.
    private static final short ACC_FINAL =        0x0010; // Declared final; for classes: no subclasses allowed; for fields: never directly assigned to after object construction; for methods: must not be overridden.
    private static final short ACC_SUPER =        0x0020; // Treat superclass methods specially when invoked by the invokespecial instruction.
    private static final short ACC_VOLATILE =     0x0040; // Declared volatile; cannot be cached.
    private static final short ACC_TRANSIENT =    0x0080; // Declared transient; not written or read by a persistent object manager.
    private static final short ACC_NATIVE =       0x0100; // Declared native; implemented in a language other than Java.
    private static final short ACC_INTERFACE =    0x0200; // Is an interface, not a class.
    private static final short ACC_ABSTRACT =     0x0400; // Declared abstract; for classes: must not be instantiated; for methods: no implementation is provided.
    private static final short ACC_STRICT =       0x0800; // Declared strictfp; floating-point mode is FP-strict.
    private static final short ACC_SYNTHETIC =    0x1000; // Declared synthetic; not present in the source code.
    private static final short ACC_ANNOTATION =   0x2000; // Declared as an annotation type.
    private static final short ACC_ENUM =         0x4000; // Declared as an enum type (classes) or as an element of an enum (fields).
    private static final short ACC_MODULE =       (short)0x8000; // Is a module, not a class or interface.
    private static final short ACC_MANDATED =     (short)0x8000;

    private static final short ACC_SYNCHRONIZED = 0x0020; // Declared synchronized; invocation is wrapped by a monitor use.
    private static final short ACC_BRIDGE =       0x0040; // A bridge method, generated by the compiler.
    private static final short ACC_VARARGS =      0x0080; // Declared with variable number of arguments.

    private static final java.lang.Class<Class> classType = Class.class;
    private static final java.lang.Class<Field> fieldType = Field.class;
    private static final java.lang.Class<Method> methodType = Method.class;
    private static final java.lang.Class<InnerClassesAttribute> innerClassesType = InnerClassesAttribute.class;
    private static final java.lang.Class<MethodParametersAttribute> methodParametersType
            = MethodParametersAttribute.class;

    private static final Set<java.lang.Class<?>> VALID_CLASSES = Set.of(classType, fieldType, methodType,
        innerClassesType, methodParametersType);

    private final short flags;
    private final java.lang.Class<?> memberType;

    private boolean hasFlagAndBelongsToOneClass(short testingFlags, short flagConstant,
            java.lang.Class<?> ... classes) {
        for (java.lang.Class<?> c : classes) {
            if (memberType == c) {
                return (testingFlags & flagConstant) != 0;
            }
        }
        return false;
    }

    public AccessFlags(short flags, java.lang.Class<?> memberType) {
        if (!VALID_CLASSES.contains(memberType)) {
            throw new IllegalArgumentException("Invalid class type: " + memberType.getName());
        }
        this.flags = flags;
        this.memberType = memberType;
    }

    public boolean isPublic() {
        return hasFlagAndBelongsToOneClass(flags, ACC_PUBLIC, classType, fieldType, methodType, innerClassesType);
    }

    public boolean isPrivate() {
        return hasFlagAndBelongsToOneClass(flags, ACC_PRIVATE, fieldType, methodType, innerClassesType);
    }

    public boolean isProtected() {
        return hasFlagAndBelongsToOneClass(flags, ACC_PROTECTED, fieldType, methodType, innerClassesType);
    }

    public boolean isStatic() {
        return hasFlagAndBelongsToOneClass(flags, ACC_STATIC, fieldType, methodType, innerClassesType);
    }

    public boolean isFinal() {
        return hasFlagAndBelongsToOneClass(flags, ACC_FINAL, classType, methodType, fieldType, innerClassesType,
                methodParametersType);
    }

    public boolean isSuper() {
        return hasFlagAndBelongsToOneClass(flags, ACC_SUPER, classType);
    }

    public boolean isVolatile() {
        return hasFlagAndBelongsToOneClass(flags, ACC_VOLATILE, fieldType);
    }

    public boolean isTransient() {
        return hasFlagAndBelongsToOneClass(flags, ACC_TRANSIENT, fieldType);
    }

    // ==============================================================================
    public boolean isNative() {
        return hasFlagAndBelongsToOneClass(flags, ACC_NATIVE, methodType);
    }

    public boolean isInterface() {
        return hasFlagAndBelongsToOneClass(flags, ACC_INTERFACE, classType, innerClassesType);
    }

    public boolean isAbstract() {
        return hasFlagAndBelongsToOneClass(flags, ACC_ABSTRACT, classType, methodType, innerClassesType);
    }

    public boolean isStrict() {
        return hasFlagAndBelongsToOneClass(flags, ACC_STRICT, methodType);
    }

    public boolean isSynthetic() {
        return hasFlagAndBelongsToOneClass(flags, ACC_SYNTHETIC, classType, methodType, fieldType, innerClassesType,
                methodParametersType);
    }

    public boolean isAnnotation() {
        return hasFlagAndBelongsToOneClass(flags, ACC_ANNOTATION, classType, innerClassesType);
    }

    public boolean isEnum() {
        return hasFlagAndBelongsToOneClass(flags, ACC_ENUM, classType, innerClassesType, fieldType);
    }

    public boolean isModule() {
        return hasFlagAndBelongsToOneClass(flags, ACC_MODULE, classType);
    }

    public boolean isSynchronized() {
        return hasFlagAndBelongsToOneClass(flags, ACC_SYNCHRONIZED, methodType);
    }

    public boolean isBridge() {
        return hasFlagAndBelongsToOneClass(flags, ACC_BRIDGE, methodType);
    }

    public boolean isVargars() {
        return hasFlagAndBelongsToOneClass(flags, ACC_VARARGS, methodType);
    }

    public boolean isMandated() {
        return hasFlagAndBelongsToOneClass(flags, ACC_MANDATED, methodParametersType);
    }

    public String toAssembly() {
        StringBuilder builder = new StringBuilder();
        builder.append("access");
        if(isPublic()) builder.append(" public");
        if(isPrivate()) builder.append(" private");
        if(isProtected()) builder.append(" protected");
        if(isStatic()) builder.append(" static");
        if(isFinal()) builder.append(" final");
        if(isSuper()) builder.append(" super");
        if(isVolatile()) builder.append(" volatile");
        if(isTransient()) builder.append(" transient");
        if(isNative()) builder.append(" Native");
        if(isInterface()) builder.append(" interface");
        if(isAbstract()) builder.append(" abstract");
        if(isStrict()) builder.append(" strict");
        if(isSynthetic()) builder.append(" synthetic");
        if(isAnnotation()) builder.append(" annotation");
        if(isEnum()) builder.append(" enum");
        if(isModule()) builder.append(" module");
        if(isSynchronized()) builder.append(" synchronized");
        if(isBridge()) builder.append(" bridge");
        if(isVargars()) builder.append(" vargars");
        if(isMandated()) builder.append(" mandated");
        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AccessFlags{");
        if(isPublic()) builder.append(" Public");
        if(isPrivate()) builder.append(" Private");
        if(isProtected()) builder.append(" Protected");
        if(isStatic()) builder.append(" Static");
        if(isFinal()) builder.append(" Final");
        if(isSuper()) builder.append(" Super");
        if(isVolatile()) builder.append(" Volatile");
        if(isTransient()) builder.append(" Transient");
        if(isNative()) builder.append(" Native");
        if(isInterface()) builder.append(" Interface");
        if(isAbstract()) builder.append(" Abstract");
        if(isStrict()) builder.append(" Strict");
        if(isSynthetic()) builder.append(" Synthetic");
        if(isAnnotation()) builder.append(" Annotation");
        if(isEnum()) builder.append(" Enum");
        if(isModule()) builder.append(" Module");
        if(isSynchronized()) builder.append(" Synchronized");
        if(isBridge()) builder.append(" Bridge");
        if(isVargars()) builder.append(" Vargars");
        if(isMandated()) builder.append(" Mandated");
        builder.append("}");
        return builder.toString();
    }
}
