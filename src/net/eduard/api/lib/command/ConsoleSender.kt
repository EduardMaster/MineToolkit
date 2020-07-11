package net.eduard.api.lib.command

import java.util.*

class ConsoleSender : Sender("CONSOLE", UUID.nameUUIDFromBytes("CONSOLE".toByteArray())) {
    override fun sendMessage(str: String) {
        println(str)
    }

    override fun hasPermission(permission: String): Boolean {
        return true
    }

}