package net.eduard.api.task

import net.eduard.api.EduardAPI
import net.eduard.api.lib.manager.TimeManager
import net.eduard.api.lib.modules.Extra
import net.eduard.api.server.EduardPlugin
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class AutoSaveAndBackupTask : TimeManager(20) {
    fun log(msg: String) {
        EduardAPI.instance.log(msg)
    }

    override fun run() {
        for (plugin in Bukkit.getPluginManager().plugins) {

            if (plugin !is EduardPlugin) continue
            val pluginSettings = plugin.settings
            try {
                val agora = Extra.getNow()

                if (pluginSettings.autoSave && pluginSettings.lastSave + pluginSettings.autoBackupSeconds * 1000 < agora) {
                    log("Salvando dados do plugin §b" + plugin.name)
                    val tempo1 = Extra.getNow()
                    plugin.autosave()
                    val tempo2 = Extra.getNow()
                    log(
                        "Tempo levado para salvar os dados do plugin: §a" + (tempo2 - tempo1)
                                + " milisegundos"
                    )
                    log("§7-----")

                }


                if (pluginSettings.autoBackup && pluginSettings.lastBackup + pluginSettings.autoBackupSeconds * 1000 < agora) {
                        log("Iniciando sistema de backup para o plugin §b" + plugin.name)
                        log("Deletando backups dos dias anteriores")
                        val tempo1 = Extra.getNow()
                        plugin.deleteOldBackups()
                        val tempo2 = Extra.getNow()
                        log("Tempo levado para deletar os backups: §a" + (tempo2 - tempo1) + " milisegundos")
                        log("Fazendo backup ")
                        val tempo3 = Extra.getNow()
                        plugin.backup()
                        val tempo4 = Extra.getNow()
                        log(
                            "Backup finalizado tempo levado para fazer: §a" + (tempo4 - tempo3)
                                    + " milisegundos"
                        )
                        log("§7-----")

                }

            } catch (ex: Exception) {
                log("Falha ao tentar salvar dados do plugin ou gerar backup do plugin " + plugin.name)
                ex.printStackTrace()
            }

        }
    }
}
