package net.eduard.api.task

import net.eduard.api.EduardAPI
import net.eduard.api.lib.manager.TimeManager
import net.eduard.api.lib.modules.Extra
import net.eduard.api.server.EduardPlugin
import org.bukkit.Bukkit

class AutoSaveAndBackupTask : TimeManager(20L) {
    fun log(msg: String) {
        EduardAPI.instance.log(msg)
    }

    override fun run() {
        for (plugin in Bukkit.getPluginManager().plugins) {
            if (plugin !is EduardPlugin) continue
            val pluginSettings = plugin.settings
            val pluginName = "§e(${plugin.name})"

            try {
                val agora = Extra.getNow()
                val canRunSaveNow = pluginSettings.lastSave + pluginSettings.autoBackupSeconds * 1000 < agora
                if (pluginSettings.isAutoSave && canRunSaveNow) {
                    log("$pluginName§f Salvando dados do plugin")
                    val inicioSave = Extra.getNow()
                    plugin.autosave()
                    val fimSave = Extra.getNow()
                    log("$pluginName§f Tempo levado para salvar §e" + (fimSave - inicioSave) + "§fms")

                }
            } catch (ex: Exception) {
                log("$pluginName §cFalha ao rodar metodo save()")
                ex.printStackTrace()
            }
            try {
                val agora = Extra.getNow()
                val canBackupNow = pluginSettings.lastBackup + pluginSettings.autoBackupSeconds * 1000L < agora
                if (canBackupNow) {
                    log("$pluginName§f Gerando Backup")
                    val inicioBackup = Extra.getNow()
                    plugin.backup()
                    val fimBackup = Extra.getNow()
                    log("$pluginName§f Backup gerado com: §e" + (fimBackup - inicioBackup) + "§fms")
                    return
                }
            } catch (ex: Exception) {
                plugin.creatingBackup=false
                log("$pluginName §cFalha ao rodar metodo backup()")
                ex.printStackTrace()
            }


        }
    }
}
