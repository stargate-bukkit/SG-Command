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
import org.sgrewritten.stargate.api.network.Network;
import org.sgrewritten.stargate.api.network.RegistryAPI;
import org.sgrewritten.stargate.network.StorageType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StyleTabCompleter implements TabCompleter {
    private final RegistryAPI registry;
    private final StyleCommandRegistry commandRegistry;

    public StyleTabCompleter(RegistryAPI registry, StyleCommandRegistry commandRegistry) {
        this.registry = registry;
        this.commandRegistry = commandRegistry;
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
                    case SET, CLEAR -> tabCompleteCategorySelectAndDeselect(subArguments, commandSender, argument == StyleArgument.SET);
                    case QUICK_SET, QUICK_CLEAR -> tabCompleteQuickSet(subArguments, commandSender, argument == StyleArgument.QUICK_SET);
                };
            } catch (IllegalArgumentException ignored) {
            }
        }
        return new ArrayList<>();
    }

    private List<String> tabCompleteCategorySelectAndDeselect(@NotNull String[] args, CommandSender commandSender, boolean isInput) {
        if (args.length == 1) {
            return Arrays.stream(ColorModificationCategory.values()).map((colorSelectionType) -> colorSelectionType.name().toLowerCase()).toList();
        }
        if (args.length < 5) {
            try {
                ColorModificationCategory colorSelectionType = ColorModificationCategory.valueOf(args[0].toUpperCase());
                String[] subArgs = ArrayUtils.remove(args, 0);
                String[] subSubArgs = ArrayUtils.remove(subArgs, 0);
                return switch (colorSelectionType) {
                    case GLOBAL, PORTAL -> tabCompleteColorSelection(subArgs, commandSender,isInput);
                    case NETWORK -> {
                        if (args.length == 2) {
                            yield registry.getNetworkRegistry(StorageType.LOCAL).stream().map(Network::getName).toList();
                        }
                        yield tabCompleteColorSelection(subSubArgs, commandSender,isInput);
                    }
                    case MATERIAL -> {
                        if (args.length == 2) {
                            yield Tag.WALL_SIGNS.getValues().stream().map((material -> material.name().toLowerCase())).toList();
                        }
                        yield tabCompleteColorSelection(subSubArgs, commandSender,isInput);
                    }
                    case GATE -> {
                        if(args.length == 2) {
                            yield GateFormatRegistry.getAllGateFormatNames().stream().toList();
                        }
                        yield tabCompleteColorSelection(subSubArgs, commandSender,isInput);
                    }
                };
            } catch (IllegalArgumentException e) {
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    private List<String> tabCompleteQuickSet(@NotNull String[] args, CommandSender commandSender, boolean isInput) {
        List<String> output = new ArrayList<>();
        if(args.length == 1) {
            List<String> colorModificationCategories = new ArrayList<>(Arrays.stream(ColorModificationCategory.values())
                    .filter(colorModificationCategory -> colorModificationCategory != ColorModificationCategory.GLOBAL).map(colorModificationCategory -> colorModificationCategory.name().toLowerCase()).toList());
            output.addAll(colorModificationCategories);
        }
        if (args.length > 0) {
            try{
                ColorModificationCategory.valueOf(args[0].toUpperCase());
                output.addAll(tabCompleteColorSelection(ArrayUtils.remove(args, 0), commandSender,isInput));
            } catch (IllegalArgumentException e){
                output.addAll(tabCompleteColorSelection(args, commandSender,isInput));
            }
        }
        return output;
    }

    private List<String> tabCompleteHexCode(CommandSender commandSender) {
        List<TextColor> trackedColors = commandRegistry.getTrackedColors(commandSender);
        if (trackedColors == null) {
            return new ArrayList<>();
        }
        List<String> output = new ArrayList<>(trackedColors.stream().map(TextColor::asHexString).toList());
        Collections.reverse(output);
        return output;
    }

    private List<String> tabCompleteColorSelection(String[] args, CommandSender commandSender, boolean isInput) {
        List<String> output = new ArrayList<>();
        if(args.length > 0 && args.length < 3){
            if(isInput) {
                output.addAll(tabCompleteHexCode(commandSender));
            }
            try{
                ColorSelectionType.valueOf(args[0].toUpperCase());
            } catch (IllegalArgumentException e){
                output.addAll(Arrays.stream(ColorSelectionType.values()).map(colorSelectionType -> colorSelectionType.name().toLowerCase()).toList());
            }
        }
        return output;
    }
}
