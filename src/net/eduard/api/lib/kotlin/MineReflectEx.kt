package net.eduard.api.lib.kotlin

import net.eduard.api.lib.modules.MineReflect
import org.bukkit.entity.Player


inline fun Player.sendTitle(title : String, subTitle : String){
    MineReflect.sendTitle(this, title,subTitle,20,20,20)
}

inline fun Player.sendTitle(title : String, subTitle : String, fadeInt : Int, stay: Int, fadeOut : Int){
    MineReflect.sendTitle(this, title,subTitle,fadeInt,stay,fadeOut)
}
inline fun Player.sendActionBar(msg : String){
    MineReflect.sendActionBar(this,msg)
}
inline fun Player.sendPacket(packet : Any){
    MineReflect.sendPacket(this,packet)
}