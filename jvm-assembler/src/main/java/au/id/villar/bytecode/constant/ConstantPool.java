package au.id.villar.bytecode.constant;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstantPool {

    private final Map<Integer, Constant> constants;

    public ConstantPool(int initialCapacity) {
        constants = new HashMap<>(initialCapacity);
    }

    public void put(Integer index, Constant constant) {
        constants.put(index, constant);
    }

    public Constant get(Integer index) {
        return constants.get(index);
    }

    public <T extends Constant> T get(Integer index, Class<T> type) {
        Constant constant = constants.get(index);
        if (!type.isInstance(constant)) {
            throw new IllegalStateException("Constant in the index " + index
                    + " does not exist or is not of the requested type");
        }
        return type.cast(constant);
    }

    public LoadableConstant getLoadable(Integer index) {
        Constant constant = get(index);
        if (constant instanceof LoadableConstant loadable) {
            return loadable;
        }
        throw new IllegalArgumentException("Constant of index (" + index + ") ");
    }

    public List<Integer> getOrderedIndexes() {
        return constants.keySet().stream().sorted(Comparator.comparingInt(i -> i)).toList();
    }

    public String getStringFromUtf8(Integer utf8Index) {
        if (constants.get(utf8Index) instanceof Utf8Constant utf8Constant) {
            return utf8Constant.getValue();
        }
        throw new IllegalStateException("Constant in the index " + utf8Index + " does not exist or is not UTF8");
    }

    public String getClassName(Integer classConstantIndex) {
        if (constants.get(classConstantIndex) instanceof ClassConstant classConstant) {
            return getStringFromUtf8(classConstant.getNameIndex());
        } else {
            throw new IllegalStateException("Class name doesn't exist of constant is not the correct type");
        }
    }
}
