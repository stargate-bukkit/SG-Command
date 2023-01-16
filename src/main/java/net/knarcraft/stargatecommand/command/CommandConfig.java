package net.knarcraft.stargatecommand.command;

import net.knarcraft.stargatecommand.formatting.StringFormat;
import net.knarcraft.stargatecommand.formatting.StringFormatter;
import net.knarcraft.stargatecommand.formatting.TranslatableMessage;
import net.knarcraft.stargatecommand.property.StargateCommandCommand;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.sgrewritten.stargate.api.config.ConfigurationAPI;
import org.sgrewritten.stargate.api.config.ConfigurationOption;
import org.sgrewritten.stargate.api.config.OptionDataType;

import java.util.Arrays;
import java.util.List;

import static net.knarcraft.stargatecommand.formatting.StringFormatter.getTranslatedErrorMessage;
import static net.knarcraft.stargatecommand.formatting.StringFormatter.getTranslatedInfoMessage;

/**
 * This command represents the config command for changing config values
 */
public class CommandConfig implements CommandExecutor {

    private final ConfigurationAPI configurationAPI;
    private final List<ConfigurationOption> bannedConfigOptions;

    /**
     * Instantiates a new instance of the config command
     *
     * @param configurationAPI    <p>A reference to the Stargate API</p>
     * @param bannedConfigOptions <p>A list of config options that shouldn't be available</p>
     */
    public CommandConfig(ConfigurationAPI configurationAPI, List<ConfigurationOption> bannedConfigOptions) {
        super();

        this.configurationAPI = configurationAPI;
        this.bannedConfigOptions = bannedConfigOptions;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] args) {
        if (commandSender instanceof Player player) {
            if (!player.hasPermission(StargateCommandCommand.CONFIG.getPermissionNode())) {
                player.sendMessage(getTranslatedErrorMessage(TranslatableMessage.PERMISSION_DENIED));
                return true;
            }
        }

        if (args.length > 0) {
            ConfigurationOption selectedOption;
            try {
                selectedOption = ConfigurationOption.valueOf(args[0].toUpperCase());
                if (bannedConfigOptions.contains(selectedOption)) {
                    return true;
                }
            } catch (IllegalArgumentException exception) {
                commandSender.sendMessage(getTranslatedErrorMessage(TranslatableMessage.INVALID_CONFIGURATION_OPTION));
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
                commandSender.sendMessage(StringFormatter.replacePlaceholder(getTranslatedErrorMessage(
                        TranslatableMessage.INVALID_DATATYPE_GIVEN), "{datatype}", "color"));
                return;
            }
        }
        OptionDataType optionDataType = selectedOption.getDataType();
        Object typeCastedValue;

        //Validate the input based on the data type
        switch (optionDataType) {
            case INTEGER -> {
                Integer intValue = getInteger(commandSender, selectedOption, value);
                if (intValue == null) {
                    return;
                } else {
                    typeCastedValue = intValue;
                }
            }
            case DOUBLE -> {
                Double doubleValue = getDouble(commandSender, selectedOption, value);
                if (doubleValue == null) {
                    return;
                } else {
                    typeCastedValue = doubleValue;
                }
            }
            case BOOLEAN -> typeCastedValue = Boolean.parseBoolean(value);
            default -> typeCastedValue = value;
        }

        /* Test any option data type with a defined set of values.
         * Color is excluded as it has a near-infinite number of valid values. */
        if (optionDataType != OptionDataType.COLOR && optionDataType.getValues() != null &&
                optionDataType.getValues().length > 0) {
            if (!checkIfValueMatchesDatatype(optionDataType, value, commandSender)) {
                return;
            }
        }

        configurationAPI.setConfigurationOptionValue(selectedOption, typeCastedValue);
        saveAndReload(commandSender);
    }

    /**
     * Checks if the given value is valid for the given option data type and warns if it isn't
     *
     * @param dataType      <p>The expected type for the value</p>
     * @param value         <p>The value to check</p>
     * @param commandSender <p>The command sender to warn about invalid values</p>
     * @return <p>True if the given value is valid for the option data type</p>
     */
    private boolean checkIfValueMatchesDatatype(OptionDataType dataType, String value, CommandSender commandSender) {
        if (!matchesOptionDataType(dataType, value)) {
            commandSender.sendMessage(StringFormatter.replacePlaceholder(getTranslatedErrorMessage(
                            TranslatableMessage.INVALID_DATATYPE_GIVEN), "{datatype}",
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
     * @param value    <p>The value to check</p>
     * @return <p>True if the given value is valid for the option data type</p>
     */
    private boolean matchesOptionDataType(OptionDataType dataType, String value) {
        return Arrays.asList(dataType.getValues()).contains(value);
    }

    /**
     * Saves the configuration file and reloads
     *
     * @param commandSender <p>The command sender that executed the config command</p>
     */
    private void saveAndReload(CommandSender commandSender) {
        configurationAPI.saveConfiguration();
        configurationAPI.reload();
        commandSender.sendMessage(getTranslatedInfoMessage(TranslatableMessage.CONFIG_UPDATED));
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
                commandSender.sendMessage(getTranslatedErrorMessage(TranslatableMessage.POSITIVE_NUMBER_REQUIRED));
                return null;
            }

            return intValue;
        } catch (NumberFormatException exception) {
            commandSender.sendMessage(getTranslatedErrorMessage(TranslatableMessage.INVALID_NUMBER_GIVEN));
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
                commandSender.sendMessage(getTranslatedErrorMessage(TranslatableMessage.POSITIVE_NUMBER_REQUIRED));
                return null;
            }

            return doubleValue;
        } catch (NumberFormatException exception) {
            commandSender.sendMessage(getTranslatedErrorMessage(TranslatableMessage.INVALID_NUMBER_GIVEN));
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
        String description = getOptionDescription(option);
        String currentValue = StringFormatter.replacePlaceholder(StringFormatter.getStringFormat(
                StringFormat.CONFIG_OPTION_CURRENT_VALUE_FORMAT), "{value}", String.valueOf(value));
        sender.sendMessage(StringFormatter.formatInfoMessage(description + currentValue));
    }

    /**
     * Displays the name and a small description of every config value
     *
     * @param sender <p>The command sender to display the config list to</p>
     */
    private void displayConfigValues(CommandSender sender) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(StringFormatter.getStringFormat(StringFormat.CONFIG_VALUES_HEADER_FORMAT));

        for (ConfigurationOption option : ConfigurationOption.values()) {
            if (!bannedConfigOptions.contains(option)) {
                stringBuilder.append(getOptionDescription(option));
            }
        }
        sender.sendMessage(stringBuilder.toString());
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
        return StringFormatter.replacePlaceholders(StringFormatter.getStringFormat(
                        StringFormat.CONFIG_OPTION_DESCRIPTION_FORMAT), new String[]{"{name}", "{description}", "{value}"},
                new String[]{option.name(), option.getDescription(), stringValue});
    }

}
