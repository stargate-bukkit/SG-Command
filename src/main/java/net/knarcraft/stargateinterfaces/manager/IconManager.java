package net.knarcraft.stargateinterfaces.manager;

import net.knarcraft.stargateinterfaces.formatting.StringFormatter;
import net.knarcraft.stargateinterfaces.property.Icon;

import java.util.HashMap;
import java.util.Map;

/**
 * This manager keeps track of the different icons used as part of output
 */
public final class IconManager {

    private final static Map<Icon, String> managedIcons = new HashMap<>();

    private IconManager() {

    }

    /**
     * Gets the String used to display the given icon
     *
     * @param icon <p>The icon to get the icon string for</p>
     * @return <p>The string used to display the icon</p>
     */
    public static String getIconString(Icon icon) {
        if (managedIcons.containsKey(icon)) {
            return managedIcons.get(icon);
        } else {
            return icon.getDefaultIconString();
        }
    }

    /**
     * Replaces all icon placeholders in the given string
     *
     * @param input <p>The input to replace placeholders for</p>
     * @return <p>The input with placeholders replaced</p>
     */
    public static String replaceIconsInString(String input) {
        for (Icon icon : Icon.values()) {
            input = StringFormatter.replacePlaceholder(input, icon.getPlaceholder(), getIconString(icon));
        }
        return input;
    }

    /**
     * Registers the given icon string to the given icon
     *
     * @param icon   <p>The icon to register an icon string for</p>
     * @param string <p>The icon string to register</p>
     */
    public static void registerIconString(Icon icon, String string) {
        if (icon == null || string == null) {
            managedIcons.remove(icon);
        } else {
            managedIcons.put(icon, string);
        }
    }

}
