package net.knarcraft.stargateinterfaces.command.style;

import net.knarcraft.stargateinterfaces.color.ColorModification;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StyleCommandRegistry {
    private final Map<CommandSender, List<TextColor>> trackedColorCodes = new HashMap<>();

    public void unTrackCommandSender(CommandSender commandSender) {
        trackedColorCodes.remove(commandSender);
    }

    public void unTrackAllCommandSenders() {
        trackedColorCodes.clear();
    }

    public @Nullable List<TextColor> getTrackedColors(CommandSender commandSender) {
        return trackedColorCodes.get(commandSender);
    }

    public void trackColorCodes(CommandSender commandSender, TextColor textColor){
        trackedColorCodes.putIfAbsent(commandSender, new ArrayList<>());
        List<TextColor> trackedColors = trackedColorCodes.get(commandSender);
        if(trackedColors.contains(textColor)){
            return;
        }
        if(trackedColors.size() > 5){
            trackedColors.remove(0);
        }
        trackedColors.add(textColor);
    }
}
