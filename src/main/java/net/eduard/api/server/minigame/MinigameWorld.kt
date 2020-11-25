package net.eduard.api.server.minigame

import net.eduard.api.lib.modules.Mine
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

/**
 * Mundo da Sala ou do Mapa
 */
class MinigameWorld( var worldName: String = "minigame-map") {


    fun nameNotSet() = worldName == "minigame-map"

    @Transient
    var world : World? = null
    fun load() : World{
        if (world!= null){
            return world!!
        }
        world = Bukkit.getWorld(worldName)
        if (world == null) {
            world = Mine.loadWorld(worldName)
            world?.setSpawnLocation(0,200,0)
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
        load()
    }
    fun clear(){
        delete()
        load()
    }
    fun copy(sourceWorld : String){
        world = Mine.copyWorld(sourceWorld ,worldName)
    }

}
