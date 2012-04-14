NuxBridge
===========

NuxBridge is a plugin that link your forum's ranks and Permissions (It only works with SMF).


Installation
------------

* First, you need the bPermissions (3) plugin ([here](http://dev.bukkit.org/server-mods/bpermissions/)).
* Download the latest jar [here](https://github.com/NuxosMinecraft/NuxBridge/downloads).
* Copy the downloaded jar file into the plugins folder and rename it to "NuxBridge.jar".

Configuration
-------------

The configuration file is : plugins/NuxBridge/config.yml

Example :

    url: mysql://localhost:3306/forum
    user: forum
    passwd: forum_passwd
    default_id: 0
    worlds :
        - world
        - world_nether
    groups :
        '0': Noob
        '1': Admin
        '2': Moderateor
        '9': Players
        '10': InJail