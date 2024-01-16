package au.id.villar.bytecode.compiler;

import au.id.villar.bytecode.AccessFlags;
import au.id.villar.bytecode.ClassFile;
import au.id.villar.bytecode.Field;
import au.id.villar.bytecode.Method;
import au.id.villar.bytecode.attribute.Attribute;
import au.id.villar.bytecode.attribute.AttributeGenerator;
import au.id.villar.bytecode.attribute.DefaultAttributeGenerator;
import au.id.villar.bytecode.constant.Constant;
import au.id.villar.bytecode.constant.ConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

class ParserClassFileHandler implements ClassFileHandler {

    private ConstantPool constantPool;
    private final ClassFile classFile;
    private final AttributeGenerator attrGenerator;

    public ParserClassFileHandler(ClassFile classFile) {
        this.classFile = classFile;
        this.attrGenerator = new DefaultAttributeGenerator();
    }

    static abstract class AttrCounter {
        List<Attribute> attrs;
        int count;

        void execute() {
        }
    }

    Deque<AttrCounter> attrCounters = new ArrayDeque<>();
    BytesReader bytesReader = new BytesReader(null);

    @Override
    public final boolean number(int number, NumberType numberType) {
        switch (numberType) {
            case MINOR:
                classFile.setMinor(number);
                break;
            case MAYOR:
                classFile.setMayor(number);
                break;
            case CONSTANT_POOL:
                constantPool = new ConstantPool(number);
                break;
            case ACCESS_FLAGS:
                classFile.setAccessFlags(new AccessFlags((short) number, AccessFlags.FlagsType.CLASS));
                break;
            case THIS_INDEX:
                classFile.setNameIndex(number);
                break;
            case SUPER_INDEX:
                classFile.setSuperClassIndex(number);
                break;
            case INTERFACES:
                classFile.setInterfaceIndexes(new ArrayList<>(number));
                break;
            case FIELDS:
                classFile.setFields(new ArrayList<>(number));
                break;
            case METHODS:
                classFile.setMethods(new ArrayList<>(number));
                break;
            case ATTRIBUTES:
                AttrCounter attrCounter = attrCounters.peekFirst();
                if (attrCounter != null) {
                    attrCounter.count = number;
                    attrCounter.attrs = new ArrayList<>(number);
                    if (number == 0) {
                        attrCounter.execute();
                        attrCounters.pollFirst();
                    }
                } else {
                    classFile.setAttributes(new ArrayList<>(number));
                }
        }
        return true;
    }

    @Override
    public final boolean constant(Constant constant, int index) {
        constantPool.put(index, constant);
        return true;
    }

    @Override
    public final boolean interfaceIndex(int constantPoolIndex) {
        classFile.getInterfaceIndexes().add(constantPoolIndex);
        return true;
    }

    @Override
    public final boolean field(final int accessFlags, final int nameIndex, final int descriptorIndex) {
        AttrCounter attrCounter = new AttrCounter() {
            @Override
            void execute() {
                classFile.getFields().add(new Field(
                        new AccessFlags((short) accessFlags, AccessFlags.FlagsType.FIELD),
                        nameIndex, descriptorIndex, attrs));
            }
        };
        attrCounters.offerFirst(attrCounter);
        return true;
    }

    @Override
    public final boolean method(final int accessFlags, final int nameIndex, final int descriptorIndex) {
        AttrCounter attrCounter = new AttrCounter() {
            @Override
            void execute() {
                classFile.getMethods().add(new Method(
                        new AccessFlags((short) accessFlags, AccessFlags.FlagsType.METHOD),
                        nameIndex, descriptorIndex, attrs));
            }
        };
        attrCounters.offerFirst(attrCounter);
        return true;
    }

    @Override
    public final boolean attribute(int nameIndex, int length, InputStream info) {
        AttrCounter attrCounter = attrCounters.peekFirst();
        Attribute attribute;
        try {
            attribute = Attribute.readAttribute(nameIndex, length, bytesReader.reuse(info), constantPool,
                    attrGenerator);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (attrCounter != null) {
            attrCounter.attrs.add(attribute);
            if (--attrCounter.count == 0) {
                attrCounters.pollFirst();
                attrCounter.execute();
            }
        } else {
            classFile.getAttributes().add(attribute);
        }
        return true;
    }

    @Override
    public void end() {
        classFile.setConstants(constantPool);
    }

}
