package net.eduard.api.lib.database

import net.eduard.api.lib.hybrid.PlayerUser
import java.util.*

object HybridTypes {

    init{
        customType<PlayerUser> {
            saveMethod={
                "$name;$uniqueId"
            }
            reloadMethod={
                val split = split(";")
                PlayerUser(split[0], UUID.fromString(split[1]))
            }
        }

    }

}