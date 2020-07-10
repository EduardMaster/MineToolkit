package net.eduard.api.lib.command

abstract class Sender(var name: String) {


    abstract fun sendMessage(str : String)

    abstract fun hasPermission(permission : String) : Boolean


}