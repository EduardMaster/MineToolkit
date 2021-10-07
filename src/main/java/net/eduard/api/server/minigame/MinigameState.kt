package net.eduard.api.server.minigame


/**
 *
 * Estado do Minigame
 * @author Eduard
 */
enum class MinigameState( var displayName: String){
    STARTING("Iniciando"),
    EQUIPPING("Pre-Jogo"),
    PLAYING("Em-Jogo"),
    ENDING("Acabando"),
    RESTARTING("Reiniciando")

}