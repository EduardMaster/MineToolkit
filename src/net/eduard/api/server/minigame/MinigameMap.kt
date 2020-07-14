package net.eduard.api.server.minigame

import java.util.ArrayList
import java.util.HashMap
import net.eduard.api.lib.storage.Storable
import net.eduard.api.lib.storage.Storable.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.game.Schematic
import net.eduard.api.lib.modules.Copyable
import org.bukkit.craftbukkit.v1_8_R3.CraftServer

/**
 * Mapa da Sala
 *
 * @author Eduard-PC
 */
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

    val worldName get() = "${minigame?.name}/map/$name"

    val isSolo get() = teamSize == 1

    val world: World
        get() {
            var mundo: World? = Bukkit.getWorld(worldName)
            if (mundo == null) {
                mundo = Mine.loadWorld(worldName)
            }
            mundo?.isAutoSave = false


            return mundo!!
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

    fun fixWorld() = world(world)

    val hasFeast get() = feast != null


    val hasLobby get() = lobby != null


    val hasBases get() = this.bases.isNotEmpty()


    val hasSpawn get() = spawn != null


    val hasSpawns get() = spawns.isNotEmpty()


    val hasSchematic get() = this.map != null


}