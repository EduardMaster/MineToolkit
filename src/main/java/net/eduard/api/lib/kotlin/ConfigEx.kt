package net.eduard.api.lib.kotlin

import net.eduard.api.lib.config.Config
import java.io.File
import java.util.function.Function


fun <T> File.reloadListFromFolder(listType: Class<T>): List<T> {
    mkdirs()
    val list = mutableListOf<T>()
    for (fileName in list()) {
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
    saveListInFolder(list, false, fileNamer)
}

fun <T> File.saveListInFolder(list: List<T>, clearConfigFirst: Boolean, fileNamer: Function<T, String>) {
    for (data in list) {
        val config = Config(this, fileNamer.apply(data) + ".yml")
        if (clearConfigFirst)
            config.clear()
        config.save(data)
    }
}