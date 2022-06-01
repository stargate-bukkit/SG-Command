package net.knarcraft.stargatecommand.command;

import net.TheDgtl.Stargate.api.StargateAPI;
import net.TheDgtl.Stargate.manager.PermissionManager;
import net.TheDgtl.Stargate.network.Network;
import net.TheDgtl.Stargate.network.RegistryAPI;
import net.TheDgtl.Stargate.network.portal.RealPortal;
import net.knarcraft.stargatecommand.formatting.TranslatableMessage;
import net.knarcraft.stargatecommand.manager.IconManager;
import net.knarcraft.stargatecommand.manager.OverrideManager;
import net.knarcraft.stargatecommand.property.Icon;
import net.knarcraft.stargatecommand.property.StargateCommandCommand;
import net.knarcraft.stargatecommand.util.PortalFinderHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.knarcraft.stargatecommand.formatting.StringFormatter.getTranslatedErrorMessage;
import static net.knarcraft.stargatecommand.formatting.StringFormatter.getTranslatedInfoMessage;

/**
 * This command represents the dial command for dialing any available Stargate
 */
public class CommandDial implements CommandExecutor {

    private final String spaceReplacement = IconManager.getIconString(Icon.SPACE_REPLACEMENT);
    private final StargateAPI stargateAPI;
    private final RegistryAPI registryAPI;

    /**
     * Instantiates a new dial command
     *
     * @param stargateAPI <p>A reference to the Stargate API</p>
     */
    public CommandDial(StargateAPI stargateAPI) {
        this.stargateAPI = stargateAPI;
        this.registryAPI = stargateAPI.getRegistry();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(getTranslatedErrorMessage(TranslatableMessage.COMMAND_PLAYER_ONLY));
            return true;
        }
        if (!player.hasPermission(StargateCommandCommand.DIAL.getPermissionNode())) {
            player.sendMessage(getTranslatedErrorMessage(TranslatableMessage.PERMISSION_DENIED));
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(getTranslatedErrorMessage(TranslatableMessage.COMMAND_DIAL_ARGUMENTS));
            return true;
        }

        PermissionManager permissionManager = stargateAPI.getPermissionManager(player);
        String networkName = args[0].replace(spaceReplacement, " ");
        String portalName = args[1].replace(spaceReplacement, " ");

        //Validate that the network and portal exists, and that the player can access the portal
        Network network = registryAPI.getNetwork(networkName, false);
        if (network == null) {
            commandSender.sendMessage(getTranslatedErrorMessage(TranslatableMessage.INVALID_NETWORK_GIVEN));
            return true;
        }
        RealPortal targetPortal = (RealPortal) network.getPortal(portalName);
        if (targetPortal == null) {
            commandSender.sendMessage(getTranslatedErrorMessage(TranslatableMessage.INVALID_PORTAL_GIVEN));
            return true;
        }
        if (!permissionManager.hasAccessPermission(targetPortal)) {
            commandSender.sendMessage(getTranslatedErrorMessage(TranslatableMessage.PORTAL_NO_ACCESS));
            return true;
        }

        //Find any Stargate block in the player's line of sight
        RealPortal originPortal = (RealPortal) PortalFinderHelper.findPortalByRaytrace(registryAPI, player, 10);
        if (originPortal == null) {
            player.sendMessage(getTranslatedErrorMessage(TranslatableMessage.NO_PORTAL_IN_SIGHT));
            return true;
        }
        originPortal.overrideDestination(targetPortal);
        OverrideManager.storeOverriddenDestination(originPortal);
        originPortal.open(player);

        player.sendMessage(getTranslatedInfoMessage(TranslatableMessage.DIAL_SUCCESSFUL));
        return true;
    }

}
