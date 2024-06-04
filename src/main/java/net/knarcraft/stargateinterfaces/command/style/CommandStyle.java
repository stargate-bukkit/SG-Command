package net.knarcraft.stargateinterfaces.command.style;

import dev.thorinwasher.utils.colorbukkit.ColorBukkitAPI;
import net.knarcraft.stargateinterfaces.color.*;
import net.knarcraft.stargateinterfaces.database.DatabaseInterface;
import net.knarcraft.stargateinterfaces.util.PortalFinderHelper;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.sgrewritten.stargate.api.gate.GateFormatRegistry;
import org.sgrewritten.stargate.api.network.RegistryAPI;
import org.sgrewritten.stargate.api.network.portal.RealPortal;

import java.util.function.Consumer;

public class CommandStyle implements CommandExecutor {
    private final RegistryAPI registry;
    private final ColorModificationRegistry colorModificationRegistry;
    private final DatabaseInterface databaseInterface;
    private ModificationTargetWrapper<?> modificationTargetWrapper;

    public CommandStyle(RegistryAPI registry, ColorModificationRegistry colorModificationRegistry, DatabaseInterface databaseInterface) {
        this.registry = registry;
        this.colorModificationRegistry = colorModificationRegistry;
        this.databaseInterface = databaseInterface;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        try {
            if (args.length < 2) {
                throw new IllegalArgumentException("");
            }
            StyleArgument argument = StyleArgument.fromName(args[0]);
            switch (argument) {
                case SET, CLEAR -> {
                    String[] subArgs = ArrayUtils.remove(args, 0);
                    ColorModificationCategory colorModificationCategory = ColorModificationCategory.valueOf(subArgs[0].toUpperCase());
                    subArgs = ArrayUtils.remove(subArgs, 0);
                    boolean shouldSkippNextArgument = setColorModificationTargetWrapper(colorModificationCategory, subArgs[0], commandSender);
                    if (shouldSkippNextArgument) {
                        subArgs = ArrayUtils.remove(subArgs, 0);
                    }
                    if (argument == StyleArgument.SET) {
                        applyColorModification(colorModificationCategory, this.modificationTargetWrapper, subArgs, commandSender);
                    } else {
                        removeColorModification(colorModificationCategory, this.modificationTargetWrapper);
                    }
                }
                case QUICK_SET, QUICK_CLEAR -> {
                    if (!(commandSender instanceof Player player)) {
                        throw new IllegalArgumentException("Only players can use this command!");
                    }

                    ColorModificationCategory colorModificationCategory;
                    String[] subArgs = ArrayUtils.remove(args, 0);
                    try {
                        colorModificationCategory = ColorModificationCategory.valueOf(args[1].toUpperCase());
                        subArgs = ArrayUtils.remove(subArgs, 0);
                    } catch (IllegalArgumentException e) {
                        colorModificationCategory = ColorModificationCategory.PORTAL;
                    }
                    RealPortal portal = PortalFinderHelper.findPortalByRaytrace(registry, player, 5);
                    if (portal == null) {
                        throw new IllegalArgumentException("You need to look at a portal to use this command!");
                    }
                    setColorModificationTargetWrapper(colorModificationCategory, portal);
                    if (argument == StyleArgument.QUICK_SET) {
                        applyColorModification(colorModificationCategory, this.modificationTargetWrapper, subArgs, commandSender);
                    } else {
                        removeColorModification(colorModificationCategory, this.modificationTargetWrapper);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage(e.getMessage());
        }
        return true;
    }

    private void setColorModificationTargetWrapper(ColorModificationCategory colorModificationCategory, RealPortal portal) {
        try {
            this.modificationTargetWrapper = switch (colorModificationCategory) {
                case GLOBAL -> new ModificationTargetWrapper<>("all");
                case NETWORK -> new ModificationTargetWrapper<>(portal.getNetwork().getId());
                case MATERIAL -> {
                    throw new IllegalArgumentException("Not implemented");
                }
                case GATE -> new ModificationTargetWrapper<>(portal.getGate().getFormat().getFileName());
                case PORTAL ->  new ModificationTargetWrapper<>(ColorUtils.convertIdToString(portal.getGlobalId()));
            };
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("");
        }
    }

    private void removeColorModification(ColorModificationCategory colorModificationCategory, ModificationTargetWrapper<?> modificationTargetWrapper) {
        ColorModification colorModification = colorModificationRegistry.getColorModification(colorModificationCategory, modificationTargetWrapper);
        if (colorModification == null) {
            return;
        }
        colorModificationRegistry.remove(colorModification);
        databaseInterface.removeColorModification(colorModification);
    }

    private void applyColorModification(ColorModificationCategory colorModificationCategory,
                                        ModificationTargetWrapper<?> modificationTargetWrapper, String[] args,
                                        CommandSender commandSender) {
        if (args.length > 0) {
            final ColorSelectionType colorSelectionType;
            ColorSelectionType colorSelectionType1;
            String[] subArgs;
            try {
                subArgs = ArrayUtils.remove(args, 0);
                colorSelectionType1 = ColorSelectionType.valueOf(args[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                subArgs = args;
                colorSelectionType1 = ColorSelectionType.ALL;
            }
            colorSelectionType = colorSelectionType1;
            Consumer<TextColor> textColorConsumer = (textColor) -> {
                ColorModification colorModification = colorModificationRegistry.getColorModification(colorModificationCategory, modificationTargetWrapper);
                if (colorModification == null) {
                    colorModification = new ColorModification(colorModificationCategory, null, null, modificationTargetWrapper, null);
                }
                ColorModification newColorModification = colorModification.createModifiedInstance(colorSelectionType, textColor);
                colorModificationRegistry.addOrUpdate(newColorModification);
                databaseInterface.updateColorModification(newColorModification);
            };
            if (subArgs.length == 1) {
                TextColor color = TextColor.fromHexString(subArgs[0]);
                textColorConsumer.accept(color);
            } else if (subArgs.length == 0 && commandSender instanceof Player player) {
                try {
                    ColorBukkitAPI.getColor(player, textColorConsumer);
                } catch (NoClassDefFoundError e) {
                    throw new IllegalArgumentException("");
                }
            }
        }
    }

    private boolean setColorModificationTargetWrapper(ColorModificationCategory colorModificationCategory, String arg, CommandSender commandSender) {
        boolean hasReadOneArgument = false;
        try {
            this.modificationTargetWrapper = switch (colorModificationCategory) {
                case GLOBAL -> new ModificationTargetWrapper<>("all");
                case NETWORK -> {
                    hasReadOneArgument = true;
                    yield new ModificationTargetWrapper<>(arg);
                }
                case MATERIAL -> {
                    hasReadOneArgument = true;
                    yield new ModificationTargetWrapper<>(Material.valueOf(arg.toUpperCase()));
                }
                case GATE -> {
                    if (GateFormatRegistry.getFormat(arg) == null) {
                        throw new IllegalArgumentException("");
                    }
                    hasReadOneArgument = true;
                    yield new ModificationTargetWrapper<>(arg);
                }
                case PORTAL -> {
                    if (!(commandSender instanceof LivingEntity commandEntity)) {
                        throw new IllegalArgumentException("");
                    }
                    RealPortal portal = PortalFinderHelper.findPortalByRaytrace(registry, commandEntity, 10);
                    if (portal == null) {
                        throw new IllegalArgumentException("");
                    }
                    yield new ModificationTargetWrapper<>(ColorUtils.convertIdToString(portal.getGlobalId()));
                }
            };
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("");
        }
        return hasReadOneArgument;
    }
}
