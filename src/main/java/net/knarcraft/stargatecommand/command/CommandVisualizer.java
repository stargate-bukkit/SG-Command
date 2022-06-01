package net.knarcraft.stargatecommand.command;

import net.TheDgtl.Stargate.network.Network;
import net.TheDgtl.Stargate.network.RegistryAPI;
import net.TheDgtl.Stargate.network.portal.FixedPortal;
import net.TheDgtl.Stargate.network.portal.Portal;
import net.TheDgtl.Stargate.network.portal.PortalFlag;
import net.knarcraft.stargatecommand.StargateCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * This command represents the command for visualizing a Stargate network
 */
public class CommandVisualizer implements CommandExecutor {

    private final char spaceReplacement = StargateCommand.getSpaceReplacementCharacter();
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
        if (!commandSender.hasPermission("stargate.command.visualizer")) {
            commandSender.sendMessage("Permission Denied");
            return true;
        }

        if (args.length < 1) {
            commandSender.sendMessage("A network must be provided");
            return true;
        }

        Network network = registryAPI.getNetwork(args[0].replace(spaceReplacement, ' '), false);
        if (network == null) {
            commandSender.sendMessage("You must provide a valid network to visualize");
            return true;
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Symbol explanation: ").append("\n");
        stringBuilder.append("⇒ = hidden, ⇄ = not hidden").append("\n");
        stringBuilder.append("⬛ = always open, ⬜ = not always open").append("\n");
        stringBuilder.append("↯ = random destination, ↠ = non-random destination").append("\n");
        stringBuilder.append("-> = fixed portal going to the specified portal").append("\n").append('|').append("\n");
        stringBuilder.append("All portals in network ").append(network.getName()).append(":");

        //Print info about all portals in the network
        for (Portal portal : network.getAllPortals()) {
            stringBuilder.append("\n");
            if (portal.hasFlag(PortalFlag.HIDDEN)) {
                stringBuilder.append('⇒');
            } else {
                stringBuilder.append('⇄');
            }
            if (portal.hasFlag(PortalFlag.ALWAYS_ON)) {
                stringBuilder.append('⬛');
            } else {
                stringBuilder.append('⬜');
            }
            if (portal.hasFlag(PortalFlag.RANDOM)) {
                stringBuilder.append('↯');
            } else {
                stringBuilder.append('↠');
            }
            //TODO: Look for the fixed flag instead of FixedPortal once it's fixed
            stringBuilder.append(" ").append(portal.getName());
            if (portal instanceof FixedPortal) {
                stringBuilder.append(" -> ");
                stringBuilder.append(portal.getDestinationName());
            }
        }

        commandSender.sendMessage(stringBuilder.toString());
        return true;
    }

}
