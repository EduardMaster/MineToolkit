package net.eduard.api.lib.kotlin

import net.eduard.api.lib.command.PlayerOffline
import net.eduard.api.lib.game.FakePlayer
import net.eduard.api.lib.modules.Mine
import org.bukkit.command.CommandSender
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack


inline fun Player.removeXP(amount: Double){
    Mine.removeXP(this,amount)
}
inline fun Player.addHotBar(item : ItemStack){
    Mine.setHotBar(this,item)
}
inline fun Player.changeTabName(tabName : String){
    Mine.changeTabName(this,tabName)
}
inline fun Player.clearHotBar(){
    Mine.clearHotBar(this)

}
inline fun LivingEntity.clearArmors(){
     Mine.clearArmours(this)
}
inline fun Player.clearInventory(){
    Mine.clearInventory(this)
}

inline fun Event.call(){
    return Mine.callEvent(this)
}

inline val FakePlayer.offline : PlayerOffline
    get() { return PlayerOffline(name,id)}

inline fun CommandSender.isPlayer(block: Player.() -> Unit){
    if (Mine.onlyPlayer(this)){
        block(this as Player)
    }
}

