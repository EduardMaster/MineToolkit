package net.eduard.api.lib.kotlin

import net.eduard.api.lib.modules.Extra
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.CropState
import org.bukkit.Material
import org.bukkit.block.BlockState
import org.bukkit.command.CommandExecutor
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.material.Crops
import org.bukkit.plugin.java.JavaPlugin

fun Listener.register(plugin: JavaPlugin) = Bukkit.getPluginManager().registerEvents(this, plugin)

fun CommandExecutor.register(cmd: String, plugin: JavaPlugin) {
    plugin.getCommand(cmd).executor = this
}


val Class<*>.plugin: JavaPlugin
    get() {
        if (JavaPlugin::class.java.isAssignableFrom(this)) {
            return JavaPlugin.getProvidingPlugin(this)
        }
        return JavaPlugin.getPlugin(this as Class<out JavaPlugin>) as JavaPlugin
    }

fun Inventory.setItem(line: Int, column: Int, item: ItemStack?) = this.setItem(Extra.getIndex(column, line), item)

val BlockState.isCrop get() = type == Material.CROPS


val BlockState.plantState: CropState?
    get() = if (type == Material.CROPS) (this as Crops).state
    else null


val InventoryClickEvent.player get() = this.whoClicked as Player

val InventoryOpenEvent.player get() = this.player as Player

fun Player.inventory(name: String, lineAmount: Int, block: Inventory.() -> Unit): Inventory? {

    val inventory = Bukkit.createInventory(this, 9 * lineAmount, name.cut(32))

    block(inventory)
    player.openInventory(inventory)

    return inventory

}

fun Inventory.item(position: Int, block: ItemStack.() -> Unit): ItemStack {

    val item = ItemStack(Material.STONE)
    block(item)

    setItem(position, item)

    return item

}

var ItemStack.name: String
    get() {

        return itemMeta.displayName;
    }
    set(value) {

        val meta = itemMeta
        meta.displayName = value
        this.itemMeta = meta

    }

var ItemStack.lore: List<String>
    get() {
        return itemMeta.lore;
    }
    set(value) {

        val meta = itemMeta
        meta.lore = value
        this.itemMeta = meta

    }


fun ItemStack.lore(vararg lore: String): ItemStack {

    this.lore = lore.toList()
    return this
}

fun ItemStack.id(id: Int): ItemStack {
    typeId = id
    return this
}

fun ItemStack.data(data: Int): ItemStack {
    durability = data.toShort()
    return this
}

fun ItemStack.addLore(vararg lore: String): ItemStack {

    if (hasItemMeta()) {
        val meta = itemMeta
        meta.lore.addAll(lore)
        this.itemMeta = meta

    }

    return this
}

fun ItemStack.addEnchant(ench: Enchantment, level: Int): ItemStack {
    addUnsafeEnchantment(ench, level)
    return this
}

fun ItemStack.color(color: Color): ItemStack {

    if (!type.name.contains("LEATHER"))
        type = Material.LEATHER_CHESTPLATE

    val meta = itemMeta as LeatherArmorMeta
    meta.color = color
    itemMeta = meta
    return this
}