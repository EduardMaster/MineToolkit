package net.eduard.api

import net.eduard.api.command.*
import net.eduard.api.command.api.ApiCommand
import net.eduard.api.command.map.MapCommand
import net.eduard.api.core.BukkitInfoGenerator
import net.eduard.api.core.BukkitReplacers
import net.eduard.api.core.PlayerSkin
import net.eduard.api.hooks.JHCashHook
import net.eduard.api.lib.bungee.BungeeAPI
import net.eduard.api.lib.bungee.ServerSpigot
import net.eduard.api.lib.config.Config
import net.eduard.api.lib.config.StorageManager
import net.eduard.api.lib.database.BukkitTypes
import net.eduard.api.lib.database.DBManager
import net.eduard.api.lib.database.SQLManager
import net.eduard.api.server.minigame.MinigameSchematic
import net.eduard.api.lib.game.SoundEffect
import net.eduard.api.lib.hybrid.BukkitServer
import net.eduard.api.lib.hybrid.Hybrid
import net.eduard.api.lib.kotlin.store
import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.menu.*
import net.eduard.api.lib.modules.*
import net.eduard.api.lib.plugin.IPlugin
import net.eduard.api.lib.plugin.PluginSettings
import net.eduard.api.lib.score.DisplayBoard
import net.eduard.api.lib.storage.StorageAPI
import net.eduard.api.lib.storage.storables.BukkitStorables
import net.eduard.api.listener.*
import net.eduard.api.server.currency.CurrencyManager
import net.eduard.api.supports.CurrencyVaultEconomy
import net.eduard.api.supports.CurrencyJHCash
import net.eduard.api.task.AutoSaveAndBackupTask
import net.eduard.api.task.DatabaseUpdater
import net.eduard.api.task.MenuAutoUpdaterTask
import net.eduard.api.task.PlayerTargetPlayerTask
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

/**
 * Classe Principal do EduardAPI
 *
 * @author Eduard
 * @version 1.3
 * @since 0.5
 */
class EduardAPI(private val plugin: JavaPlugin) : IPlugin, BukkitTimeHandler {

    override var started = false
    override var configs = Config(plugin, "config.yml")
    override var storage = Config(plugin, "storage.yml")
    override var messages = Config(plugin, "messages.yml")
    override lateinit var settings: PluginSettings
    override var dbManager = DBManager()
    override lateinit var sqlManager: SQLManager
    override lateinit var storageManager: StorageManager
    override fun onLoad() {
        StorageAPI.setDebug(false)
        instance = this
        super.onLoad()
        BukkitTypes


    }


    override fun deleteOldBackups() {

    }

    override fun backup() {

    }


    override val pluginFolder: File
        get() = plugin.dataFolder

    override fun log(message: String) {
        console("§f$message")
    }

    override fun console(message: String) {
        Bukkit.getConsoleSender().sendMessage("§b[EduardAPI]§r $message")
    }

    override fun error(message: String) {
        console("§c$message")
    }

    fun storage() {
        StorageAPI.setDebug(configs.getBoolean("debug-storage"))
        log("Registrando classes da EduardLIB")
        //StorageAPI.registerPackage(javaClass, "net.eduard.api.lib")
        store<Product>()
        store<MenuButton>()
        store<Shop>()
        store<Menu>()
        store<Slot>()
        //StorageAPI.registerPackage(Minigame::class.java)
        BukkitStorables.load()
        StorageAPI.startGson()
        log("Storables do Bukkit carregado!")
    }


    override fun onEnable() {
        if (!started) {
            this.onLoad()
        }
        MinigameSchematic.MAPS_FOLDER = File(instance.pluginFolder, "maps/")
        storage()
        VaultAPI.setupVault()
        BukkitBungeeAPI.register(plugin)
        BukkitBungeeAPI.requestCurrentServer()
        BungeeAPI.bukkit.register(plugin)

        reload()
        commands()
        events()
        tasks()
        loadServers()
        log("§aCarregado com sucesso!")


    }


    fun resetScoreboards() {
        for (teams in Mine.getMainScoreboard().teams) {
            teams.unregister()
        }
        for (objective in Mine.getMainScoreboard().objectives) {
            objective.unregister()
        }
        for (player in Mine.getPlayers()) {
            player.scoreboard = Mine.getMainScoreboard()
            player.maxHealth = 20.0
            player.health = 20.0
            player.isHealthScaled = false
        }
    }

