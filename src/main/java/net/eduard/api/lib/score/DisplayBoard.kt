package net.eduard.api.lib.score

import net.eduard.api.lib.abstraction.PlayerScore
import net.eduard.api.lib.kotlin.cut
import java.util.HashMap
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import java.lang.Exception

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
    /**
     * Construtor vazio setando o nome da Scoreboard de '§6§lScoreboard'
     */
    constructor() : this("§6§lScoreboard", mutableListOf("§aLinha1"))
    constructor(title: String, vararg lines: String) : this(title, lines.toMutableList())

    var customLines = mutableListOf<DisplayBoardLine>()
    lateinit var customTitle: DisplayBoardLine

    @Transient
    lateinit var score: PlayerScore

    fun hasScore() = this::score.isInitialized

    @Transient
    private var cache: MutableMap<Int, String> = HashMap()

    /**
     * Aplica a Scoreboard no Jogador
     *
     * @param player Jogador
     */
    fun apply(player: Player) {
        player.resetScore()
        score = PlayerScore.getScore(player)
        score.removeAllLines()
        score.removeAllTeams()
        score.setTitle(title)
        var id = 15
        for (line in lines) {
            set(id, line)
            id--
        }

    }

    fun update() {
        //updateScrolls()

        var id = 15
        for (line in lines) {
            set(id, line)
            id--
        }

        // setT Mine.getReplacers(title, player) }
        //  calc("removeTrash") { removeTrash() }
    }

    fun setHealthBar(health: String) {

    }
    open fun setLine(prefix: String, center: String, suffix: String, line: Int) {
        score.setLine(line, prefix, center, suffix)
    }
    fun copy() : DisplayBoard {
        return this
    }
    fun clone() : DisplayBoard {
        return this
    }
    open fun updateHealthBar(player: Player) {

    }
    open fun empty(slot: Int) {

    }
    fun update(player : Player ) {

    }
    var healthBarEnabled = false
    open fun getDisplay(): String {
        return score.getTitle()
    }
    open fun setDisplay(name: String) {
        score.setTitle(name)
    }

    fun getScoreboard()
            : Scoreboard? = null

    open fun getHealthBar(): String {
        return ""
    }

    open fun add(line: String) {

    }

    /**
     * Tenta definir a score padrão do servidor
     */
    private fun Player.resetScore() {
        try {
            scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        } catch (er: Exception) {
        }
    }


    private fun id(slot: Int): Int {
        return if (slot <= 0) 1 else Math.min(slot, 15)
    }

    operator fun set(slot: Int, line: String): Boolean {
        println("Aew")
        if (!hasScore()) return false
        println("Teste1")
        var text = line
        val id = id(slot)


        if (line == cache[id]) {
            println("Teste3")
            // Não vai atualizar pois é a mesma
            return true
        }
        val prefixLimit = 16
        val suffixLimit = 16
        // 16 + 40 + 16 = Tamanho maximo de uma linha
        text = text.cut(prefixLimit + score.getNameLimit() + suffixLimit)


        var prefix = ""

        var center = ""

        var suffix = ""

        if (text.length <= score.getNameLimit()) {
            center = text
        } else if (text.length <= score.getNameLimit() + prefixLimit) {
            center = text.substring(0, score.getNameLimit())
            suffix = text.substring(score.getNameLimit())
        } else if (text.length <= (score.getNameLimit() + prefixLimit + suffixLimit)) {
            prefix = text.substring(0, prefixLimit)
            center = text.substring(prefixLimit, prefixLimit + score.getNameLimit() - 1)
            suffix = text.substring(prefixLimit + score.getNameLimit())
        }
        cache[id] = text
        score.setLine(id, prefix, center, suffix)
        return true
    }

    /**
     * Remove a linha da Scoreboard
     *
     * @param slot Linha
     */
    fun clear(slot: Int) {
        val id = id(slot)
        remove(id)
    }

    /**
     * Remove a linha da Scoreboard
     *
     * @param slot Linha
     */
    fun remove(id: Int): Boolean {

        // Antigo código
        //scoreboard!!.resetScores(fake)
        score.clearLine(id)
        return false
    }

    /*

     /**
     * Método criado dia 25/03/21 para calcular lag de cada função desta classe
     * @param name Nome da Funçõo
     * @param action Runnable da Função
     */
    fun calc(name: String?, action: Runnable) {
        val start = System.currentTimeMillis()
        action.run()
        val end = System.currentTimeMillis()
        val dif = end - start
        //System.out.println("[ScoreLag] " + name + ": " + dif + "ms");
    }


     */

}

