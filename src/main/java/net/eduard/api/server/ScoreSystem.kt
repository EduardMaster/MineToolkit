package net.eduard.api.server

import org.bukkit.entity.Player

import lib.game.DisplayBoard

interface ScoreSystem {

    fun setScore(player: Player, scoreboard: lib.game.DisplayBoard)
    fun setScoreDefault(player: Player)
    fun getScore(player: Player): lib.game.DisplayBoard
    fun hasScore(player: Player): Boolean

}
