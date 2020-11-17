package net.eduard.api.core

import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*
import java.util.concurrent.TimeUnit


@SuppressWarnings("unused")
object Licence {
    private const val DEBUG = true
    private const val SITE = "https://eduard.com.br/license/?"


    private fun test(plugin: String, owner: String, key: String): PluginActivationStatus {
        return try {
            val tag = "[$plugin] "
            val link = SITE + "key=" + key + "&plugin=" + plugin + "&owner=" + owner
            val connect = URL(link).openConnection()
            connect.connectTimeout = 2000
            connect.readTimeout = 2000
            connect.addRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0"
            )
            val scan = Scanner(connect.getInputStream())
            val readingSite = StringBuilder()
            while (scan.hasNext()) {
                val text = scan.next()
                readingSite.append(text)
            }
            scan.close()
            try {
                PluginActivationStatus.valueOf(readingSite.toString()
                    .toUpperCase().replace(" ", "_"))
            } catch (ex: Exception) {
                if (DEBUG) {
                    ex.printStackTrace()
                    println(tag + "Verificando pelo link: " + link)
                }
                PluginActivationStatus.ERROR
            }
        } catch (ex: IOException) {
            if (DEBUG) {
                ex.printStackTrace()
            }
            PluginActivationStatus.SITE_OFF
        } catch (ex: Exception) {
            if (DEBUG) {
                ex.printStackTrace()
            }
            PluginActivationStatus.ERROR
        }
    }

    internal enum class PluginActivationStatus(val message: String, val isActive: Boolean = false) {
        INVALID_KEY("§cNao foi encontrado esta Licensa no Sistema."),
        WRONG_KEY("§cNao possui esta Licensa no Sistema."),
        KEY_TO_WRONG_PLUGIN("§cA Licensa usada nao serve para este plugin."),
        KEY_TO_WRONG_OWNER("§cA Licensa usada nao serve para este Dono"),
        INVALID_IP("§cEste IP usado nao corresponde ao IP da Licença"),
        ERROR("§cO plugin nao ativou pois deu algum tipo de erro ao receber a resposta do Site"),
        SITE_OFF("§eO Sistema de licença nao respondeu, §aplugin ativado para testes", true),
        PLUGIN_ACTIVATED(
            "§aPlugin ativado com sucesso, Licensa permitida.",
            true
        ),
        FOR_TEST("§aO plugin foi liberado para testes no PC do Eduard", true);

    }

    object BukkitLicense {
        fun test(plugin: JavaPlugin, activation: Runnable) {
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
                val result = test(pluginName, owner, key)
                Bukkit.getConsoleSender().sendMessage(tag + result.message)
                if (!result.isActive) {
                    Bukkit.getPluginManager().disablePlugin(plugin)
                } else {
                    Bukkit.getScheduler().runTask(plugin, activation)
                }
            }
        }
    }

    object BungeeLicense {

        fun Configuration.add(key: String, value: Any?) {
            if (!contains(key)) {
                set(key, value)
            }
        }

        fun test(plugin: Plugin, activation: Runnable) {
            val pluginName = plugin.description.name
            ProxyServer.getInstance().console
                .sendMessage(TextComponent("§aAutenticando o plugin $pluginName"))
            val arquivo = File(plugin.dataFolder, "license.yml")
            val provider =
                ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration::class.java)
            val config = provider.load(arquivo)
            config.add("key", "INSIRA_KEY")
            config.add("owner", "INSIRA_Dono")
            provider.save(config, arquivo)
            val key = config.getString("key")
            val owner = config.getString("owner")
            val tag = "§b[" + plugin.description.name + "] §f"
            ProxyServer.getInstance().scheduler.runAsync(plugin) {
                val result = test(pluginName, owner, key)
                ProxyServer.getInstance().console.sendMessage(TextComponent(tag + result.message))
                if (!result.isActive) {
                    ProxyServer.getInstance().pluginManager.unregisterListeners(plugin)
                    ProxyServer.getInstance().pluginManager.unregisterCommands(plugin)
                } else {
                    ProxyServer.getInstance().scheduler
                        .schedule(plugin ,activation, 50, TimeUnit.MILLISECONDS)

                }
            }
        }
    }
}