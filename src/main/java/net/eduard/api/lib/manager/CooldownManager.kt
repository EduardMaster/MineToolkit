package net.eduard.api.lib.manager

import java.util.UUID
import net.eduard.api.lib.modules.Mine
import org.bukkit.entity.Player

class CooldownManager( var duration: Long = 20) {

    var msgCooldown: String? = null
    var msgOver: String? = null
    var msgStart: String? = null
    init{
        msgCooldown = "§cAinda faltam §7%time segundos §cpara desabilitar o sistema"
    }
    fun noMessages(){
        msgCooldown = null
        msgOver = null
        msgStart = null
    }

    @Transient
    val cooldowns= mutableMapOf<UUID, TimeManager>()



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
        val cd = cooldowns[player.uniqueId]
        cd!!.stopTask()
        cooldowns.remove(player.uniqueId)
    }

    fun onCooldown(player: Player): Boolean {
        return getResult(player) > 0
    }

    fun setOnCooldown(player: Player): CooldownManager {
        if (onCooldown(player)) {
            stopCooldown(player)
        }
        val cd: TimeManager = object : TimeManager() {
            override fun run() {
                sendOverCooldown(player)
            }
        }
        cd.time = duration
        cd.asyncDelay()
        cooldowns[player.uniqueId] = cd
        return this
    }

    fun sendOverCooldown(player: Player) {
        msgOver?:return
        player.sendMessage(msgOver)
    }

    fun sendOnCooldown(player: Player) {
        msgCooldown?:return
        player.sendMessage(msgCooldown!!
            .replace("%time" ,""+ getCooldown(player)))
    }

    fun sendStartCooldown(player: Player) {
        msgStart?:return
        player.sendMessage(msgStart)
    }

    fun getResult(player: Player): Long {
        if (cooldowns.containsKey(player.uniqueId)) {
            val now = Mine.getNow()
            val cd = cooldowns[player.uniqueId]
            val before = cd!!.startedTime
            val cooldown = cd.time * 50
            val calc = before + cooldown
            val result = calc - now
            return if (result <= 0) {
                0
            } else result / 50
        }
        return 0
    }

    fun getCooldown(player: Player): Int {
        return (getResult(player) / 20).toInt() +1
    }
}