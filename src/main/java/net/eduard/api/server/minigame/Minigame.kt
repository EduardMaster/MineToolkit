package net.eduard.api.server.minigame

import java.util.ArrayList
import org.bukkit.Location
import org.bukkit.entity.Player

import net.eduard.api.lib.manager.TimeManager
import net.eduard.api.lib.config.Config
import net.eduard.api.lib.game.DisplayBoard
import net.eduard.api.lib.game.Kit
import net.eduard.api.lib.modules.BukkitBungeeAPI
import net.eduard.api.lib.modules.FakePlayer
import net.eduard.api.lib.modules.Mine
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.lang.Exception

/**
 * Representa um Minigame ou Evento <br></br>
 * Nomes anteriores<br>
 * MinigameSetup 1.0
 *
 * @version 2.0
 * @since EduardAPI 2.0
 * @author Eduard
 */
@Suppress("unused")
open class Minigame : TimeManager {

    var name = "Minigame"
    var messagePrefix = "[Minigame] "
    var isEnabled = true
    var isBungeecord = false
    var isLobby = false
    var bungeeLobby = "Lobby"
    var scoreboardStarting = DisplayBoard("Minigame iniciando")
    var scoreboardLobby = DisplayBoard("Minigame lobby")
    var scoreboardPlaying = DisplayBoard("Minigame em jogo")
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


    @Transient
    var chests = MinigameChest()

    @Transient
    var chestsFeast = MinigameChest()

    @Transient
    var chestMiniFeast = MinigameChest()

    @Transient
    var kits: MutableList<Kit> = ArrayList()

    @Transient
    open var lobbies: MutableList<MinigameLobby> = ArrayList()

    @Transient
    var maps: MutableList<MinigameMap> = ArrayList()

    @Transient
    open var rooms: MutableList<MinigameRoom> = ArrayList()


    /**
     * Pega a primera sala existente do Minigame
     *
     * @return Sala
     */
    //		getRooms().get(0);
    open val game: MinigameRoom?
        get() = rooms.firstOrNull()

    open val mainLobby: MinigameLobby
        get() = if (lobbies.size > 0) lobbies[0] else newLobby(1)

    /**
     * Pega o mapa referente a sala principal do Minigame
     *
     * @return
     */
    val map: MinigameMap?
        get() = getMap(name)


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


