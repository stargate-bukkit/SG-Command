package net.knarcraft.stargatecommand.command;

import net.TheDgtl.Stargate.network.RegistryAPI;
import net.TheDgtl.Stargate.network.portal.Portal;
import net.TheDgtl.Stargate.network.portal.PortalFlag;
import net.knarcraft.stargatecommand.formatting.StringFormat;
import net.knarcraft.stargatecommand.formatting.StringFormatter;
import net.knarcraft.stargatecommand.formatting.TranslatableMessage;
import net.knarcraft.stargatecommand.property.StargateCommandCommand;
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

import static net.knarcraft.stargatecommand.formatting.StringFormatter.getTranslatedErrorMessage;

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
            commandSender.sendMessage(getTranslatedErrorMessage(TranslatableMessage.COMMAND_PLAYER_ONLY));
            return true;
        }
        if (!player.hasPermission(StargateCommandCommand.INFO.getPermissionNode())) {
            player.sendMessage(getTranslatedErrorMessage(TranslatableMessage.PERMISSION_DENIED));
            return true;
        }

        Portal portal = PortalFinderHelper.findPortalByRaytrace(registryAPI, player, 15);
        if (portal == null) {
            commandSender.sendMessage(getTranslatedErrorMessage(TranslatableMessage.NO_PORTAL_IN_SIGHT));
            return true;
        }

        Player owner = Bukkit.getPlayer(portal.getOwnerUUID());
        String name = portal.getName();
        String destination = portal.getDestinationName() == null ? "" : portal.getDestinationName();
        String network = portal.getNetwork().getName();
        String ownerName = owner != null ? owner.getName() : portal.getOwnerUUID().toString();
        Set<PortalFlag> portalFlags = PortalFlag.parseFlags(portal.getAllFlagsString());
        String flags = StringUtils.join(portalFlags, ", ");

        String infoMessage = StringFormatter.replacePlaceholders(StringFormatter.getStringFormat(
                StringFormat.COMMAND_INFO_FORMAT), new String[]{"{portal}", "{destination}", "{network}", "{owner}",
                "{flags}"}, new String[]{name, destination, network, ownerName, flags});

        player.sendMessage(StringFormatter.formatInfoMessage(infoMessage));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] strings) {
        return new ArrayList<>();
    }

}
