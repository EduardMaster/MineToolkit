package net.eduard.api.lib.kotlin

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta

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


fun ItemStack.lore(vararg lore: String) : ItemStack {

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
