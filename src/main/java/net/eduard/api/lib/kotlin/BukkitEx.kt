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
import org.bukkit.command.CommandExecutor
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
import kotlin.reflect.KClass


inline val ItemStack.extra get() = MineReflect.getData(this)


fun ItemStack.extra(setup: MineReflect.ItemExtraData.(ItemStack) -> Unit): ItemStack {
    val extra = MineReflect.getData(this)
    setup(extra, this)
    return MineReflect.setData(this, extra).apply {
        lore = if (extra.customStack > 0.0) {
            val currentLore = lore
            currentLore.removeIf { it.startsWith(MineReflect.MSG_ITEM_STACK) }
            currentLore.add(
                MineReflect.MSG_ITEM_STACK
                    .replace("%stack", Extra.formatMoney(extra.customStack))
            )
            currentLore
        } else {
            val currentLore = lore
            currentLore.removeIf { it.startsWith(MineReflect.MSG_ITEM_STACK) }
            currentLore
        }
    };
}


/**
 * Atalho para Mine.removeXP
 */
inline fun Player.removeXP(amount: Double) {
    Mine.removeXP(this, amount)
}

/**
 * Atalho para Mine.addHotBar
 */
inline fun Player.addHotBar(item: ItemStack) {
    Mine.setHotBar(this, item)
}

/**
 * Traduz os simbolos & para § <br>
 * Alias para ChatColor.translateAlternateColorCodes
 */
inline fun String.colored(): String {
    return ChatColor.translateAlternateColorCodes('&', this)
}

/**
 * Atalho para Mine.changeTabName
 */
inline fun Player.changeTabName(tabName: String) {
    Mine.changeTabName(this, tabName)
}

/**
 * Atalho para Mine.clearHotBar
 */
inline fun Player.clearHotBar() {
    Mine.clearHotBar(this)

}

/**
 * Atalho para Mine.clearArmors
 */
inline fun LivingEntity.clearArmors() {
    Mine.clearArmours(this)
}

/**
 * Atalho para Mine.clearInventory
 */
inline fun Player.clearInventory() {
    Mine.clearInventory(this)
}

/**
 * Atalho para MineReflect.sendTitle
 */
inline fun Player.sendTitle(title: String, subTitle: String) {
    MineReflect.sendTitle(this, title, subTitle, 20, 20, 20)
}

/**
 * Atalho para MineReflect.sendTitle
 */
inline fun Player.sendTitle(title: String, subTitle: String, fadeInt: Int, stay: Int, fadeOut: Int) {
    MineReflect.sendTitle(this, title, subTitle, fadeInt, stay, fadeOut)
}

/**
 * Atalho para MineReflect.sendActionBar
 */
inline fun Player.sendActionBar(msg: String) {
    MineReflect.sendActionBar(this, msg)
}

/**
 * Atalho para MineReflect.sendPacket
 */
inline fun Player.sendPacket(packet: Any) {
    MineReflect.sendPacket(this, packet)
}

/**
 * Atalho para Mine.callEvent
 */
inline fun Event.call() {
    return Mine.callEvent(this)
}

inline val FakePlayer.offline get() = PlayerUser(name, id)


inline val Player.fake: FakePlayer
    get() = FakePlayer(this)

/**
 * Expecifique se quem fez o comando é um player
 */
inline fun CommandSender.isPlayer(block: Player.() -> Unit) {
    if (Mine.onlyPlayer(this)) {
        block(this as Player)
    }
}

inline fun Listener.register(plugin: Plugin) = Bukkit.getPluginManager().registerEvents(this, plugin)

inline fun CommandExecutor.register(cmd: String, plugin: JavaPlugin) {
    plugin.getCommand(cmd).executor = this
}

inline val Player.offline get() = PlayerUser(this.name, this.uniqueId)


val <T> Class<T>.plugin: JavaPlugin
    get() {
        if (!JavaPlugin::class.java.isAssignableFrom(this)) {
            return JavaPlugin.getProvidingPlugin(this)
        }
        return JavaPlugin.getPlugin(this as Class<out JavaPlugin>) as JavaPlugin
    }

inline fun Inventory.setItem(line: Int, column: Int, item: ItemStack?) =
    this.setItem(Extra.getIndex(column, line), item)

inline val BlockState.isCrop get() = type == Material.CROPS


inline val BlockState.plantState: CropState?
    get() = if (type == Material.CROPS) (this as Crops).state
    else null


inline val InventoryClickEvent.player get() = this.whoClicked as Player

inline val InventoryOpenEvent.opener get() = this.player as Player


/**
 * Cria um menu com DSL, parametros, nome, linhas, e DSL Block em seguida
 */
inline fun Player.inventory(name: String, lineAmount: Int, block: Inventory.() -> Unit): Inventory {

    val inventory = Bukkit.createInventory(this, 9 * lineAmount, name.cut(32))
    block(inventory)
    player.openInventory(inventory)
    return inventory

}


/**
 * Cria um item para o menu com DSQL< parametros posicao, e SQL Block
 */
inline fun Inventory.item(position: Int, block: ItemStack.() -> Unit): ItemStack {
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

inline val Enchantment.nameBR get() = enchantmentsNames.getOrDefault(this, name)


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


inline var ItemStack.name: String
    get() {
        return Mine.getName(this)
    }
    set(value) {
        Mine.setName(this, value)

    }

inline var ItemStack.lore: MutableList<String>
    get() {
        return Mine.getLore(this)
    }
    set(value) {
        Mine.setLore(this, value)
    }


inline operator fun ItemStack.minus(enchament: Enchantment?): ItemStack {
    removeEnchantment(enchament)
    return this
}

inline operator fun ItemStack.plus(amount: Int): ItemStack {
    this.amount += amount
    return this
}

inline operator fun ItemStack.plus(map: Map<Enchantment, Int>): ItemStack {
    addUnsafeEnchantments(map)
    return this
}

inline infix fun Enchantment.level(level: Int): Map<Enchantment, Int> {
    return mapOf(this to level)
}

inline fun <T : ItemStack> T.id(id: Int): T {
    typeId = id
    return this
}


inline fun <T : ItemStack> T.addLore(vararg lore: String): T {
    Mine.addLore(this, *lore)
    return this
}

inline fun <T : ItemStack> T.lore(vararg lore: String?): T {
    Mine.setLore(this, *lore)
    return this
}

inline fun <T : ItemStack> T.addEnchant(ench: Enchantment?, level: Int): T {
    addUnsafeEnchantment(ench, level)
    return this
}

inline fun <T : ItemStack> T.color(color: Color): T {
    Mine.setColor(this, color)
    return this
}


object BukkitAlterations : Listener

fun <T : Event> KClass<T>.event(actionToDo: T.() -> Unit) {
    Bukkit.getPluginManager()
        .registerEvent(
            this.java,
            BukkitAlterations, EventPriority.NORMAL, { _, event ->
                if (this.java.isAssignableFrom(event.javaClass))
                    actionToDo(event as T)
            }, BukkitAlterations.javaClass.plugin
        )
}


inline fun <reified T : Event> event(noinline actionToDo: T.() -> Unit) {
    T::class.event(actionToDo)
}




