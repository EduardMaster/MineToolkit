@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package net.eduard.api.lib.kotlin

import net.eduard.api.lib.game.EnchantGlow
import net.eduard.api.lib.hybrid.PlayerUser
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.FakePlayer
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.modules.MineReflect
import org.bukkit.*

import org.bukkit.block.BlockState
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.material.Crops
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin


inline val ItemStack.extra get() = MineReflect.getData(this)


fun ItemStack.extra(setup: MineReflect.ItemExtraData.(ItemStack) -> Unit): ItemStack {
    val extra = MineReflect.getData(this)
    setup(extra, this)
    return MineReflect.setData(this, extra).apply {
        mineLore = if (extra.customStack > 0.0) {
            val currentLore = mineLore
            currentLore.removeIf { it.startsWith(MineReflect.MSG_ITEM_STACK) }
            currentLore.add(
                MineReflect.MSG_ITEM_STACK
                    .replace("%stack", Extra.formatMoney(extra.customStack))
            )
            currentLore
        } else {
            val currentLore = mineLore
            currentLore.removeIf { it.startsWith(MineReflect.MSG_ITEM_STACK) }
            currentLore
        }
    }
}


/**
 * Atalho para Mine.removeXP
 */
fun Player.mineRemoveXP(amount: Double) {
    Mine.removeXP(this, amount)
}

/**
 * Atalho para Mine.addHotBar
 */
fun Player.mineSetHotBar(item: ItemStack) {
    Mine.setHotBar(this, item)
}

/**
 * Atalho para Mine.addHotBar
 */
fun Player.addHotBar(item: ItemStack) {
    for (i in 0..9) {
        inventory.setItem(i, item)
    }
}

/**
 * Traduz os simbolos & para § <br>
 * Alias para ChatColor.translateAlternateColorCodes
 */
fun String.colored(): String {
    return ChatColor.translateAlternateColorCodes('&', this)
}

/**
 * Atalho para Mine.changeTabName
 */
fun Player.mineChangeTabName(tabName: String) {
    Mine.setTabName(this, tabName)
}

/**
 * Atalho para Mine.changeTabName
 */
fun Player.changeTabName(tabName: String) {
    player.playerListName = Extra.cutText(tabName, 32)
}

/**
 * Atalho para Mine.clearHotBar
 */
fun Player.mineClearHotBar() {
    Mine.clearHotBar(this)
}

/**
 * Atalho para Mine.clearHotBar
 */
fun Player.clearHotBar() {
    for (slot in 0..9) {
        player.inventory.setItem(slot, null)
    }
}

/**
 * Atalho para Mine.clearArmors
 */
fun LivingEntity.mineClearArmors() {
    Mine.clearArmours(this)
}

/**
 * Atalho para Mine.clearArmors
 */
fun LivingEntity.clearArmors() {
    equipment.armorContents = arrayOf()
}

/**
 * Atalho para Mine.clearInventory
 */
fun Player.mineClearInventory() {
    Mine.clearInventory(this)
}

/**
 *  Limpa o inventario
 */
fun Player.clearInventory() {
    inventory.clear()
    inventory.armorContents = arrayOf()
}


/**
 * Atalho para MineReflect.sendTitle
 */
@Deprecated("Aliases alterada", ReplaceWith("mineSendTitle(title, subTitle, 20, 20, 20)"))
fun Player.sendTitle(title: String, subTitle: String) = mineSendTitle(title, subTitle, 20, 20, 20)


fun Player.sendTitle(title: String, subTitle: String, fadeInt: Int, stay: Int, fadeOut: Int) {
    return mineSendTitle(title, subTitle, fadeInt, stay, fadeOut);
}


/**
 * Atalho para MineReflect.sendTitle
 */
fun Player.mineSendTitle(title: String, subTitle: String, fadeInt: Int, stay: Int, fadeOut: Int) {
    MineReflect.sendTitle(this, title, subTitle, fadeInt, stay, fadeOut)
}

