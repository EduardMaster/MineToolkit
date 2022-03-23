package net.eduard.api.lib.manager

import net.eduard.api.EduardAPI
import org.bukkit.entity.Entity
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerQuitEvent

/**
 * Verificador de quem causou o ultimo Dano no jogador
 * @author Eduard
 */
object DamagerManager : EventsManager() {

    private val lastPvP: MutableMap<Entity, Entity> = mutableMapOf()
    private val lastHitTaken = mutableMapOf<Entity, Long>()

    init{
        register(EduardAPI.instance)
    }
    fun getLastDamageMoment(entity: Entity) : Long{
        return lastHitTaken.getOrElse(entity){0}
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
        lastHitTaken[e.entity] = System.currentTimeMillis()
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private fun onQuit(e: PlayerQuitEvent) {
        lastPvP.remove(e.player)
        lastHitTaken.remove(e.player)
    }
}