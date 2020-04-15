package net.eduard.api.server

import org.bukkit.entity.Player

import net.eduard.api.lib.player.DisplayBoard

interface ScoreSystem {

    fun setScore(player: Player, scoreboard: DisplayBoard)
    fun setScoreDefault(player: Player)
    fun getScore(player: Player): DisplayBoard
    fun hasScore(player: Player): Boolean

}