    fun tasks() {
        resetScoreboards()
        log("Scoreboards dos jogadores online resetadas!")

        BukkitReplacers()
        log("Replacers ativados")

        log("Ativando Sistema atualizador de menus abertos")

        if (getBoolean("menu-auto-updater"))
            MenuAutoUpdaterTask().asyncTimer()

        BukkitInfoGenerator(this)
        log("Base de dados de Enums do Bukkit gerado com sucesso")

        log("Carregando Moedas")
        store<CurrencyJHCash>()
        store<CurrencyVaultEconomy>()
        CurrencyManager.register(CurrencyVaultEconomy())
        JHCashHook()
        log("Moedas carregadas")

        log("Ativando tasks (Timers)")
        // Na versão 1.16 precisa ser em Sync não pode ser Async
        PlayerTargetPlayerTask().asyncTimer()
        AutoSaveAndBackupTask().asyncTimer()
        DatabaseUpdater().asyncTimer()
        log("Tasks ativados com sucesso")
    }

    fun events() {
        log("Ativando listeners dos Eventos")
        EduardAPIListener().register(this)
        HooksListener().register(this)
        EduWorldEditListener().register(this)
        PlayerTargetListener().register(this)
        BukkitPlugins().register(this)
        log("Listeners dos Eventos ativados com sucesso")
    }

    fun commands() {
        log("Ativando comandos")
        ApiCommand().register()
        MapCommand().register()
        EnchantCommand().register()
        GotoCommand().register()
        SoundCommand().register()
        SetXPCommand().register()
        SetSkinCommand().register()
        MemoryCommand().register()
        MacroCommand().registerCommand(plugin)
        RunCommand().registerCommand(plugin)
        log("Comandos ativados com sucesso")
    }

    fun loadServers() {
        if (sqlManager.hasConnection()) {
            sqlManager.cacheInfo()
            log("Carregando infos dos servidores")
            sqlManager.createTable(ServerSpigot::class.java)
            for (server in sqlManager.getAll<ServerSpigot>()) {
                BungeeAPI.servers[server.name.toLowerCase()] = server
            }
        }
    }


    override fun reload() {
        log("Inicio do Recarregamento do EduardAPI")
        configs.reloadConfig()
        messages.reloadConfig()
        configDefault()
        log("Ativando debug de sistemas caso marcado na config como 'true'")
        StorageAPI.setDebug(configs.getBoolean("debug.storage"))
        DBManager.setDebug(configs.getBoolean("debug.database"))
        Config.isDebug = configs.getBoolean("debug.config")
        Menu.isDebug = configs.getBoolean("debug.menu")
        CommandManager.isDebug = configs.getBoolean("debug.commands")
        Copyable.setDebug(configs.getBoolean("debug.copyable"))
        BukkitBungeeAPI.setDebug(configs.getBoolean("debug.bungee-bukkit"))
        Mine.OPT_DEBUG_REPLACERS = configs.getBoolean("debug.replacers")
        PlayerSkin.reloadSkins()
        MineReflect.MSG_ITEM_STACK = configs.message("stack-design")
        loadMaps()

        configs.add("sound-teleport", OPT_SOUND_TELEPORT)
        configs.add("sound-error", OPT_SOUND_ERROR)
        configs.add("sound-success", OPT_SOUND_SUCCESS)
        configs.add("show-plugins-on-join", true)
        configs.saveConfig()
        Mine.OPT_AUTO_RESPAWN = configs.getBoolean("auto-respawn")
        Mine.OPT_NO_JOIN_MESSAGE = configs.getBoolean("no-join-message")
        Mine.OPT_NO_QUIT_MESSAGE = configs.getBoolean("no-quit-message")
        Mine.OPT_NO_DEATH_MESSAGE = configs.getBoolean("no-death-message")

        try {
            log("Carregando formatador de dinheiro")
            val format = configs.getString("money-format")
            val locale =
                DecimalFormatSymbols.getInstance(Locale.forLanguageTag(configs.getString("money-format-locale")))

            Extra.MONEY = DecimalFormat(
                format,
                locale
            )
            log("Formatador de dinheiro: $format")
            log("Locale do Formatador de dinheiro: $locale")
            log("Formatando numero 1m: " + Extra.MONEY.format(1000000))
            log("Formatando numero 1000,50: " + Extra.MONEY.format(1000.500))
        } catch (e: Exception) {
            error("Formato do dinheiro invalido " + configs.getString("money-format"))

        }
        MSG_ON_JOIN = configs.message("on-join-message")
        MSG_ON_QUIT = configs.message("on-quit-message")
        OPT_SOUND_TELEPORT = configs["sound-teleport", SoundEffect::class.java]
        OPT_SOUND_ERROR = configs["sound-error", SoundEffect::class.java]
        OPT_SOUND_SUCCESS = configs["sound-success", SoundEffect::class.java]
        DisplayBoard.colorFix = configs.getBoolean("scoreboard.color-fix")
        DisplayBoard.nameLimit = configs.getInt("scoreboard.name-limit")
        DisplayBoard.prefixLimit = configs.getInt("scoreboard.prefix-limit")
        DisplayBoard.suffixLimit = configs.getInt("scoreboard.suffix-limit")

        log("Recarregamento do EduardAPI concluido.")
    }


