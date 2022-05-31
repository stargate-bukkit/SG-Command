> Please visit [our discord](https://discord.gg/mTaHuK6BVa) for all updates and support!

# Description

Stargate-Command is an addon for Stargate which adds additional useful commands to the vanilla Stargate experience.

#### Features:

* The ability to edit the config file through commands, and automated reloading of changed values
* The ability to dial any Stargate accessible to the Player
* The ability to visualize Stargates in a network
* The ability to see information about the Stargate you are looking at

## Dependencies

[The most recent version of Stargate (> 1.0.0.4)](https://www.spigotmc.org/resources/stargate.87978/)

# Permissions

### Nodes

```
stargate.command.config - Gives access to the `/sgc config` command
stargate.command.dial - Gives access to the `/sgc dial` command
stargate.command.visualizer - Gives access to the `/sgc visualizer` command
stargate.command.info - Gives access to the `/sgc info` command
```

# Changes

[Version 0.1.0]

- Full takeover removing old functionality, and, for now, replacing it with config editing
- Adds /sgc dial for dialing any wanted Stargate
- Adds /sgc visualizer for visualizing the portals in a network
- Adds /sgc info for seeing information about the Stargate the player is looking at
  [Version 0.0.4]
- Fix for Bukkit's direction fix
  [Version 0.0.3]
- Added /sgc owner <player> command
- Now requires at least Stargate v0.7.8.0
  [Version 0.0.2]
- Fix an issue with dialing on a specific network
- Change permission checks to use Stargate, this allows proper permission debugging
  [Version 0.0.1]
- Initial Release
