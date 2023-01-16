package net.knarcraft.stargateinterfaces.formatting;

import net.knarcraft.stargateinterfaces.StargateInterfaces;
import net.knarcraft.stargateinterfaces.util.FileHelper;

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

/**
 * A tool to get strings translated to the correct language
 */
public final class Translator {

    private static final String translationsFolder = "translations";
    private static Map<TranslatableMessage, String> translatedMessages;
    private static Map<TranslatableMessage, String> backupTranslatedMessages;

    private Translator() {

    }

    /**
     * Loads the languages used by this translator
     */
    public static void loadLanguages(String selectedLanguage) {
        backupTranslatedMessages = loadTranslatedMessages("en-US");
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
        String translatedMessage;
        if (translatedMessages != null && translatedMessages.containsKey(translatableMessage)) {
            translatedMessage = translatedMessages.get(translatableMessage);
        } else if (backupTranslatedMessages != null && backupTranslatedMessages.containsKey(translatableMessage)) {
            translatedMessage = backupTranslatedMessages.get(translatableMessage);
        } else {
            StargateInterfaces.getInstance().getLogger().log(Level.WARNING,
                    "No translation found for translatable message " + translatableMessage.name());
            return "Translated strings not loaded";
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
            BufferedReader reader = FileHelper.getBufferedReaderForInternalFile("/" + translationsFolder + "/" +
                    language + ".yml");
            return loadTranslatableMessages(reader);
        } catch (FileNotFoundException e) {
            StargateInterfaces.getInstance().getLogger().log(Level.SEVERE,
                    String.format("Unable to load translated messages from %s.yml", language));
            return null;
        }
    }

    /**
     * Tries to load translated messages from a custom en-US.yml file
     *
     * @param language <p>The selected language</p>
     * @return <p>The loaded translated strings, or null if no custom language file exists</p>
     */
    public static Map<TranslatableMessage, String> loadCustomTranslatedMessages(String language) {
        File translationsFolderFile = new File(StargateInterfaces.getInstance().getDataFolder(), translationsFolder);
        File languageFile = new File(translationsFolderFile, language + ".yml");
        if (!languageFile.exists()) {
            return null;
        }

        try {
            StargateInterfaces.getInstance().getLogger().log(Level.INFO,
                    String.format("Loading custom strings from %s.yml", language));
            return loadTranslatableMessages(new BufferedReader(new InputStreamReader(new FileInputStream(languageFile),
                    StandardCharsets.UTF_8)));
        } catch (FileNotFoundException e) {
            StargateInterfaces.getInstance().getLogger().log(Level.WARNING,
                    String.format("Unable to load custom messages from %s.yml", language));
            return null;
        }
    }

    /**
     * Loads translatable messages from the given reader
     *
     * @param reader <p>The buffered reader to read from</p>
     * @return <p>The loaded translated strings</p>
     */
    private static Map<TranslatableMessage, String> loadTranslatableMessages(BufferedReader reader) {
        Map<TranslatableMessage, String> translatedMessages = new HashMap<>();
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(reader);

        for (TranslatableMessage message : TranslatableMessage.values()) {
            String translated = configuration.getString(message.toString());
            if (translated != null) {
                translatedMessages.put(message, translated);
            }
        }
        return translatedMessages;
    }

}