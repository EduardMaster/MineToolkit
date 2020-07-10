package net.eduard.api.lib.command

class ConsoleSender : Sender("CONSOLE") {
    override fun sendMessage(str: String) {
        print(str)
    }

    override fun hasPermission(permission: String): Boolean {
        return true
    }

}