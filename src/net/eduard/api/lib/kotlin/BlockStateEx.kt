package net.eduard.api.lib.kotlin

import org.bukkit.CropState
import org.bukkit.Material
import org.bukkit.block.BlockState
import org.bukkit.material.Crops

val BlockState.isCrop: Boolean
    get() {
        return type == Material.CROPS
    }

val BlockState.plantState: CropState?
    get() {
        val type = type
        if (type == Material.CROPS) {
            val crop = data as Crops
            return crop.state

        }

        return null
    }