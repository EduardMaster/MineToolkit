package net.eduard.api.server.minigame

import org.bukkit.Location
import org.bukkit.World

import net.eduard.api.lib.modules.Copyable
import net.eduard.api.lib.storage.annotations.StorageIndex
import org.bukkit.Bukkit
import org.bukkit.block.Chest
import org.bukkit.util.Vector

/**
 * Representa o Mapa da Sala do Minigame
 *
 * @author Eduard
 */


class MinigameMap(

    @StorageIndex
    var name: String = "mapa",
    var displayName: String = "mapinha"
) {
    @Transient
    lateinit var minigame: Minigame
    var feastRadius = 20
    var feastCenter : Location? = null
     get() {
       if (field == null)
           field = Location(world, 0.0, 100.0, 0.0)
         return field
     }

    @Transient
    var minPlayersAmount = 2
    var maxPlayersAmount = 20
    var neededPlayersAmount = 16
    var spawn: Location? = null
    var lobby: Location? = null
    var locations = mutableMapOf<String, Location>()
    var spawns = mutableListOf<Location>()
    val map: MinigameSchematic
        get() = MinigameSchematic.getSchematic(mapName())

    @Transient
    val allChests = mutableListOf<Chest>()
    val feastChests get() = allChests.filter { insideFeast(it.location) }

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
    fun setupChests(){
        allChests.clear()
        allChests.addAll(map.getAllChests(feastCenter!!))
    }


    fun copyWorld(map: MinigameMap) {
        worldUsed.copy(map.worldName)
        fixWorld()
    }

    fun insideFeast(location: Location): Boolean {
        val raioDoFeast = (feastRadius * feastRadius )
        val distanciaDoBloco = feastCenter!!.distanceSquared(location)
      //  debug("Bloco: "+ location+" RaioDoFeast: "+ raioDoFeast+" distanciaBloco: " +distanciaDoBloco)
        return distanciaDoBloco < raioDoFeast
    }
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
        feastCenter?.world = world

        return this
    }

    var worldUsed = MinigameWorld()
        get() {
            if (field.nameNotSet()) {
                field.worldName = defaultWorldName()
            }
            return field
        }

    fun debug(msg: String) {
        Bukkit.getConsoleSender().sendMessage("§b[MinigameMap] §f$msg")
    }

    fun paste(relative: Location) {
        world(relative.world)
        val dif = feastCenter!!.clone().subtract(relative)
        feastCenter = relative
        map.paste(relative, true)
        allChests.clear()
        allChests.addAll(map.getAllChests(relative))
        this.spawn?.add(dif)
        this.lobby?.add(dif)
        for (spawn in spawns) {
            spawn.add(dif)
        }
    }
}