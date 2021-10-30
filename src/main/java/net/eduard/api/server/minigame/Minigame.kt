package net.eduard.api.server.minigame

import org.bukkit.Location
import org.bukkit.entity.Player

import net.eduard.api.lib.manager.TimeManager
import net.eduard.api.lib.kotlin.reloadListFromFolder
import net.eduard.api.lib.kotlin.saveListInFolder
import net.eduard.api.lib.modules.BukkitBungeeAPI
import net.eduard.api.lib.modules.FakePlayer
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.score.DisplayBoard
import net.eduard.api.lib.storage.annotations.StorageIndex
import java.io.File

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

open class Minigame(
    @StorageIndex
    var name: String,
    var messagePrefix: String
) : TimeManager() {

    constructor() : this( "Minigame")
    constructor(name: String) : this(name,"§8[§b$name§8]§f")

    var isEnabled = true
    var isBungeecord = false
    var schematicBased = true

    var worldBased
        get() = !schematicBased
        set(value) {
            schematicBased = !value
        }
    var schematicBasedRestartWorld = false
    var isLobby = false
    var bungeeLobby = "Lobby"
    var scoreboardLobby = DisplayBoard("Minigame lobby")
    var maxPlayersPerLobby = 20

    @Transient
    var setting: MinigameMap? = null
    var lobby: Location? = null
    var mapsWorld = MinigameWorld("worlds/$name/map_creation")

    @Transient
    var players: MutableMap<FakePlayer, MinigamePlayer> = mutableMapOf()


    @Transient
    var lobbies = mutableListOf<MinigameLobby>()

    @Transient
    var maps = mutableListOf<MinigameMap>()

    @Transient
    var rooms = mutableListOf<MinigameRoom>()

    @Transient
    var modes = mutableListOf<MinigameMode>()

    /**
     * Pega a primera sala existente do Minigame
     *
     * @return Sala
     */
    //		getRooms().get(0);
    open val game: MinigameRoom?
        get() = rooms.firstOrNull()

    open val mainLobby: MinigameLobby
        get() = if (lobbies.isNotEmpty()) lobbies[0] else createLobby(1)
    open val mode: MinigameMode?
        get() = modes.firstOrNull()

    /**
     * Pega o mapa referente a sala principal do Minigame
     *
     * @return
     */
    val map: MinigameMap?
        get() = getMap(name)

    fun getMode(name: String) = modes.firstOrNull { it.name.equals(name, true) }


    val isSetting: Boolean
        get() = setting != null

    /**
     * Conecta todos jogadores no servidor Lobby
     */
    open fun connectAllPlayersToLobby() {
        for (player in players.values) {
            BukkitBungeeAPI.connectToServer(player.player, bungeeLobby)
        }

    }

    /**
     * Registra o Modo de Jogo
     */
    fun register(mode: MinigameMode) {
        modes.add(mode)
        mode.minigame = this

    }

    /**
     * Teleporta todos os jogadores para o Local do Lobby
     */
    fun teleportAllPlayersToLobby() {
        lobby ?: players.values.forEach { it.player.teleport(lobby) }
    }


    /**
     * Cria um Mapa
     *
     * @param nome Nome
     * @return Mapa Novo
     */
    fun createMap(nome: String): MinigameMap {
        val map = MinigameMap(nome)
        map.minigame = this
        maps.add(map)
        return map
    }


    /**
     * Pega o mapa existente pelo seu nome
     *
     * @param name Nome
     * @return Mapa
     */
    open fun getMap(name: String): MinigameMap? {
        return maps.firstOrNull { it.name.equals(name, true) }
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
     * @return se encontrou o mapa
     */
    fun hasMap(name: String): Boolean {
        return getMap(name) != null
    }

    /**
     * Cria um sala unica e um mapa unico Também usando o Nome do Minigame
     *
     * @return Minigame criado com mapa já configurado
     */
    open fun uniqueGame(): MinigameRoom {
        return createRoom(createMap(name), mode!!)
    }


    /**
     * Manda mensagem para todos jogadores participando do minigame
     *
     * @param message Mensagem
     */
    fun broadcast(message: String) {
        for (minigamePlayer in players.values) {
            minigamePlayer.sendMessage(
                messagePrefix +
                        Mine.getReplacers(message, minigamePlayer.player)
            )

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
    open fun hasRoom(id: Int) = getRoom(id) != null


    /**
     * Cria uma Sala com este ID para o Mapa expecifico
     *
     * @param map Mapa
     * @param id  ID
     * @return Nova Sala
     */
    open fun createRoom(map: MinigameMap, mode: MinigameMode): MinigameRoom {
        val room = MinigameRoom()
        room.minigame = this
        room.id = rooms.size + 1
        room.mode = mode
        rooms.add(room)
        room.map = map
        this.isEnabled = true
        return room
    }


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

    /**
     * Cria um lobby com ID novo
     */
    open fun createLobby(id: Int): MinigameLobby {

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

    /**
     * Alias para o getPlayer(FakePlayer())
     */
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
     * @return se sim ou não
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
            room.mode.event(room)
        }
    }

    open fun save() {
        saveLobbies()
        saveModes()
        saveMaps()
        saveRooms()

    }

    fun saveModes() {
        for (mode in modes) {
            mode.saveChests()
            mode.saveKits()
        }
        File(plugin.dataFolder, "modes/")
            .saveListInFolder(modes) { it.modeName }
    }


    fun saveRooms() {
        File(plugin.dataFolder, "rooms/")
            .saveListInFolder(rooms) { it.id.toString() }
    }
    open fun hasMinPlayerAmount(room : MinigameRoom) : Boolean{
        return room.getPlayers(MinigamePlayerState.NORMAL).size >= room.map.minPlayersAmount
    }

    fun saveLobbies() {
        File(plugin.dataFolder, "lobby/")
            .saveListInFolder(lobbies) {
                it.id.toString()
            }

    }


    fun saveMaps() {
        File(plugin.dataFolder, "maps/").saveListInFolder(maps) {
            name.toLowerCase()
                .replace(" ", "_")
        }

    }

    open fun reload() {
        reloadLobbies()
        reloadMaps()
        reloadModes()
        reloadRooms()
    }

    fun reloadModes() {
        modes.clear()
        modes.addAll(File(plugin.dataFolder, "modes/").reloadListFromFolder())
        modes.forEach {
            it.minigame = this
            it.reloadChests()
            it.reloadKits()
        }

    }

    fun reloadMaps() {
        maps.clear()
        maps.addAll(File(plugin.dataFolder, "maps/").reloadListFromFolder())
        maps.forEach { it.minigame = this }
    }

    fun reloadLobbies() {
        lobbies.clear()
        lobbies.addAll(File(plugin.dataFolder, "lobby/").reloadListFromFolder())
    }


    fun reloadRooms() {
        rooms.clear()
        rooms.addAll(File(plugin.dataFolder, "rooms/").reloadListFromFolder())
        rooms.forEach { it.minigame = this }
    }


}