package net.knarcraft.stargatecommand.formatting;

import net.knarcraft.stargatecommand.StargateCommand;
import net.knarcraft.stargatecommand.util.FileHelper;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A formatter for formatting displayed messages
 */
public final class StringFormatter {

    private static final String formatFileName = "format.yml";
    private static Map<StringFormat, String> loadedFormats;
    private static Map<StringFormat, String> backupLoadedFormats;

    private StringFormatter() {

    }

    /**
     * Loads string formats from disk
     */
    public static void loadStringFormats() {
        loadedFormats = loadCustomStringFormat();
        backupLoadedFormats = loadStringFormat();
    }

    /**
     * Gets a string format from the format file
     *
     * @param stringFormat <p>The string format to get</p>
     * @return <p>The translated message</p>
     */
    public static String getStringFormat(StringFormat stringFormat) {
        String translatedMessage;
        if (loadedFormats != null && loadedFormats.containsKey(stringFormat)) {
            translatedMessage = loadedFormats.get(stringFormat);
        } else if (backupLoadedFormats != null && backupLoadedFormats.containsKey(stringFormat)) {
            translatedMessage = backupLoadedFormats.get(stringFormat);
        } else {
            return "String formats not loaded";
        }
        return replaceTranslatablePlaceholders(translateAllColorCodes(translatedMessage));
    }

    /**
     * Replaces a placeholder in a string
     *
     * @param input       <p>The input string to replace in</p>
     * @param placeholder <p>The placeholder to replace</p>
     * @param replacement <p>The replacement value</p>
     * @return <p>The input string with the placeholder replaced</p>
     */
    public static String replacePlaceholder(String input, String placeholder, String replacement) {
        return input.replace(placeholder, replacement);
    }

    /**
     * Replaces placeholders in a string
     *
     * @param input        <p>The input string to replace in</p>
     * @param placeholders <p>The placeholders to replace</p>
     * @param replacements <p>The replacement values</p>
     * @return <p>The input string with placeholders replaced</p>
     */
    public static String replacePlaceholders(String input, String[] placeholders, String[] replacements) {
        for (int i = 0; i < Math.min(placeholders.length, replacements.length); i++) {
            input = replacePlaceholder(input, placeholders[i], replacements[i]);
        }
        return input;
    }

    /**
     * Gets a translated and formatted info message
     *
     * @param translatableMessage <p>The translatable message to translate and format</p>
     * @return <p>The translated and formatted message</p>
     */
    public static String getTranslatedInfoMessage(TranslatableMessage translatableMessage) {
        return formatInfoMessage(Translator.getTranslatedMessage(translatableMessage));
    }

    /**
     * Gets a translated and formatted error message
     *
     * @param translatableMessage <p>The translatable message to translate and format</p>
     * @return <p>The translated and formatted message</p>
     */
    public static String getTranslatedErrorMessage(TranslatableMessage translatableMessage) {
        return formatErrorMessage(Translator.getTranslatedMessage(translatableMessage));
    }

    /**
     * Formats an information message by adding the prefix and text color
     *
     * @param message <p>The message to format</p>
     * @return <p>The formatted message</p>
     */
    public static String formatInfoMessage(String message) {
        return ChatColor.DARK_GREEN + formatMessage(message);
    }

    /**
     * Formats an error message by adding the prefix and text color
     *
     * @param message <p>The message to format</p>
     * @return <p>The formatted message</p>
     */
    public static String formatErrorMessage(String message) {
        return ChatColor.DARK_RED + formatMessage(message);
    }

    /**
     * Translates all found color codes to formatting in a string
     *
     * @param message <p>The string to search for color codes</p>
     * @return <p>The message with color codes translated</p>
     */
    public static String translateAllColorCodes(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        Pattern pattern = Pattern.compile("(#[a-fA-F0-9]{6})");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            message = message.replace(matcher.group(), "" + ChatColor.of(matcher.group()));
        }
        return message;
    }

    /**
     * Formats a message by adding the prefix and text color
     *
     * @param message <p>The message to format</p>
     * @return <p>The formatted message</p>
     */
    private static String formatMessage(String message) {
        return Translator.getTranslatedMessage(TranslatableMessage.PREFIX) + " " +
                ChatColor.RESET + message;
    }

    /**
     * Loads all string formats
     *
     * @return <p>A mapping of all string formats</p>
     */
    public static Map<StringFormat, String> loadStringFormat() {
        try {
            BufferedReader reader = FileHelper.getBufferedReaderForInternalFile("/" + formatFileName);
            return loadFormats(reader);
        } catch (FileNotFoundException e) {
            StargateCommand.getInstance().getLogger().log(Level.SEVERE, "Unable to load string formats from " +
                    formatFileName);
            return null;
        }
    }

    /**
     * Tries to load translated messages from a custom en-US.yml file
     *
     * @return <p>The loaded translated strings, or null if no custom language file exists</p>
     */
    public static Map<StringFormat, String> loadCustomStringFormat() {
        File formatFile = new File(StargateCommand.getInstance().getDataFolder(), formatFileName);
        if (!formatFile.exists()) {
            return null;
        }

        try {
            StargateCommand.getInstance().getLogger().log(Level.INFO, "Loading custom formats from " +
                    formatFileName);
            return loadFormats(new BufferedReader(new InputStreamReader(new FileInputStream(formatFile),
                    StandardCharsets.UTF_8)));
        } catch (FileNotFoundException e) {
            StargateCommand.getInstance().getLogger().log(Level.WARNING,
                    "Unable to load custom formats from " + formatFileName);
            return null;
        }
    }

    /**
     * Replaces all placeholders corresponding to translatable messages in the given input
     *
     * @param input <p>The input to replace placeholders for</p>
     * @return <p>The input with placeholders replaced</p>
     */
    private static String replaceTranslatablePlaceholders(String input) {
        for (TranslatableMessage translatableMessage : TranslatableMessage.values()) {
            input = StringFormatter.replacePlaceholder(input, "{" + translatableMessage.name() + "}",
                    Translator.getTranslatedMessage(translatableMessage));
        }
        return input;
    }

    /**
     * Loads string formats from the given reader
     *
     * @param reader <p>The buffered reader to read from</p>
     * @return <p>The loaded formats strings</p>
     */
    private static Map<StringFormat, String> loadFormats(BufferedReader reader) {
        Map<StringFormat, String> stringFormats = new HashMap<>();
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(reader);

        for (StringFormat message : StringFormat.values()) {
            String format = configuration.getString(message.toString());
            if (format != null) {
                stringFormats.put(message, format);
            }
        }
        return stringFormats;
    }

}
