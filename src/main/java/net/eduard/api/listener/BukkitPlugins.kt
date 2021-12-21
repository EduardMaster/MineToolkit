package net.eduard.api.listener

import net.eduard.api.EduardAPI
import net.eduard.api.core.Licence
import net.eduard.api.lib.config.Config
import net.eduard.api.lib.manager.EventsManager
import net.eduard.api.server.EduardPlugin
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.server.PluginEnableEvent

class BukkitPlugins : EventsManager() {

    @EventHandler
    fun onPluginActive(e: PluginEnableEvent) {
        if (e.plugin !is EduardPlugin) return
        val plugin = (e.plugin as EduardPlugin)
        if (plugin.isFree) return
        val pluginName = plugin.name
        val tag = "§b[" + plugin.name + "]§f"
        Bukkit.getConsoleSender().sendMessage("$tag Verificando licenca do Plugin no site")
        val config = Config(plugin, "license.yml")
        config.add("key", "INSIRA_KEY")
        config.add("owner", "INSIRA_Dono")
        config.saveConfig()
        val key = config.getString("key")
        val owner = config.getString("owner")
        val asyncCheck = EduardAPI.instance.configs.getBoolean("async-license-check")
        if (asyncCheck)
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, {
                check(plugin, pluginName, owner, key)
            }, 5)
       else check(plugin, pluginName, owner, key)
    }

    fun check(plugin : EduardPlugin, pluginName: String, owner: String, key: String) {
        val tag = "§b[$pluginName]§f"
        val result = Licence.test(pluginName, owner, key);
        Bukkit.getConsoleSender().sendMessage(tag + result.message)
        if (!result.isActive) {
            Bukkit.getPluginManager().disablePlugin(plugin)
        } else {
            Bukkit.getScheduler().runTask(plugin) {
                plugin.onActivation()
            }
        }
    }

}