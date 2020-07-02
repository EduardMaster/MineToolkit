package net.eduard.api.lib.kotlin

import net.eduard.api.lib.modules.Extra
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

fun Listener.register(plugin: JavaPlugin) = Bukkit.getPluginManager().registerEvents(this, plugin)


fun Inventory.setItem(line: Int, column: Int, item: ItemStack?) = this.setItem(Extra.getIndex(column, line), item)


val InventoryClickEvent.player get() = this.whoClicked as Player
