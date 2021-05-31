package net.eduard.api.server

import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData

interface MineSystem {

    fun hasMine(name : String) : Boolean
    fun getMine(name: String) : MineModule?
    fun getMine(location: Location) : MineModule?

    interface MineModule{
        fun destroy() : Map<ItemStack, Double>
        fun clear(y : Int) : Map<ItemStack, Double>
        fun clear(x : Int,z : Int) : Map<ItemStack, Double>
        fun regen()
        fun setType(type : MaterialData)
        fun autoRegen()

    }
}