package net.eduard.api.server

import net.eduard.api.lib.game.DisplayBoard
import org.bukkit.entity.Player


interface ScoreSystem {

    fun setScore(player: Player, scoreboard: DisplayBoard)
    fun setScoreDefault(player: Player)
    fun getScore(player: Player): DisplayBoard
    fun hasScore(player: Player): Boolean

}
