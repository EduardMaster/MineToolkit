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
import net.eduard.api.lib.task.CooldownManager
import net.eduard.api.lib.modules.Copyable.*
import net.eduard.api.lib.storage.Storable

open class KitAbility : CooldownManager() {

    @NotCopyable
    @Transient
    var click: PlayerInteract? = null
    @NotCopyable
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


    protected val potions
        get() = APPLY_POTIONS

    protected fun setTime(n : Int){

        super.time = n.toLong()
    }


    protected var jump
        get() = PLAY_JUMP
        set(value){
            PLAY_JUMP = value
        }

    protected var sound
        get() = PLAY_SOUND
        set(value){
            PLAY_SOUND = value
        }


    protected var explosion
        get() = MAKE_EXPLOSION
        set(value) {
            MAKE_EXPLOSION = value
        }
    protected var display
        get() = PLAY_VISUAL
        set(value) {
            PLAY_VISUAL = value
        }


    protected val items
        get() = ITEMS_TO_GIVE
    protected var message
        get() = SEND_MESSAGE
        set(value) {
            SEND_MESSAGE = value
        }

    fun getIcon(): ItemStack {

        var item = ItemStack(itemType, 1)
        var meta = item.itemMeta
        meta.displayName = "§6Kit: §e$name"
        meta.lore = lore
        item.itemMeta = meta


        return item
    }

    @Storable.StorageAttributes(reference = true)
    var kits: List<KitAbility> = ArrayList()
    @Transient
    var players: List<Player> = ArrayList()
        private set

    init {

        if (name.isEmpty())
            name = (javaClass.simpleName)


        REQUIRE_PERMISSION = name.toLowerCase().replace(" ", "")
    }

    fun add(item: ItemStack) {
        ITEMS_TO_GIVE.add(Mine.setName(item, "§b" + name!!))

    }

    fun add(type: Material) {
        ITEMS_TO_GIVE.add(Mine.newItem("§b" + name, type))

    }

    override fun cooldown(player: Player): Boolean {
        if (!isEnabled) {
            Mine.send(player, "")
            return false
        }
        if (onCooldown(player)) {
            sendOnCooldown(player)
            return false
        }
        var x = 0
        if (timesUsed.containsKey(player)) {
            x = timesUsed.getOrDefault(player, 0)
        }
        x++
        if (x == useLimit) {
            setOnCooldown(player)
            sendStartCooldown(player)
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
                    setOnCooldown(p)
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


}
