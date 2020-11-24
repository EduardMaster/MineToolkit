package net.eduard.api.server.minigame

/**
 * Modo do Minigame
 * @author Eduard
 */
enum class MinigameMode(val id : Int, var namePT  : String) {
    NORMAL(0,"Normal"),
    INSANE(1,"Insano"),
    TIMED(2,"Temporalizado"),
    TOURNAMENT(3,"Terneio")

}
