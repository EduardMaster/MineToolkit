package net.eduard.api.lib.manager

import java.util.UUID
import org.bukkit.entity.Player

class CooldownManager(var duration: Long = 20) {

    var messageOnCooldown: String = ""
    var messageCooldownOver: String = ""
    var messageCooldownStart: String = ""

    init {
        messageOnCooldown = "§cAinda faltam §7%time segundos §cpara desabilitar o sistema"
    }

    fun noMessages() {
        messageOnCooldown = ""
        messageCooldownOver = ""
        messageCooldownStart = ""
    }

    @Transient
    val cooldowns = mutableMapOf<UUID, TimeManager>()

    fun cooldown(player: Player): Boolean {
        if (onCooldown(player)) {
            sendOnCooldown(player)
            return false
        }
        setOnCooldown(player)
        sendStartCooldown(player)
        return true
    }

    fun stopCooldown(player: Player) {
        val timeManager = cooldowns[player.uniqueId]
        timeManager?.stopTask()
        cooldowns.remove(player.uniqueId)
    }

    fun onCooldown(player: Player): Boolean {
        return getResult(player) > 0
    }

    inner class CooldownOverTask(val player: Player) : TimeManager(duration) {
        override fun run() {
            sendOverCooldown(player)
        }
        init {
            asyncDelay()
        }
    }

    fun setOnCooldown(player: Player): CooldownManager {
        if (onCooldown(player)) {
            stopCooldown(player)
        }
        cooldowns[player.uniqueId] = CooldownOverTask(player)
        return this
    }

    fun sendOverCooldown(player: Player) {
        if (messageCooldownOver.isNotEmpty())
            player.sendMessage(messageCooldownOver)
    }

    fun sendOnCooldown(player: Player) {
        if (messageOnCooldown.isNotEmpty())
            player.sendMessage(
                messageOnCooldown
                    .replace("%time", "" + getCooldown(player))
            )
    }

    fun sendStartCooldown(player: Player) {
        if (messageCooldownStart.isNotEmpty())
            player.sendMessage(messageCooldownStart)
    }

    fun getResult(player: Player): Long {
        if (cooldowns.containsKey(player.uniqueId)) {
            val now = System.currentTimeMillis()
            val timeManager = cooldowns[player.uniqueId]!!
            val before = timeManager.taskStart
            val cooldownDuration = timeManager.taskDuration * 50
            val endOfCooldown = before + cooldownDuration
            val durationLeft = endOfCooldown - now
            return if (durationLeft <= 0) {
                0
            } else durationLeft / 50
        }
        return 0
    }

    fun getCooldown(player: Player): Int {
        return (getResult(player) / 20).toInt() + 1
    }
}