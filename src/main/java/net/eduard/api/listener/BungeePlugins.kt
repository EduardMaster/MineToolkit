package net.eduard.api.listener

import net.eduard.api.core.Licence
import net.eduard.api.lib.config.Config
import net.eduard.api.server.EduardBungeePlugin
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.config.Configuration
import java.util.concurrent.TimeUnit

class BungeePlugins : Runnable, Listener {
    fun Configuration.add(key: String, value: Any?) {
        if (!contains(key)) {
            set(key, value)
        }
    }

    private val activated = mutableSetOf<EduardBungeePlugin>()
    private val disabled = mutableSetOf<EduardBungeePlugin>()
    private val verifying = mutableSetOf<EduardBungeePlugin>()
    override fun run() {
        for (plugin in ProxyServer.getInstance().pluginManager.plugins) {
            if (plugin !is EduardBungeePlugin) continue
            if (plugin.isFree) continue
            if (disabled.contains(plugin)) continue
            if (activated.contains(plugin)) continue
            if (verifying.contains(plugin)) continue
            val pluginName = plugin.description.name
            val tag = "§b[" + plugin.description.name + "] §f"
            ProxyServer.getInstance().console
                .sendMessage(TextComponent("${tag}Verificando licenca do Plugin no site"))
            val config = Config(plugin, "license.yml")
            config.add("key", "INSIRA_KEY")
            config.add("owner", "INSIRA_Dono")
            config.saveConfig()
            val key = config.getString("key")
            val owner = config.getString("owner")
            verifying.add(plugin)
            ProxyServer.getInstance().scheduler.runAsync(plugin) {
                val result = Licence.test(pluginName, owner, key)
                ProxyServer.getInstance().console.sendMessage(TextComponent(tag + result.message))
                if (!result.isActive) {
                    ProxyServer.getInstance().pluginManager.unregisterListeners(plugin)
                    ProxyServer.getInstance().pluginManager.unregisterCommands(plugin)
                    disabled.add(plugin)
                } else {
                    activated.add(plugin)
                    ProxyServer.getInstance().scheduler
                        .schedule(plugin, { plugin.onActivation() }, 50, TimeUnit.MILLISECONDS)
                }
                verifying.remove(plugin)
            }

        }
    }

}