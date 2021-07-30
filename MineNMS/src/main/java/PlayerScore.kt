package net.eduard.api.lib.abstraction

import org.bukkit.entity.Player
import java.lang.reflect.Constructor

interface PlayerScore {

    companion object {
        var construct : Constructor<out PlayerScore>? = null
        private val players = mutableMapOf<Player, PlayerScore>()
        var constructor: (Player) -> PlayerScore = {
            if (construct== null){
                construct = Class.forName("net.eduard.api.lib.abstraction.PlayerScore_" + Minecraft.getVersion())
                    .getDeclaredConstructor(Player::class.java) as Constructor<out PlayerScore>?
            }
            construct!!.newInstance(it) as PlayerScore
        }

        fun hasScore(player : Player) = player in players
        fun removeScore(player : Player){
            if (hasScore(player)) {
                players.remove(player)
            }
        }
        fun getScore(player: Player): PlayerScore {
            var playerScore = players[player]
            if (playerScore == null){
                playerScore = constructor.invoke(player)
                players[player] = playerScore

            }
            return playerScore
        }


    }


    var player: Player

    fun getNameLimit() = 40

    fun clearLine(lineID: Int)

    fun setLine(lineID: Int, text: String)

    fun setLine(lineID: Int, prefix: String, suffix: String)
    fun setLine(lineID: Int, prefix: String, center: String, suffix: String)
    fun setTitle(title: String)

    fun getTitle()  :String

    fun getLine(lineID: Int): String

    fun remove()

    fun rebuild()


    fun hide()

    fun show()

    fun removeAllLines()

    fun clearAllLines()

    fun update()

    fun create()

    fun removeTeam(lineID: Int);

    fun removeAllTeams()

    fun setPlayerTagPosition(playerName: String, position: Int)

    fun setTagPosition(position: Int) {
        setPlayerTagPosition(player.name, position)
    }

    fun setTag(prefix: String, suffix: String) {
        setPlayerTag(player.name, prefix, suffix)
    }


    fun setPlayerTag(playerName: String, prefix: String, suffix: String)


}