package net.knarcraft.stargateinterfaces.command;

import net.knarcraft.stargateinterfaces.formatting.StringFormat;
import net.knarcraft.stargateinterfaces.formatting.StringFormatter;
import net.knarcraft.stargateinterfaces.formatting.TranslatableMessage;
import net.knarcraft.stargateinterfaces.manager.IconManager;
import net.knarcraft.stargateinterfaces.property.Icon;
import net.knarcraft.stargateinterfaces.util.NameHelper;

import static net.knarcraft.stargateinterfaces.formatting.StringFormatter.getTranslatedErrorMessage;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.sgrewritten.stargate.api.network.Network;
import org.sgrewritten.stargate.api.network.RegistryAPI;
import org.sgrewritten.stargate.api.network.portal.Portal;
import org.sgrewritten.stargate.api.network.portal.PortalFlag;

/**
 * This command represents the command for visualizing a Stargate network
 */
public class CommandVisualizer implements CommandExecutor {

    private final String spaceReplacement = IconManager.getIconString(Icon.SPACE_REPLACEMENT);
    private final RegistryAPI registryAPI;

    /**
     * Instantiates a visualizer command
     *
     * @param registryAPI <p>A reference to the registry API</p>
     */
    public CommandVisualizer(RegistryAPI registryAPI) {
        this.registryAPI = registryAPI;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] args) {
        if (!commandSender.hasPermission(StargateCommandType.VISUALIZER.getPermissionNode())) {
            commandSender.sendMessage(getTranslatedErrorMessage(TranslatableMessage.PERMISSION_DENIED));
            return true;
        }

        if (args.length < 1) {
            commandSender.sendMessage(getTranslatedErrorMessage(TranslatableMessage.COMMAND_VISUALIZER_ARGUMENTS));
            return true;
        }

        Network network = NameHelper.getNetworkFromName(registryAPI, args[0]);

        if (network == null) {
            commandSender.sendMessage(getTranslatedErrorMessage(TranslatableMessage.INVALID_NETWORK_GIVEN));
            return true;
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(StringFormatter.formatInfoMessage(StringFormatter.getStringFormat(
                StringFormat.COMMAND_VISUALIZER_FORMAT)));

        //Print info about all portals in the network
        for (Portal portal : network.getAllPortals()) {
            StringBuilder iconBuilder = new StringBuilder();
            if (portal.hasFlag(PortalFlag.HIDDEN)) {
                iconBuilder.append(Icon.HIDDEN.getPlaceholder());
            } else {
                iconBuilder.append(Icon.NOT_HIDDEN.getPlaceholder());
            }
            if (portal.hasFlag(PortalFlag.ALWAYS_ON)) {
                iconBuilder.append(Icon.ALWAYS_ON.getPlaceholder());
            } else {
                iconBuilder.append(Icon.NOT_ALWAYS_ON.getPlaceholder());
            }
            if (portal.hasFlag(PortalFlag.RANDOM)) {
                iconBuilder.append(Icon.RANDOM.getPlaceholder());
            } else {
                iconBuilder.append(Icon.NOT_RANDOM.getPlaceholder());
            }
            String fixedString = "";
            if (portal.hasFlag(PortalFlag.FIXED)) {
                fixedString = StringFormatter.replacePlaceholder(StringFormatter.getStringFormat(
                                StringFormat.COMMAND_VISUALIZER_FIXED_FORMAT), "{portal}",
                        portal.getDestinationName());
            }

            stringBuilder.append(StringFormatter.replacePlaceholders(StringFormatter.getStringFormat(
                            StringFormat.COMMAND_VISUALIZER_PORTAL_FORMAT), new String[]{"{icons}", "{portal}", "{fixed}"},
                    new String[]{iconBuilder.toString(), portal.getName(), fixedString}));
        }

        commandSender.sendMessage(IconManager.replaceIconsInString(
                StringFormatter.replacePlaceholder(stringBuilder.toString(), "{network}", network.getName())));
        return true;
    }

}
