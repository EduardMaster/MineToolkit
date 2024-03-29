package net.eduard.api.server

import net.eduard.api.lib.modules.FakePlayer
import org.bukkit.inventory.ItemStack

/**
 * API de Armazenamento e Venda de Drops do Servidor
 */
interface DropSystem : PluginSystem{
    fun setDrop(player : FakePlayer, item : ItemStack , amount : Double)
    fun addDrop(player : FakePlayer, item : ItemStack , amount : Double)
    fun getDropAmount(player : FakePlayer, item : ItemStack) : Double
    fun getDrops(player : FakePlayer) : Map<ItemStack, Double>
    fun getDropUnitPrice(item : ItemStack) : Double
    fun setDropUnitPrice(item : ItemStack, price : Double)
}