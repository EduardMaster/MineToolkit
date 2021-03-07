package net.eduard.api.listener

import net.eduard.api.core.Licence
import net.eduard.api.server.EduardBungeePlugin
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import java.io.File
import java.util.concurrent.TimeUnit

class BungeePlugins : Runnable{
    fun Configuration.add(key: String, value: Any?) {
        if (!contains(key)) {
            set(key, value)
        }
    }
    private val activated = mutableSetOf<EduardBungeePlugin>()
    private val disabled = mutableSetOf<EduardBungeePlugin>()
    override fun run() {
        for (plugin in ProxyServer.getInstance().pluginManager.plugins){
            if (plugin is EduardBungeePlugin){
                if (plugin.isFree)continue
                if (disabled.contains(plugin))continue
                if (activated.contains(plugin))continue
                val pluginName = plugin.description.name
                ProxyServer.getInstance().console
                    .sendMessage(TextComponent("§aAutenticando o plugin $pluginName"))
                val arquivo = File(plugin.dataFolder, "license.yml")
                val provider =
                    ConfigurationProvider.getProvider(YamlConfiguration::class.java)
                val config = provider.load(arquivo)
                config.add("key", "INSIRA_KEY")
                config.add("owner", "INSIRA_Dono")
                provider.save(config, arquivo)
                val key = config.getString("key")
                val owner = config.getString("owner")
                val tag = "§b[" + plugin.description.name + "] §f"
                ProxyServer.getInstance().scheduler.runAsync(plugin) {
                    val result = Licence.test(pluginName, owner, key)
                    ProxyServer.getInstance().console.sendMessage(TextComponent(tag + result.message))
                    if (!result.isActive) {
                        ProxyServer.getInstance().pluginManager.unregisterListeners(plugin)
                        ProxyServer.getInstance().pluginManager.unregisterCommands(plugin)
                        disabled.add(plugin)
                    } else {
                        ProxyServer.getInstance().scheduler
                            .schedule(plugin, { plugin.onActivation() }, 50, TimeUnit.MILLISECONDS)
                    }
                }
            }
        }
    }


}