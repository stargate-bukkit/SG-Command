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
     * The message to use when displaying that something is Stargate related
     */
    STARGATE,

    /**
     * The configuration values text displayed when displaying all configuration values
     */
    CONFIGURATION_VALUES_PROMPT,

    /**
     * The current value text displayed when displaying a configuration option's current value
     */
    CURRENT_VALUE_PROMPT,

    /**
     * The default text displayed when displaying a configuration option's default value
     */
    DEFAULT_PROMPT,

    /**
     * The header displayed at the beginning of the info command's output
     */
    COMMAND_INFO_HEADER,

    /**
     * The name text displayed when displaying information about a Stargate
     */
    NAME_PROMPT,

    /**
     * The destination text displayed when displaying information about a Stargate
     */
    DESTINATION_PROMPT,

    /**
     * The network text displayed when displaying information about a Stargate
     */
    NETWORK_PROMPT,

    /**
     * The owner text displayed when displaying information about a Stargate
     */
    OWNER_PROMPT,

    /**
     * The flags text displayed when displaying information about a Stargate
     */
    FLAGS_PROMPT,

    /**
     * The header of the visualizer's symbol explanation section
     */
    VISUALIZER_SYMBOL_EXPLANATION,

    /**
     * The text used to explain the symbol used for hidden portals
     */
    VISUALIZER_HIDDEN,

    /**
     * The text used to explain the symbol used for non-hidden portals
     */
    VISUALIZER_NOT_HIDDEN,

    /**
     * The text used to explain the symbol used for always open portals
     */
    VISUALIZER_ALWAYS_OPEN,

    /**
     * The text used to explain the symbol used for not always open portals
     */
    VISUALIZER_NOT_ALWAYS_OPEN,

    /**
     * The text used to explain the symbol used for random portals
     */
    VISUALIZER_RANDOM,

    /**
     * The text used to explain the symbol used for non-random portals
     */
    VISUALIZER_NOT_RANDOM,

    /**
     * The text used to explain the symbol used for fixed portals
     */
    VISUALIZER_FIXED,

    /**
     * The header of the visualizer's Stargate list section
     */
    VISUALIZER_LIST_HEADER,

}
