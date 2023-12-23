package net.knarcraft.stargateinterfaces;

import org.sgrewritten.stargate.api.StargateAPI;
import org.sgrewritten.stargate.api.config.ConfigurationOption;

import net.knarcraft.stargateinterfaces.command.CommandStarGateCommand;
import net.knarcraft.stargateinterfaces.command.StargateCommandTabCompleter;
import net.knarcraft.stargateinterfaces.formatting.StringFormatter;
import net.knarcraft.stargateinterfaces.formatting.Translator;
import net.knarcraft.stargateinterfaces.listener.StargateListener;
import net.knarcraft.stargateinterfaces.manager.IconManager;
import net.knarcraft.stargateinterfaces.property.Icon;

import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * The main class for the Stargate-Command add-on
 */
@SuppressWarnings("unused")
public class StargateInterfaces extends JavaPlugin {

    private static StargateInterfaces instance;
    private List<ConfigurationOption> bannedConfigOptions;

    @Override
    public void onEnable() {
        //Initialize the list of banned configuration options
        if (bannedConfigOptions == null) {
            initializeBannedConfigOptions();
        }
        instance = this;

        FileConfiguration configuration = this.getConfig();
        this.saveDefaultConfig();
        configuration.options().copyDefaults(true);
        loadConfiguration(configuration);
        saveConfig();

        //Get the Stargate API
        ServicesManager servicesManager = this.getServer().getServicesManager();
        RegisteredServiceProvider<StargateAPI> stargateProvider = servicesManager.getRegistration(StargateAPI.class);
        if (stargateProvider != null) {
            StargateAPI stargateAPI = stargateProvider.getProvider();

            //Register commands
            PluginCommand stargateCommand = this.getCommand("stargateCommand");
            if (stargateCommand != null) {
                stargateCommand.setExecutor(new CommandStarGateCommand(stargateAPI, bannedConfigOptions));
                stargateCommand.setTabCompleter(new StargateCommandTabCompleter(stargateAPI, bannedConfigOptions));
            }
            PluginManager pluginManager = getServer().getPluginManager();
            pluginManager.registerEvents(new StargateListener(stargateAPI), this);
        } else {
            throw new IllegalStateException("Unable to hook into Stargate. Make sure the Stargate plugin is installed " +
                    "and enabled.");
        }
    }

    @Override
    public void onDisable() {
        //Currently, nothing needs to be disabled
    }

    /**
     * Gets an instance of this plugin
     *
     * @return <p>An instance of this plugin</p>
     */
    public static StargateInterfaces getInstance() {
        return instance;
    }

    /**
     * Loads all configuration values from the given configuration
     *
     * @param fileConfiguration <p>The configuration to load</p>
     */
    private void loadConfiguration(FileConfiguration fileConfiguration) {
        Translator.loadLanguages(fileConfiguration.getString("language"));
        StringFormatter.loadStringFormats();
        //Load all icons from config
        for (Icon icon : Icon.values()) {
            String iconString = fileConfiguration.getString(icon.getConfigNode());
            if (iconString != null) {
                IconManager.registerIconString(icon, iconString);
            }
        }
    }

    /**
     * Initializes the list of banned configuration options
     *
     * <p>The banned configuration options are those options that should never be updated manually, and the ones deemed
     * too risky to be updated on a running Stargate instance.</p>
     */
    private void initializeBannedConfigOptions() {
        bannedConfigOptions = new ArrayList<>();
        bannedConfigOptions.add(ConfigurationOption.CONFIG_VERSION);
        bannedConfigOptions.add(ConfigurationOption.BUNGEE_DATABASE);
        bannedConfigOptions.add(ConfigurationOption.BUNGEE_USERNAME);
        bannedConfigOptions.add(ConfigurationOption.BUNGEE_ADDRESS);
        bannedConfigOptions.add(ConfigurationOption.DATABASE_NAME);
        bannedConfigOptions.add(ConfigurationOption.BUNGEE_INSTANCE_NAME);
        bannedConfigOptions.add(ConfigurationOption.USING_REMOTE_DATABASE);
        bannedConfigOptions.add(ConfigurationOption.BUNGEE_DRIVER);
        bannedConfigOptions.add(ConfigurationOption.BUNGEE_PASSWORD);
        bannedConfigOptions.add(ConfigurationOption.BUNGEE_PORT);
        bannedConfigOptions.add(ConfigurationOption.BUNGEE_USE_SSL);
        bannedConfigOptions.add(ConfigurationOption.SHOW_HIKARI_CONFIG);
        for (ConfigurationOption option : ConfigurationOption.values()) {
            //Configuration options with a null description are unimplemented and should not be used
            if (option.getDescription() == null || option.isHidden()) {
                bannedConfigOptions.add(option);
            }
        }
    }

}
