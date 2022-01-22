package net.eduard.api.command.api

import net.eduard.api.EduardAPIMain
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.PluginCommand
import org.bukkit.command.SimpleCommandMap
import org.bukkit.event.Event
import org.bukkit.plugin.*
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

/**
 * Esta classe controla os Plugins ela foi criado com ajuda
 * do plugin PlugMan um Gerenciador de Plugins famoso do Spigot/Bukkit org
 *
 */
object PluginsManager {

    private const val pre = "§b[PluginsController]"
    const val red = "§c"
    const val green = "§a"

    private fun getPlugin(pluginName: String): Plugin? {
        for (plugin in Bukkit.getServer().pluginManager.plugins) {
            if (plugin.description.name.equals(pluginName, ignoreCase = true)) {
                return plugin
            }
        }
        return null
    }
    fun loadPlugin(pluginName: String): String? {
        val targetPlugin: Plugin?
        var msg = ""
        val pluginDir = File("plugins")
        if (!pluginDir.isDirectory) {
            return pre + red + "Plugin directory not found!"
        }
        val loader = (EduardAPIMain.getPlugin(EduardAPIMain::class.java) as JavaPlugin).pluginLoader
        var pluginFile = File(pluginDir, "$pluginName.jar")
        if (!pluginFile.isFile) {
            for (file in pluginDir.listFiles()!!) {
                try {
                    if (file.name.endsWith(".jar")) {
                        val pdf = loader
                            .getPluginDescription(file)
                        if (pdf.name.equals(pluginName, ignoreCase = true)) {
                            pluginFile = file
                            msg = "(via search) "
                            break
                        }
                    }
                } catch (e: InvalidDescriptionException) {
                    return pre + red + "Couldn't find file and failed to search descriptions!"
                }
            }
        }
        try {
            Bukkit.getServer().pluginManager.loadPlugin(pluginFile)
            targetPlugin = getPlugin(pluginName)
            Bukkit.getServer().pluginManager.enablePlugin(targetPlugin)
            return pre + green + getPlugin(pluginName) + " loaded " + msg + "and enabled!"
        } catch (e: UnknownDependencyException) {
            return pre + red + "File exists, but is missing a dependency!"
        } catch (e: InvalidPluginException) {
            println("Tried to load invalid Plugin.\n")
            return pre + red + "File exists, but isn't a loadable plugin file!"
        } catch (ignored: InvalidDescriptionException) {
        }
        return pre + red + "Plugin exists, but has an invalid description!"
    }


    private fun unloadPlugin(pl: String): String? {
        val pm = Bukkit.getServer().pluginManager
        val spm = pm as SimplePluginManager
        var cmdMap: SimpleCommandMap? = null
        var plugins: MutableList<Plugin>? = null
        var names: MutableMap<String, Plugin>? = null
        var commands: MutableMap<String, Command>? = null
        var listeners: Map<Event, SortedSet<RegisteredListener>>? = null
        var reloadlisteners = true
        try {
            val pluginsField = spm.javaClass.getDeclaredField("plugins")
            pluginsField.isAccessible = true
            plugins = pluginsField[spm] as MutableList<Plugin>
            val lookupNamesField = spm.javaClass.getDeclaredField("lookupNames")
            lookupNamesField.isAccessible = true
            names = lookupNamesField[spm] as MutableMap<String, Plugin>
            try {
                val listenersField = spm.javaClass.getDeclaredField("listeners")
                listenersField.isAccessible = true
                listeners = listenersField[spm] as Map<Event, SortedSet<RegisteredListener>>
            } catch (e: Exception) {
                reloadlisteners = false
            }
            val commandMapField = spm.javaClass.getDeclaredField("commandMap")
            commandMapField.isAccessible = true
            cmdMap = commandMapField[spm] as SimpleCommandMap
            val knownCommandsField = cmdMap.javaClass.getDeclaredField("knownCommands")
            knownCommandsField.isAccessible = true
            commands = knownCommandsField[cmdMap] as MutableMap<String, Command>
        } catch (e: NoSuchFieldException) {
            return pre + red + "Failed to unload plugin!"
        } catch (e: IllegalAccessException) {
            return pre + red + "Failed to unload plugin!"
        }
        var tp = ""
        for (plugin in Bukkit.getServer().pluginManager.plugins) {
            if (plugin.description.name.equals(pl, ignoreCase = true)) {
                pm.disablePlugin(plugin)
                tp = tp + plugin.name + " "
                if (plugins != null && plugins.contains(plugin)) {
                    plugins.remove(plugin)
                }
                if (names != null && names.containsKey(pl)) {
                    names.remove(pl)
                }
                if (listeners != null && reloadlisteners) {
                    for (set in listeners.values) {
                        val it2 = set.iterator()
                        while (it2.hasNext()) {
                            val value = it2.next() as RegisteredListener
                            if (value.plugin === plugin) {
                                it2.remove()
                            }
                        }
                    }
                }
                val it3: MutableIterator<Map.Entry<String?, Command?>> = commands!!.entries.iterator()
                if (cmdMap != null) {
                    while (it3.hasNext()) {
                        val (_, value) = it3.next()
                        if (value is PluginCommand) {
                            val c = value
                            if (c.plugin === plugin) {
                                c.unregister(cmdMap)
                                it3.remove()
                            }
                        }
                    }
                }
            }
        }
        return pre + green + tp + "has been unloaded and disabled!"
    }
}