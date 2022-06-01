package net.knarcraft.stargatecommand.formatting;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A formatter for formatting displayed messages
 */
public class StringFormatter {

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

}
