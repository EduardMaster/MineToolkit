package net.eduard.api.lib.hybrid

import net.eduard.api.lib.storage.annotations.StorageAttributes
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.ConcurrentModificationException

@StorageAttributes(inline = true)
class PlayerUser(
    var name: String = "Eduard",
    uuid: UUID? = null
) {

    companion object {
       private val offlineUUIDCache = mutableMapOf<String, UUID>()
        fun createID(name: String): UUID {
            val id = UUID.nameUUIDFromBytes(("OfflinePlayer:$name").toByteArray(StandardCharsets.UTF_8))
            try {
                offlineUUIDCache[name.toLowerCase()] = id
            } catch (ex: ConcurrentModificationException) {
            }
            return id;
        }

        fun getUUIDOrCreate(name: String): UUID {
            var id: UUID? = null
            try {
                id = offlineUUIDCache[name]
            } catch (ex: ConcurrentModificationException) {
            }
            return id ?: createID(name);
        }

    }

    var uniqueId: UUID? = null
        get() {
            if (field == null) {
                field = getUUIDOrCreate(name)
            }
            return field
        }


    init {
        if (uuid != null) {
            uniqueId = uuid
        }
    }


    fun sendMessage(message: String) {
        player.sendMessage(message)
    }

    constructor(name: String) : this(name, null)

    override fun toString(): String {
        return "$name;$uniqueId"
    }

    constructor(player: IPlayer<*>) : this(player.name, player.uniquedId) {
        this.onlinePlayer = player
    }

    val isOnline: Boolean
        get() = player.isOnline


    val player: IPlayer<*>
        get() {
            if (onlinePlayer == null) {
                onlinePlayer = Hybrid.instance.getPlayer(name, uniqueId!!)
            }
            return onlinePlayer!!
        }

    @Transient
    private var onlinePlayer: IPlayer<*>? = null

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