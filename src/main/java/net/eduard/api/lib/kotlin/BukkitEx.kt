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
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.material.Crops
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import kotlin.reflect.KClass

/**
 * Atalho para Mine.removeXP
 */
fun Player.removeXP(amount: Double) {
    Mine.removeXP(this, amount)
}

/**
 * Atalho para Mine.addHotBar
 */
fun Player.addHotBar(item: ItemStack) {
    Mine.setHotBar(this, item)
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
fun Player.changeTabName(tabName: String) {
    Mine.changeTabName(this, tabName)
}

/**
 * Atalho para Mine.clearHotBar
 */
fun Player.clearHotBar() {
    Mine.clearHotBar(this)

}

/**
 * Atalho para Mine.clearArmors
 */
fun LivingEntity.clearArmors() {
    Mine.clearArmours(this)
}

/**
 * Atalho para Mine.clearInventory
 */
fun Player.clearInventory() {
    Mine.clearInventory(this)
}

/**
 * Atalho para MineReflect.sendTitle
 */
fun Player.sendTitle(title: String, subTitle: String) {
    MineReflect.sendTitle(this, title, subTitle, 20, 20, 20)
}

/**
 * Atalho para MineReflect.sendTitle
 */
fun Player.sendTitle(title: String, subTitle: String, fadeInt: Int, stay: Int, fadeOut: Int) {
    MineReflect.sendTitle(this, title, subTitle, fadeInt, stay, fadeOut)
}

/**
 * Atalho para MineReflect.sendActionBar
 */
fun Player.sendActionBar(msg: String) {
    MineReflect.sendActionBar(this, msg)
}

/**
 * Atalho para MineReflect.sendPacket
 */
fun Player.sendPacket(packet: Any) {
    MineReflect.sendPacket(this, packet)
}

/**
 * Atalho para Mine.callEvent
 */
fun Event.call() {
    return Mine.callEvent(this)
}

inline val FakePlayer.offline: PlayerUser
    get() {
        return PlayerUser(name, id)
    }

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

fun Listener.register(plugin: Plugin) = Bukkit.getPluginManager().registerEvents(this, plugin)

fun CommandExecutor.register(cmd: String, plugin: JavaPlugin) {
    plugin.getCommand(cmd).executor = this
}

val Player.offline get() = PlayerUser(this.name, this.uniqueId)


val <T> Class<T>.plugin: JavaPlugin
    get() {
        if (!JavaPlugin::class.java.isAssignableFrom(this)) {
            return JavaPlugin.getProvidingPlugin(this)
        }
        return JavaPlugin.getPlugin(this as Class<out JavaPlugin>) as JavaPlugin
    }

fun Inventory.setItem(line: Int, column: Int, item: ItemStack?) = this.setItem(Extra.getIndex(column, line), item)

val BlockState.isCrop get() = type == Material.CROPS


val BlockState.plantState: CropState?
    get() = if (type == Material.CROPS) (this as Crops).state
    else null


inline val InventoryClickEvent.player get() = this.whoClicked as Player

inline val InventoryOpenEvent.player get() = this.player as Player


/**
 * Cria um menu com DSL, parametros, nome, linhas, e DSL Block em seguida
 */
inline fun Player.inventory(name: String, lineAmount: Int, block: Inventory.() -> Unit): Inventory {

    val inventory = Bukkit.createInventory(this, 9 * lineAmount, name.cut(32))

    block(inventory)
    player.openInventory(inventory)

    return inventory

}



fun <T : ItemStack> T.potion(effect : PotionEffect): T {
    if (type!= Material.POTION)
        type = Material.POTION
    val meta = itemMeta as PotionMeta
    meta.setMainEffect(effect.type)
    meta.addCustomEffect(effect,true)
    itemMeta = meta
    return this
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

val Enchantment.nameBR get() = enchantmentsNames[this] ?: name


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


var ItemStack.name: String
    get() {
        return itemMeta.displayName ?: ""
    }
    set(value) {
        val meta = itemMeta
        meta.displayName = value
        this.itemMeta = meta

    }

var ItemStack.lore: MutableList<String>
    get() {
        return itemMeta.lore ?: mutableListOf()
    }
    set(value) {
        val meta = itemMeta
        meta.lore = value
        this.itemMeta = meta

    }


operator  fun <T : ItemStack> T.invoke(name: String): T {
    this.name = name
    return this
}

operator fun ItemStack.invoke(enchament: Enchantment, level: Int): ItemStack {
    addUnsafeEnchantment(enchament, level)
    return this
}

operator fun ItemStack.minus(enchament: Enchantment): ItemStack {
    removeEnchantment(enchament)
    return this
}

operator fun ItemStack.plus(amount: Int): ItemStack {
    this.amount += amount
    return this
}

operator fun ItemStack.plus(map: Map<Enchantment, Int>): ItemStack {
    addUnsafeEnchantments(map)
    return this
}

infix fun Enchantment.level(level: Int): Map<Enchantment, Int> {
    return mapOf(this to level)
}

fun <T : ItemStack> T.id(id: Int): T {
    typeId = id
    return this
}

fun <T : ItemStack> T.data(data: Int): T {
    durability = data.toShort()
    return this
}

fun <T : ItemStack> T.addLore(vararg lore: String): T {
    val list = this.lore
    list.addAll(lore)
    this.lore = list
    return this
}

fun <T : ItemStack> T.lore(vararg lore: String): T {
    val list = this.lore
    list.clear()
    list.addAll(lore)
    this.lore = list
    return this
}

fun <T : ItemStack> T.addEnchant(ench: Enchantment, level: Int): T {
    addUnsafeEnchantment(ench, level)
    return this
}

fun <T : ItemStack> T.color(color: Color): T {
    if (!type.name.contains("LEATHER"))
        type = Material.LEATHER_CHESTPLATE
    val meta = itemMeta as LeatherArmorMeta
    meta.color = color
    itemMeta = meta
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




