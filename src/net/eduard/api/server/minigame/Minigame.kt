package net.eduard.api.server.minigame

import java.util.ArrayList
import net.eduard.api.lib.storage.Storable.*
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.game.DisplayBoard
import net.eduard.api.lib.game.Kit
import net.eduard.api.lib.manager.TimeManager
import net.eduard.api.lib.bungee.BukkitBungeeAPI
import net.eduard.api.lib.config.Config
import net.eduard.api.lib.game.FakePlayer
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import java.io.File
import java.lang.Exception

/**
 * Representa um Jogo <br></br>
 * MinigameSetup 1.0
 *
 * @version 2.0
 * @since EduardAPI 2.0
 * @author Eduard
 */
@StorageAttributes(indentificate = true)
open class Minigame : TimeManager {

    var name = "Minigame"
    var messagePrefix = "[Minigame] "
    var isEnabled = true

    var isBungeecord = true
    var bungeeLobby = "Lobby"

    var maxPlayersPerLobby = 20
    var timeIntoStart = 60
    var timeIntoRestart = 20
    var timeIntoGameOver = 15 * 60
    var timeIntoPlay = 2 * 60

    var timeOnStartTimer = 0
    var timeOnRestartTimer = 40
    var timeOnForceTimer = 10
    var timeOnStartingToBroadcast = 15
    var timeOnEquipingToBroadcast = 1

    @Transient
    var setting: MinigameMap? = null
    var lobby: Location? = null

    @Transient
    var players: MutableMap<FakePlayer, MinigamePlayer> = mutableMapOf()
    var scoreboardStarting = DisplayBoard("Minigame iniciando")
    var scoreboardLobby = DisplayBoard("Minigame lobby")
    var scoreboardPlaying = DisplayBoard("Minigame em jogo")
    var chests = MinigameChest()
    var chestsFeast = MinigameChest()
    var chestMiniFeast = MinigameChest()
    var kits: MutableList<Kit> = ArrayList()
    var lobbies: MutableList<MinigameLobby> = ArrayList()

    @Transient
    var maps: MutableList<MinigameMap> = ArrayList()

    @Transient
    var rooms: MutableList<MinigameRoom> = ArrayList()


    /**
     * Pega a primera sala existente do Minigame
     *
     * @return Sala
     */
    //		getRooms().get(0);
    val game: MinigameRoom
        get() = rooms.iterator().next()

    val mainLobby: MinigameLobby
        get() = if (lobbies.size > 0) lobbies[0] else newLobby(1)

    /**
     * Pega o mapa referente a sala principal do Minigame
     *
     * @return
     */
    val map: MinigameMap?
        get() = getMap(name)

    /**
     * Pega os jogadores que estão jogando
     *
     * @return Lista de Jogadores ([Player])
     */
    val playersOnline: List<Player>
        get() = players.values.filter { it.isOnline }.map { it.player!! }


    val isSetting: Boolean
        get() = setting != null

    /**
     * Conecta todos jogadores no servidor Lobby
     */
    fun connectAllPlayersToLobby() {
        for (player in players.values) {
            BukkitBungeeAPI.connectToServer(player.player, bungeeLobby)
        }

    }

    /**
     * Teleporta todos os jogadores para o Local do Lobby
     */
    fun teleportAllPlayersToLobby() {


        lobby ?: playersOnline.forEach { it.teleport(lobby) }

    }

    constructor()

    constructor(name: String) {
        this.name = name
        messagePrefix = "§8[§b$name§8] "
        lobbies.add(MinigameLobby())
    }

    /**
     * Cria um Mapa
     *
     * @param nome Nome
     * @return Mapa Novo
     */
    fun createMap(nome: String) = MinigameMap(this, nome)


    /**
     * Timer do Minigame define oque acontece a cada segundo que se passa do
     * Minigame em cada Sala
     *
     * @param room Sala
     */
    open fun event(room: MinigameRoom) {}

    /**
     * Pega o mapa existente pelo seu nome
     *
     * @param name Nome
     * @return Mapa
     */
    fun getMap(name: String): MinigameMap? {
        for (map in maps) {
            if (map.name.equals(name, ignoreCase = true)) {
                return map
            }
        }
        return null
    }

    /**
     * Remove o mapa da lista de mapas existentes
     *
     * @param map
     */
    fun removeMap(map: MinigameMap) {
        maps.remove(map)
    }

