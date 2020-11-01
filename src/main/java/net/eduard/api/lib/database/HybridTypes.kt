package net.eduard.api.lib.database

import net.eduard.api.lib.hybrid.PlayerUser
import java.util.*

object HybridTypes {

    init{
        save<PlayerUser>(75) {
            "$name;$uniqueId"
        }
        load {
            val split = it.split(";")
            PlayerUser(split[0], UUID.fromString(split[1]))
        }
    }

}