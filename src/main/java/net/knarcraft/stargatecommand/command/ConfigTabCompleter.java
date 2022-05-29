package net.knarcraft.stargatecommand.command;

import net.TheDgtl.Stargate.config.ConfigurationOption;
import net.TheDgtl.Stargate.config.OptionDataType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the completer for stargates config sub-command (/sg config)
 */
public class ConfigTabCompleter implements TabCompleter {

    private List<String> booleans;
    private List<String> integers;
    private List<String> chatColors;
    private List<String> doubles;

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] args) {
        if (booleans == null || integers == null || chatColors == null) {
            initializeAutoCompleteLists();
        }
        if (args.length > 1) {
            ConfigurationOption selectedOption;
            try {
                selectedOption = ConfigurationOption.valueOf(args[0].toUpperCase());
            } catch (IllegalArgumentException exception) {
                return new ArrayList<>();
            }
            return getPossibleOptionValues(selectedOption, args[1]);
        } else {
            List<String> configOptionNames = new ArrayList<>();
            for (ConfigurationOption option : ConfigurationOption.values()) {
                configOptionNames.add(option.name());
            }
            return filterMatching(configOptionNames, args[0]);
        }
    }

    /**
     * Find completable strings which match the text typed by the command's sender
     *
     * @param values    <p>The values to filter</p>
     * @param typedText <p>The text the player has started typing</p>
     * @return <p>The given string values which start with the player's typed text</p>
     */
    private List<String> filterMatching(List<String> values, String typedText) {
        List<String> configValues = new ArrayList<>();
        for (String value : values) {
            if (value.toLowerCase().startsWith(typedText.toLowerCase())) {
                configValues.add(value);
            }
        }
        return configValues;
    }

    /**
     * Get possible values for the selected option
     *
     * @param selectedOption <p>The selected option</p>
     * @param typedText      <p>The beginning of the typed text, for filtering matching results</p>
     * @return <p>Some or all of the valid values for the option</p>
     */
    private List<String> getPossibleOptionValues(ConfigurationOption selectedOption, String typedText) {
        switch (selectedOption) {
            case LANGUAGE:
                //Return available languages
                return filterMatching(List.of(OptionDataType.LANGUAGE.getValues()), typedText);
            case DEFAULT_NETWORK:
                //Just return the default value as most values should be possible
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
