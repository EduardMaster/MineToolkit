package net.eduard.api.server

import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

interface GeneratorSystem {

    fun getGenerator(type: EntityType): ItemStack

    fun getGeneratorType(item: ItemStack): EntityType

    fun getGeneratorStack(location: Location): Int

    fun getGeneratorType(location: Location): EntityType

    fun isGenerator(item: ItemStack): Boolean
}
