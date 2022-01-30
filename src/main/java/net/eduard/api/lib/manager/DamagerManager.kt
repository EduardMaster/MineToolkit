package net.eduard.api.lib.manager

import net.eduard.api.EduardAPI
import net.eduard.api.lib.kotlin.register
import org.bukkit.entity.Entity
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

/**
 * Verificador de quem causou o ultimo Dano no jogador
 * @author Eduard
 */
object DamagerManager : EventsManager() {

    private val lastPvP: MutableMap<Entity, Entity> = HashMap()

    init{
        register(EduardAPI.instance)
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




    @EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
    private fun onDamage(e: EntityDamageByEntityEvent) {
        lastPvP[e.entity] = e.damager
    }
}