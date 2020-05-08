package net.eduard.api.server.minigame

import java.util.ArrayList
import java.util.HashMap
import net.eduard.api.lib.storage.Storable
import net.eduard.api.lib.storage.Storable.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.world.Schematic
import net.eduard.api.lib.modules.Copyable

/**
 * Mapa da Sala
 *
 * @author Eduard-PC
 */
@StorageAttributes(indentificate = true)
class MinigameMap : Storable<MinigameMap>, Copyable {

    var name: String? = null
    var displayName: String? = null
    var worldName: String? = null
    var teamSize = 1
    var minPlayersAmount = 2
    var maxPlayersAmount = 20
    var neededPlayersAmount = 16
    var maxRounds = 3
    var spawn: Location? = null
    var lobby: Location? = null
    var locations: MutableMap<String, Location> = HashMap()
    var bases: MutableList<Schematic> = ArrayList()
    var spawns: MutableList<Location> = ArrayList()
    var map: Schematic? = null
    var feast: Schematic? = null
    var feastLocation: Location? = null

    val isSolo: Boolean
        get() = teamSize == 1

    val world: World?
        get() {
            var mundo: World? = Bukkit.getWorld(worldName)
            if (mundo == null) {
                mundo = Mine.loadWorld(worldName)

            }
            return mundo
        }

    constructor() {

        // TODO Auto-generated constructor stub
    }

    override fun copy(): MinigameMap? {
        return copy(this)

    }

    constructor(name: String) {
        this.name = name
        this.displayName = name
    }

    constructor(minigame: Minigame, name: String) : this(name) {
        minigame.maps.add(this)
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


    val hasFeast: Boolean
        get() {
            return feast != null
        }

    val hasLobby: Boolean
        get() {
            return lobby != null
        }

    val hasBases: Boolean
        get() {
            return !this.bases.isEmpty()
        }

    val hasSpawn: Boolean
        get() {
            return spawn != null
        }

    val hasSpawns: Boolean
        get() {
            return !spawns.isEmpty()
        }

    fun hasSchematic(): Boolean {
        return this.map != null
    }

    fun fixWorld(): MinigameMap {
        return world(world!!)
    }

}