package net.eduard.api.lib.bungee

interface ServerMessageHandler {
    fun onMessage(server: String, tag: String, line: String)
}