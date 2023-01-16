package net.knarcraft.stargateinterfaces.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sgrewritten.stargate.api.config.ConfigurationOption;
import org.sgrewritten.stargate.api.config.OptionDataType;

import net.knarcraft.stargateinterfaces.property.StargateCommandCommand;

import static net.knarcraft.stargateinterfaces.util.TabCompleterHelper.filterMatching;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the completer for stargates config sub-command (/sgc config)
 */
public class ConfigTabCompleter implements TabCompleter {

    private final List<ConfigurationOption> bannedConfigOptions;
    private List<String> booleans;
    private List<String> integers;
    private List<String> chatColors;
    private List<String> doubles;

    /**
     * Instantiates a new config tab completer
     *
     * @param bannedConfigOptions <p>A list of config options that shouldn't be available</p>
     */
    public ConfigTabCompleter(List<ConfigurationOption> bannedConfigOptions) {
        this.bannedConfigOptions = bannedConfigOptions;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] args) {
        //Don't display any info to non-authorized users
        if (!commandSender.hasPermission(StargateCommandCommand.CONFIG.getPermissionNode())) {
            return new ArrayList<>();
        }

        if (booleans == null || integers == null || chatColors == null) {
            initializeAutoCompleteLists();
        }

        if (args.length > 2) {
            return new ArrayList<>();
        } else if (args.length > 1) {
            ConfigurationOption selectedOption;
            try {
                selectedOption = ConfigurationOption.valueOf(args[0].toUpperCase());
                if (bannedConfigOptions.contains(selectedOption)) {
                    return new ArrayList<>();
                }
            } catch (IllegalArgumentException exception) {
                return new ArrayList<>();
            }
            return getPossibleOptionValues(selectedOption, args[1]);
        } else {
            List<String> configOptionNames = new ArrayList<>();
            for (ConfigurationOption option : ConfigurationOption.values()) {
                if (!bannedConfigOptions.contains(option)) {
                    configOptionNames.add(option.name());
                }
            }
            return filterMatching(configOptionNames, args[0]);
        }
    }

    /**
     * Get possible values for the selected option
     *
     * @param selectedOption <p>The selected option</p>
     * @param typedText      <p>The beginning of the typed text, for filtering matching results</p>
     * @return <p>Some or all of the valid values for the option</p>
     */
    private List<String> getPossibleOptionValues(ConfigurationOption selectedOption, String typedText) {
        String[] availableValues = selectedOption.getDataType().getValues();
        if (availableValues != null && availableValues.length > 0) {
            return filterMatching(List.of(availableValues), typedText);
        }

        //Just return the default value as most values should be possible
        if (selectedOption == ConfigurationOption.DEFAULT_NETWORK) {
            if (typedText.trim().isEmpty()) {
                return putStringInList((String) selectedOption.getDefaultValue());
            } else {
                return new ArrayList<>();
            }
        }

        if (selectedOption.getDataType() == OptionDataType.COLOR) {
            return filterMatching(chatColors, typedText);
        }

        //If the config value is a boolean, show the two boolean values
        if (selectedOption.getDataType() == OptionDataType.BOOLEAN) {
            return filterMatching(booleans, typedText);
        }

        //If the config value is an integer, display some valid numbers
        if (selectedOption.getDataType() == OptionDataType.INTEGER) {
            if (typedText.trim().isEmpty()) {
                return integers;
            } else {
                return new ArrayList<>();
            }
        }

        //If the config value is a double, display some valid numbers
        if (selectedOption.getDataType() == OptionDataType.DOUBLE) {
            if (typedText.trim().isEmpty()) {
                return doubles;
            } else {
                return new ArrayList<>();
            }
        }
        return null;
    }

    /**
     * Puts a single string value into a string list
     *
     * @param value <p>The string to make into a list</p>
     * @return <p>A list containing the string value</p>
     */
    private List<String> putStringInList(String value) {
        List<String> list = new ArrayList<>();
        list.add(value);
        return list;
    }

    /**
     * Initializes all lists of auto-completable values
     */
    private void initializeAutoCompleteLists() {
        booleans = new ArrayList<>();
        booleans.add("true");
        booleans.add("false");

        integers = new ArrayList<>();
        integers.add("0");
        integers.add("5");

        getColors();

        doubles = new ArrayList<>();
        doubles.add("5");
        doubles.add("1");
        doubles.add("0.5");
        doubles.add("0.1");
    }

    /**
     * Initializes the list of chat colors
     */
    private void getColors() {
        chatColors = List.of(OptionDataType.COLOR.getValues());
    }

}
