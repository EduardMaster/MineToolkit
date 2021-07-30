package net.eduard.api.task

import net.eduard.api.EduardAPIBungee
import net.eduard.api.lib.modules.Extra
import net.eduard.api.server.EduardBungeePlugin
import net.md_5.bungee.api.ProxyServer

class BungeeDatabaseUpdater : Runnable {
    fun log(msg: String) {
        EduardAPIBungee.instance.log(msg)
    }

    override fun run() {
        for (plugin in ProxyServer.getInstance().pluginManager.plugins) {
            if (plugin !is EduardBungeePlugin) continue
            if (plugin.dbManager.hasConnection()) {
                val name = plugin.pluginName
                run {
                    val agora = Extra.getNow()
                    val amountUpdated = plugin.sqlManager.runUpdatesQueue()
                    val tempoDepois = Extra.getNow()
                    val tempoPassado = tempoDepois - agora
                    if (amountUpdated > 0)
                        log("Atualizando $amountUpdated objetos na tabela (tempo levado: ${tempoPassado}ms) do plugin $name")
                }
                run {
                    val agora = Extra.getNow()
                    val amountDeleted = plugin.sqlManager.runDeletesQueue()
                    val tempoDepois = Extra.getNow()
                    val tempoPassado = tempoDepois - agora
                    if (amountDeleted > 0)
                        log("Deletando $amountDeleted objetos na tabela (tempo levado: ${tempoPassado}ms) do plugin $name")
                }


            }
        }
    }
}