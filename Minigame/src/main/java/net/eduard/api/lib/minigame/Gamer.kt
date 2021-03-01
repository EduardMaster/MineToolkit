package net.eduard.api.lib.minigame

interface Gamer {


    fun join(room : GameRoom)
    fun quit()
    fun getCurrentMinigame() : Game
    fun getCurrentGame() : GameRoom
    fun getCurrentGameMode() : GameMode
    fun getCurrentLobby() : GameLobby


}