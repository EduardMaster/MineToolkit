package net.eduard.api.lib.abstraction

import lib.modules.MineReflect
import org.bukkit.Location
import org.bukkit.entity.Player
import java.lang.Exception

interface Hologram {

    fun getText(): String
    fun setText(text: String)

    fun canSee(player: Player): Boolean
    fun getPlayers(): List<Player>

    fun spawn(local : Location)

    fun isSpawned() : Boolean

    fun move(local : Location)


    fun show(player: Player)
    fun hide(player: Player)


    companion object {
        fun create(text: String): Hologram? {
            return try {
                val holo = Class.
                forName("net.eduard.api.lib.abstraction.Hologram_"
                        + lib.modules.MineReflect.getVersion())
                        .newInstance() as Hologram
                holo.setText(text)
                holo
            } catch (e: Exception) {
                null
            }
        }
    }


}