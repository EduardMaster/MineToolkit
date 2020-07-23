package net.eduard.api.lib.game

import net.eduard.api.lib.kotlin.sendTitle
import net.eduard.api.lib.modules.Copyable
import org.bukkit.entity.Player

/**
 * Representa um Titulo e Subtitulo
 */
class Title(

        var title: String = "Titulo",
        var subTitle: String = "Subtitulo",
        var fadeIn: Int = 20,
        var stay: Int = 20,
        var fadeOut: Int = 20
) {

    fun copy(): Title {
        return Copyable.copyObject(this)
    }


    fun create(player: Player): Title {
        player.sendTitle(title, subTitle, fadeIn, stay, fadeOut)
        return this
    }

}