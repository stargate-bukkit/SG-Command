package net.knarcraft.stargateinterfaces.command.style;

import java.util.ArrayList;
import java.util.List;

public enum StyleArgument {
    SET("set"),
    CLEAR("clear"),
    QUICK_SET("quickset"),
    QUICK_CLEAR("quickclear");

    private final String commandName;

    StyleArgument(String name){
        this.commandName = name;
    }

    public static StyleArgument fromName(String name){
        for(StyleArgument commandType : StyleArgument.values()){
            if(commandType.commandName.equalsIgnoreCase(name)){
                return commandType;
            }
        }
        throw new IllegalArgumentException("No command type exist of name: " + name);
    }

    public static List<String> getNames(){
        List<String> output = new ArrayList<>();
        for(StyleArgument argument : StyleArgument.values()){
            output.add(argument.commandName);
        }
        return output;
    }
}
