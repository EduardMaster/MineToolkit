package net.eduard.api.lib.bungee


interface ServerController<T> {
    fun sendMessage(server: String, tag: String, line: String)
    fun sendMessage(tag: String, line: String)
    fun receiveMessage(server: String, tag: String, line: String)
    fun register(plugin: T)
    fun unregister()
    fun connect(player: String, serverType: String, subType: String, teamSize: Int) : String
    fun connect(player: String, serverType: String, subType: String): String {
        return connect(player, serverType, subType, 1)
    }

    fun setState(server: String, state: ServerState)
}