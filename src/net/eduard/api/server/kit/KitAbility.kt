package net.eduard.api.server.kit

import java.util.ArrayList
import java.util.HashMap

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack

import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.click.PlayerInteract
import net.eduard.api.lib.game.Explosion
import net.eduard.api.lib.game.Jump
import net.eduard.api.lib.manager.CooldownManager
import net.eduard.api.lib.manager.EffectManager
import net.eduard.api.lib.manager.EventsManager
import net.eduard.api.lib.modules.BukkitTimeHandler
import net.eduard.api.lib.storage.Storable
import org.bukkit.plugin.Plugin

open class KitAbility : EventsManager(), BukkitTimeHandler {

    @Transient
    var click: PlayerInteract? = null

    @Transient
    val timesUsed = HashMap<Player, Int>()
    var name = "Kit"
    var price = 0.0
    var isShowOnGui = true
    var isEnabled = true
    var activeCooldownOnPvP = false
    var useLimit = 1
    val lore = ArrayList<String>()
    var itemType = Material.DIAMOND_AXE
    var itemData = 0
    var effects = EffectManager()
    var jump: Jump? = null
    var explosion: Explosion? = null
    var cooldown = CooldownManager()
    val permission get() = effects.requirePermission


    val potions
        get() = effects.potionsToApply

    var time: Int
        set(value) {
            cooldown.duration = value * 20.toLong()
        }
        get() = cooldown.duration.toInt()


    var sound
        get() = effects.soundToPlay
        set(value) {
            effects.soundToPlay = value
        }


    var display
        get() = effects.visualEffectToShow
        set(value) {
            effects.visualEffectToShow = value
        }


    val items
        get() = effects.itemsToGive


    var message
        get() = effects.messageToSend
        set(value) {
            effects.messageToSend = value
        }

    val icon: ItemStack
        get() {

            val item = ItemStack(itemType, 1)
            val meta = item.itemMeta
            meta.displayName = "§e$name"
            val list = mutableListOf<String>()
            for (line in lore) {
                list.add("§f$line")
            }
            meta.lore = list
            item.itemMeta = meta


            return item
        }

    @Storable.StorageAttributes(reference = true)
    var kits: List<KitAbility> = ArrayList()

    @Transient
    var players: List<Player> = ArrayList()

    init {

        if (name.isEmpty() || name == "Kit")
            name = (javaClass.simpleName)


        effects.requirePermission = name.toLowerCase().replace(" ", "")
    }

    fun add(item: ItemStack) {
        items.add(Mine.setName(item, "§b" + name))

    }

    fun add(type: Material) {
        items.add(Mine.newItem(type, "§b$name"))

    }


    fun onCooldown(player: Player): Boolean {
        return cooldown.onCooldown(player)
    }

    fun setOnCooldown(player: Player) {
        cooldown.setOnCooldown(player)
    }

    fun cooldown(player: Player): Boolean {
        if (!isEnabled) {
            Mine.send(player, "")
            return false
        }
        if (cooldown.onCooldown(player)) {
            cooldown.sendOnCooldown(player)
            return false
        }
        var x = 0
        if (timesUsed.containsKey(player)) {
            x = timesUsed.getOrDefault(player, 0)
        }
        x++
        if (x == useLimit) {
            cooldown.setOnCooldown(player)
            cooldown.sendStartCooldown(player)
            timesUsed.remove(player)
        } else {
            timesUsed[player] = x
        }
        return true
    }

    @EventHandler
    open fun event(e: EntityDamageByEntityEvent) {
        if (activeCooldownOnPvP) {
            if (e.entity is Player) {
                val p = e.entity as Player
                if (hasKit(p)) {
                    cooldown.setOnCooldown(p)
                }
            }
        }
    }


    fun hasKit(player: Player): Boolean {
        return players.contains(player)
    }


    fun setIcon(material: Material, vararg lore: String) {
        setIcon(material, 0, *lore)
    }

    fun setIcon(material: Material, data: Int, vararg lore: String) {
        itemType = material
        itemData = data
        this.lore.addAll(lore)
    }

    override fun getPluginConnected(): Plugin {
        return plugin
    }


}
