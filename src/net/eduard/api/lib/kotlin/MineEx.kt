package net.eduard.api.lib.kotlin

import net.eduard.api.lib.modules.Mine
import org.bukkit.command.CommandSender
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack


fun Player.removeXP(amount: Double){
    Mine.removeXP(this,amount)
}
fun Player.addHotBar(item : ItemStack){
    Mine.setHotBar(this,item)
}
fun Player.changeTabName(tabName : String){
    Mine.changeTabName(this,tabName)
}
fun Player.clearHotBar(){
    Mine.clearHotBar(this)

}
fun LivingEntity.clearArmors(){
     Mine.clearArmours(this)
}
fun Player.clearInventory(){
    Mine.clearInventory(this)
}

fun Event.call(){
    return Mine.callEvent(this)
}
fun CommandSender.isPlayer(block: Player.() -> Unit){
    if (Mine.onlyPlayer(this)){
        block(this as Player)
    }
}

