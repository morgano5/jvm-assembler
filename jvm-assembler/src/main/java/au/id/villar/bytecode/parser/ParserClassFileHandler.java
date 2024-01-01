package au.id.villar.bytecode.parser;

import au.id.villar.bytecode.AccessFlags;
import au.id.villar.bytecode.Class;
import au.id.villar.bytecode.Field;
import au.id.villar.bytecode.Method;
import au.id.villar.bytecode.attribute.Attribute;
import au.id.villar.bytecode.constant.Constant;
import au.id.villar.bytecode.parser.constant.Loadable;
import au.id.villar.bytecode.parser.constant.ParsingConstant;
import au.id.villar.bytecode.parser.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ParserClassFileHandler implements ClassFileHandler {

    private ParsingConstantPool constantPool;
    private final Class aClass;

    public ParserClassFileHandler(Class aClass) {
        this.aClass = aClass;
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
    public final void number(int number, NumberType numberType) {
        switch (numberType) {
            case MINOR:
                aClass.setMinor(number);
                break;
            case MAYOR:
                aClass.setMayor(number);
                break;
            case CONSTANT_POOL:
                constantPool = new ParsingConstantPool(number);
                break;
            case ACCESS_FLAGS:
                aClass.setAccessFlags(new AccessFlags((short) number, false));
                break;
            case THIS_INDEX:
                aClass.setName(constantPool.getClassName(number));
                break;
            case SUPER_INDEX:
                if (number != 0) {
                    aClass.setSuperClass(constantPool.getClassName(number));
                }
                break;
            case INTERFACES:
                aClass.setInterfaces(new ArrayList<>(number));
                break;
            case FIELDS:
                aClass.setFields(new ArrayList<>(number));
                break;
            case METHODS:
                aClass.setMethods(new ArrayList<>(number));
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
                    aClass.setAttributes(new ArrayList<>(number));
                }
        }
    }

    @Override
    public final void constant(ParsingConstant constant, int index) {
        constantPool.put(index, constant);
    }

    @Override
    public final void interfaceIndex(int constantPoolIndex) {
        aClass.getInterfaces().add(constantPool.getClassName(constantPoolIndex));
    }

    @Override
    public final void field(final int accessFlags, final int nameIndex, final int descriptorIndex) {
        AttrCounter attrCounter = new AttrCounter() {
            @Override
            void execute() {
                aClass.getFields().add(new Field(
                        new AccessFlags((short) accessFlags, false),
                        constantPool.getStringFromUtf8(nameIndex),
                        constantPool.getStringFromUtf8(descriptorIndex),
                        attrs
                ));
            }
        };
        attrCounters.offerFirst(attrCounter);
    }

    @Override
    public final void method(final int accessFlags, final int nameIndex, final int descriptorIndex) {
        AttrCounter attrCounter = new AttrCounter() {
            @Override
            void execute() {
                aClass.getMethods().add(new Method(
                        new AccessFlags((short) accessFlags, true),
                        constantPool.getStringFromUtf8(nameIndex),
                        constantPool.getStringFromUtf8(descriptorIndex),
                        attrs
                ));
            }
        };
        attrCounters.offerFirst(attrCounter);
    }

    @Override
    public final void attribute(int nameIndex, int length, InputStream info) {
        AttrCounter attrCounter = attrCounters.peekFirst();
        String name = constantPool.getStringFromUtf8(nameIndex);
        Attribute attribute;
        try {
            attribute = Attribute.readAttribute(name, length, bytesReader.reuse(info), constantPool);
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
            aClass.getAttributes().add(attribute);
        }
    }

    @Override
    public void end() {
        final Map<Integer, Constant> constants = new HashMap<>();
        for (Integer index : constantPool.getIndexes()) {
            ParsingConstant constant = constantPool.get(index);
            if (constant instanceof Loadable<?> loadable) {
                constants.put(index, loadable.toConstant(constantPool, aClass.getAttributes(), aClass.getMayor()));
            }
        }
        aClass.setConstants(constants);
    }

}
