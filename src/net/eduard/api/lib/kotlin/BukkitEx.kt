package net.eduard.api.lib.kotlin

import net.eduard.api.lib.modules.Extra
import org.bukkit.Bukkit
import org.bukkit.CropState
import org.bukkit.Material
import org.bukkit.block.BlockState
import org.bukkit.command.CommandExecutor
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.material.Crops
import org.bukkit.plugin.java.JavaPlugin

fun Listener.register() {
    register(javaClass.plugin)
}

fun Listener.register(plugin: JavaPlugin) = Bukkit.getPluginManager().registerEvents(this, plugin)

fun CommandExecutor.register(cmd: String, plugin: JavaPlugin) {
    plugin.getCommand(cmd).executor = this
}

fun CommandExecutor.register(cmd: String) {
    register(cmd, javaClass.plugin)
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
