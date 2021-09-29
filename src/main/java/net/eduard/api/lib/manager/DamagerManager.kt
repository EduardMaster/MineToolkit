package net.eduard.api.lib.manager

import net.eduard.api.EduardAPI
import net.eduard.api.lib.kotlin.register
import org.bukkit.entity.Entity
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

/**
 * Sistema de verificar quem deu o ultimo Hit no jogador
 * @author Eduard
 */
object DamagerManager : Listener {

    private val lastPvP: MutableMap<Entity, Entity> = HashMap()

    init{
        register(EduardAPI.instance.plugin)
    }

    fun getLastDamager(entity: Entity): Entity? {
        val damager = lastPvP[entity]?:return null
        if (damager is Projectile) {
            if (damager.shooter != null) {
                return damager.shooter as Entity
            }
        }
        return damager
    }




    @EventHandler
    private fun aoIrPvP(e: EntityDamageByEntityEvent) {
        lastPvP[e.entity] = e.damager
    }
}