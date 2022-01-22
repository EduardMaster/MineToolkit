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


    fun unloadPlugin(pluginName: String): String? {
        val pluginManager = Bukkit.getServer().pluginManager
        val serverPluginManager = pluginManager as SimplePluginManager
        var cmdMap: SimpleCommandMap? = null
        var plugins: MutableList<Plugin>? = null
        var names: MutableMap<String, Plugin>? = null
        var commands: MutableMap<String, Command>? = null
        var listeners: Map<Event, SortedSet<RegisteredListener>>? = null
        var reloadlisteners = true
        try {
            val pluginsField = serverPluginManager.javaClass.getDeclaredField("plugins")
            pluginsField.isAccessible = true
            plugins = pluginsField[serverPluginManager] as MutableList<Plugin>
            val lookupNamesField = serverPluginManager.javaClass.getDeclaredField("lookupNames")
            lookupNamesField.isAccessible = true
            names = lookupNamesField[serverPluginManager] as MutableMap<String, Plugin>
            try {
                val listenersField = serverPluginManager.javaClass.getDeclaredField("listeners")
                listenersField.isAccessible = true
                listeners = listenersField[serverPluginManager] as Map<Event, SortedSet<RegisteredListener>>
            } catch (e: Exception) {
                reloadlisteners = false
            }
            val commandMapField = serverPluginManager.javaClass.getDeclaredField("commandMap")
            commandMapField.isAccessible = true
            cmdMap = commandMapField[serverPluginManager] as SimpleCommandMap
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
            if (plugin.description.name.equals(pluginName, ignoreCase = true)) {
                pluginManager.disablePlugin(plugin)
                tp = tp + plugin.name + " "
                if (plugins != null && plugins.contains(plugin)) {
                    plugins.remove(plugin)
                }
                if (names != null && names.containsKey(pluginName)) {
                    names.remove(pluginName)
                }
                if (listeners != null && reloadlisteners) {
                    for (eventListeners in listeners.values) {
                        val eventListenersIterator = eventListeners.iterator()
                        while (eventListenersIterator.hasNext()) {
                            val value = eventListenersIterator.next() as RegisteredListener
                            if (value.plugin === plugin) {
                                eventListenersIterator.remove()
                            }
                        }
                    }
                }
                val commandsEntySetIterator: MutableIterator<Map.Entry<String, Command>> = commands.entries.iterator()
                if (cmdMap != null) {
                    while (commandsEntySetIterator.hasNext()) {
                        val (_, commmand) = commandsEntySetIterator.next()
                        if (commmand is PluginCommand) {
                            val pluginCmd = commmand
                            if (pluginCmd.plugin === plugin) {
                                pluginCmd.unregister(cmdMap)
                                commandsEntySetIterator.remove()
                            }
                        }
                    }
                }
            }
        }
        return pre + green + tp + "has been unloaded and disabled!"
    }
}