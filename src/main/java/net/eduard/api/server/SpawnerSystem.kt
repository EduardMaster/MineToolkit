package net.eduard.api.server

import net.eduard.api.lib.modules.FakePlayer
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

interface SpawnerSystem : PluginSystem{
    fun getMaxAmount(player: FakePlayer, entity: EntityType): Double
    fun getAmount(player: FakePlayer, entity: EntityType): Double
    fun removeSpawner(player: FakePlayer)
    fun hasSpawnerPlaced(player: FakePlayer): Boolean
    fun getSpawnerLocation(player: FakePlayer): Location
    fun giveSpawnerOf(inventory: Inventory, entity: EntityType)
    fun getSpawnerIcon(): ItemStack
    fun getSpawnerOf(entity: EntityType): ItemStack
}