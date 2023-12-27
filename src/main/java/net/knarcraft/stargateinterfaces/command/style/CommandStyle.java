package net.knarcraft.stargateinterfaces.command.style;

import net.knarcraft.stargateinterfaces.command.StargateCommandType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.sgrewritten.stargate.api.network.RegistryAPI;

public class CommandStyle implements CommandExecutor {
    private final RegistryAPI registry;

    public CommandStyle(RegistryAPI registry) {
        this.registry = registry;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return false;
    }


}
