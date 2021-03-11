package net.eduard.api.lib.config

import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.storage.StorageAPI
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.ArrayList
import java.util.LinkedHashMap

/**
 * Armazenamento de Dados para servidores Bukkit com suporte a
 * [StorageAPI]
 *
 * @author Eduard
 * @version 1.2
 * @since 1.0
 */
class BukkitConfig(
    var name: String = "config.yml",
    var plugin: Plugin = JavaPlugin.getProvidingPlugin(javaClass)

)  {

    init {
        reloadConfig()
    }

    /**
     *
     * @return Arquivo
     */
    var file: File = plugin.dataFolder

    /**
     *
     * @return Config ([FileConfiguration]
     */
    var config: YamlConfiguration = YamlConfiguration()



    fun createConfig(name: String): BukkitConfig {
        return BukkitConfig(name, plugin)
    }

    /**
     * Recarrega a config pelo conteudo do Arquivo
     *
     * @return Config
     */
    fun reloadConfig() {
        file = File(plugin.dataFolder, name)
        plugin.dataFolder.mkdirs()
        file.parentFile.mkdirs()
        if (file.exists() && file.isFile) config = Mine.loadConfig(file)
        val defaults = plugin.getResource(file.name)
        if (defaults != null) {
            val loadConfig = YamlConfiguration.loadConfiguration(defaults)
            config.defaults = loadConfig
        }

    }

    /**
     * Salva Config em forma de Texto no Arquivo
     *
     * @return Config
     */
    fun saveConfig() {
        try {
            val fileWriter: Writer = BufferedWriter(
                OutputStreamWriter(
                    FileOutputStream(file),
                    StandardCharsets.UTF_8
                )
            )
            fileWriter.write(config.saveToString())
            fileWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    /**
     *
     * @param path Path
     * @return Pega uma mensagem
     */
    fun message(path: String): String {
        return ChatColor.translateAlternateColorCodes('&',
            config.getString(path))
    }

    fun getMessages(path: String): List<String> {
        val messages: MutableList<String> = ArrayList()
        for (line in getStringList(path)) {
            messages.add(toChatMessage(line))
        }
        return messages
    }

    /**
     * Salva a Config padr§o caso n§o existe a Arquivo
     */
    fun saveDefaultConfig() {
        if (plugin.getResource(name) != null) plugin.saveResource(name, false)
        reloadConfig()
    }

    /**
     * Salva a config padrao
     */
    fun saveResource() {
        plugin.saveResource(name, true)
    }

    /**
     * Remove a Secao
     *
     * @param path Secao
     */
    fun remove(path: String) {
        config[path] = null
    }

    /**
     * Salva os padroes da Config
     *
     * @return
     */
    fun saveDefault() {
        config.options().copyDefaults(true)
        saveConfig()

    }

    /**
     *
     * @param path Secao
     * @return Item da Secao
     */
    fun getItem(path: String): ItemStack {
        return get(path) as ItemStack
    }

    fun getLocation(path: String): Location {
        return get(path) as Location
    }

    fun delete(): Boolean {
        return file.delete()
    }

    fun exists(): Boolean {
        return file.exists()
    }

    fun add(path: String, value: Any) {
        config.addDefault(path, value)
    }

    operator fun contains(path: String): Boolean {
        return config.contains(path)
    }

    fun create(path: String): ConfigurationSection {
        return config.createSection(path)
    }

    operator fun get(path: String): Any {
        var obj = config[path]
        if (obj is ConfigurationSection) {
            obj = toMap(path)
        }

        return StorageAPI.restore(null, obj)
    }
    fun toMap(path: String): Map<String, Any> {
        val map = LinkedHashMap<String, Any>()
        val sec = getSection(path).getValues(false)
        for ((name, value) in sec) {
            if (value is ConfigurationSection) {
                map[name] = toMap("$path.$name")
            } else {
                map[name] = value
            }
        }
        return map
    }
    fun getBoolean(path: String): Boolean {
        return config.getBoolean(path)
    }
    fun getSection(path: String): ConfigurationSection {
        if (!config.isConfigurationSection(path)) {
            config.createSection(path)
        }
        return config.getConfigurationSection(path)
    }
    fun getDouble(path: String): Double {
        return config.getDouble(path, 0.0)
    }
    fun getInt(path: String): Int {
        return config.getInt(path)
    }
    fun getIntegerList(path: String): List<Int> {
        return config.getIntegerList(path)
    }
    fun getItemStack(path: String): ItemStack {
        return config.getItemStack(path)
    }
    fun getKeys(deep: Boolean): Set<String> {
        return config.getKeys(deep)
    }
    fun getList(path: String): List<*> {
        return config.getList(path)
    }
    fun getLong(path: String): Long {
        return config.getLong(path)
    }
    fun getLongList(path: String): List<Long> {
        return config.getLongList(path)
    }

    fun getMapList(path: String): List<Map<*, *>> {
        return config.getMapList(path)
    }

    fun getString(path: String): String {
        return config.getString(path)
    }

    fun getStringList(path: String): List<String> {
        return config.getStringList(path)
    }

    fun getValues(deep: Boolean): Map<String, Any> {
        return config.getValues(deep)
    }

    operator fun set(path: String, value: Any?) {
        if (value == null) {
            config[path] = null
        } else {
            config[path] = StorageAPI.store(value.javaClass, value)
        }
    }

    fun createSubConfig(name: String): BukkitConfig {
        return createConfig(name + name)
    }

    companion object {
        fun toChatMessage(text : String) : String {
            return ChatColor.translateAlternateColorCodes('&', text)
        }
        fun toConfigMessage(text: String): String {
            return text.replace("§", "&")
        }
    }


}