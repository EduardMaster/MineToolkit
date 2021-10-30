package net.eduard.api.command.map

import net.eduard.api.lib.manager.CommandManager

class MapCommand : CommandManager("map") {
    init {
        register(MapCopyCommand())
        register(MapPasteCommand())
        register(MapPos1Command())
        register(MapPos2Command())
        register(MapLoadCommand())
        register(MapSaveCommand())
        register(MapSetCommand())
        register(MapListCommand())
        register(MapHelpCommand())
    }
}