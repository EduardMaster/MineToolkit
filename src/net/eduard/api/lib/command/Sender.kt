package net.eduard.api.lib.command

import java.util.*

abstract class Sender(var name: String,var uniqueId: UUID) {


    abstract fun sendMessage(str : String)

    abstract fun hasPermission(permission : String) : Boolean


}