/**
 * Atalho para MineReflect.sendActionBar
 */
@Deprecated("Aliases alterada", ReplaceWith("mineSendActionBar(msg)"))
fun Player.sendActionBar(msg: String) = mineSendActionBar(msg)

/**
 * Atalho para MineReflect.sendActionBar
 */
fun Player.mineSendActionBar(msg: String) = MineReflect.sendActionBar(this, msg)


@Deprecated("Aliases alterada", ReplaceWith("mineSendPacket(packet)"))
fun Player.sendPacket(packet: Any) = mineSendPacket(packet)

/**
 * Atalho para MineReflect.sendPacket
 */
fun Player.mineSendPacket(packet: Any) = MineReflect.sendPacket(this, packet)

/**
 * Atalho para Mine.callEvent
 */
fun Event.mineCallEvent() {
    return Mine.callEvent(this)
}

@Deprecated("Aliases alterada", ReplaceWith("mineCallEvent()"))
fun Event.call() = mineCallEvent()


inline val FakePlayer.offline get() = PlayerUser(name, id)
inline val PlayerUser.fake get() = FakePlayer(name, uniqueId)
inline val Player.fake get() = FakePlayer(this)
inline val Player.offline get() = PlayerUser(this.name, this.uniqueId)


val <T> Class<T>.autoPlugin: JavaPlugin
    get() {
        if (!JavaPlugin::class.java.isAssignableFrom(this)) {
            return JavaPlugin.getProvidingPlugin(this)
        }
        return JavaPlugin.getPlugin(this as Class<out JavaPlugin>) as JavaPlugin
    }

fun Inventory.setItem(line: Int, column: Int, item: ItemStack?) =
    this.setItem(Extra.getIndex(column, line), item)

val BlockState.isCrop get() = type == Material.CROPS


val BlockState.plantState: CropState?
    get() = if (type == Material.CROPS) (this as Crops).state
    else null


val InventoryClickEvent.player get() = this.whoClicked as Player

val InventoryOpenEvent.opener get() = this.player as Player


/**
 * Cria um menu com DSL, parametros, nome, linhas, e DSL Block em seguida
 */
inline fun Player.openInventory(name: String, lineAmount: Int, block: Inventory.() -> Unit): Inventory {
    val inventory = Bukkit.createInventory(this, 9 * lineAmount, name.cut(32))
    block(inventory)
    player.openInventory(inventory)
    return inventory

}


/**
 * Cria um item para o menu com DSQL< parametros posicao, e SQL Block
 */
inline fun Inventory.addItem(position: Int, block: ItemStack.() -> Unit): ItemStack {
    val item = ItemStack(Material.STONE)
    block(item)
    setItem(position, item)
    return item

}

val enchantmentsPrefix = "§k§f"
val enchantmentsNames = mutableMapOf(
    Enchantment.DAMAGE_ALL to "Afiada",
    Enchantment.ARROW_DAMAGE to "Força",
    Enchantment.ARROW_FIRE to "Chama",
    Enchantment.ARROW_INFINITE to "Flecha Infinita",
    Enchantment.ARROW_KNOCKBACK to "Flecha Repulsiva",
    Enchantment.SILK_TOUCH to "Toque Suave",
    Enchantment.LUCK to "Sorte em Pescaria",
    Enchantment.DAMAGE_UNDEAD to "Dano contra Mortos-vivos",
    Enchantment.DAMAGE_ARTHROPODS to "Dano contra Aranhas",
    Enchantment.DIG_SPEED to "Eficiência",
    Enchantment.DURABILITY to "Durabilidade",
    Enchantment.OXYGEN to "Respiração",
    Enchantment.THORNS to "Espinhos",
    Enchantment.LOOT_BONUS_BLOCKS to "Fortuna",
    Enchantment.LOOT_BONUS_MOBS to "Pilhagem",
    Enchantment.FIRE_ASPECT to "Aspecto Flamejante",
    Enchantment.DEPTH_STRIDER to "Passos Profundos",
    Enchantment.KNOCKBACK to "Repulsão",
    Enchantment.LURE to "Pescaria Eficiênte",
    Enchantment.WATER_WORKER to "Eficiência de baixo d'agua",
    Enchantment.PROTECTION_ENVIRONMENTAL to "Proteção",
    Enchantment.PROTECTION_EXPLOSIONS to "Proteção contra Explosões",
    Enchantment.PROTECTION_FALL to "Proteção contra Queda",
    Enchantment.PROTECTION_FIRE to "Proteção contra Fogo",
    Enchantment.PROTECTION_PROJECTILE to "Proteção contra Projéteis"
)

