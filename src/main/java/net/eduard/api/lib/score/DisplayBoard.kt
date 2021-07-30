package net.eduard.api.lib.score

import net.eduard.api.lib.kotlin.copy
import net.eduard.api.lib.kotlin.cut
import net.eduard.api.lib.modules.Mine
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team

/**
 * API de criação de Scoreboard feita para facilitar sua vida
 * <br>
 * Updates
 * <br>
 * v1.1 Suporte a Animação de Frames a cada ticks
 * <br>
 * v1.2 Usando PlayerScore Classe para Scoreobard direto com NMS
 *
 * @author Eduard
 * @version 1.1
 */
open class DisplayBoard(
    /**
     * Titulo da scoreboard
     */

    var title: String,

    var lines: MutableList<String>
) {

    companion object {
        private val nameLimit = 16
        private val teamPrefix = "display-"
        private val prefixLimit = 16
        private val suffixLimit = 16
    }

    /**
     * Construtor vazio setando o nome da Scoreboard de '§6§lScoreboard'
     */
    constructor() : this("§6§lScoreboard", mutableListOf("§aLinha1"))
    constructor(title: String, vararg lines: String) : this(
        title,
        lines.toMutableList()
    )

    var healthBarEnabled = false
    var customLines = mutableListOf<DisplayBoardLine>()
    var customTitle: DisplayBoardLine? = null

    @Transient
    var scoreboard: Scoreboard? = null

    @Transient
    var usingScore: Player? = null

    @Transient
    var objective: Objective? = null
    fun hasScore() = scoreboard != null

    @Transient
    private var linesUsed = mutableMapOf<Int, String>()

    @Transient
    private var linesCenters = mutableMapOf<Int, String>()

    @Transient
    private var teams = mutableMapOf<Int, Team>()


    fun create(): Scoreboard {
        val score = Bukkit.getScoreboardManager().newScoreboard!!
        scoreboard = score
        objective = score.registerNewObjective("displayBoard", "dummy")
        for (position in 1..15) {
            teams[position] = score.registerNewTeam("$teamPrefix$position")
        }
        objective!!.displaySlot = DisplaySlot.SIDEBAR

        return score
    }

    /**
     * Aplica a Scoreboard no Jogador
     *
     * @param player Jogador
     */
    fun apply(player: Player) {
        player.scoreboard = scoreboard ?: create()
        usingScore = player
    }

    fun update() {
        val player = usingScore ?: return
        var id = 15
        for (line in lines) {
            set(id, Mine.getReplacers(line, player))
            id--
        }
        for (line in customLines) {

            set(line.position, Mine.getReplacers(line.get(), player))
        }

        setDisplay(Mine.getReplacers(customTitle?.get() ?: title , player))
    }



    fun copy(): DisplayBoard {
        val newScore = DisplayBoard(this.title, this.lines.toMutableList())
        for (customLine in this.customLines) {
            newScore.customLines.add(customLine.copy())
        }

        return newScore
    }

    fun clone(): DisplayBoard {
        return copy()
    }

    fun setHealthBar(health: String) {

    }
    open fun updateHealthBar(player: Player) {

    }

    open fun getHealthBar(): String {
        return ""
    }

    open fun empty(slot: Int) {
        set(slot, "§f" + ChatColor.values()[slot - 1])
    }

    fun update(player: Player) {
        update()
    }

    open fun getDisplay(): String {
        return objective?.displayName ?: title
    }

    open fun setDisplay(name: String) {
        objective?.displayName = name
    }



    open fun add(line: String) {
        this.lines.add(line)
    }

    private inline val Int.position get() = id(this)

    private fun id(slot: Int): Int {
        return if (slot <= 0) 1 else slot.coerceAtMost(15)
    }

    operator fun set(slot: Int, line: String): Boolean {
        scoreboard ?: create()
        var text = line
        val id = slot.position

        if (line.trim().isEmpty()) {
            text = "§f" + ChatColor.values()[id - 1]
        }




        // 16 + 40 + 16 = Tamanho maximo de uma linha
        text = text.cut(prefixLimit + nameLimit + suffixLimit)
        if (text == linesUsed[id]) {
            return false
        }

        var prefix = ""
        var center = ""
        var suffix = ""

        if (text.length <= nameLimit) {
            center = text
        } else if (text.length <= nameLimit + prefixLimit) {
            center = text.substring(0, nameLimit)
            suffix = text.substring(nameLimit)
        } else if (text.length <= (nameLimit + prefixLimit + suffixLimit)) {
            prefix = text.substring(0, prefixLimit)
            center = text.substring(prefixLimit, prefixLimit + nameLimit - 1)
            suffix = text.substring(prefixLimit + nameLimit)
        }
        val used = linesCenters[id]
        val team = teams[id]!!
        linesUsed[id] = text
        var have = false
        if (used != null) {
            if (used != center) {
                remove(id)
            } else have = true
        }
        if (!have) {
            objective?.getScore(center)?.score = id
            team.addEntry(center)
            linesCenters[id] = center
        }
        team.prefix = prefix
        team.suffix = suffix
        return true
    }


    open fun set(slot: Int, prefix: String, center: String, suffix: String): Boolean {
        scoreboard ?: create()
        val id = slot.position
        val centerFixed = center.cut(nameLimit)
        var text = prefix.cut(prefixLimit) + centerFixed + suffix.cut(suffixLimit)
        if (text.trim().isEmpty()) {
            text = "§f" + ChatColor.values()[id - 1]
        }
        if (text == linesUsed[id]) {
            // Não vai atualizar pois é a mesma
            return false
        }
        linesUsed[id] = text
        val team = teams[id]!!
        val used = linesCenters[id]
        var have = false
        if (used != null) {
            if (used != center) {
                remove(id)
            } else have = true
        }
        if (!have) {
            objective?.getScore(centerFixed)?.score = id
            team.addEntry(centerFixed)
            linesCenters[id] = centerFixed
        }
        team.prefix = prefix.cut(16)
        team.suffix = suffix.cut(16)
        return true
    }

    /**
     * Remove a linha da Scoreboard
     *
     * @param slot Linha
     */
    fun remove(slot: Int): Boolean {
        val linha = linesCenters[slot.position] ?: return false
        teams[slot.position]?.removeEntry(linha)
        scoreboard?.resetScores(linha)
        return true
    }


}

