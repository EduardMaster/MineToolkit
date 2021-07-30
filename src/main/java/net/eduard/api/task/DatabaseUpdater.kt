package net.eduard.api.task

import net.eduard.api.EduardAPI
import net.eduard.api.lib.manager.TimeManager
import net.eduard.api.lib.modules.Extra
import net.eduard.api.server.EduardPlugin
import org.bukkit.Bukkit

class DatabaseUpdater : TimeManager(20) {
    fun log(msg: String) {
        EduardAPI.instance.log(msg)
    }

    override fun run() {
        for (plugin in Bukkit.getPluginManager().plugins) {
            if (plugin !is EduardPlugin) continue
            if (plugin.dbManager.hasConnection()) {
                val name = plugin.name

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