package net.knarcraft.stargateinterfaces.command.style;

import net.knarcraft.stargateinterfaces.color.ColorModificationCategory;
import net.knarcraft.stargateinterfaces.color.ColorSelectionType;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Tag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sgrewritten.stargate.api.gate.GateFormatRegistry;
import org.sgrewritten.stargate.api.network.RegistryAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StyleTabCompleter implements TabCompleter {
    private final RegistryAPI registry;

    public StyleTabCompleter(RegistryAPI registry) {
        this.registry = registry;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            return StyleArgument.getNames();
        } else if (args.length > 1) {
            try {
                StyleArgument argument = StyleArgument.fromName(args[0]);
                String[] subArguments = ArrayUtils.remove(args, 0);
                return switch (argument) {
                    case SET, CLEAR -> tabCompleteCategorySelectAndDeselect(subArguments, commandSender, argument);
                    case QUICK_SET -> tabCompleteQuickSet(subArguments, commandSender);
                };
            } catch (IllegalArgumentException ignored) {
            }
        }
        return new ArrayList<>();
    }

    private List<String> tabCompleteCategorySelectAndDeselect(@NotNull String[] args, CommandSender commandSender, StyleArgument argument) {
        if (args.length == 1) {
            return Arrays.stream(ColorModificationCategory.values()).map((colorSelectionType) -> colorSelectionType.name().toLowerCase()).toList();
        }
        if (args.length < 5) {
            try {
                ColorModificationCategory colorSelectionType = ColorModificationCategory.valueOf(args[0].toUpperCase());
                String[] subArgs = ArrayUtils.remove(args, 0);
                String[] subSubArgs = ArrayUtils.remove(subArgs, 0);
                return switch (colorSelectionType) {
                    case GLOBAL, PORTAL -> tabCompleteColorSelection(subArgs, commandSender);
                    case NETWORK -> {
                        if (args.length == 2) {
                            yield new ArrayList<>(registry.getNetworkMap().keySet());
                        }
                        yield tabCompleteColorSelection(subSubArgs, commandSender);
                    }
                    case MATERIAL -> {
                        if (args.length == 2) {
                            yield Tag.WALL_SIGNS.getValues().stream().map((material -> material.name().toLowerCase())).toList();
                        }
                        yield tabCompleteColorSelection(subSubArgs, commandSender);
                    }
                    case GATE -> {
                        if(args.length == 2) {
                            yield GateFormatRegistry.getAllGateFormatNames().stream().toList();
                        }
                        yield tabCompleteColorSelection(subSubArgs, commandSender);
                    }
                };
            } catch (IllegalArgumentException e) {
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    private List<String> tabCompleteQuickSet(@NotNull String[] args, CommandSender commandSender) {
        List<String> output = new ArrayList<>();
        if(args.length == 1) {
            List<String> colorModificationCategories = new ArrayList<>(Arrays.stream(ColorModificationCategory.values())
                    .filter(colorModificationCategory -> colorModificationCategory != ColorModificationCategory.GLOBAL).map(colorModificationCategory -> colorModificationCategory.name().toLowerCase()).toList());
            output.addAll(colorModificationCategories);
        }
        if (args.length > 0) {
            try{
                ColorModificationCategory.valueOf(args[0].toUpperCase());
                output.addAll(tabCompleteColorSelection(ArrayUtils.remove(args, 0), commandSender));
            } catch (IllegalArgumentException e){
                output.addAll(tabCompleteColorSelection(args, commandSender));
            }
        }
        return output;
    }

    private List<String> tabCompleteHexCode(CommandSender commandSender) {
        List<TextColor> trackedColors = StyleCommandRegistry.getTrackedColors(commandSender);
        if (trackedColors == null) {
            return new ArrayList<>();
        }
        List<String> output = new ArrayList<>(trackedColors.stream().map(TextColor::asHexString).toList());
        Collections.reverse(output);
        return output;
    }

    private List<String> tabCompleteColorSelection(String[] args, CommandSender commandSender) {
        List<String> output = new ArrayList<>();
        if(args.length > 0 && args.length < 3){
            output.addAll(tabCompleteHexCode(commandSender));
            try{
                ColorSelectionType.valueOf(args[0].toUpperCase());
            } catch (IllegalArgumentException e){
                output.addAll(Arrays.stream(ColorSelectionType.values()).map(colorSelectionType -> colorSelectionType.name().toLowerCase()).toList());
            }
        }
        return output;
    }
}
