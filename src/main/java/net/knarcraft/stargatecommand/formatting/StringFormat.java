package net.knarcraft.stargatecommand.formatting;

/**
 * An enum representing the formats of various output messages
 */
public enum StringFormat {

    /**
     * The format describing the visualizer's output format
     */
    COMMAND_VISUALIZER_FORMAT,

    /**
     * The format describing one portal line in the visualizer's output format
     */
    COMMAND_VISUALIZER_PORTAL_FORMAT,

    /**
     * The format used for displaying a fixed portal's destination
     */
    COMMAND_VISUALIZER_FIXED_FORMAT,

    /**
     * The format describing the output from the info command
     */
    COMMAND_INFO_FORMAT,

    /**
     * The format used when describing a configuration option
     */
    CONFIG_OPTION_DESCRIPTION_FORMAT,

    /**
     * The format describing a configuration option's current value
     */
    CONFIG_OPTION_CURRENT_VALUE_FORMAT,

    /**
     * The format describing the header when displaying configuration values
     */
    CONFIG_VALUES_HEADER_FORMAT,

}
