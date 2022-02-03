package net.eduard.api.lib.kotlin

import net.eduard.api.lib.config.Config
import java.io.File
import java.util.function.Function


fun <T> File.reloadListFromFolder(listType: Class<T>): List<T> {
    mkdirs()
    val list = mutableListOf<T>()
    for (fileName in list()!!) {
        if (!fileName.endsWith(".yml", false)) continue
        val config = Config(this, fileName)
        val data = config[listType]
        list.add(data)
    }
    return list
}

inline fun <reified T> File.reloadListFromFolder(): List<T> {
    return reloadListFromFolder(T::class.java)
}

fun <T> Config.save(data: T) {
    set(data)
    saveConfig()
}

inline fun <reified T> Config.reload(): T {
    return get(T::class.java)
}

fun <T> File.saveListInFolder(list: List<T>, fileNamer: Function<T, String>) {
    saveListInFolder(list, true, fileNamer)
}

fun <T> File.saveListInFolder(list: List<T>, clearConfigFirst: Boolean, fileNamer: Function<T, String>) {
    saveAll(list, clearConfigFirst , fileNamer)
}

fun <T> File.saveAll(collection: Collection<T>, clearConfigFirst: Boolean, fileNamer: Function<T, String>) {
    for (item in collection) {
        val config = Config(this, fileNamer.apply(item) + ".yml", !clearConfigFirst)
        if (clearConfigFirst)
            config.clear()
        config.save(item)
    }
}

fun <T> File.saveAll(collection: Collection<T>, fileNamer: Function<T, String>) {
    saveAll(collection, true, fileNamer)
}
inline fun <T> File.saveListIn(list: List<T>, fileNamer: T.() -> String) {
    for (itemList in list) {
        val config = Config(this, fileNamer(itemList) + ".yml", false)
        config.clear()
        config.save(itemList)
    }
}
inline fun <T> File.saveAllIn(collection: Collection<T>, fileNamer: T.() -> String) {
    for (item in collection) {
        val config = Config(this, fileNamer(item) + ".yml", false)
        config.clear()
        config.save(item)
    }
}