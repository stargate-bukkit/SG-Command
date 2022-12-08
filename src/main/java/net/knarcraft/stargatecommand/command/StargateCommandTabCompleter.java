package net.knarcraft.stargatecommand.command;

import org.sgrewritten.stargate.api.StargateAPI;
import org.sgrewritten.stargate.config.ConfigurationOption;
import net.knarcraft.stargatecommand.property.StargateCommandCommand;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A tab completer for the main /sgc command
 */
public class StargateCommandTabCompleter implements TabCompleter {

    private final StargateAPI stargateAPI;
    private final List<ConfigurationOption> bannedConfigOptions;

    /**
     * Instantiates a new Stargate-command tab completer
     *
     * @param stargateAPI         <p>A reference to the Stargate API</p>
     * @param bannedConfigOptions <p>A list of config options that shouldn't be available</p>
     */
    public StargateCommandTabCompleter(StargateAPI stargateAPI, List<ConfigurationOption> bannedConfigOptions) {
        this.stargateAPI = stargateAPI;
        this.bannedConfigOptions = bannedConfigOptions;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command,
                                                @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> commands = getAvailableCommands(commandSender);
            List<String> matchingCommands = new ArrayList<>();
            for (String availableCommand : commands) {
                if (availableCommand.startsWith(args[0])) {
                    matchingCommands.add(availableCommand);
                }
            }
            return matchingCommands;
        } else if (args.length > 1) {
            //Get the actual arguments for the specified sub-command
            String[] subArgs = (String[]) ArrayUtils.remove(args, 0);

            if (args[0].equalsIgnoreCase(StargateCommandCommand.CONFIG.getName())) {
                return new ConfigTabCompleter(bannedConfigOptions).onTabComplete(commandSender, command, s, subArgs);
            } else if (args[0].equalsIgnoreCase(StargateCommandCommand.DIAL.getName())) {
                return new DialTabCompleter(stargateAPI).onTabComplete(commandSender, command, s, subArgs);
            } else if (args[0].equalsIgnoreCase(StargateCommandCommand.VISUALIZER.getName())) {
                return new VisualizerTabCompleter(stargateAPI.getRegistry()).onTabComplete(commandSender, command, s, subArgs);
            } else if (args[0].equalsIgnoreCase(StargateCommandCommand.INFO.getName())) {
                return new TabCommandInfo(stargateAPI.getRegistry()).onTabComplete(commandSender, command, s, subArgs);
            }
        }
        return new ArrayList<>();
    }

    /**
     * Gets the available commands
     *
     * @param commandSender <p>The command sender to get available commands for</p>
     * @return <p>The commands available to the command sender</p>
     */
    private List<String> getAvailableCommands(CommandSender commandSender) {
        List<String> commands = new ArrayList<>();
        for (StargateCommandCommand command : StargateCommandCommand.values()) {
            if ((!command.requiresPlayer() || commandSender instanceof Player) &&
                    commandSender.hasPermission(command.getPermissionNode())) {
                commands.add(command.getName());
            }
        }
        return commands;
    }

}
