# EduardAPI
## Toolkit for Developing Bukkit/BungeeCord/Spigot/Paper Plugins

### Features
- Custom Database ORM based on **Hibernate** called "SQLMaanger"
- Custom YAML Implememntation (Simple) => (Config, ConfigSection)
- Custom Object Mapper (Transforming Objects in HashMap) called "Storage"
- Auto loading of Java Libraries in folder /server/libs/
- Auto downloading of Kotlin Libraries from Maven repository to make usage of this Toolkit easy

### Helpers
- Usage of Database with class **DBManager**
- Creating Inventories with Actions (Menus) with classes **Menu** , **MenuButton** e **Shop**
- Creating Scoreboard (Informative Screens) more easy with class **DisplayBoard**
- Helpers classes for Java data control
- Kotlin Extensions for many functions of Toolkit

## Why I was using this Toolkit called "EduardAPI"
- I didn't like to install gradle or maven libraries when I started to create this tool, so, I needed to create
- a data Save/Load from/to .yaml files, and I create this at end of 2017, but with time turned to a good and performed tool
- I didn't like to move Helpers classes between projects so I let all helpers at same Project Library
- I challenged myself in creating a thing very hard like ORM Hibernate, that I called SQLManager
- My first creation was my own Yaml implementation
- I created a transformer of Object to HashMap with 'Keys' and 'Values'
- using their variables Names and Variables Values (Deep Tranformation) that I called StorageAPI
- I created some custom NMS Implementations for doing some special things not in spigot.jar, at version 1.8.9

## Why no more Updates
- I stopped making Plugins in 2022
- I start a new Career creating Applications for Android, IOS and another devices, Desktop too
- Maybe someday I remove the ORM from here and create a very useful ORM for Java or Javascript

