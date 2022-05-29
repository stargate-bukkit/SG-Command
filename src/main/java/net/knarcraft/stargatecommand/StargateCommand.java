package net.knarcraft.stargatecommand;

import net.TheDgtl.Stargate.api.StargateAPI;
import net.knarcraft.stargatecommand.command.CommandStarGateCommand;
import net.knarcraft.stargatecommand.command.StargateCommandTabCompleter;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class for the Stargate-Command add-on
 */
@SuppressWarnings("unused")
public class StargateCommand extends JavaPlugin {

    @Override
    public void onEnable() {
        //Get the Stargate API
        ServicesManager servicesManager = this.getServer().getServicesManager();
        RegisteredServiceProvider<StargateAPI> stargateProvider = servicesManager.getRegistration(StargateAPI.class);
        if (stargateProvider != null) {
            StargateAPI stargateAPI = stargateProvider.getProvider();
            
            //Register commands
            PluginCommand stargateCommand = this.getCommand("stargatecommand");
            if (stargateCommand != null) {
                stargateCommand.setExecutor(new CommandStarGateCommand(stargateAPI));
                stargateCommand.setTabCompleter(new StargateCommandTabCompleter());
            }
        } else {
            throw new IllegalStateException("Unable to hook into Stargate. Make sure the Stargate plugin is installed " +
                    "and enabled.");
        }
    }

    @Override
    public void onDisable() {
        //Currently, nothing needs to be disabled
    }
    
}
