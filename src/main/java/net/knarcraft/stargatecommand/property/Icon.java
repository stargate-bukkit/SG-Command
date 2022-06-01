package net.knarcraft.stargatecommand.property;

/**
 * A representation of configurable icons used in this plugin
 */
public enum Icon {

    /**
     * The icon used to mark a Stargate as hidden
     */
    HIDDEN("⇒", "{icon_h}"),

    /**
     * The icon used to mark a Stargate as not hidden
     */
    NOT_HIDDEN("⇄", "{icon_nh}"),

    /**
     * The icon used to mark a Stargate as always on/open
     */
    ALWAYS_ON("⬛", "{icon_a}"),

    /**
     * The icon used to mark a Stargate as not always on/open
     */
    NOT_ALWAYS_ON("⬜", "{icon_na}"),

    /**
     * The icon used to mark a Stargate as random
     */
    RANDOM("↯", "{icon_r}"),

    /**
     * The icon used to mark a Stargate as not random
     */
    NOT_RANDOM("↠", "{icon_nr}"),

    /**
     * The icon used as a rightwards arrow between two items
     */
    ARROW_RIGHT("->", "{icon_arrow_right}"),

    /**
     * The icon used to replace the space character for any names with spaces
     */
    SPACE_REPLACEMENT("⚊", "{icon_space}");

    private final String defaultIcon;
    private final String placeholder;

    /**
     * Instantiates a new icon
     *
     * @param defaultIcon <p>The default value used unless another is specified</p>
     */
    Icon(String defaultIcon, String placeholder) {
        this.defaultIcon = defaultIcon;
        this.placeholder = placeholder;
    }

    /**
     * Gets the default icon string used for this icon
     *
     * @return <p>The default string used for this icon</p>
     */
    public String getDefaultIconString() {
        return this.defaultIcon;
    }

    /**
     * Gets the placeholder this icon should replace
     *
     * @return <p>The placeholder this icon should replace</p>
     */
    public String getPlaceholder() {
        return this.placeholder;
    }

}
