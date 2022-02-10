package net.eduard.api.lib.hybrid

import net.eduard.api.lib.storage.annotations.StorageAttributes
import java.io.Serializable
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

@StorageAttributes(inline = true)
class PlayerUser(
    var name: String = "Eduard",
    uuid: UUID? = null
) {

    var uniqueId: UUID? = null
    get() {
        if (field==null){
            setUUIDByName()
        }
        return field
    }


    init {
        if (uuid == null) {
            asyncSetUUIDByName()
        } else {
            uniqueId = uuid
        }
    }

    fun asyncSetUUIDByName() {
        uuidIndentifier.submit(this::setUUIDByName)
    }

    fun setUUIDByName() {
        uniqueId = UUID.nameUUIDFromBytes(
            ("OfflinePlayer:$name").toByteArray(StandardCharsets.UTF_8)
        )
    }

    fun sendMessage(message: String) {
        player.sendMessage(message)
    }

    constructor(name: String) : this(name, null)

    override fun toString(): String {
        return "$name;$uniqueId"
    }

    constructor(player: IPlayer<*>) : this(player.name , player.uniquedId) {
        this.onlinePlayer = player
    }

    val isOnline: Boolean
        get() = onlinePlayer?.isOnline ?: false


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

    companion object{
        val uuidIndentifier = Executors.newFixedThreadPool(4)

    }

}