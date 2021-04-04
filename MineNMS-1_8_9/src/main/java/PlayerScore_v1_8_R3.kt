package net.eduard.api.lib.abstraction


import net.eduard.api.lib.modules.Extra
import net.minecraft.server.v1_8_R3.*
import org.bukkit.ChatColor
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import java.lang.IllegalStateException

/**
 *
 */
class PlayerScore_v1_8_R3(override var player: Player) : PlayerScore {

    private val score = Scoreboard();
    private val objective = score.registerObjective("PerPlayerScore", IScoreboardCriteria.b);
    private val scores = mutableMapOf<Int, ScoreboardScore>()
    private val teams = mutableMapOf<Int, ScoreboardTeam>()
    private val playersPosition = mutableMapOf<String, Int>()
    private val playersTeam = mutableMapOf<String, ScoreboardTeam>()

    init {


    }
    override fun rebuild(){
        for (line in scores.values){
            sendPacket(PacketPlayOutScoreboardScore(line.playerName, objective))
        }
        for (team in teams.values){
            sendPacket(PacketPlayOutScoreboardTeam(team , 1))
        }
        for (team in playersTeam.values){
            sendPacket(PacketPlayOutScoreboardTeam(team , 1))
        }
        remove()
        create()
        for (line in scores.values){
            sendPacket(PacketPlayOutScoreboardScore(line))
        }
        for (team in teams.values){
            sendPacket(PacketPlayOutScoreboardTeam(team , 0))
        }
        show()
        /*
        for (lineId in 1 until 16) {
            setLine(lineId, Extra.newKey(Extra.KeyType.LETTER, 16))
        }
        setTitle("§aTitulo da Score")
         */
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
             sendPacket(PacketPlayOutScoreboardScore(score.playerName,objective))
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
        return ""
    }

    override fun remove() {
        sendPacket(PacketPlayOutScoreboardObjective(objective, 1))
    }

    override fun hide() {
        sendPacket(PacketPlayOutScoreboardDisplayObjective(0, null))
        sendPacket(PacketPlayOutScoreboardDisplayObjective(1, null))
        sendPacket(PacketPlayOutScoreboardDisplayObjective(2, null))
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
            val position = playersPosition[playerName.toLowerCase()]?:10
            team = score.createTeam("$position$playerName".cut(16))
            team.prefix = prefix.cut(16)
            team.suffix = suffix.cut(16)
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
        playersPosition[playerName.toLowerCase()] = position
        val teamOriginal = playersTeam[playerName.toLowerCase()]
        if (teamOriginal != null){
            sendPacket(PacketPlayOutScoreboardTeam(teamOriginal, 1))
            score.removeTeam(teamOriginal)
            val team = score.createTeam("$position$playerName".cut(16))
            playersTeam[playerName.toLowerCase()] = team
            team.prefix = teamOriginal.prefix
            team.suffix = teamOriginal.suffix
            team.playerNameSet.add(playerName)
            sendPacket(PacketPlayOutScoreboardTeam(team, 0))
        }
    }


}