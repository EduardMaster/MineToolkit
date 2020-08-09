package net.eduard.api.server

import net.eduard.api.lib.config.BukkitConfig
import net.eduard.api.lib.config.BungeeConfig
import net.md_5.bungee.BungeeCord
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Plugin
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException
import java.net.URL
import java.util.*


@SuppressWarnings("unused")
object Licence {
    private const val DEBUG = true
    private const val SITE = "http://eduard.com.br/license/?"


    private fun test(plugin: String, owner: String, key: String): PluginActivationStatus {
        return try {
            val tag = "[$plugin] "
            val link = SITE + "key=" + key + "&plugin=" + plugin + "&owner=" + owner
            val connect = URL(link).openConnection()
            connect.connectTimeout = 5000
            connect.readTimeout = 5000
            connect.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0")
            val scan = Scanner(connect.getInputStream())
            val b = StringBuilder()
            while (scan.hasNext()) {
                val text = scan.next()
                b.append(text)
            }
            scan.close()
            try {
                PluginActivationStatus.valueOf(b.toString().toUpperCase().replace(" ", "_"))
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
        INVALID_IP("§cEste IP usado nao corresponde ao IP da Licensa"),
        ERROR("§cO plugin nao ativou pois deu algum tipo de erro ao receber a resposta do Site"),
        SITE_OFF("§eO Sistema de licença nao respondeu, §aplugin ativado para testes", true),
        PLUGIN_ACTIVATED("§aPlugin ativado com sucesso, Licensa permitida.", true), FOR_TEST("§aO plugin foi liberado para testes no PC do Eduard", true);

    }

     object BukkitLicense {
        fun test(plugin: JavaPlugin, activation: Runnable) {
            val pluginName = plugin.name
            val tag = "§b[" + plugin.name + "] §f"
            Bukkit.getConsoleSender().sendMessage("$tag§eFazendo autenticacao do Plugin no site")
            val config = BukkitConfig("license.yml", plugin)
            config.add("key", "INSIRA_KEY")
            config.add("owner", "INSIRA_Dono")
            config.saveDefault()
            val key = config.getString("key")
            val owner = config.getString("owner")
            Bukkit.getScheduler().runTaskAsynchronously(plugin) {
                val result = test(pluginName, owner, key)
                Bukkit.getConsoleSender().sendMessage(tag + result.message)
                if (!result.isActive) {
                    Bukkit.getPluginManager().disablePlugin(plugin)
                } else {
                    activation.run()
                }
            }
        }
    }

     object BungeeLicense {
        fun test(plugin: Plugin, activation: Runnable) {
            val pluginName = plugin.description.name
            BungeeCord.getInstance().console
                    .sendMessage(TextComponent("§aAutenticando o plugin $pluginName"))
            val config = BungeeConfig("license.yml", plugin)
            config.add("key", "INSIRA_KEY")
            config.add("owner", "INSIRA_Dono")
            config.saveConfig()
            val key = config.getString("key")
            val owner = config.getString("owner")
            val tag = "§b[" + plugin.description.name + "] §f"
            BungeeCord.getInstance().scheduler.runAsync(plugin) {
                val result = test(pluginName, owner, key)
                BungeeCord.getInstance().console.sendMessage(TextComponent(tag + result.message))
                if (!result.isActive) {
                    BungeeCord.getInstance().getPluginManager().unregisterListeners(plugin)
                    BungeeCord.getInstance().getPluginManager().unregisterCommands(plugin)
                } else {
                    activation.run()
                }
            }
        }
    }
}