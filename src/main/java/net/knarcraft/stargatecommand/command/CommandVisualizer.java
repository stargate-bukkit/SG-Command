package net.knarcraft.stargatecommand.command;

import net.TheDgtl.Stargate.network.Network;
import net.TheDgtl.Stargate.network.RegistryAPI;
import net.TheDgtl.Stargate.network.portal.FixedPortal;
import net.TheDgtl.Stargate.network.portal.Portal;
import net.TheDgtl.Stargate.network.portal.PortalFlag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * This command represents the command for visualizing a Stargate network
 */
public class CommandVisualizer implements CommandExecutor {

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

        Network network = registryAPI.getNetwork(args[0], false);
        if (network == null) {
            commandSender.sendMessage("You must provide a valid network to visualize");
            return true;
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("All portals in network: ").append(network.getName()).append("\n");
        stringBuilder.append("Symbol explanation: ").append("\n");
        stringBuilder.append("# = hidden, ¤ = not hidden").append("\n");
        stringBuilder.append("O = always on, - = not always on").append("\n");
        stringBuilder.append("| = fixed, > = destination choose-able");

        //Print info about all portals in the network
        for (Portal portal : network.getAllPortals()) {
            stringBuilder.append("\n");
            if (portal.hasFlag(PortalFlag.HIDDEN)) {
                stringBuilder.append('#');
            } else {
                stringBuilder.append('¤');
            }
            if (portal.hasFlag(PortalFlag.ALWAYS_ON)) {
                stringBuilder.append('O');
            } else {
                stringBuilder.append('-');
            }
            //TODO: Look for the fixed flag instead of FixedPortal once it's fixed
            if (portal instanceof FixedPortal) {
                stringBuilder.append('|');
            } else {
                stringBuilder.append('>');
            }
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
