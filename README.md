> !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!<br>
>  THIS ADDON IS A **WORK IN PROGRESS**.<br>DO __**NOT**__ USE IT ON PRODUCTION INSTANCES<br><br>
>                              No stable releases are available at this time.<br>
> !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!<br>


> Please visit [our discord](https://discord.gg/mTaHuK6BVa) for all updates and support!

# Description
StargateCommand is an addon for Stargate that allows users to interact with Stargate with commands.

#### Features:
##### Import
Places a specified `.gate` file in-game.
##### Export
Saves your selection to file as a `.gate`
##### Dial
Specifies your destination for networked gates.

## Dependencies
[The most recent version of Stargate](https://www.spigotmc.org/resources/stargate.87978/)

# Permissions

### Nodes
```
stargate.command.import -- Allow the use of /sgc import
stargate.command.export -- Allow the use of /sgc export
stargate.command.dial -- Allow the use of /dial
  stargate.command.dial.interactive -- Allow use of /dial <dest>
  stargate.command.dial.direct -- Allow use of /dial <src> <dest>
```
### Defaults  
```
stargate.command.import -- Op
stargate.command.export -- Op
stargate.command.dial -- True
  stargate.command.dial.interactive -- True
  stargate.command.dial.direct -- False
```

## Instructions
### Import

The "/sgc import" command allows you to import a .gate file without having to manually build it. To import a gate you simply type:
    `/sgc import <gatefile>`
Where <gatefile> is any .gate file without ".gate" at the end. You will then be asked to select two blocks next to each other by right clicking them, this specifies the gates bottom-left location, and orientation.
You can see an example of importing a gate in [this YouTube video](http://youtu.be/Y7KQ0wUUP8c).

### Export
The "/sgc export" command is slightly more complicated than importing, but will allow you to take a gate layout ingame, and export it to a .gate file without having to hand-edit the file.
The first step in building a gate for export is creating a frame out of bedrock material, everything inside of this frame will be treated as the gate, and any bedrock inside of this frame will be treated as the entrance/exit.
Once you have your bedrock frame created, make any shape with any blocks inside of the frame, here is an example export frame: Export Frame
Once you are happy with the gate, run the command:
    /sgc export <gatefile>
You will then be asked to select a few blocks:
-------------------------------------------------------
The first block is where the sign will be drawn
The second block is where the button will be drawn
The third block is where the player will exit the gate
The last block is the top-left of the bedrock frame
Once you have selected these four blocks, if there were no errors, your gate will be saved to disk, and loaded into memory for instant use.
You can see an example of exporting a gate in [this YouTube video](http://youtu.be/-U6IVWt43qw).

### Dial
The "/dial" command allows players to select a destination for a Stargate without requiring them to scroll through a large list of destinations. There are two modes to the /dial command.

> Interactive
The interactive mode is invoked with the command:
    `/dial <destination>`
This will make the next gate the player activates dial the given destination if available.

> Direct
Direct dialing is used to open a gate directly to another without requiring the player to interact with the gate, it is invoked as such:
    `/dial <source> <destination> <network>`
If the network is not supplied, the default Stargate network will be used. If the source/destination exist on the same network, and the player has access to them, the gates will be opened with a connection to eachother.

## Configuration
Not yet available.

# Changes
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
