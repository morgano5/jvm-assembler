package au.id.villar.bytecode.attribute.annotation;

//import au.id.villar.bytecode.attribute.annotation.target.Target;
import au.id.villar.bytecode.parser.constant.ParsingConstant;
import au.id.villar.bytecode.parser.constant.ParsingConstantPool;
import au.id.villar.bytecode.util.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TypeAnnotation extends Annotation {

    public static class TypePath {
        public final int kind;
        public final int index;

        public TypePath(int kind, int index) {
            this.kind = kind;
            this.index = index;
        }

        @Override
        public String toString() {
            return "TypePath{" +
                    "kind=" + kind +
                    ", index=" + index +
                    '}';
        }
    }

    //	private Target target;
    private List<TypePath> typePaths;

    public TypeAnnotation(String type, List<ElementValuePair> values/*, Target target*/, List<TypePath> typePaths) {
        super(type, values);
//		this.target = target;
        this.typePaths = Collections.unmodifiableList(new ArrayList<>(typePaths));
    }

//	public Target getTarget() {
//		return target;
//	}

    public List<TypePath> getTypePaths() {
        return typePaths;
    }

    public static TypeAnnotation readTypeAnnotation(BytesReader bytesReader, ParsingConstantPool constantPool)
            throws IOException {
//		Target target = Target.readTarget(bytesReader);
        int typePathSize = bytesReader.readByte();
        List<TypePath> typePathList = new ArrayList<>(typePathSize);
        for(int count = 0; count < typePathSize; count++) {
            typePathList.add(new TypePath(bytesReader.readByte(), bytesReader.readByte()));
        }
        String type = ParsingConstant.toString(bytesReader.readShort(), constantPool);
        int numElementValuePairs = bytesReader.readShort();
        List<ElementValuePair> list = new ArrayList<>(numElementValuePairs);
        for(int count = 0; count < numElementValuePairs; count++) {
            list.add(ElementValuePair.readElementValuePair(bytesReader, constantPool));
        }
        return new TypeAnnotation(type, list/*, target*/, typePathList);
    }



}
