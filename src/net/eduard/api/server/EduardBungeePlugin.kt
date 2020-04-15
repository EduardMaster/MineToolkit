package net.eduard.api.server

import net.eduard.api.lib.config.Config
import net.eduard.api.lib.database.DBManager
import net.md_5.bungee.BungeeCord
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin

/**
 * Representa os Plugins meus feitos para o Bungeecord
 *
 * @author Eduard
 */
open class EduardBungeePlugin : Plugin(), Listener {
    var isLogEnabled = true
    var isErrorLogEnabled = true
    lateinit var config: Config
    lateinit var messages: Config
    lateinit var db: DBManager
        protected set

    val prefix: String
        get() = "§b[" + description.name + "] "

    fun reloadDBManager() {
        db = config.get("database") as DBManager

    }

    override fun onLoad() {
        config = Config(this, "bungee-config.yml")
        messages = Config(this, "bungee-messages.yml")
        db = DBManager()
        config.add("database", db)
        config.saveConfig()
        reloadDBManager()

    }

    fun message(path: String): String {
        return messages.message(path)
    }

    fun registerEvents(events: Listener) {
        BungeeCord.getInstance().getPluginManager().registerListener(this, events)
    }

    fun registerCommand(comand: Command) {
        BungeeCord.getInstance().getPluginManager().registerCommand(this, comand)
    }

    fun log(msg: String) {
        if (isLogEnabled) {
            console("$prefix§f$msg")
        }
    }

    fun error(msg: String) {
        if (isErrorLogEnabled) {
            console("$prefix§c$msg")
        }
    }

    fun console(msg: String) {
        BungeeCord.getInstance().console.sendMessage(TextComponent(msg))
    }

}
