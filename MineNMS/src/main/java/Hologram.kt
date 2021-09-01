package net.eduard.api.lib.abstraction

import net.eduard.api.lib.modules.Mine
import org.bukkit.Location
import org.bukkit.entity.Player

interface Hologram {
    val id : Int
    var text : String
    var playersSeeing : MutableSet<Player>
    val isSpawned : Boolean
    var location : Location
    fun canSee(player: Player): Boolean
    fun spawn(local: Location)
    fun move(local: Location)
    fun show(player: Player)
    fun hide(player: Player)
    fun toggle(player: Player)
    fun showAllIn(distance : Int)
    fun hideAllIn(distance : Int)
    fun updateAllIn(distance: Int)

    fun update(player : Player)


    companion object {
        var debug = true
        fun log(msg: String) {
            if (debug) Mine.console("§b[HologramAPI]§f $msg")
        }

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