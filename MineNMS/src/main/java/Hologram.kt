package net.eduard.api.lib.abstraction

import org.bukkit.Location
import org.bukkit.entity.Player

interface Hologram {

    var text : String
    var playersSeeing : MutableList<Player>
    val isSpawned : Boolean
    fun canSee(player: Player): Boolean
    fun spawn(local: Location)
    fun move(local: Location)
    fun show(player: Player)
    fun hide(player: Player)


    companion object {
        fun create(text: String): Hologram? {
            return try {
                val holo = Class.forName(
                    "net.eduard.api.lib.abstraction.Hologram_"
                            + Minecraft.getVersion()
                )
                    .newInstance() as Hologram
                holo.text = text
                holo
            } catch (e: Exception) {
                null
            }
        }
    }


}