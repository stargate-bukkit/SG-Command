package net.knarcraft.stargatecommand.formatting;

import net.knarcraft.stargatecommand.StargateCommand;
import net.knarcraft.stargatecommand.util.FileHelper;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * A tool to get strings translated to the correct language
 */
public final class Translator {

    private static Map<TranslatableMessage, String> translatedMessages;
    private static Map<TranslatableMessage, String> backupTranslatedMessages;

    private Translator() {

    }

    /**
     * Loads the languages used by this translator
     */
    public static void loadLanguages(String selectedLanguage) {
        backupTranslatedMessages = loadTranslatedMessages("en");
        translatedMessages = loadCustomTranslatedMessages(selectedLanguage);
        if (translatedMessages == null) {
            translatedMessages = loadTranslatedMessages(selectedLanguage);
        }
    }

    /**
     * Gets a translated version of the given translatable message
     *
     * @param translatableMessage <p>The message to translate</p>
     * @return <p>The translated message</p>
     */
    public static String getTranslatedMessage(TranslatableMessage translatableMessage) {
        if (translatedMessages == null) {
            return "Translated strings not loaded";
        }
        String translatedMessage;
        if (translatedMessages.containsKey(translatableMessage)) {
            translatedMessage = translatedMessages.get(translatableMessage);
        } else if (backupTranslatedMessages.containsKey(translatableMessage)) {
            translatedMessage = backupTranslatedMessages.get(translatableMessage);
        } else {
            translatedMessage = translatableMessage.toString();
        }
        return StringFormatter.translateAllColorCodes(translatedMessage);
    }

    /**
     * Loads all translated messages for the given language
     *
     * @param language <p>The language chosen by the user</p>
     * @return <p>A mapping of all strings for the given language</p>
     */
    public static Map<TranslatableMessage, String> loadTranslatedMessages(String language) {
        try {
            BufferedReader reader = FileHelper.getBufferedReaderForInternalFile("/strings.yml");
            return loadTranslatableMessages(language, reader);
        } catch (FileNotFoundException e) {
            StargateCommand.getInstance().getLogger().log(Level.SEVERE, "Unable to load translated messages");
            return null;
        }
    }

    /**
     * Tries to load translated messages from a custom strings.yml file
     *
     * @param language <p>The selected language</p>
     * @return <p>The loaded translated strings, or null if no custom language file exists</p>
     */
    public static Map<TranslatableMessage, String> loadCustomTranslatedMessages(String language) {
        File strings = new File(StargateCommand.getInstance().getDataFolder(), "strings.yml");
        if (!strings.exists()) {
            StargateCommand.getInstance().getLogger().log(Level.FINEST, "Strings file not found");
            return null;
        }

        try {
            StargateCommand.getInstance().getLogger().log(Level.WARNING, "Loading custom strings...");
            return loadTranslatableMessages(language, new BufferedReader(new InputStreamReader(new FileInputStream(strings))));
        } catch (FileNotFoundException e) {
            StargateCommand.getInstance().getLogger().log(Level.WARNING, "Unable to load custom messages");
            return null;
        }
    }

    /**
     * Loads translatable messages from the given reader
     *
     * @param language <p>The selected language</p>
     * @param reader   <p>The buffered reader to read from</p>
     * @return <p>The loaded translated strings</p>
     */
    private static Map<TranslatableMessage, String> loadTranslatableMessages(String language, BufferedReader reader) {
        Map<TranslatableMessage, String> translatedMessages = new HashMap<>();
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(reader);

        for (TranslatableMessage message : TranslatableMessage.values()) {
            String translated = configuration.getString(language + "." + message.toString());
            if (translated != null) {
                translatedMessages.put(message, translated);
            }
        }
        return translatedMessages;
    }

}