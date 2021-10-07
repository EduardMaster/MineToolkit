package net.eduard.api.lib.game

import net.eduard.api.lib.kotlin.sendTitle
import net.eduard.api.lib.modules.Copyable
import net.eduard.api.lib.modules.Mine
import org.bukkit.entity.Player

/**
 * Representa um Titulo e Subtitulo
 */
class Title(

    var title: String,
    var subTitle: String,
    var fadeIn: Int,
    var stay: Int,
    var fadeOut: Int
) {

    constructor(title: String,subTitle: String , duration : Int):
            this(title,subTitle,duration, duration,duration)
    constructor(title: String,subTitle: String):
            this(title,subTitle,20)
    constructor(title: String):
            this(title,"")
    constructor():
            this("TituloVazio")
    fun copy(): Title {
        return Copyable.copyObject(this)
    }


    fun create(player: Player) {
        player.sendTitle(Mine.getReplacers(title, player), Mine.getReplacers(subTitle, player), fadeIn, stay, fadeOut)
    }

}