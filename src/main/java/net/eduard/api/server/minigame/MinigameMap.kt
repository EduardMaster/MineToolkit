package net.eduard.api.server.minigame

import org.bukkit.Location
import org.bukkit.World

import net.eduard.api.lib.modules.Copyable
import net.eduard.api.lib.storage.annotations.StorageAttributes
import org.bukkit.block.Chest
import org.bukkit.util.Vector

/**
 * Representa o Mapa da Sala do Minigame
 *
 * @author Eduard-PC
 */


@StorageAttributes(indentificate = true)
class MinigameMap(
    mini: Minigame? = null,
    var name: String = "mapa",
    var displayName: String = "mapinha"
) {
    @Transient
    lateinit var minigame: Minigame

    init {
        mini?.maps?.add(this)
        if (mini != null) {
            minigame = mini
        }
    }

    var teamSize = 1

    var feastRadius = 50
    val feastCenter get() = Location(world,0.0,200.0,0.0)
    var minPlayersAmount = 2
    var maxPlayersAmount = 20
    var neededPlayersAmount = 16
    var maxRounds = 3
    var spawn: Location? = null
    var lobby: Location? = null
    var locations = mutableMapOf<String, Location>()
    var spawns = mutableListOf<Location>()
    val map: GameSchematic
        get() = GameSchematic.getSchematic(mapName())




    @Transient
    val allChests = mutableListOf<Chest>()
    @Transient
    val feastChests = mutableListOf<Chest>()

    fun mapName() = minigame.name + "-" + name


    val world get() = worldUsed.load()

    var worldName
        get() = worldUsed.worldName
        set(value) {
            worldUsed.worldName = value
        }


    val hasLobby get() = lobby != null


    val hasSpawn get() = spawn != null


    val hasSpawns get() = spawns.isNotEmpty()


    fun defaultWorldName(): String {
        return "${minigame.name}/map/$name"
    }

    val isSolo get() = teamSize == 1
    val isDuo get() = teamSize == 2

    fun copyWorld(map: MinigameMap) {
        worldUsed.copy(map.worldName)
        fixWorld()
    }
    fun insideFeast(location: Location) = location.distanceSquared(feastCenter) < (feastRadius*feastRadius)


    fun unloadWorld() = worldUsed.unload()


    fun loadWorld() = worldUsed.load()


    fun fixWorld() = world(world)

    fun resetWorld() {
        worldUsed.reset()
        fixWorld()
    }

    fun copy(): MinigameMap {
        return Copyable.copyObject(this)

    }


    fun world(world: World): MinigameMap {
        for (spawn in this.spawns) {
            spawn.world = world
        }
        if (hasSpawn) {
            spawn!!.world = world
        }
        if (hasLobby) {
            lobby!!.world = world
        }
        for (loc in locations.values) {
            loc.world = world
        }

        return this
    }

    var worldUsed = MinigameWorld()
        get() {
            if (field.nameNotSet()) {
                field.worldName = defaultWorldName()
            }
            return field
        }

    fun paste(relative: Location) {
        world(relative.world)

        val dif = relative.clone().subtract(map!!.relative)
        if (hasSpawn) {
            this.spawn!!.add(dif)
        }
        if (hasLobby) {
            this.lobby!!.add(dif)
        }
        for (spawn in spawns) {
            spawn.add(dif)
        }
        map.paste(relative, true)

    }


}