    /**
     * Verifica se existe este Map com este Nome
     *
     * @param name Nome
     * @return
     */
    fun hasMap(name: String): Boolean {
        for (map in maps) {
            if (map.name.equals(name, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    /**
     * Cria um sala unica e um mapa unico Também usando o Nome do Minigame
     *
     * @return Minigame criado com mapa já configurado
     */
    fun uniqueGame(): MinigameRoom {
        return MinigameRoom(this, MinigameMap(this, name))
    }

    constructor(name: String, plugin: JavaPlugin) : this(name) {
        this.plugin = plugin
    }

    /**
     * Manda mensagem para todos jogadores participando do minigame
     *
     * @param message Mensagem
     */
    fun broadcast(message: String) {
        for (player in playersOnline) {
            player.sendMessage(messagePrefix + Mine.getReplacers(message, player))
        }
    }

    /**
     * Pega a sala pelo seu ID
     *
     * @param id ID
     * @return Sala
     */
    fun getRoom(id: Int) = rooms.firstOrNull { it.id == id }


    /**
     * Verifica se a sala com este ID existe
     *
     * @param id ID
     * @return Sala
     */
    fun hasRoom(id: Int) = getRoom(id) != null


    /**
     * Cria uma Sala com este ID para o Mapa expecifico
     *
     * @param map Mapa
     * @param id  ID
     * @return Nova Sala
     */
    fun createRoom(map: MinigameMap, id: Int) = MinigameRoom(this, map)


    /**
     * Pega a sala que o jogador esta jogando
     *
     * @param player Jogador
     * @return Sala do jogador
     */
    fun getGame(player: Player): MinigameRoom? {
        return getPlayer(player).game
    }

    /**
     * Pega a sala com o nome do seu mapa igual a este
     *
     * @param name Nome
     * @return
     */
    fun getGame(name: String): MinigameRoom? {
        for (room in rooms) {
            if (room.map.name.equals(name, ignoreCase = true)) {
                return room
            }
        }
        return null
    }

    fun newLobby(id: Int): MinigameLobby {

        val lobby = MinigameLobby()
        lobby.id = id
        lobbies.add(lobby)
        return lobby

    }

    /**
     * Pega o MinigamePlayer referente ao jogador e se caso não exista cria um
     *
     * @param player Jogador
     * @return Instancia de MinigamePlayer (MP)
     */
    fun getPlayer(player: FakePlayer): MinigamePlayer {
        var member = players[player]
        if (member == null) {
            member = MinigamePlayer()
            member.fakePlayer = player
            players[player] = member
        }



        return member
    }

    fun getPlayer(player: Player): MinigamePlayer {
        return getPlayer(FakePlayer(player))
    }

    /**
     * Verifica se existe o lobby
     *
     * @return
     */
    fun hasLobby(): Boolean {
        return lobby != null
    }

    /**
     * Verifica se o jogador esta no modo Admin
     *
     * @param player Jogador
     * @return
     */
    fun isAdmin(player: Player): Boolean {
        return getPlayer(player).isState(MinigamePlayerState.ADMIN)

    }

    /**
     * Verifica se o Jogador esta no modo Normal (sem ser Admin ou Spectador)
     *
     * @param player Jogador
     * @return
     */
    fun isPlayer(player: Player): Boolean {
        return getPlayer(player).isState(MinigamePlayerState.NORMAL)

    }

    /**
     * Verifica se o jogador esta no Minigame
     *
     * @param player Jogador
     * @return
     */
    fun isPlaying(player: Player): Boolean {
        return getPlayer(player).isPlaying
    }

    /**
     * Verifica se o Jogador esta no modo Spectador
     *
     * @param player Jogador
     * @return
     */
    fun isSpectator(player: Player): Boolean {
        return getPlayer(player).isState(MinigamePlayerState.SPECTATOR)

    }

    /**
     * Verifica se o Estado da Sala principal é igual este estado
     *
     * @param state Estado
     * @return
     */
    fun isState(state: MinigameState): Boolean {
        return game.isState(state)
    }

    /**
     * Entrar em uma Sala
     *
     * @param game   Sala
     * @param player Jogador
     */
    fun joinPlayer(game: MinigameRoom, player: Player) {
        val p = getPlayer(player)
        p.join(game)

    }

    /**
     * Entrar em um Time
     *
     * @param team   Time
     * @param player Jogador
     */
    fun joinPlayer(team: MinigameTeam, player: Player) {
        val p = getPlayer(player)
        p.join(team)
    }

    /**
     * Remover o jogador da sala e do time Atual dele
     *
     * @param player Jogador
     */
    fun leavePlayer(player: Player) {
        val p = getPlayer(player)
        if (p.isPlaying) {
            p.game?.leave(p)
        }

        if (p.hasTeam()) {
            p.team?.leave(p)
        }

    }

    /**
     * Remove o jogador da HashMap de jogadores [MinigamePlayer]
     *
     * @param player Jogador
     */
    fun remove(player: Player) {
        players.remove(FakePlayer(player))
    }

    /**
     * Remove a Sala da lista de salas existentes
     *
     * @param game Sala
     */
    fun removeGame(game: MinigameRoom) {
        this.rooms.remove(game)
    }

    /**
     * Remove a sala pelo seu ID
     *
     * @param id
     */
    fun removeGame(id: Int) {
        val game = getRoom(id)
        this.rooms.remove(game)
    }


    /**
     * Metodo que é executado a cada segundo e executa o metodo de cada sala
     * ` listener(room)`
     */
    override fun run() {
        if (!isEnabled)
            return
        for (room in rooms) {
            if (!room.isEnabled)
                continue
            event(room)
        }
    }

    fun save() {


        try {
            if (plugin == null) return

            var pastaMapas = File(plugin!!.dataFolder, "maps/")
            pastaMapas.mkdirs()

            var pastaSalas = File(plugin!!.dataFolder, "rooms/")
            pastaSalas.mkdirs()
            for (mapa in maps) {

                var configMapa = Config(plugin, "maps/${mapa.name.toLowerCase()}.yml")

                configMapa.set("map", mapa);
                configMapa.saveConfig()

            }
            for (sala in rooms) {
                var configSala = Config(plugin, "rooms/${sala.id}.yml")
                configSala.set("room", sala)
                configSala.saveConfig()

            }

        } catch (erro: Exception) {
            erro.printStackTrace()
        }
    }


    fun reload() {
        var pastaMapas = File(plugin!!.dataFolder, "maps/")
        pastaMapas.mkdirs()

        var pastaSalas = File(plugin!!.dataFolder, "rooms/")
        pastaSalas.mkdirs()



        maps.clear()
        rooms.clear()
        for (arquivoNome in pastaMapas.list()) {
            var configMapa = Config(plugin, "maps/$arquivoNome")
            var mapa = configMapa.get("map", MinigameMap::class.java)
            maps.add(mapa)
        }
        for (arquivoNome in pastaSalas.list()) {
            var configMapa = Config(plugin, "rooms/$arquivoNome")
            var room = configMapa.get("room", MinigameRoom::class.java)
            rooms.add(room)
        }

    }

}