package net.knarcraft.stargateinterfaces.command;

import org.sgrewritten.stargate.api.StargateAPI;
import org.sgrewritten.stargate.api.config.ConfigurationOption;

import net.knarcraft.stargateinterfaces.property.StargateCommandCommand;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This command represents any command which starts with stargate-command (sgc)
 */
public class CommandStarGateCommand implements CommandExecutor {

    private final StargateAPI stargateAPI;
    private final List<ConfigurationOption> bannedConfigOptions;

    /**
     * Instantiates a new Stargate-command command
     *
     * @param stargateAPI         <p>A reference to the Stargate API</p>
     * @param bannedConfigOptions <p>A list of config options that shouldn't be available</p>
     */
    public CommandStarGateCommand(StargateAPI stargateAPI, List<ConfigurationOption> bannedConfigOptions) {
        this.stargateAPI = stargateAPI;
        this.bannedConfigOptions = bannedConfigOptions;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] args) {
        if (args.length > 0) {
            String[] subArgs = (String[]) ArrayUtils.remove(args, 0);
            if (args[0].equalsIgnoreCase(StargateCommandCommand.CONFIG.getName())) {
                return new CommandConfig(stargateAPI.getConfigurationAPI(), bannedConfigOptions).onCommand(
                        commandSender, command, s, subArgs);
            } else if (args[0].equalsIgnoreCase(StargateCommandCommand.DIAL.getName())) {
                return new CommandDial(stargateAPI).onCommand(commandSender, command, s, subArgs);
            } else if (args[0].equalsIgnoreCase(StargateCommandCommand.VISUALIZER.getName())) {
                return new CommandVisualizer(stargateAPI.getRegistry()).onCommand(commandSender, command, s, subArgs);
            } else if (args[0].equalsIgnoreCase(StargateCommandCommand.INFO.getName())) {
                return new TabCommandInfo(stargateAPI.getRegistry()).onCommand(commandSender, command, s, subArgs);
            }
        }
        return false;
    }

}
