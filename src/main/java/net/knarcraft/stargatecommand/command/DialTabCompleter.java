package net.knarcraft.stargatecommand.command;

import net.TheDgtl.Stargate.api.StargateAPI;
import net.TheDgtl.Stargate.manager.PermissionManager;
import net.TheDgtl.Stargate.network.Network;
import net.TheDgtl.Stargate.network.RegistryAPI;
import net.TheDgtl.Stargate.network.portal.Portal;
import net.TheDgtl.Stargate.network.portal.RealPortal;
import net.knarcraft.stargatecommand.StargateCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static net.knarcraft.stargatecommand.util.TabCompleterHelper.filterMatching;

/**
 * A tab completer for the /sgc dial command
 */
public class DialTabCompleter implements TabCompleter {

    private final char spaceReplacement = StargateCommand.getSpaceReplacementCharacter();
    private final StargateAPI stargateAPI;

    /**
     * Instantiates a new dial tab completer
     *
     * @param stargateAPI <p>A reference to the Stargate API</p>
     */
    public DialTabCompleter(StargateAPI stargateAPI) {
        this.stargateAPI = stargateAPI;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("This command can only be used by players");
            return new ArrayList<>();
        }

        List<String> availableNetworks = new ArrayList<>();
        Map<Network, List<String>> availablePortals = new HashMap<>();
        RegistryAPI registryAPI = stargateAPI.getRegistry();
        PermissionManager permissionManager = stargateAPI.getPermissionManager(player);

        //Populate the collections with available networks and portals
        populateNetworksAndPortals(permissionManager, availableNetworks, availablePortals);

        if (args.length > 1) {
            Network network = registryAPI.getNetwork(args[0].replace(spaceReplacement, ' '), false);
            if (network != null && availablePortals.containsKey(network)) {
                return filterMatching(availablePortals.get(network), args[1].replace(spaceReplacement, ' '));
            } else {
                return new ArrayList<>();
            }
        } else {
            return filterMatching(availableNetworks, args[0].replace(spaceReplacement, ' '));
        }
    }

    /**
     * Populates the given collections with available networks and portals
     *
     * @param permissionManager <p>The permission manager to use to check for availability</p>
     * @param availableNetworks <p>The list to store available networks to</p>
     * @param availablePortals  <p>The map to store available portals to</p>
     */
    private void populateNetworksAndPortals(PermissionManager permissionManager, List<String> availableNetworks,
                                            Map<Network, List<String>> availablePortals) {
        List<Network> networks = new LinkedList<>(stargateAPI.getRegistry().getNetworkMap().values());
        //Get all available networks and portals
        for (Network network : networks) {
            Collection<Portal> portals = network.getAllPortals();
            for (Portal portal : portals) {
                if (permissionManager.hasAccessPermission((RealPortal) portal)) {
                    //Add an empty list if the network has not been encountered before
                    if (!availablePortals.containsKey(network)) {
                        availablePortals.put(network, new LinkedList<>());
                    }
                    availablePortals.get(network).add(portal.getName().replace(' ', spaceReplacement));
                }
            }
        }
        //Add only the network names with portals available to the player
        availablePortals.keySet().forEach((item) -> availableNetworks.add(item.getName().replace(' ',
                spaceReplacement)));
    }

}
