package net.eduard.api.task

import net.eduard.api.EduardAPIBungee
import net.eduard.api.lib.modules.Extra
import net.eduard.api.server.EduardBungeePlugin
import net.md_5.bungee.api.ProxyServer

class BungeeDatabaseUpdaterTask : Runnable {
    fun log(msg: String) {
        EduardAPIBungee.instance.log(msg)
    }

    override fun run() {
        for (plugin in ProxyServer.getInstance().pluginManager.plugins) {
            if (plugin !is EduardBungeePlugin) continue
            if (plugin.dbManager.hasConnection()) {
                run {
                    val agora = Extra.getNow()
                    val amountChanges = plugin.sqlManager.runChanges()
                    val tempoDepois = Extra.getNow()
                    val tempoPercorrido = tempoDepois - agora
                    if (amountChanges > 0)
                        plugin.log("Database Update: §e$amountChanges §fChanges in §c${tempoPercorrido}ms")
                }
            }
        }
    }
}