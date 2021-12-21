package net.eduard.api.server

import org.bukkit.entity.LivingEntity

interface MobStackAPI : EduardPluginsAPI {
    fun stack(mob : LivingEntity, amount : Double)
    fun isStacked(mob : LivingEntity) : Boolean
    fun getStack(mob : LivingEntity) : Double
}