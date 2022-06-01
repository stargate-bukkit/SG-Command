package net.knarcraft.stargatecommand.formatting;

/**
 * An enum representing all translatable messages
 */
public enum TranslatableMessage {

    /**
     * The prefix to display in messages
     */
    PREFIX,

    /**
     * The message displayed when a player is denied the usage of a command
     */
    PERMISSION_DENIED,

    /**
     * The message displayed when the user provides an invalid configuration option for the /sgc config command
     */
    INVALID_CONFIGURATION_OPTION,

    /**
     * The message displayed when the user provides an invalid value for the required datatype
     */
    INVALID_DATATYPE_GIVEN,

    /**
     * The message to display after a configuration value has been successfully updated
     */
    CONFIG_UPDATED,

    /**
     * The message to display if a negative number is provided, but a positive number is required
     */
    POSITIVE_NUMBER_REQUIRED,

    /**
     * The message to display if a number is expected, but a non-number is provided
     */
    INVALID_NUMBER_GIVEN,

    /**
     * The message to display when displaying the current value of a configuration option
     */
    CONFIG_OPTION_CURRENT_VALUE,

    /**
     * The header to display when showing all configuration options and their values
     */
    CONFIG_VALUES_HEADER,

    /**
     * The message to display when showing a full description of a configuration option
     */
    CONFIG_OPTION_DESCRIPTION,

    /**
     * The message to display when a command is used from the console, but requires a player
     */
    COMMAND_PLAYER_ONLY,

    /**
     * The message to display for explaining expected arguments for the dial command
     */
    COMMAND_DIAL_ARGUMENTS,

    /**
     * The message to display when the user provides an invalid network name
     */
    INVALID_NETWORK_GIVEN,

    /**
     * The message to display when the user provides an invalid portal name
     */
    INVALID_PORTAL_GIVEN,

    /**
     * The message to display when the user provides a portal they cannot access
     */
    PORTAL_NO_ACCESS,

    /**
     * The message to display when a command cannot find a portal in the player's line of sight
     */
    NO_PORTAL_IN_SIGHT,

    /**
     * The message to display when a Stargate has been successfully dialed
     */
    DIAL_SUCCESSFUL,

    /**
     * The message to display for explaining expected arguments for the visualizer command
     */
    COMMAND_VISUALIZER_ARGUMENTS,

    /**
     * The format used to display the visualization of a Stargate network
     */
    COMMAND_VISUALIZER_FORMAT,

    /**
     * The format used to display one portal int the visualized network
     */
    COMMAND_VISUALIZER_PORTAL_FORMAT,

    /**
     * The format used to display info about a portal's fixed destination
     */
    COMMAND_VISUALIZER_FIXED_FORMAT,

}
