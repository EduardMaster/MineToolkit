package net.eduard.api.lib.config

import net.md_5.bungee.config.ConfigurationProvider
import net.eduard.api.lib.config.BungeeConfig
import java.io.IOException
import net.eduard.api.lib.storage.StorageAPI
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.YamlConfiguration
import java.io.File
import java.lang.Exception
import java.nio.file.Files
import java.util.LinkedHashMap
import java.util.stream.Collectors

/**
 * Armazenamento de Dados para servidores BungeeCord com suporte a
 * [StorageAPI]
 *
 * @author Eduard
 * @version 1.2
 * @since 1.0
 */
class BungeeConfig (
    var name: String = "config.yml",
    var plugin: Plugin
)  {

    val file: File = plugin.dataFolder
    var config: Configuration = Configuration()

    init {
        reloadConfig()
    }
    fun reloadConfig() {
        try {
            saveDefaultConfig()
            if (!file.exists()) {
                file.createNewFile()
            }
            config = provider.load(file)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveDefaultConfig() {
        try {
            file.parentFile.mkdirs()
            if (!file.exists()) {
                val stream = plugin.getResourceAsStream(name)
                if (stream != null) {
                    Files.copy(stream, file.toPath())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val provider: ConfigurationProvider
        get() = ConfigurationProvider.getProvider(YamlConfiguration::class.java)

    fun saveConfig(): BungeeConfig {
        try {
            provider.save(config, file)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return this
    }

    fun message(path: String): String {
        return toChatMessage(config.getString(path))
    }

    fun remove(path: String) {
        config[path] = null
    }

    fun toConfigMessage(text: String): String {
        return text.replace("§", "&")
    }

    fun delete(): Boolean {
        return file.delete()
    }

    fun exists(): Boolean {
        return file.exists()
    }

    operator fun contains(path: String): Boolean {
        return config.contains(path)
    }

    fun create(path: String): Configuration {
        return config.getSection(path)
    }

    operator fun get(path: String): Any {
        var obj = config[path]
        if (obj is Configuration) {
            obj = toMap(path)
        }
        if (obj is Map<*, *>) {
        }
        return StorageAPI.restore(null, obj)
    }

    private fun getValues(config: Configuration): Map<String, Any>? {
        try {
            val field = config.javaClass.getDeclaredField("self")
            field.isAccessible = true
            return field[config] as Map<String, Any>
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun toMap(config: Configuration): Map<String, Any> {
        val sec = getValues(config)
        val map = LinkedHashMap<String, Any>()
        for ((key, value) in sec!!) {
            if (value is Configuration) {
                map[key] = toMap(value)
            } else {
                map[key] = value
            }
        }
        return map
    }

    fun toMap(path: String): Map<String, Any> {
        return toMap(getSection(path))
    }

    fun getBoolean(path: String): Boolean {
        return config.getBoolean(path)
    }

    fun getDouble(path: String): Double {
        return config.getDouble(path)
    }

    fun getInt(path: String): Int {
        return config.getInt(path)
    }

    fun getIntegerList(path: String): List<Int> {
        return config.getIntList(path)
    }

    val keys: Collection<String>
        get() = config.keys

    fun getList(path: String): List<*> {
        return config.getList(path)
    }

    fun getLong(path: String): Long {
        return config.getLong(path)
    }

    fun getLongList(path: String): List<Long> {
        return config.getLongList(path)
    }

    fun getString(path: String): String {
        return config.getString(path)
    }

    fun getStringList(path: String): List<String> {
        return config.getStringList(path)
    }

    fun getSection(path: String): Configuration {
        return config.getSection(path)
    }

    operator fun set(path: String, value: Any?) {
        if (value == null) {
            remove(path)
        } else {
            config[path] = StorageAPI.store(value.javaClass, value)
        }
    }

    fun add(path: String, value: Any?) {
        if (!config.contains(path)) {
            set(path, value)
        }
    }

    fun getMessages(path: String): List<String> {
        return getStringList(path).map { toChatMessage(it) }
    }

    companion object {
        fun toChatMessage(text: String): String {
            return ChatColor.translateAlternateColorCodes('&', text)
        }
    }

}