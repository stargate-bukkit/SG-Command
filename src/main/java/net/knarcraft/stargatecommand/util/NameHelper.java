package net.knarcraft.stargatecommand.util;

import net.TheDgtl.Stargate.network.Network;
import net.TheDgtl.Stargate.network.RegistryAPI;
import net.knarcraft.stargatecommand.manager.IconManager;
import net.knarcraft.stargatecommand.property.Icon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * A helper class for dealing with Portal and Network names
 */
public final class NameHelper {
    
    private NameHelper() {
        
    }

    /**
     * Gets the network name to display in tab completion for the given network
     *
     * @param network <p>The network to display</p>
     * @return <p>The network name</p>
     */
    public static String getVisualNetworkName(Network network) {
        String networkName = network.getId().replace(" ", IconManager.getIconString(
                Icon.SPACE_REPLACEMENT));
        try {
            UUID userID = UUID.fromString(network.getName());
            Player player = Bukkit.getPlayer(userID);
            if (player != null) {
                networkName = "{" + player.getName() + "}";
            }
        } catch (IllegalArgumentException exception) {
            //Ignored. Not a UUID
        }
        return networkName;
    }

    /**
     * Gets a network from the given network name
     *
     * @param registryAPI <p>The registry API to get the network from</p>
     * @param networkName <p>The name of the network to get</p>
     * @return <p>The network corresponding to the given name</p>
     */
    public static Network getNetworkFromName(RegistryAPI registryAPI, String networkName) {
        networkName = networkName.trim().replace(IconManager.getIconString(Icon.SPACE_REPLACEMENT), " ");

        //Replace {playerName} with network UUID
        if (networkName.matches("^\\{.*}$")) {
            Player player = Bukkit.getPlayer(networkName.substring(1, networkName.length() - 1));
            if (player != null) {
                networkName = player.getUniqueId().toString();
            }
        }

        return registryAPI.getNetwork(networkName, false);
    }

}
