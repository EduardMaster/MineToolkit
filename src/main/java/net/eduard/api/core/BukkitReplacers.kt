package net.eduard.api.core

import net.eduard.api.lib.kotlin.format
import lib.modules.Extra
import lib.modules.Mine
import lib.modules.VaultAPI
import org.bukkit.Statistic

/**
 * Registrando os replacers mais usados na Displayboard
 *
 * @author Eduard
 */
class BukkitReplacers {
    init {
        if (lib.modules.Mine.hasPlugin("Vault")) {
            lib.modules.Mine.addReplacer("\$player_group") { lib.modules.VaultAPI.getPermission().getPrimaryGroup(it) }
            lib.modules.Mine.addReplacer("\$player_prefix") { lib.modules.Mine.toChatMessage(
                lib.modules.VaultAPI.getChat().getPlayerPrefix(it)) }
            lib.modules.Mine.addReplacer("\$player_suffix") { lib.modules.Mine.toChatMessage(
                lib.modules.Mine.toChatMessage(lib.modules.VaultAPI.getChat().getPlayerPrefix(it))) }
            lib.modules.Mine.addReplacer("\$group_prefix") {
                lib.modules.Mine.toChatMessage(
                        lib.modules.VaultAPI.getChat().getGroupPrefix("null", lib.modules.VaultAPI.getPermission().getPrimaryGroup(it)))
            }
            lib.modules.Mine.addReplacer("\$group_suffix") {
                lib.modules.Mine.toChatMessage(
                        lib.modules.VaultAPI.getChat().getGroupSuffix("null", lib.modules.VaultAPI.getPermission().getPrimaryGroup(it)))
            }
            lib.modules.Mine.addReplacer("\$player_money") {
                if (lib.modules.VaultAPI.hasVault() && lib.modules.VaultAPI.hasEconomy()) {
                    return@addReplacer lib.modules.Extra.MONEY.format(lib.modules.VaultAPI.getEconomy().getBalance(it))
                }
                "0.00"
            }
            lib.modules.Mine.addReplacer("\$player_balance") {
                if (lib.modules.VaultAPI.hasVault() && lib.modules.VaultAPI.hasEconomy()) {
                    return@addReplacer lib.modules.Extra.formatMoney(lib.modules.VaultAPI.getEconomy().getBalance(it))
                }
                "0.00"
            }
        }
        lib.modules.Mine.addReplacer("\$players_online") { lib.modules.Mine.getPlayers().size }
        lib.modules.Mine.addReplacer("\$player_world") { it.world.name }
        lib.modules.Mine.addReplacer("\$player_display_name") { it.displayName }
        lib.modules.Mine.addReplacer("\$player_name") { it.name }
        lib.modules.Mine.addReplacer("\$player_health") { it.health.format() }
        lib.modules.Mine.addReplacer("\$player_max_health") { it.maxHealth.format() }
        lib.modules.Mine.addReplacer("\$player_level") { it.level }
        lib.modules.Mine.addReplacer("\$player_xp") { lib.modules.Extra.MONEY.format(it.totalExperience.toLong()) }
        lib.modules.Mine.addReplacer("\$player_kills") { it.getStatistic(Statistic.PLAYER_KILLS) }
        lib.modules.Mine.addReplacer("\$player_deaths") { it.getStatistic(Statistic.DEATHS) }
        lib.modules.Mine.addReplacer("\$player_kdr") { p ->
            val kill = p.getStatistic(Statistic.PLAYER_KILLS)
            val death = p.getStatistic(Statistic.DEATHS)
            if (kill == 0) return@addReplacer 0
            if (death == 0) return@addReplacer 0
            kill / death
        }
        lib.modules.Mine.addReplacer("\$player_kill/death") { player ->
            val kill = player.getStatistic(Statistic.PLAYER_KILLS)
            val death = player.getStatistic(Statistic.DEATHS)
            if (kill == 0) return@addReplacer 0
            if (death == 0) return@addReplacer 0
            kill / death
        }
        lib.modules.Mine.addReplacer("\$player_x") { it.location.x }
        lib.modules.Mine.addReplacer("\$player_y") { it.location.y }
        lib.modules.Mine.addReplacer("\$player_z") { it.location.z }
    }
}