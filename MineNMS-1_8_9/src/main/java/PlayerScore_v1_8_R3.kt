package net.eduard.api.lib.abstraction


import net.eduard.api.lib.modules.Extra
import net.minecraft.server.v1_8_R3.*
import org.bukkit.ChatColor
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import java.lang.IllegalStateException

class PlayerScore_v1_8_R3(override var player: Player) : PlayerScore {

    private val score = Scoreboard();
    private val objective = score.registerObjective("PerPlayerScore", IScoreboardCriteria.b);
    private val scores = mutableMapOf<Int, ScoreboardScore>()
    private val teams = mutableMapOf<Int, ScoreboardTeam>()
    private val playersTeam = mutableMapOf<String, ScoreboardTeam>()

    init {
        remove()
        create()
        show()
        for (lineId in 1 until 16) {
            setLine(lineId, Extra.newKey(Extra.KeyType.LETTER, 16))
        }
        setTitle("§aTitulo da Score")

    }
    fun sendPacket(packet : Packet<*>){
        (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
    }

    override fun getTitle(): String {
        return objective.displayName
    }
    override fun clearLine(lineID: Int) {

        if (lineID in scores) {
            val score = scores[lineID]!!
            if (lineID in teams) {
                val team = teams[lineID]!!
                sendPacket(PacketPlayOutScoreboardTeam(team, team.playerNameSet, 4))
                team.playerNameSet.remove(score.playerName)

            }
          sendPacket(PacketPlayOutScoreboardScore(score.playerName))
            scores.remove(lineID)
        }
    }
    fun String.cut(size : Int) = Extra.cutText(this, size)


    override fun setLine(lineID: Int, text: String) {
        if (lineID in scores) {
            if (scores[lineID]!!.playerName == text) return // Não atualiza se não precisa
        }
        clearLine(lineID)
        val score = score.getPlayerScoreForObjective(text.cut(40), objective)!!
        score.score = lineID
        scores[lineID] = score
        sendPacket(PacketPlayOutScoreboardScore(scores[lineID]))
        if (lineID in teams) {
            val team = teams[lineID]!!
            team.playerNameSet.add(score.playerName)
           sendPacket(PacketPlayOutScoreboardTeam(team, team.playerNameSet, 3))

        }

    }

    override fun setLine(lineID: Int, prefix: String, suffix: String) {
        if (lineID in teams) {
            val team = teams[lineID]!!
            team.prefix = prefix.cut(16)
            team.suffix = suffix.cut(16)
            if (lineID in scores) {
                val score = scores[lineID]!!
                sendPacket(PacketPlayOutScoreboardTeam(team, team.playerNameSet, 4))
                team.playerNameSet.clear()
                team.playerNameSet.add(score.playerName)
               sendPacket(PacketPlayOutScoreboardTeam(team, team.playerNameSet, 3))
            }
            sendPacket(PacketPlayOutScoreboardTeam(team, 2))
        } else {

            val team = score.createTeam("PlayerScore$lineID")
            team.prefix = prefix.cut(16)
            team.suffix = suffix.cut(16)
            teams[lineID] = team
            if (lineID in scores) {
                val score = scores[lineID]!!
                team.playerNameSet.clear()
                team.playerNameSet.add(score.playerName)
            }

            sendPacket(PacketPlayOutScoreboardTeam(team, 0))
        }

    }

    override fun setLine(lineID: Int, prefix: String, center: String, suffix: String) {
        setLine(lineID,center)
        setLine(lineID,prefix,suffix)
    }

    override fun setTitle(title: String) {
        objective.displayName = title.cut(32)
        update()
    }

    override fun getLine(lineID: Int): String {
        if (lineID in scores) {
            return scores[lineID]!!.playerName
        }
        return "";
    }

    override fun remove() {
        sendPacket(PacketPlayOutScoreboardObjective(objective, 1))
    }

    override fun hide() {
        sendPacket(PacketPlayOutScoreboardDisplayObjective(1, null))

    }

    override fun show() {
        sendPacket(PacketPlayOutScoreboardDisplayObjective(1, objective))
    }

    override fun removeAllLines() {
        for (lineId in 1 until 16) {
            clearLine(lineId)
        }
    }

    override fun clearAllLines() {
        for (lineId in 1 until 16) {
            setLine(lineId, ("§f" + ChatColor.values()[lineId - 1]))
        }
    }

    override fun update() {
        sendPacket(PacketPlayOutScoreboardObjective(objective, 2))
    }

    override fun create() {
        sendPacket(PacketPlayOutScoreboardObjective(objective, 0))

    }

    override fun removeTeam(lineID: Int) {
        if (lineID in teams) {
            val team = teams[lineID]!!
            sendPacket(PacketPlayOutScoreboardTeam(team, 1))
            score.removeTeam(team)
        }
    }

    override fun removeAllTeams() {
        for (lineID in 1 until 16) {
            removeTeam(lineID)
        }
    }

    override fun setPlayerTag(playerName: String, prefix: String, suffix: String) {
        if (playerName.isEmpty()){
            throw IllegalStateException("'playerName' nao pode ser vazio")
        }
        if (playerName.length > 16){
            throw IllegalStateException("'playerName' precisa ser menor que 16")
        }
        var team = playersTeam[playerName.toLowerCase()]
        if (team == null){
            team = score.createTeam(playerName)
            team.prefix = prefix.cut(16)
            team.suffix = suffix.cut(16)
            team!!.displayName = "0$playerName".cut(16)
            team.playerNameSet.add(playerName)
            playersTeam[playerName.toLowerCase()] = team
            sendPacket(PacketPlayOutScoreboardTeam(team, 0))
        }else{
            val score = playersTeam[playerName.toLowerCase()]!!
            score.prefix = prefix.cut(16)
            score.suffix = suffix.cut(16)
            sendPacket(PacketPlayOutScoreboardTeam(team, 2))

        }
    }

    override fun setPlayerTagPosition(playerName: String, position: Int) {
        val team = playersTeam[playerName.toLowerCase()]
        if (team != null){
            team.displayName = "$position$playerName".cut(16)
            sendPacket(PacketPlayOutScoreboardTeam(team, 2))
        }
    }


}