    override fun configDefault() {
        configs.add("debug.config", false)
        configs.add("debug.bungee-bukkit", false)
        configs.add("debug.storage", false)
        configs.add("debug.copyable", false)
        configs.add("debug.commands", false)
        configs.add("debug.replacers", false)
        configs.add("debug.database", false)
        configs.add("debug.menu", false)
        configs.add("menu-auto-updater", false)
        configs.add("scoreboard.name-limit", 40)
        configs.add("scoreboard.prefix-limit", 16)
        configs.add("scoreboard.suffix-limit", 16)
        configs.add("scoreboard.color-fix", true)
        configs.add("async-license-check", false);
        configs.add("skins", false)
        configs.add("auto-respawn", true)
        configs.add("custom-skin", "EduardKillerPro")
        configs.add("on-target.show-text" , false)
        configs.add("on-target.text", "{player_name} - {player_level}")
        configs.add("stack-design", "§aQuantidade: §f\$stack")
        configs.add("money-format", "###,###.##")
        configs.add("money-format-locale", "PT-BR")
        configs.add("no-join-message", true)
        configs.add("custom-join-message", false)
        configs.add("custom-first-join-message", false)
        configs.add("on-join-message", "&6O jogador &e\$player &6entrou no server")
        configs.add("first-join-message", "&6Seja bem vindo ao servidor!!")
        configs.add("no-quit-message", true)
        configs.add("custom-quit-message", false)
        configs.add("on-quit-message", "&6O jogador &e\$player &6saiu do server")
        configs.add("no-quit-message", true)
        configs.add("no-death-message", true)
        configs.add("custom-death-message", false)
        configs.add("on-death-player-message", "&6O jogador &e\$killer matou o jogador \$player")

        configs.add("custom-motd", false)
        configs.add("custom-motd-amount", -1)
        configs.add("motd", listOf("Seja bem vindo", "Ao meu servidor!"))

        configs.saveConfig()


    }

    override fun save() {

    }


    override val pluginName: String
        get() = plugin.name


    override fun onDisable() {

        PlayerSkin.saveSkins()
        saveMaps()
        log("desativado com sucesso!")
        BungeeAPI.controller.unregister()
        unregisterCommands()
        unregisterTasks()
        unregisterListeners()
        unregisterServices()
        MinigameSchematic.unloadAll()
        sqlManager.dbManager.closeConnection()
    }

    override fun unregisterTasks() {

    }

    override fun unregisterListeners() {

    }

    override fun unregisterServices() {

    }

    override fun unregisterCommands() {
        for ((name, cmd) in CommandManager.commandsRegistred) {
            log("Comando $name desregisrado")
            cmd.unregisterCommand()
            cmd.unregisterListener()
        }
        CommandManager.commandsRegistred.clear()
    }


    override fun getPlugin(): Plugin {
        return this.plugin
    }

    companion object {

        lateinit var instance: EduardAPI


        init {
            Hybrid.instance = BukkitServer
        }

        /**
         * Som para o Teleporte
         */
        var OPT_SOUND_TELEPORT = SoundEffect.create("ENDERMAN_TELEPORT")

        /**
         * Som para algum sucesso
         */
        var OPT_SOUND_SUCCESS = SoundEffect.create("LEVEL_UP")

        /**
         * Som para algum erro
         */
        var OPT_SOUND_ERROR = SoundEffect.create("NOTE_BASS_DRUM")


        /*

        Som do rosnar do gato
        private val ROSNAR = SoundEffect.create("CAT_PURR")

        private val VALUE_TNT_POWER = 4f

        private val VALUE_CREEPER_POWER = 3f

        private val VALUE_WALKING_VELOCITY = -0.08f

        private val DAY_IN_HOUR = 24

        private val DAY_IN_MINUTES = DAY_IN_HOUR * 60

        private val DAY_IN_SECONDS = DAY_IN_MINUTES * 60

        private val DAY_IN_TICKS = (DAY_IN_SECONDS * 20).toLong()

        private val DAY_IN_MILLIS = DAY_IN_TICKS * 50

         */
        /**
         * Mensagem de quando Entrar no Servidor
         */
        var MSG_ON_JOIN = "§6O jogador \$player entrou no Jogo!"

        /**
         * Mensagem de quando Sair do Servidor
         */
        var MSG_ON_QUIT = "§6O jogador \$player saiu no Jogo!"


        fun loadMaps() {
            MinigameSchematic.loadAll(MinigameSchematic.MAPS_FOLDER)
            instance.log("Mapas carregados!")
        }


        /**
         * Salva todos os mapas no sistema de armazenamento
         */
        fun saveMaps() {
            MinigameSchematic.saveAll(MinigameSchematic.MAPS_FOLDER)
            instance.log("Mapas salvados!")
        }
    }

    override fun getPluginConnected(): Plugin {
        return plugin
    }

}
