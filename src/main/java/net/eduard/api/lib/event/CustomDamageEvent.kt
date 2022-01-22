package net.eduard.api.lib.event

import net.eduard.api.lib.kotlin.percent
import net.eduard.api.lib.kotlin.text
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.event.entity.EntityDamageByEntityEvent

class CustomDamageEvent(
    val attacker: Player?,
    val defenser: Player?,
    val attackerEntity: LivingEntity?,
    val defenserEntity: LivingEntity,
    val damageEvent: EntityDamageByEntityEvent,
    var baseDamage: CustomDamage,
    var baseProtection: CustomDamgeReduction,

    ) : Event() {
    var extraDamage = mutableListOf<CustomDamage>()
    var extraProtection = mutableListOf<CustomDamgeReduction>()
    var damageIgnoringDefense = mutableListOf<CustomDamage>()

    class CustomDamage(
        var name: String,
        val value: Double,
        val percent: Double = 0.0) {
        fun text() = "§c${value.text} §4${percent.percent()}§7%"
    }

    class CustomDamgeReduction(
        var name: String,
        val value: Double,
        val percent: Double = 0.0) {
        fun text() = "§a${value.text} §2${percent.percent()}§7%"
    }

    fun sendDetails(player: Player) {
        player.sendMessage("§7Dano base: " + baseDamage.text())
        player.sendMessage("§7Proteção base: " + baseProtection.text())
        extraDamage.forEach {
            player.sendMessage("§7Dano extra §f${it.name}§7: " + it.text())
        }
        extraProtection.forEach {
            player.sendMessage("§7Proteção extra §f${it.name}§7: " + it.text())
        }
        damageIgnoringDefense.forEach {
            player.sendMessage("§7Dano Real §f${it.name}§7: " + it.text())
        }
    }

    fun calculateFinalDamage(): Double {
        val finalDamage = baseDamage.value + extraDamage.sumOf { it.value }
        val finalPercent = baseDamage.percent + extraDamage.sumOf { it.percent }
        return finalDamage + (finalDamage * finalPercent)
    }

    fun calculateFinalDamageAnulation(): Double {
        return baseProtection.value + extraProtection.sumOf { it.value };
    }

    fun calculateFinalDamageReduction(): Double {
        val finalValue = baseProtection.percent + extraProtection.sumOf { it.percent }
        return if (finalValue > maxDamageReduction) maxDamageReduction else finalValue
    }

    fun calculateResult(): Double {
        val finalDamageReduced = calculateFinalDamage() - calculateFinalDamageAnulation()
        if (finalDamageReduced < 0) return 0.0
        val result = finalDamageReduced - (finalDamageReduced * calculateFinalDamageReduction())
        return result + damageIgnoringDefense.sumOf { it.value }
    }

    companion object {
        val maxDamageReduction = 0.95

        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList {
        return handlerList;
    }
}