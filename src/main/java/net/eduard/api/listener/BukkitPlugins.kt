package net.eduard.api.listener

import net.eduard.api.core.Licence
import net.eduard.api.lib.manager.EventsManager
import net.eduard.api.server.EduardPlugin
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.server.PluginEnableEvent
import java.io.File

class BukkitPlugins : EventsManager(){

    @EventHandler
    fun onPluginActive(e : PluginEnableEvent){
        if (e.plugin !is EduardPlugin)return
        val plugin = (e.plugin as EduardPlugin)
        if (plugin.isFree)return
        val pluginName = plugin.name
        val tag = "§b[" + plugin.name + "] §f"
        Bukkit.getConsoleSender().sendMessage("$tag§eFazendo autenticacao do Plugin no site")
        val arquivo = File(plugin.dataFolder, "license.yml")
        val config = YamlConfiguration.loadConfiguration(arquivo)

        config.addDefault("key", "INSIRA_KEY")
        config.addDefault("owner", "INSIRA_Dono")
        config.options().copyDefaults(true)
        config.save(arquivo)
        val key = config.getString("key")
        val owner = config.getString("owner")
        Bukkit.getScheduler().runTaskAsynchronously(plugin) {
            val result = Licence.test(pluginName, owner, key)
            Bukkit.getConsoleSender().sendMessage(tag + result.message)
            if (!result.isActive) {
                Bukkit.getPluginManager().disablePlugin(plugin)
            } else {
                Bukkit.getScheduler().runTask(plugin){
                    plugin.onActivation()
                }
            }
        }
    }




}