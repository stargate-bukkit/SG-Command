package net.knarcraft.stargateinterfaces.command;

import org.sgrewritten.stargate.api.network.RegistryAPI;

import net.knarcraft.stargateinterfaces.property.StargateCommandCommand;
import net.knarcraft.stargateinterfaces.util.NameHelper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.knarcraft.stargateinterfaces.util.TabCompleterHelper.filterMatching;

import java.util.ArrayList;
import java.util.List;

/**
 * A tab completer for the /sgc visualizer command
 */
public class VisualizerTabCompleter implements TabCompleter {

    private final RegistryAPI registryAPI;

    /**
     * Instantiates a visualizer tab completer
     *
     * @param registryAPI <p>A reference to the registry API</p>
     */
    public VisualizerTabCompleter(RegistryAPI registryAPI) {
        this.registryAPI = registryAPI;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] args) {
        //Don't display any info to non-authorized users
        if (!commandSender.hasPermission(StargateCommandCommand.VISUALIZER.getPermissionNode())) {
            return new ArrayList<>();
        }

        if (args.length < 2) {
            List<String> networkNames = new ArrayList<>();
            registryAPI.getNetworkMap().values().forEach(network -> networkNames.add(NameHelper.getVisualNetworkName(network)));
            return filterMatching(networkNames, args[0]);
        } else {
            return new ArrayList<>();
        }
    }

}
