package net.eduard.api.lib.minigame

interface GameMode {

    fun timer(room: GameRoom, gameMode: GameMode)
    var timeToStart : Int
    var timeToFinish : Int



}