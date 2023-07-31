# AnvilPanel - Custom Command Menu With Param
![logo](https://raw.githubusercontent.com/Tining123/anvilpanel/master/img/logo.png)
**AnvilPanel** is an Minecraft plugin designed to allow server owners to create customizable, parameterized command panel with anvil interfaces. The plugin configures everything within GUI, and no need to edit config files.

## Features:
- **Anvil Interface**: All plugin operations are done through the anvil interface. No need for any complex configuration. Everything can be managed with a simple GUI.
- **Customizable Command Panel**: Server owners can create their own command panel with parameters.
- **GUI-based Operations**: All operations can be done through a GUI. No need for any file handling or console commands.
- **Compatibility**: Commands, whether they require arguments or not, can be conveniently configured with this plugin.
- **Permission Management**: With the GUI, it's very easy to toggle on/off or configure permission groups.


## Usage
#### Players could use /anvilpanel or /ap
+ /ap use [panel] - Use a pre-configured command panel

#### Admins could use /anvilpaneladmin or /apadmin
+ /apadmin list - List all command panels
+ /apadmin create - Create a command panel
+ /apadmin group - group management

All you need is create command, and list GUI will provide all configurable setting.

## Example
![craete](https://github.com/Tining123/AnvilPanel/blob/main/img/create.gif?raw=true)
![use](https://github.com/Tining123/AnvilPanel/blob/main/img/use.gif?raw=true)
![use](https://github.com/Tining123/AnvilPanel/blob/main/img/setting.gif?raw=true)

## Permission
+ anvilpanel.use - Player need this permission node to use anvilpanel
+ If you are using Groupmanager, try /mangaddp builder anvilpanel.use or /mangaddp default anvilpanel.use
+ If you are using luckperm, try /lp editor

## config.yml
+ lang: Set language manually.

## Developing
+ GUI entrance to all function [-]
+ Custom sign for each panel [-]
+ Custom user authority for single panel [-]
+ Custom menus setting with GUI [-]
## About
+ MIT lisence
+ If you have any suggestion, complain or recommend function, don't be hesitated and contact me via GitHub or spigot.
## Contact
- Github: https://github.com/Tining123/DemonMarket
- Email: tingave201@outlook.com