val Enchantment.nameBR get() = enchantmentsNames.getOrDefault(this, name)


fun <T : ItemStack> T.displayEnchants(): T {
    val meta = itemMeta
    val lore = meta.lore ?: mutableListOf<String>()

    val it = lore.iterator()
    while (it.hasNext()) {
        val line = it.next()
        if (line.startsWith(enchantmentsPrefix)) {
            it.remove()
        }
    }
    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)

    for ((enchant, level) in enchantments) {
        if (level == 0) continue
        if (enchant == EnchantGlow.getGlow()) continue
        val name = enchant.nameBR
        lore.add("$enchantmentsPrefix§7$name §3$level")
    }
    meta.lore = lore
    itemMeta = meta

    return this

}


@Deprecated("Aliases Alterada", ReplaceWith("mineName"))
inline var ItemStack.name: String
    get() = mineName
    set(value) {
        mineName = value
    }

var ItemStack.mineName: String
    get() {
        return Mine.getName(this)
    }
    set(value) {
        Mine.setName(this, value)
    }


@Deprecated("Aliases Alterada", ReplaceWith("mineLore"))
inline var ItemStack.lore: MutableList<String>
    get() = mineLore
    set(value) {
        mineLore = value
    }

var ItemStack.mineLore: MutableList<String>
    get() {
        return Mine.getLore(this)
    }
    set(value) {
        Mine.setLore(this, value)
    }

@Deprecated("Aliases Alterada", ReplaceWith("mineAddLore(*lore)"))
fun <T : ItemStack> T.addLore(vararg lore: String) = mineAddLore(*lore)

fun <T : ItemStack> T.mineAddLore(vararg lore: String): T {
    Mine.addLore(this, *lore)
    return this
}

@Deprecated("Aliases Alterada", ReplaceWith("mineSetLore(*lore)"))
fun <T : ItemStack> T.lore(vararg lore: String?) = mineSetLore(*lore)
fun <T : ItemStack> T.mineSetLore(vararg lore: String?): T {
    Mine.setLore(this, *lore)
    return this
}

fun CommandSender.isPlayer(action: Player.() -> Unit) {
    if (this is Player) {
        action.invoke(this)
    }
}

fun <T : ItemStack> T.addEnchant(ench: Enchantment?, level: Int): T {
    addUnsafeEnchantment(ench, level)
    return this
}

fun <T : ItemStack> T.mineSetColor(color: Color): T {
    Mine.setColor(this, color)
    return this
}


object BukkitAlterations : Listener

fun <T : Event> Class<T>.event(plugin : Plugin , actionToDo: T.() -> Unit) {
    val eventClass = this
    Bukkit.getPluginManager()
        .registerEvent(
            eventClass,
            BukkitAlterations, EventPriority.NORMAL, { _, event ->
                if (eventClass.isAssignableFrom(event.javaClass))
                    actionToDo(event as T)
            }, plugin
        )
}

inline fun <reified T : Event> Any.event(noinline actionToDo: T.() -> Unit) {
    T::class.java.event(this.javaClass.autoPlugin, actionToDo)
}





