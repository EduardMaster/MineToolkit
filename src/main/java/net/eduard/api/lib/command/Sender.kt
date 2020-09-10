package net.eduard.api.lib.command

import java.util.*

abstract class Sender(var name: String,var uniqueId: UUID) {


    abstract fun sendMessage(str : String)

    abstract fun hasPermission(permission : String) : Boolean
    override fun equals(other: Any?): Boolean {
        if (other == null)return false
        if (other is Sender){
            return name == other.name && uniqueId == other.uniqueId
        }
        return false
    }

    override fun hashCode(): Int {
        var result = name.toLowerCase().hashCode()
        result = 31 * result + uniqueId.hashCode()
        return result
    }


}