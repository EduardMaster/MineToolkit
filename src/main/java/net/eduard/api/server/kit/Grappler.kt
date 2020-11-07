package net.eduard.api.server.kit

import net.eduard.api.lib.modules.Mine
import java.util.HashMap

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.server.PluginDisableEvent


class Grappler : KitAbility() {
    @EventHandler
    fun event(event: PlayerInteractEvent) {
        val player = event.player
        if (event.material == Material.LEASH) {
            if (hasKit(player)) {
                if (!onCooldown(player)) {
                    if (event.action == Action.LEFT_CLICK_AIR) {
                        if (hooks.containsKey(player)) {
                            hooks[player]!!.remove()
                            hooks.remove(player)
                        }
                        hooks[player] = GrapplerHook(player, 1.5)
                    } else if (event.action == Action.RIGHT_CLICK_AIR) {
                        if (hooks.containsKey(player)) {
                            val hook = hooks[player]
                            if (hook!!.isHooked) {
                                player.fallDistance = -10f
                                //								Location target = hook.getBukkitEntity()
                                //										.getLocation();
                                //								Vector velocity = GrapplerHook.moveTo(
                                //										player.getLocation(), target, 0.5, 1.5,
                                //										0.5, 0.04, 0.06, 0.04);
                                //								player.setVelocity(velocity);
                            } else {
                                player.sendMessage(
                                        "§6O gancho nao se prendeu em nada!")
                            }
                        }
                    }
                } else {
                    player.sendMessage(
                            "§6Voce esta em PvP e n§o pode usar o Kit!")
                }
            }
        }
    }

    init {
        setIcon(Material.LEASH, "§fSe mova mais rapido")
        add(Material.LEASH)
        activeCooldownOnPvP = true
        time=(5)
        price = 50 * 1000.0
    }

    @EventHandler
    fun event(e: PlayerItemHeldEvent) {
        val p = e.player
        if (hooks.containsKey(p)) {
            hooks[p]!!.remove()
            hooks.remove(p)
        }
    }

    @EventHandler
    fun event(e: PluginDisableEvent) {
        if (plugin == e.plugin)
            for (p in Mine.getPlayers()) {
                if (hooks.containsKey(p)) {
                    hooks[p]!!.remove()
                    hooks.remove(p)
                }
            }

    }

    companion object {
        val hooks = HashMap<Player, GrapplerHook>()
    }

}
