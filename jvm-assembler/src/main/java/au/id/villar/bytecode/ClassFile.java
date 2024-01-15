package au.id.villar.bytecode;

import au.id.villar.bytecode.attribute.Attribute;
import au.id.villar.bytecode.constant.ConstantPool;

import java.util.List;

public class ClassFile {

    private int mayor;
    private int minor;
    private AccessFlags accessFlags;
    private ConstantPool constants;
    private Integer nameIndex;
    private Integer superClassIndex;
    private List<Integer> interfaceIndexes;
    private List<Field> fields;
    private List<Method> methods;
    private List<Attribute> attributes;

    public int getMayor() {
        return mayor;
    }

    public void setMayor(int mayor) {
        this.mayor = mayor;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public ConstantPool getConstants() {
        return constants;
    }

    public void setConstants(ConstantPool constants) {
        this.constants = constants;
    }

    public Integer getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(Integer nameIndex) {
        this.nameIndex = nameIndex;
    }

    public String getName() {
        return constants.getClassName(nameIndex);
    }

    public Integer getSuperClassIndex() {
        return superClassIndex;
    }

    public void setSuperClassIndex(Integer superClassIndex) {
        this.superClassIndex = superClassIndex;
    }

    public String getSuperClass() {
        return constants.getClassName(superClassIndex);
    }

    public AccessFlags getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(AccessFlags accessFlags) {
        this.accessFlags = accessFlags;
    }

    public List<Integer> getInterfaceIndexes() {
        return interfaceIndexes;
    }

    public void setInterfaceIndexes(List<Integer> interfaceIndexes) {
        this.interfaceIndexes = interfaceIndexes;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "ClassFile{" +
                "nameIndex='" + nameIndex + '\'' +
                ", mayor=" + mayor +
                ", minor=" + minor +
                ", constants=" + constants +
                ", accessFlags=" + accessFlags +
                ", superClassIndex='" + superClassIndex + '\'' +
                ", interfaces=" + interfaceIndexes +
                ", fields=" + fields +
                ", methods=" + methods +
                ", attributes=" + attributes +
                '}';
    }

}
