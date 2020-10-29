package net.eduard.api.lib.database

import net.eduard.api.lib.command.PlayerOffline
import java.util.*

object HybridTypes {

    init{
        save<PlayerOffline>(75) {
            "$name;$uniqueId"
        }
        load {
            val split = it.split(";")
            PlayerOffline(split[0], UUID.fromString(split[1]))
        }
    }

}