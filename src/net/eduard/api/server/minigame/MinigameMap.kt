package net.eduard.api.server.minigame

import net.eduard.api.lib.database.annotations.TableName
import net.eduard.api.lib.storage.Storable.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.game.Schematic
import net.eduard.api.lib.modules.Copyable
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard

/**
 * Mapa da Sala
 *
 * @author Eduard-PC
 */

@TableName("minigame_maps")
@StorageAttributes(indentificate = true)
class MinigameMap(


        @Transient
        var minigame: Minigame? = null,
        var name: String = "mapa",
        var displayName: String = "mapinha"
) {
    init {
        minigame?.maps?.add(this)

    }
    lateinit var b : CraftScoreboard


    var teamSize = 1
    var minPlayersAmount = 2
    var maxPlayersAmount = 20
    var neededPlayersAmount = 16
    var maxRounds = 3
    var spawn: Location? = null
    var lobby: Location? = null
    var locations = mutableMapOf<String, Location>()
    var bases = mutableListOf<Schematic>()
    var spawns = mutableListOf<Location>()
    var map: Schematic? = null
    var feast: Schematic? = null
    var feastLocation: Location? = null

    var worldName = defaultWorldName()

    fun defaultWorldName(): String {
        return "${minigame?.name}/map/$name"
    }

    val isSolo get() = teamSize == 1

    @Transient
    var world: World = loadWorld()

    fun copyWorld(map : MinigameMap) {
        world = Mine.copyWorld(map.worldName, worldName)
        fixWorld()
    }

    fun unloadWorld() {
        Mine.unloadWorld(worldName, world.isAutoSave)
    }

    fun loadWorld(): World {
        var mundo: World? = Bukkit.getWorld(worldName)
        if (mundo == null) {
            mundo = Mine.loadWorld(worldName)
        }
        return mundo!!

    }
    fun fixWorld() = world(world)

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



    val hasFeast get() = feast != null


    val hasLobby get() = lobby != null


    val hasBases get() = this.bases.isNotEmpty()


    val hasSpawn get() = spawn != null


    val hasSpawns get() = spawns.isNotEmpty()


    val hasSchematic get() = this.map != null


}