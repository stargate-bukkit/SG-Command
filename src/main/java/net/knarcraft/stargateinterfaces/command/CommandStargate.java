package net.knarcraft.stargateinterfaces.command;

import net.knarcraft.stargateinterfaces.color.ColorModificationRegistry;
import net.knarcraft.stargateinterfaces.command.style.CommandStyle;
import org.sgrewritten.stargate.api.StargateAPI;
import org.sgrewritten.stargate.api.config.ConfigurationOption;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This command represents any command which starts with stargate-command (sgc)
 */
public class CommandStargate implements CommandExecutor {

    private final StargateAPI stargateAPI;
    private final List<ConfigurationOption> bannedConfigOptions;
    private final ColorModificationRegistry registry;

    /**
     * Instantiates a new Stargate-command command
     *
     * @param stargateAPI         <p>A reference to the Stargate API</p>
     * @param bannedConfigOptions <p>A list of config options that shouldn't be available</p>
     */
    public CommandStargate(StargateAPI stargateAPI, List<ConfigurationOption> bannedConfigOptions, ColorModificationRegistry registry) {
        this.stargateAPI = stargateAPI;
        this.bannedConfigOptions = bannedConfigOptions;
        this.registry = registry;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] args) {
        if (args.length > 0) {
            String[] subArgs = ArrayUtils.remove(args, 0);
            try{
                StargateCommandType commandType = StargateCommandType.fromName(args[0]);
                CommandExecutor executor = switch (commandType){
                    case CONFIG -> new CommandConfig(stargateAPI.getConfigurationAPI(), bannedConfigOptions);
                    case DIAL -> new CommandDial(stargateAPI);
                    case VISUALIZER -> new CommandVisualizer(stargateAPI.getRegistry());
                    case INFO -> new TabCommandInfo(stargateAPI.getRegistry());
                    case STYLE -> new CommandStyle(stargateAPI.getRegistry(), registry);
                };
                return executor.onCommand(commandSender, command, s, subArgs);
            } catch (IllegalArgumentException ignored){}
        }
        return false;
    }

}
