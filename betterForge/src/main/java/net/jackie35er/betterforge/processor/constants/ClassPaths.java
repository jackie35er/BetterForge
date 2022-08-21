package net.jackie35er.betterforge.processor.constants;

public enum ClassPaths {
    ITEM_REGISTER("modProcessor.item.ItemRegister"),

    FORGE_ITEM("net.minecraft.world.item.Item")
    ;

    public final String value;
    ClassPaths(String value){
        this.value = value;
    }

    public String getPackageName(){
        int lastDot = value.lastIndexOf(".");
        return value.substring(0,lastDot);
    }

    public String getClassName(){
        int lastDot = value.lastIndexOf(".");
        return value.substring(lastDot + 1);
    }
}
