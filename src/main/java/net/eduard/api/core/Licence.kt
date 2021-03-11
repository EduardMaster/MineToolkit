package net.eduard.api.core

import net.eduard.api.lib.hybrid.Hybrid
import java.io.IOException
import java.net.URL
import java.util.*


@SuppressWarnings("unused")
object Licence {
    private const val DEBUG = true
    private const val SITE = "https://eduard.com.br/license/?"

    private fun checandoIP(){

        val link = "https://eduard.com.br/license/meurealip";
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
        Hybrid.instance.console.sendMessage("Site:$readingSite");

    }

    fun test(plugin: String, owner: String, key: String): PluginActivationStatus {
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
            //Hybrid.instance.console.sendMessage(tag + "Verificando pelo link: "
               //     + link)
            val scan = Scanner(connect.getInputStream())
            val readingSite = StringBuilder()
            while (scan.hasNext()) {
                val text = scan.next()
                readingSite.append(text)
            }
            scan.close()
            try {
                PluginActivationStatus
                    .valueOf(readingSite.toString()
                    .toUpperCase().replace(" ", "_"))
            } catch (ex: Exception) {
                if (DEBUG) {
                    ex.printStackTrace()

                }
                PluginActivationStatus.SITE_OFF
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
            PluginActivationStatus.SITE_OFF
        }
    }

    enum class PluginActivationStatus(val message: String, val isActive: Boolean = false) {
        INVALID_KEY("§cNao foi encontrado esta Licensa no Sistema."),
        SITE_OFF("§eO Sistema de licença nao respondeu, §aplugin ativado para testes", true),
        PLUGIN_ACTIVATED(
            "§aPlugin ativado com sucesso, Licensa permitida.",
            true
        ),
        FOR_TEST("§aO plugin foi liberado para testes no PC do Eduard", true);
    }

}