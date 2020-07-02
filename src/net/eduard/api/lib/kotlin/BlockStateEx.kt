package net.eduard.api.lib.kotlin

import org.bukkit.CropState
import org.bukkit.Material
import org.bukkit.block.BlockState
import org.bukkit.inventory.ItemStack
import org.bukkit.material.Crops

val BlockState.isCrop get() = type == Material.CROPS


val BlockState.plantState: CropState?
    get() = if (type == Material.CROPS) (this as Crops).state
    else null



