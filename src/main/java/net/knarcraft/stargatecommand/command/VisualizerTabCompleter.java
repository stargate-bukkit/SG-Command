package net.knarcraft.stargatecommand.command;

import net.TheDgtl.Stargate.network.RegistryAPI;
import net.knarcraft.stargatecommand.manager.IconManager;
import net.knarcraft.stargatecommand.property.Icon;
import net.knarcraft.stargatecommand.property.StargateCommandCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.knarcraft.stargatecommand.util.TabCompleterHelper.filterMatching;

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
            registryAPI.getNetworkMap().values().forEach(item -> networkNames.add(item.getName().replace(" ",
                    IconManager.getIconString(Icon.SPACE_REPLACEMENT))));
            return filterMatching(networkNames, args[0]);
        } else {
            return new ArrayList<>();
        }
    }

}
