package net.eduard.api.server.minigame

import org.bukkit.Location
import org.bukkit.World

import net.eduard.api.lib.modules.Copyable
import net.eduard.api.lib.storage.annotations.StorageAttributes
import net.eduard.api.lib.storage.annotations.StorageIndex
import org.bukkit.Bukkit
import org.bukkit.block.Chest
import org.bukkit.util.Vector

/**
 * Representa o Mapa da Sala do Minigame
 *
 * @author Eduard-PC
 */



class MinigameMap(
    mini: Minigame? = null,
    @StorageIndex
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

    var feastRadius = 20
    val feastCenter get() = Location(world, 0.0, 200.0, 0.0)
    @Transient
    var lastRelative = Vector(0,0,0)
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


    val world: World
        get() = worldUsed.loadOrGet()


    var worldName
        get() = worldUsed.worldName
        set(value) {
            worldUsed.worldName = value
        }

    fun fixWorld() = world(world)


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

    fun insideFeast(location: Location) = location.distanceSquared(feastCenter) < (feastRadius * feastRadius)


    fun unloadWorld() = worldUsed.unload()




    fun resetWorld() {
        worldUsed.reset()
        fixWorld()
    }
    fun clearWorld() {
        worldUsed.clear()
        fixWorld()
    }
    fun copy(): MinigameMap {
        return Copyable.copyObject(this)

    }


    fun world(world: World): MinigameMap {
        this.worldName = world.name
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

    fun debug(msg : String){
        Bukkit.getConsoleSender().sendMessage("§b[MinigameMap] §f$msg")
    }
    fun paste(relative: Location) {

        val modified = (relative.toVector().distance(lastRelative) > 0)
        lastRelative = relative.toVector()
        world(relative.world)
        val dif = relative.clone().subtract(map.relative)

        map.paste(relative, true)
        allChests.clear()
        allChests.addAll(map.chests)
        feastChests.addAll(map.feastChests(feastCenter,feastRadius))
        allChests.removeAll(feastChests)

        if (modified) {
            this.spawn?.add(dif)
            this.lobby?.add(dif)
            for (spawn in spawns) {
                spawn.add(dif)
            }
        }
    }


}