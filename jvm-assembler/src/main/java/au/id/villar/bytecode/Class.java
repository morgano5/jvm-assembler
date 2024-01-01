package au.id.villar.bytecode;

import au.id.villar.bytecode.attribute.Attribute;
import au.id.villar.bytecode.constant.Constant;

import java.util.List;
import java.util.Map;

public class Class {

    private int mayor;
    private int minor;
    private AccessFlags accessFlags;
    private Map<Integer, Constant> constants;
    private String name;
    private String superClass;
    private List<String> interfaces;
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

    public Map<Integer, Constant> getConstants() {
        return constants;
    }

    public void setConstants(Map<Integer, Constant> constants) {
        this.constants = constants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuperClass() {
        return superClass;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }

    public AccessFlags getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(AccessFlags accessFlags) {
        this.accessFlags = accessFlags;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<String> interfaces) {
        this.interfaces = interfaces;
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
        return "Class{" +
                "name='" + name + '\'' +
                ", mayor=" + mayor +
                ", minor=" + minor +
                ", constants=" + constants +
                ", accessFlags=" + accessFlags +
                ", superClass='" + superClass + '\'' +
                ", interfaces=" + interfaces +
                ", fields=" + fields +
                ", methods=" + methods +
                ", attributes=" + attributes +
                '}';
    }

}
