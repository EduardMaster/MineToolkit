package net.eduard.api.lib.kotlin

import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

fun Listener.register(plugin : JavaPlugin) {
    Bukkit.getPluginManager().registerEvents(this,plugin)
}
fun Int.centralized() : Int {
    while(Extra.isColumn(this,1) || Extra.isColumn(this,9)){
        this.inc()
    }
    return this
}

fun Inventory.setItem(line : Int, column : Int, item : ItemStack?){
    this.setItem(Extra.getIndex(column,line),item)
}


