package net.knarcraft.stargateinterfaces.property;

/**
 * A representation of configurable icons used in this plugin
 */
public enum Icon {

    /**
     * The icon used to mark a Stargate as hidden
     */
    HIDDEN("⇒", "{icon_h}", "icon.hiddenIcon"),

    /**
     * The icon used to mark a Stargate as not hidden
     */
    NOT_HIDDEN("⇄", "{icon_nh}", "icon.notHiddenIcon"),

    /**
     * The icon used to mark a Stargate as always on/open
     */
    ALWAYS_ON("⬛", "{icon_a}", "icon.alwaysOpenIcon"),

    /**
     * The icon used to mark a Stargate as not always on/open
     */
    NOT_ALWAYS_ON("⬜", "{icon_na}", "icon.notAlwaysOpenIcon"),

    /**
     * The icon used to mark a Stargate as random
     */
    RANDOM("↯", "{icon_r}", "icon.randomIcon"),

    /**
     * The icon used to mark a Stargate as not random
     */
    NOT_RANDOM("↠", "{icon_nr}", "icon.notRandomIcon"),

    /**
     * The icon used as a rightwards arrow between two items
     */
    ARROW_RIGHT("->", "{icon_arrow_right}", "icon.arrowRightIcon"),

    /**
     * The icon used to replace the space character for any names with spaces
     */
    SPACE_REPLACEMENT("⚊", "{icon_space}", "icon.spaceReplacementIcon");

    private final String defaultIcon;
    private final String placeholder;
    private final String configNode;

    /**
     * Instantiates a new icon
     *
     * @param defaultIcon <p>The default value used unless another is specified</p>
     * @param placeholder <p>The placeholder this icon should replace</p>
     * @param configNode  <p>The config node used to specify this icon</p>
     */
    Icon(String defaultIcon, String placeholder, String configNode) {
        this.defaultIcon = defaultIcon;
        this.placeholder = placeholder;
        this.configNode = configNode;
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

    /**
     * Gets the config node used to specify this icon
     *
     * @return <p>The config node used to specify this icon</p>
     */
    public String getConfigNode() {
        return this.configNode;
    }

}
