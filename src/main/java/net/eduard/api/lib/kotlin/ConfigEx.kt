package net.eduard.api.lib.kotlin

import net.eduard.api.lib.config.Config
import java.io.File

inline fun <reified T> File.reloadListFromFolder(): List<T> {
    mkdirs()
    val list = mutableListOf<T>()
    for (fileName in list()!!) {
        if (!fileName.endsWith(".yml"))continue
        val config = Config(this, fileName)
        val data = config.get(T::class.java)
        list.add(data)
    }
    return list
}
inline fun <T> Config.save(data : T){
    set(data)
    saveConfig()
}
inline fun <reified T> Config.reload(): T {
    return get(T::class.java)
}
inline fun <T> File.saveListInFolder(list: List<T>, fileNamer: T.() -> String) {
    for (data in list) {
        val config = Config(this, fileNamer(data) + ".yml")
        config.set(data)
        config.saveConfig()
    }
}