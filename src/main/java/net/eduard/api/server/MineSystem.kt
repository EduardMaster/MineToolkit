package net.eduard.api.server

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData

interface MineSystem : PluginSystem{

    fun hasMine(name : String) : Boolean
    fun getMine(name: String) : MineModule?
    fun getMine(location: Location) : MineModule?

    interface MineModule{
        fun destroy(player : Player) : Map<MaterialData, Double>
        fun clear(y : Int,player : Player) : Map<MaterialData, Double>
        fun clear(x : Int,z : Int,player : Player) : Map<MaterialData, Double>
        fun regen()
        fun setType(type : MaterialData)
        fun setTypes(vararg type : MaterialData)
        fun autoRegen()

    }
}