package net.eduard.api.server.minigame

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

import net.eduard.api.lib.modules.Copyable
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.storage.annotations.StorageAttributes

/**
 * Representa o Mapa da Sala do Minigame
 *
 * @author Eduard-PC
 */


@StorageAttributes(indentificate = true)
class MinigameMap (
        mini : Minigame? = null,
        var name: String = "mapa",
        var displayName: String = "mapinha"
) {
    @Transient
    lateinit var minigame : Minigame
    init {
        mini?.maps?.add(this)
        if (mini!=null) {
            minigame = mini
        }
    }
    var teamSize = 1
    var minPlayersAmount = 2
    var maxPlayersAmount = 20
    var neededPlayersAmount = 16
    var maxRounds = 3
    var spawn: Location? = null
    var lobby: Location? = null
    var locations = mutableMapOf<String, Location>()

    var spawns = mutableListOf<Location>()
    var map: GameSchematic? = null
    var feast: GameSchematic? = null
    var worldName = "world-map"
        get(){
            if (field == "world-map"){
                field = defaultWorldName()
            }
           return field
        }

    val hasFeast get() = feast != null


    val hasLobby get() = lobby != null


    val hasSpawn get() = spawn != null


    val hasSpawns get() = spawns.isNotEmpty()


    val hasSchematic get() = this.map != null

    fun defaultWorldName(): String {
        return "${minigame.name}/map/$name"
    }

    val isSolo get() = teamSize == 1

    @Transient
    var world: World? = null
            get() {
                if (field == null){
                    field = loadWorld()
                }
                return field
            }

    fun copyWorld(map: MinigameMap) {
        world = Mine.copyWorld(map.worldName, worldName)
        fixWorld()
    }

    fun unloadWorld() {

        Mine.unloadWorld(worldName, false)
    }

    fun clearWorld() {
        Mine.deleteWorld(worldName)
        world = Mine.newEmptyWorld(worldName)
    }

    fun loadWorld(): World {
        var mundo: World? = Bukkit.getWorld(worldName)
        if (mundo == null) {
            mundo = Mine.loadWorld(worldName)
        }
        return mundo!!

    }

    fun fixWorld() = world(world!!)

    fun resetWorld() {
        unloadWorld()
        world = loadWorld()
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
        map!!.paste(relative, true)

    }



}