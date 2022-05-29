package net.knarcraft.stargatecommand.command;

import net.TheDgtl.Stargate.api.StargateAPI;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * This command represents any command which starts with stargate-command (sgc)
 */
public class CommandStarGateCommand implements CommandExecutor {
    
    private StargateAPI stargateAPI;
    
    public CommandStarGateCommand(StargateAPI stargateAPI) {
        this.stargateAPI = stargateAPI;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("config")) {
                String[] subArgs = (String[]) ArrayUtils.remove(args, 0);
                return new CommandConfig(stargateAPI.getConfigurationAPI()).onCommand(commandSender, command, s, subArgs);
            }
        }
        return false;
    }

}
