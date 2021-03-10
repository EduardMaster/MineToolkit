package net.eduard.api.lib.bungee

import net.eduard.api.lib.database.annotations.ColumnSize
import net.eduard.api.lib.database.annotations.ColumnPrimary
import net.eduard.api.lib.database.annotations.TableName
import java.io.Serializable
import java.util.ArrayList

@TableName("network_servers")
class ServerSpigot : Serializable {
    @ColumnSize(100)
    @ColumnPrimary
    var name = "lobby01"

    @ColumnSize(100)
    var type = "lobby"

    @ColumnSize(100)
    var subType = "principal"
    var max = 1
    var count = 0
        set(value) {
            if (field != value)
                changed = true
            field = value

        }
    var teamSize = 1
    var state = ServerState.OFFLINE
        set(value) {
            field = value
            changed = true
        }
    var host = "localhost"
    var port = 25565

    @Transient
    var changed = false

    @Transient
    var players: List<String> = ArrayList()

    constructor() {}

    val isSolo: Boolean
        get() = teamSize == 1
    val isDuo: Boolean
        get() = teamSize == 2

    constructor(name: String) {
        this.name = name
    }

    fun canConnect(): Boolean {
        return isOnline && !isFull && !isRestarting && !isGameStarted
    }

    val isRestarting: Boolean
        get() = isState(ServerState.RESTARTING)

    fun isState(state: ServerState): Boolean {
        return this.state === state
    }

    val isGameStarted: Boolean
        get() = isState(ServerState.IN_GAME)
    val isOffline: Boolean
        get() = isState(ServerState.OFFLINE)
    val isDisabled: Boolean
        get() = isState(ServerState.DISABLED)
    val isOnline: Boolean
        get() = !isOffline && !isDisabled
    val isFull: Boolean
        get() = players.size == max
    val isEmpty: Boolean
        get() = players.isEmpty()
}