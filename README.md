NuxBridge
===========

NuxBridge is a plugin that link your forum's ranks and Permissions (It only works with SMF).
Now it supports the NuxWS ( see : http://github.com/NuxosMinecraft/NuxWS/ ) json api.

Installation
------------

* First, you need the PermissionsEx plugin ([here](http://dev.bukkit.org/server-mods/permissionsex/)).
* Download the latest jar [here](https://github.com/NuxosMinecraft/NuxBridge/downloads).
* Copy the downloaded jar file into the plugins folder and rename it to "NuxBridge.jar".
* Create a basic NuxBridge/config.yml file, based on the exemple below.

Configuration
-------------

The configuration file is : plugins/NuxBridge/config.yml

Example :

    type: json
    mysql: mysql://localhost:3306/forum
    json: http://localhost/user/
    user: forum
    passwd: forum_passwd
    mysql_groups :
        '0': Noob
        '1': Admin
        '2': Moderateor
        '9': Players
        '10': InJail
    json_group:
        '1': Guest
        '4': Player
        '8': SuperUser
        '12': Whatever