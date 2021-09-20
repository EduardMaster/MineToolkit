package net.eduard.api.server.minigame

import org.bukkit.Location
import org.bukkit.World

import net.eduard.api.lib.modules.Copyable
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.storage.annotations.StorageIndex
import org.bukkit.Bukkit
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
    var chestsMiniFeastLocations = mutableListOf<Location>()
    var chestsFeastsLocations = mutableListOf<Location>()
    var chestsIslandsLocations = mutableListOf<Location>()

    @Transient
    var diferenceApplied = Vector()

    var feastCenter: Location? = null
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
    var islands = mutableMapOf<Int, MinigameIsland>()
    var spawns = mutableListOf<Location>()
    val gameMap: MinigameSchematic?
        get() {
            return if (MinigameSchematic.hasSchematic(mapName()))
                MinigameSchematic.getSchematic(mapName())
            else null
        }

    val allChests get() = gameMap!!.getAllChests(feastCenter!!)
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
        return "worlds/${minigame.name}/map/$name"
    }


    fun copyWorld(map: MinigameMap) {
        worldUsed.copy(map.worldName)
        fixWorld()
    }


    fun insideFeast(location: Location): Boolean {
        val raioDoFeast = (feastRadius * feastRadius)
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
        for (loc in chestsIslandsLocations) {
            loc.world = world
        }
        for (loc in chestsMiniFeastLocations) {
            loc.world = world
        }
        for (loc in chestsFeastsLocations) {
            loc.world = world
        }
        for ((id,island) in islands){
            island.chest1Location?.world = world
            island.chest2Location?.world = world
            island.spawnLocation?.world = world
            island.centerLocation?.world = world
            island.lowLocation?.world = world
            island.highLocation?.world = world
            for (locChest in island.chestsLocation){
                locChest.world = world
            }
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
        feastCenter = relative
        val map = gameMap
        if (map == null){
            debug("Nao foi encontrado o schematic do mapa "+ mapName())
            return
        }
        debug("Local central da colagem: " + relative.toVector())
        debug("Local central do Schematic: " + map.relative)
        val difX = relative.blockX - map.relative.blockX
        val difY = relative.blockY - map.relative.blockY
        val difZ = relative.blockZ - map.relative.blockZ
        val diference = Vector(difX,difY,difZ)
        debug("Diferenca entre Ambos: $diference")

        map.paste(relative, true)

        if (diferenceApplied == Vector()) {
            debug("Aplicando diferencial em todas as locations")
            this.spawn?.add(diference)
            this.lobby?.add(diference)

            for (spawn in spawns) {
                spawn.add(diference)
            }
            for (chest in chestsIslandsLocations) {
                chest.add(diference)
            }
            for (chest in chestsFeastsLocations) {
                chest.add(diference)
            }

            for (chest in chestsMiniFeastLocations) {
                chest.add(diference)
            }
            for ((id,island) in islands){
                island.chest1Location?.add(diference)
                island.chest2Location?.add(diference)
                island.spawnLocation?.add(diference)
                island.centerLocation?.add(diference)
                island.lowLocation?.add(diference)
                island.highLocation?.add(diference)
                for (locChest in island.chestsLocation){
                    locChest.add(diference)
                }
            }
            diferenceApplied = diference
        }else{
            debug("Diferencial ja foi aplicado nas locations")
        }
    }
}