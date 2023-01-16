package net.knarcraft.stargateinterfaces.property;

/**
 * An enum representing the commands of this plugin
 */
public enum StargateCommandCommand {

    /**
     * The config command
     */
    CONFIG("config", "stargate.command.config", false),

    /**
     * The dial command
     */
    DIAL("dial", "stargate.command.dial", true),

    /**
     * The visualizer command
     */
    VISUALIZER("visualizer", "stargate.command.visualizer", false),

    /**
     * The info command
     */
    INFO("info", "stargate.command.info", true);

    private final String name;
    private final String permissionNode;
    private final boolean requiresPlayer;

    /**
     * Instantiates a new Stargate-Command Command
     *
     * @param name           <p>The name of the new command</p>
     * @param permissionNode <p>The permission node required for using the command</p>
     */
    StargateCommandCommand(String name, String permissionNode, boolean requiresPlayer) {
        this.name = name;
        this.permissionNode = permissionNode;
        this.requiresPlayer = requiresPlayer;
    }

    /**
     * Gets the name of this command (the string after /sgc)
     *
     * @return <p>The name of this command</p>
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the permission node required for this command
     *
     * @return <p>The permission node required for this command</p>
     */
    public String getPermissionNode() {
        return permissionNode;
    }

    /**
     * Gets whether this command requires usage by a player
     *
     * @return <p>True if this command can only be used by a player</p>
     */
    public boolean requiresPlayer() {
        return requiresPlayer;
    }

}
