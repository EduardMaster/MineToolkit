package net.eduard.api.lib.hybrid

import net.eduard.api.lib.storage.Storable
import java.io.Serializable
import java.util.*

@Storable.StorageAttributes(inline = true)
class PlayerUser(
    var name: String = "Eduard",
    uuid: UUID? = null
) : Serializable {

    lateinit var uniqueId: UUID

    init {
        if (uuid == null) {
            setUUIDByName()
        } else {
            uniqueId = uuid
        }
    }

    @Transient
    private var onlinePlayer: IPlayer<*> = Hybrid.instance.getPlayer(name,uniqueId)

    fun setUUIDByName(): UUID {
        uniqueId = UUID.nameUUIDFromBytes(("OfflinePlayer:$name").toByteArray())
        return uniqueId
    }

    fun sendMessage(message: String) {
      player.sendMessage(message)
    }

    constructor(name: String) : this(name, null)

    override fun toString(): String {
        return "$name;$uniqueId"
    }

    constructor(player: IPlayer<*>) : this(player.name, player.uniqueId) {
        this.onlinePlayer = player
    }

    val isOnline: Boolean
        get() =onlinePlayer.isOnline



    val player: IPlayer<*> get() = onlinePlayer

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true
        if (other is PlayerUser) {
            return (name.equals(other.name, true))
        }
        return true
    }

    override fun hashCode(): Int {
        return name.toLowerCase().hashCode()
    }


}