        lobby ?: players.values.forEach { it.player.teleport(lobby) }

    }

    constructor()

    constructor(name: String) {
        this.name = name
        messagePrefix = "§8[§b$name§8]§f"
        lobbies.add(MinigameLobby())
    }

    constructor(name: String, plugin: JavaPlugin) : this(name) {
        this.plugin = plugin
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
    open fun uniqueGame(): MinigameRoom {
        return MinigameRoom(this, MinigameMap(this, name))
    }


    /**
     * Manda mensagem para todos jogadores participando do minigame
     *
     * @param message Mensagem
     */
    fun broadcast(message: String) {
        for (minigamePlayer in players.values) {
            minigamePlayer.sendMessage(messagePrefix + Mine.getReplacers(message, minigamePlayer.player))
        }
    }

    /**
     * Pega a sala pelo seu ID
     *
     * @param id ID
     * @return Sala
     */
    open fun getRoom(id: Int) = rooms.firstOrNull { it.id == id }


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
    open fun createRoom(map: MinigameMap, id: Int) = MinigameRoom(this, map)


    /**
     * Pega a sala que o jogador esta jogando
     *
     * @param player Jogador
     * @return Sala do jogador
     */
    open fun getGame(player: Player): MinigameRoom? {
        return getPlayer(player).game
    }

    /**
     * Pega a sala com o nome do seu mapa igual a este
     *
     * @param name Nome
     * @return
     */
    open fun getGame(name: String): MinigameRoom? {
        for (room in rooms) {
            if (room.map.name.equals(name, ignoreCase = true)) {
                return room
            }
        }
        return null
    }

    open fun newLobby(id: Int): MinigameLobby {

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
        return game?.isState(state) ?: false
    }

    /**
     * Entrar em uma Sala
     *
     * @param game   Sala
     * @param player Jogador
     */
    fun joinPlayer(game: MinigameRoom, player: Player) {
        val minigamePlayer = getPlayer(player)
        minigamePlayer.join(game)

    }

    /**
     * Entrar em um Time
     *
     * @param team   Time
     * @param player Jogador
     */
    fun joinPlayer(team: MinigameTeam, player: Player) {
        val minigamePlayer = getPlayer(player)
        minigamePlayer.join(team)
    }

    /**
     * Remover o jogador da sala e do time Atual dele
     *
     * @param player Jogador
     */
    fun leavePlayer(player: Player) {
        val minigamePlayer = getPlayer(player)
        if (minigamePlayer.isPlaying) {
            minigamePlayer.game?.leave(minigamePlayer)
        }

        if (minigamePlayer.hasTeam()) {
            minigamePlayer.team?.leave(minigamePlayer)
        }

    }

    /**
     * Remove o jogador da HashMap de jogadores [MinigamePlayer]
     *
     * @param player Jogador
     */
    open fun remove(player: Player) {
        players.remove(FakePlayer(player))
    }

    /**
     * Remove a Sala da lista de salas existentes
     *
     * @param game Sala
     */
    open fun removeGame(game: MinigameRoom) {
        this.rooms.remove(game)
    }

    /**
     * Remove a sala pelo seu ID
     *
     * @param id
     */
    open fun removeGame(id: Int) {
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
            this.event(room)
        }
    }

    open fun save() {
        try {
            saveMaps()
            saveLobbies()
            saveRooms()
            saveChests()
            saveKits()
        } catch (erro: Exception) {
            erro.printStackTrace()
        }
    }

    fun saveChests() {
        val configChest = Config(plugin, "chests/normal.yml")
        configChest.set(chests)
        configChest.saveConfig()

        val configFeast = Config(plugin, "chests/feast.yml")
        configFeast.set(chestsFeast)
        configFeast.saveConfig()

        val configMiniFeast = Config(plugin, "chests/mini-feast.yml")
        configMiniFeast.set(chestMiniFeast)
        configMiniFeast.saveConfig()
    }

    fun saveRooms() {
        for (room in rooms) {
            val config = Config(plugin, "rooms/${room.id}.yml")
            config.set(room)
            config.saveConfig()

        }
    }

    fun saveLobbies() {
        for (lobby in lobbies) {

            val config = Config(plugin, "lobby/lobby-${lobby.id}.yml")

            config.set("", lobby)
            config.saveConfig()

        }
    }

    fun saveMaps() {

        for (mapa in maps) {
            val config = Config(plugin, "maps/${mapa.name.toLowerCase()}.yml")
            config.set(mapa)
            config.saveConfig()

        }
    }

    fun saveKits() {
        val kitsConfig = Config(plugin, "kits/kits.yml");
        for (kit in kits) {
            kitsConfig.set(kit.name, kit)
        }
        kitsConfig.saveConfig()
    }

    open fun reload() {
        reloadKits()
        reloadChests()
        reloadLobbies()
        reloadMaps()
        reloadRooms()
    }

    fun reloadKits() {
        kits.clear()
        val kitsConfig = Config(plugin, "kits/kits.yml");
        for (id in kitsConfig.keys) {
            val kit = kitsConfig.get(id, Kit::class.java)
            kits.add(kit)
        }
    }

    fun reloadMaps() {
        val pastaMapas = File(plugin.dataFolder, "maps/")
        pastaMapas.mkdirs()
        for (arquivoNome: String in pastaMapas.list()!!) {
            val config = Config(plugin, "maps/$arquivoNome")
            val mapa = config.get(MinigameMap::class.java)
            mapa.minigame = this
            maps.add(mapa)
        }
    }

    fun reloadLobbies() {
        val pastaLobbies = File(plugin.dataFolder, "lobby/")
        pastaLobbies.mkdirs()
        for (arquivoNome: String in pastaLobbies.list()!!) {
            val config = Config(plugin, "lobby/$arquivoNome")
            val lobby = config.get(MinigameLobby::class.java)

            lobbies.add(lobby)
        }

    }

    fun reloadRooms() {
        val pastaSalas = File(plugin.dataFolder, "rooms/")
        pastaSalas.mkdirs()
        for (arquivoNome: String in pastaSalas.list()!!) {
            val config = Config(plugin, "rooms/$arquivoNome")
            val room = config.get(MinigameRoom::class.java)
            room.minigame = this
            rooms.add(room)
        }
    }


    fun reloadChests() {
        chests = Config(plugin, "chests/normal.yml").get(MinigameChest::class.java)
        chestsFeast = Config(plugin, "chests/feast.yml").get(MinigameChest::class.java)
        chestMiniFeast = Config(plugin, "chests/mini-feast.yml").get(MinigameChest::class.java)

    }

}