package net.eduard.api.lib.bungee

enum class ServerState(val value: Int) {
    RESTARTING(3),
    IN_GAME(2),
    ONLINE(1),
    OFFLINE(0),
    DISABLED(-1);

}