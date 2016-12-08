package au.id.villar.bytecode;

import au.id.villar.bytecode.attribute.Attribute;
import au.id.villar.bytecode.parser.*;
import au.id.villar.bytecode.parser.constant.Constant;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Class {

	public static Class build(InputStream stream) throws IOException {

		final Class aClass = new Class();

		ClassFileParser parser = new ClassFileParser();
		parser.setBytecodeStream(stream);
		parser.setHandler(new ClassFileHandler() {

			class AttrCounter { List<Attribute> attrs; int count; void execute() {} }
			Deque<AttrCounter> attrCounters = new ArrayDeque<>();
			BytesReader bytesReader = new BytesReader(null);

			@Override
			public final void number(int number, NumberType numberType) {
				switch(numberType) {
					case MINOR:
						aClass.minor = number;
						break;
					case MAYOR:
						aClass.mayor = number;
						break;
					case CONSTANT_POOL:
						aClass.constants = new HashMap<>(number);
						break;
					case ACCESS_FLAGS:
						aClass.accessFlags = new AccessFlags((short)number, false);
						break;
					case THIS_INDEX:
						aClass.name = Constant.toString(number, aClass.constants);
						break;
					case SUPER_INDEX:
						aClass.superClass = Constant.toString(number, aClass.constants);
						break;
					case INTERFACES:
						aClass.interfaces = new ArrayList<>(number);
						break;
					case FIELDS:
						aClass.fields = new ArrayList<>(number);
						break;
					case METHODS:
						aClass.methods = new ArrayList<>(number);
						break;
					case ATTRIBUTES:
						AttrCounter attrCounter = attrCounters.peekFirst();
						if(attrCounter != null) {
							attrCounter.count = number;
							attrCounter.attrs = new ArrayList<>(number);
							if(number == 0) {
								attrCounter.execute();
								attrCounters.pollFirst();
							}
						} else {
							aClass.attributes = new ArrayList<>(number);
						}
				}
			}

			@Override
			public final void constant(Constant constant, int index) {
				aClass.constants.put(index, constant);
			}

			@Override
			public final void interfaceIndex(int constantPoolIndex) {
				aClass.interfaces.add(Constant.toString(constantPoolIndex, aClass.constants));
			}

			@Override
			public final void field(final int accessFlags, final int nameIndex, final int descriptorIndex) {
				AttrCounter attrCounter = new AttrCounter() {
					@Override
					void execute() {
						aClass.fields.add(new Field(
								new AccessFlags((short)accessFlags, false),
								Constant.toString(nameIndex, aClass.constants),
								Constant.toString(descriptorIndex, aClass.constants),
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
						aClass.methods.add(new Method(
								new AccessFlags((short)accessFlags, true),
								Constant.toString(nameIndex, aClass.constants),
								Constant.toString(descriptorIndex, aClass.constants),
								attrs
						));
					}
				};
				attrCounters.offerFirst(attrCounter);
			}

			@Override
			public final void attribute(int nameIndex, int length, InputStream info) {
				AttrCounter attrCounter = attrCounters.peekFirst();
				String name = Constant.toString(nameIndex, aClass.constants);
				Attribute attribute;
				try {
					attribute = Attribute.readAttribute(name, length, bytesReader.reuse(info), aClass.constants);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				if(attrCounter != null) {
					attrCounter.attrs.add(attribute);
					if(--attrCounter.count == 0) {
						attrCounters.pollFirst();
						attrCounter.execute();
					}
				} else {
					aClass.attributes.add(attribute);
				}
			}
		});

		parser.parse();

		aClass.constants = Collections.unmodifiableMap(aClass.constants);
		aClass.interfaces = Collections.unmodifiableList(aClass.interfaces);
		aClass.fields = Collections.unmodifiableList(aClass.fields);
		aClass.methods = Collections.unmodifiableList(aClass.methods);
		aClass.attributes = Collections.unmodifiableList(aClass.attributes);

		return aClass;
	}

	private int mayor;
	private int minor;
	private Map<Integer, Constant> constants;
	private AccessFlags accessFlags;
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

	public Map<Integer, Constant> getConstants() {
		return constants;
	}

	public void setConstants(Map<Integer, Constant> constants) {
		this.constants = constants;
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
