package net.eduard.api.server

interface ChatAPI :  EduardPluginsAPI {

    fun isMuted(playerName : String) : Boolean
    fun mute(playerName: String)
    fun getPlayersMuted() : List<String>

}