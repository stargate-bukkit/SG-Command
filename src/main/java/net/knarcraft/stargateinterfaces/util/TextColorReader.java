package net.knarcraft.stargateinterfaces.util;

import net.knarcraft.stargateinterfaces.StargateInterfaces;
import net.kyori.adventure.text.format.TextColor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class TextColorReader {

    private static final Properties COLOR_CODES = loadColorCodes();

    private static Properties loadColorCodes() {
        try (InputStream inputStream = StargateInterfaces.class.getResourceAsStream("/colors/colorCodes.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private TextColorReader() {
        throw new IllegalStateException("Utility class");
    }

    public static Optional<TextColor> parseTextColor(String textColorString) {
        if(COLOR_CODES.contains(textColorString)){
            textColorString = COLOR_CODES.getProperty(textColorString);
        }
        return Optional.ofNullable(TextColor.fromCSSHexString(textColorString));
    }
}
