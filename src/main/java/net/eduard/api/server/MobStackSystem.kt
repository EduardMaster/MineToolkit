package net.eduard.api.server

import org.bukkit.entity.LivingEntity

interface MobStackSystem : PluginSystem {
    fun stack(mob : LivingEntity, amount : Double)
    fun isStacked(mob : LivingEntity) : Boolean
    fun getStack(mob : LivingEntity) : Double
}