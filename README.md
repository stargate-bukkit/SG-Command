> !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!<br>
>  THIS MODULE IS A **WORK IN PROGRESS**.<br>DO __**NOT**__ USE IT ON PRODUCTION INSTANCES<br><br>
>         NOT ALL OF THE FEATURES DESCRIBED HAVE BEEN IMPLEMENTED!<br><br>
>                              No stable releases are available at this time.<br>
> !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!<br>


> Please visit [our discord](https://discord.gg/mTaHuK6BVa) for all updates and support!

# Description
StarGate-Interfaces is an official module that adds numerous extra ways to create, use, and modify portals made with [the Stargate plugin](sgrewritten.org/bukkitsource).<br>

## Features:
This is a work in progress: many features are [still being determined](https://github.com/stargate-rewritten/Stargate-Interfaces/issues)

StarGate-Interfaces consists of two parts (both of which may be toggled in whole or in part):
### A Commands System.
- An admin-facing configuration modification command.
- A dial command to remotely activate specific stargates.
- A visualisation command to display a network's contents.
- An information command to display information about a specific gate.
### An Assortment of GUIs.
- Right-clicking a sign you own with a brush will open a [ColourBukkit](https://github.com/stargate-rewritten/ColorBukkit)-based GUI for customisation.
- Right-clicking any portal with a spyglass will open an inventory-based GUI that allows you to see the portal's destinations and properties, owner, and allows you to activate or destroy the portal (depending on your permissions).
  
## Dependencies
[The most recent version of Stargate](https://sgrewritten.org/download)

# Permissions
### Nodes
```
sg.interfaces.command.config - Gives access to the `/sgc config` command.
sg.interfaces.command.dial  - Gives access to the `/sgc dial` command.
sg.interfaces.command.visualiser  - Gives access to the `/sgc visualiser ` command.
sg.interfaces.command.info  - Gives access to the `/sgc info` command.
sg.interfaces.interface.colour - Allows use of a brush on signs.
sg.interfaces.interface.general - Allows use of a spyglass on signs.

```
### Defaults
*not yet available*
```
sg.interfaces.node -- op
```

## Instructions
Not yet available.

## Configuration
Not yet available.

# Changes
[Version 0.1.0]

    Full takeover removing old functionality, and, for now, replacing it with config editing
    Adds /sgc dial for dialing any wanted Stargate
    Adds /sgc visualizer for visualizing the portals in a network
    Adds /sgc info for seeing information about the Stargate the player is looking at [Version 0.0.4]
    Fix for Bukkit's direction fix [Version 0.0.3]
    Added /sgc owner command
    Now requires at least Stargate v0.7.8.0 [Version 0.0.2]
    Fix an issue with dialing on a specific network
    Change permission checks to use Stargate, this allows proper permission debugging [Version 0.0.1]
    Initial Release

