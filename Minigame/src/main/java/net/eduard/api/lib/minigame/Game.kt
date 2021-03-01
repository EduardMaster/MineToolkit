package net.eduard.api.lib.minigame



interface Game {

    fun playAvaliable(gameMode : GameMode)
    fun getRoomsByMapName(mapName : String) : List<GameRoom>
    fun joinFirstLobby(game : Gamer)
    fun getGamerByPlayerName(player : String)
    fun play(gamer : Gamer, room : GameRoom)
}