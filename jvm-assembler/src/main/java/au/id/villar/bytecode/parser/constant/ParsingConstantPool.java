package au.id.villar.bytecode.parser.constant;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ParsingConstantPool {

    private final Map<Integer, ParsingConstant> constants;

    public ParsingConstantPool(int initialCapacity) {
        constants = new HashMap<>(initialCapacity);
    }

    public void put(Integer index, ParsingConstant constant) {
        constants.put(index, constant);
    }

    public ParsingConstant get(Integer index) {
        return constants.get(index);
    }

    public <T extends ParsingConstant> T get(Integer index, Class<T> type) {
        ParsingConstant constant = constants.get(index);
        if (!type.isInstance(constant)) {
            throw new IllegalStateException("Constant in the index " + index
                    + " does not exist or is not of the requested type");
        }
        return type.cast(constant);
    }

    public Set<Integer> getIndexes() {
        return constants.keySet();
    }

    public String getStringFromUtf8(Integer utf8Index) {
        if (constants.get(utf8Index) instanceof Utf8ParsingConstant utf8Constant) {
            return utf8Constant.getValue();
        }
        throw new IllegalStateException("Constant in the index " + utf8Index + " does not exist or is not UTF8");
    }

    public String getClassName(Integer classConstantIndex) {
        if (constants.get(classConstantIndex) instanceof ClassParsingConstant classConstant) {
            return getStringFromUtf8(classConstant.getNameIndex());
        } else {
            throw new IllegalStateException("Class name doesn't exist of constant is not the correct type");
        }
    }
}
