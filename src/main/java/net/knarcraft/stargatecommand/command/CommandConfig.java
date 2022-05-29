package net.knarcraft.stargatecommand.command;

import net.TheDgtl.Stargate.config.ConfigurationAPI;
import net.TheDgtl.Stargate.config.ConfigurationOption;
import net.TheDgtl.Stargate.config.OptionDataType;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * This command represents the config command for changing config values
 */
public class CommandConfig implements CommandExecutor {
    
    private final ConfigurationAPI configurationAPI;

    /**
     * Instantiates a new instance of the config command
     * 
     * @param configurationAPI <p>A reference to the Stargate API</p>
     */
    public CommandConfig(ConfigurationAPI configurationAPI) {
        super();
        
        this.configurationAPI = configurationAPI;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] args) {
        if (commandSender instanceof Player player) {
            if (!player.hasPermission("stargate.command.config")) {
                player.sendMessage("Permission Denied");
                return true;
            }
        }

        if (args.length > 0) {
            ConfigurationOption selectedOption;
            try {
                selectedOption = ConfigurationOption.valueOf(args[0].toUpperCase());
            } catch (IllegalArgumentException exception) {
                commandSender.sendMessage("Invalid configuration option specified");
                return true;
            }
            if (args.length > 1) {
                updateConfigValue(selectedOption, commandSender, args[1]);
            } else {
                //Display info and the current value of the given config value
                printConfigOptionValue(commandSender, selectedOption);
            }
            return true;
        } else {
            //Display all config options
            displayConfigValues(commandSender);
        }
        return true;
    }

    /**
     * Updates a config value
     *
     * @param selectedOption <p>The option which should be updated</p>
     * @param commandSender  <p>The command sender that changed the value</p>
     * @param value          <p>The new value of the config option</p>
     */
    private void updateConfigValue(ConfigurationOption selectedOption, CommandSender commandSender, String value) {
        //Validate any sign colors
        if (selectedOption.getDataType() == OptionDataType.COLOR) {
            try {
                ChatColor.of(value.toUpperCase());
            } catch (IllegalArgumentException | NullPointerException ignored) {
                commandSender.sendMessage(ChatColor.RED + "Invalid color given");
                return;
            }
        }
        OptionDataType optionDataType = selectedOption.getDataType();

        //Validate the input based on the data type
        switch (optionDataType) {
            case INTEGER -> {
                if (getInteger(commandSender, selectedOption, value) == null) {
                    return;
                }
            }
            case DOUBLE -> {
                if (getDouble(commandSender, selectedOption, value) == null) {
                    return;
                }
            }
        }
        
        /* Test any option data type with a defined set of values. 
         * Color is excluded as it has a near-infinite number of valid values. */
        if (optionDataType != OptionDataType.COLOR && optionDataType.getValues() != null && 
                optionDataType.getValues().length > 0) {
            if (!checkIfValueMatchesDatatype(optionDataType, value, commandSender)) {
                return;
            }
        }

        configurationAPI.setConfigurationOptionValue(selectedOption, value);
        saveAndReload(commandSender);
    }

    /**
     * Checks if the given value is valid for the given option data type and warns if it isn't
     * 
     * @param dataType <p>The expected type for the value</p>
     * @param value <p>The value to check</p>
     * @param commandSender <p>The command sender to warn about invalid values</p>
     * @return <p>True if the given value is valid for the option data type</p>
     */
    private boolean checkIfValueMatchesDatatype(OptionDataType dataType, String value, CommandSender commandSender) {
        if (!matchesOptionDataType(dataType, value)) {
            commandSender.sendMessage(String.format("Invalid %s given", 
                    dataType.name().toLowerCase().replace('_', ' ')));
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if the given value is valid for the given option data type
     * 
     * @param dataType <p>The expected type for the value</p>
     * @param value <p>The value to check</p>
     * @return <p>True if the given value is valid for the option data type</p>
     */
    private boolean matchesOptionDataType(OptionDataType dataType, String value) {
        return Arrays.asList(dataType.getValues()).contains(value);
    }

    /**
     * Saves the configuration file and reloads
     *
     * @param commandSender  <p>The command sender that executed the config command</p>
     */
    private void saveAndReload(CommandSender commandSender) {
        configurationAPI.saveConfiguration();
        configurationAPI.reload();
        commandSender.sendMessage("Config updated");
    }

    /**
     * Gets an integer from a string
     *
     * @param commandSender  <p>The command sender that sent the config command</p>
     * @param selectedOption <p>The option the command sender is trying to change</p>
     * @param value          <p>The value given</p>
     * @return <p>An integer, or null if it was invalid</p>
     */
    private Integer getInteger(CommandSender commandSender, ConfigurationOption selectedOption, String value) {
        try {
            int intValue = Integer.parseInt(value);

            if ((selectedOption == ConfigurationOption.USE_COST || selectedOption == ConfigurationOption.CREATION_COST) && intValue < 0) {
                commandSender.sendMessage(ChatColor.RED + "This config option cannot be negative.");
                return null;
            }

            return intValue;
        } catch (NumberFormatException exception) {
            commandSender.sendMessage(ChatColor.RED + "Invalid number given");
            return null;
        }
    }

    /**
     * Gets a double from a string
     *
     * @param commandSender  <p>The command sender that sent the config command</p>
     * @param selectedOption <p>The option the command sender is trying to change</p>
     * @param value          <p>The value given</p>
     * @return <p>A double, or null if it was invalid</p>
     */
    private Double getDouble(CommandSender commandSender, ConfigurationOption selectedOption, String value) {
        try {
            double doubleValue = Double.parseDouble(value);

            if (selectedOption == ConfigurationOption.GATE_EXIT_SPEED_MULTIPLIER && doubleValue < 0) {
                commandSender.sendMessage(ChatColor.RED + "This config option cannot be negative.");
                return null;
            }

            return doubleValue;
        } catch (NumberFormatException exception) {
            commandSender.sendMessage(ChatColor.RED + "Invalid number given");
            return null;
        }
    }

    /**
     * Prints information about a config option and its current value
     *
     * @param sender <p>The command sender that sent the command</p>
     * @param option <p>The config option to print information about</p>
     */
    private void printConfigOptionValue(CommandSender sender, ConfigurationOption option) {
        Object value = configurationAPI.getConfigurationOptionValue(option);
        sender.sendMessage(getOptionDescription(option));
        sender.sendMessage(ChatColor.GREEN + "Current value: " + ChatColor.GOLD + value);
    }

    /**
     * Displays the name and a small description of every config value
     *
     * @param sender <p>The command sender to display the config list to</p>
     */
    private void displayConfigValues(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "Stargate" + ChatColor.GOLD + "Config values:");
        
        for (ConfigurationOption option : ConfigurationOption.values()) {
            sender.sendMessage(getOptionDescription(option));
        }
    }

    /**
     * Gets the description of a single config option
     *
     * @param option <p>The option to describe</p>
     * @return <p>A string describing the config option</p>
     */
    private String getOptionDescription(ConfigurationOption option) {
        Object defaultValue = option.getDefaultValue();
        String stringValue = String.valueOf(defaultValue);
        if (option.getDataType() == OptionDataType.STRING_LIST) {
            stringValue = "[" + StringUtils.join((String[]) defaultValue, ",") + "]";
        }
        return ChatColor.GOLD + option.name() + ChatColor.WHITE + " - " + ChatColor.GREEN + option.getDescription() +
                ChatColor.DARK_GRAY + " (Default: " + ChatColor.GRAY + stringValue + ChatColor.DARK_GRAY + ")";
    }

}
