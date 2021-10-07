package net.eduard.api.server.minigame

import net.eduard.api.lib.modules.Mine
import org.bukkit.Bukkit
import org.bukkit.World

/**
 * Mundo da Sala ou do Mapa
 */
class MinigameWorld( var worldName: String ) {

    constructor() : this("minigame-map")

    fun nameNotSet() = worldName == "minigame-map"

    @Transient
    var world : World? = null
    fun loadOrGet() : World{
        if (world!= null){
            if (world!!.name.equals(worldName, true)) {
                return world!!
            }
        }
        world = Bukkit.getWorld(worldName)
        if (world == null) {
            world = Mine.loadWorld(worldName)

        }
        if (world != null && world!!.spawnLocation == null) {
            world!!.setSpawnLocation(0, 200, 0)
        }
        notSave()
        return world!!
    }


   fun save(){
       world?.save()
   }

    fun notSave(){
        world?.isAutoSave = false
    }

    fun unload(){
        Mine.unloadWorld(worldName, false)
        world = null
    }
    fun delete(){
        Mine.deleteWorld(worldName)
        world = null
    }
    fun reset(){
        unload()
        loadOrGet()
    }
    fun clear(){
        delete()
        loadOrGet()
    }
    fun copy(sourceWorld : String){
        world = Mine.copyWorld(sourceWorld ,worldName)
    }

}
