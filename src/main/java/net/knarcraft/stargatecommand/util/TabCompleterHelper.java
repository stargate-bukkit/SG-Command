package net.knarcraft.stargatecommand.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A helper class for helping with tab completion
 */
public final class TabCompleterHelper {

    private TabCompleterHelper() {

    }

    /**
     * Find completable strings which match the text typed by the command's sender
     *
     * @param values    <p>The values to filter</p>
     * @param typedText <p>The text the player has started typing</p>
     * @return <p>The given string values which start with the player's typed text</p>
     */
    public static List<String> filterMatching(List<String> values, String typedText) {
        List<String> configValues = new ArrayList<>();
        for (String value : values) {
            if (value.toLowerCase().startsWith(typedText.toLowerCase())) {
                configValues.add(value);
            }
        }
        return configValues;
    }

}
