package net.eduard.api.server.minigame

import org.bukkit.Location

class MinigameIsland {
    var id = 1
    var chestsLocation = mutableListOf<Location>()
    var chest1Location: Location? = null
    var chest2Location: Location? = null
    var spawnLocation : Location? = null
    var centerLocation : Location? = null
    var highLocation : Location? = null
    var lowLocation : Location? = null
}