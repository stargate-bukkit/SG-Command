package net.knarcraft.stargatecommand.command;

import net.TheDgtl.Stargate.network.RegistryAPI;
import net.TheDgtl.Stargate.network.portal.Portal;
import net.TheDgtl.Stargate.network.portal.PortalFlag;
import net.knarcraft.stargatecommand.util.PortalFinderHelper;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This tab-command represents the command for getting information about a seen portal
 */
public class TabCommandInfo implements TabExecutor {

    private final RegistryAPI registryAPI;

    /**
     * Instantiates a new info command
     *
     * @param registryAPI <p>A reference to the Registry API</p>
     */
    public TabCommandInfo(RegistryAPI registryAPI) {
        this.registryAPI = registryAPI;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("This command can only be used by a player");
            return true;
        }
        if (!player.hasPermission("stargate.command.info")) {
            player.sendMessage("Permission Denied");
            return true;
        }

        Portal portal = PortalFinderHelper.findPortalByRaytrace(registryAPI, player, 15);
        if (portal == null) {
            commandSender.sendMessage("You need to look directly at a portal to get information about it");
            return true;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Information about the Stargate you are currently looking at:").append("\n");
        stringBuilder.append("|- ").append("Name: ").append(portal.getName()).append("\n");
        String destination = portal.getDestinationName();
        if (destination != null && !destination.equalsIgnoreCase("null")) {
            stringBuilder.append("|- ").append("Destination: ").append(portal.getDestinationName()).append("\n");
        }
        stringBuilder.append("|- ").append("Network: ").append(portal.getNetwork().getName()).append("\n");
        Player owner = Bukkit.getPlayer(portal.getOwnerUUID());
        if (owner != null) {
            stringBuilder.append("|- ").append("Owner: ").append(owner.getName()).append("\n");
        } else {
            stringBuilder.append("|- ").append("Owner: ").append(portal.getOwnerUUID()).append("\n");
        }
        Set<PortalFlag> portalFlags = PortalFlag.parseFlags(portal.getAllFlagsString());
        stringBuilder.append("|- ").append("Flags: ").append(StringUtils.join(portalFlags, ", ")).append("\n");
        player.sendMessage(stringBuilder.toString());
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] strings) {
        return new ArrayList<>();
    }

}
