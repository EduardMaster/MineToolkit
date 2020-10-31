package net.eduard.api.lib.hybrid

interface ISender {
    val name : String
    fun sendMessage(message : String)
    fun hasPermission(permission : String) : Boolean
}