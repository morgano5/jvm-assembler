package au.id.villar.bytecode.constant;

public final class DynamicConstant extends Constant {

//    private final String name;
//    private final String descriptor;



    @Override
    public boolean isLoadable() {
        return true;
    }

    @Override
    public String toAssemblyDefinition(String identifier) {
        return String.format("d_dynamic %s %s", identifier, "PENDING");
    }

    @Override
    public String toString() {
        return "DynamicConstant{}";
    }
}
