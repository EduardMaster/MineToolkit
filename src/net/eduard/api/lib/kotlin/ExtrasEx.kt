package net.eduard.api.lib.kotlin

import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.plugin.java.JavaPlugin

fun Listener.register(plugin : JavaPlugin) {
    Bukkit.getPluginManager().registerEvents(this,plugin)
}


