package net.knarcraft.stargatecommand.command;

import net.TheDgtl.Stargate.api.StargateAPI;
import net.TheDgtl.Stargate.manager.PermissionManager;
import net.TheDgtl.Stargate.network.Network;
import net.TheDgtl.Stargate.network.RegistryAPI;
import net.TheDgtl.Stargate.network.portal.RealPortal;
import net.knarcraft.stargatecommand.StargateCommand;
import net.knarcraft.stargatecommand.manager.OverrideManager;
import net.knarcraft.stargatecommand.property.StargateCommandCommand;
import net.knarcraft.stargatecommand.util.PortalFinderHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This command represents the dial command for dialing any available Stargate
 */
public class CommandDial implements CommandExecutor {

    private final char spaceReplacement = StargateCommand.getSpaceReplacementCharacter();
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
            commandSender.sendMessage("This command can only be used by players");
            return true;
        }
        if (!player.hasPermission(StargateCommandCommand.DIAL.getPermissionNode())) {
            player.sendMessage("Permission Denied");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage("You need to provide a network name and a portal name to dial");
            return true;
        }

        PermissionManager permissionManager = stargateAPI.getPermissionManager(player);
        String networkName = args[0].replace(spaceReplacement, ' ');
        String portalName = args[1].replace(spaceReplacement, ' ');
        Network network = registryAPI.getNetwork(networkName, false);
        if (network == null) {
            commandSender.sendMessage("Invalid network selected");
            return true;
        }

        RealPortal targetPortal = (RealPortal) network.getPortal(portalName);
        if (targetPortal == null) {
            commandSender.sendMessage("Invalid portal selected");
            return true;
        }

        if (!permissionManager.hasAccessPermission(targetPortal)) {
            commandSender.sendMessage("You don't have access to the selected portal");
            return true;
        }

        //Find any Stargate block in the player's line of sight
        RealPortal originPortal = (RealPortal) PortalFinderHelper.findPortalByRaytrace(registryAPI, player, 10);
        if (originPortal == null) {
            player.sendMessage("You need to look at a portal to dial");
            return true;
        }
        originPortal.overrideDestination(targetPortal);
        OverrideManager.storeOverriddenDestination(originPortal);
        originPortal.open(player);

        player.sendMessage("Your Stargate has been prepared");
        return true;
    }

}
