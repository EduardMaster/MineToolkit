package net.eduard.api.lib.config

import net.eduard.api.lib.config.ConfigUtil.removeQuotes
import net.eduard.api.lib.config.ConfigUtil.getPath
import net.eduard.api.lib.config.ConfigUtil.getSpace
import net.eduard.api.lib.config.ConfigUtil.isList
import net.eduard.api.lib.config.ConfigUtil.getList
import net.eduard.api.lib.config.ConfigUtil.isComment
import net.eduard.api.lib.config.ConfigUtil.getComment
import net.eduard.api.lib.config.ConfigUtil.isSection
import net.eduard.api.lib.config.ConfigUtil.getSpaceAmount
import net.eduard.api.lib.config.ConfigUtil.getKey
import net.eduard.api.lib.config.ConfigUtil.getValue
import net.eduard.api.lib.hybrid.Hybrid
import java.util.LinkedHashMap
import net.eduard.api.lib.storage.StorageAPI
import net.eduard.api.lib.modules.Extra
import java.util.ArrayList

/**
 * Interpretador de YAML proprio, Secao da [Config]
 *
 * @author Eduard
 */
class ConfigSection(var key: String, var data: Any) {
    var indent = 0
    var comments = mutableListOf<String>()

    val completeName : String get() = (if (!isRoot()) father.completeName + ConfigUtil.SECTION_SEPARATOR else "") + this.key

    lateinit var father: ConfigSection
    fun log(msg: String) {
       if (Config.isDebug)
        Hybrid.instance.console
            .sendMessage("§b[ConfigSection] §3($completeName)§2[${ if (isMap())map.size else "N"}] §f$msg")
    }

    val map: MutableMap<String, ConfigSection>
        get() {
            if (data !is LinkedHashMap<*, *>) {
                data = LinkedHashMap<String, ConfigSection>()
                log("Criando o HashMap")
            }
            return data as MutableMap<String, ConfigSection>
        }

    override fun toString(): String {
        return ("( " + (if (isMap()) ("" + map) else data) + " )")
    }

    fun add(path: String, value: Any?): ConfigSection {
        return add(path, value, *arrayOf())
    }

    fun add(path: String, value: Any?, vararg comments: String): ConfigSection {
        val section = getSection(path)
        val comentarios: List<String> = section.comments
        if (value != null) StorageAPI.autoRegisterClass(value.javaClass)
        if (!contains(path)) {
            set(path, value)
        }
        if (comentarios.isEmpty()) section.setComments(*comments)
        return section
    }

    operator fun contains(path: String): Boolean {
        val sec = getSection(path)
        val contains = sec.data != ""
        if (!contains) {
            remove(path)
        }
        return contains
    }

    fun getIntList(path: String): List<Int> {
        return getSection(path).intList
    }

    private fun get(): Any {
        if (data == ConfigUtil.EMPTY_LIST) {
            return list
        }
        if (data == ConfigUtil.EMPTY_SECTION) {
            return map
        }
        if (data is String) {
            var string = data as String
            string = Extra.toChatMessage(string)
            return string
        }
        return data
    }

    operator fun get(path: String): Any {
        return getSection(path).data
    }

    val boolean: Boolean
        get() = Extra.toBoolean(data)

    fun getBoolean(path: String): Boolean {
        return getSection(path).boolean
    }

    val double: Double
        get() = Extra.toDouble(data)

    fun getDouble(path: String): Double {
        return getSection(path).double
    }

    val float: Float
        get() = Extra.toFloat(data)

    fun getFloat(path: String): Float {
        return getSection(path).float
    }

    val int: Int
        get() = Extra.toInt(data)

    fun getInt(path: String): Int {
        return getSection(path).int
    }

    val intList: List<Int>
        get() {
            val list = ArrayList<Int>()
            for (item in list) {
                list.add(Extra.toInt(item))
            }
            return list
        }

    operator fun <T> get(path: String, claz: Class<T>?): T {
        return getSection(path).getValue(claz) as T
    }

    val keys: Set<String>
        get() = map.keys

    fun getKeys(path: String): Set<String> {
        return getSection(path).keys
    }

    val list: MutableList<Any>
        get() {
            if (data !is List<*>) {
                log("Criando a lista")
                data = ArrayList<Any>()
            }
            return data as MutableList<Any>
        }
    val long: Long
        get() = Extra.toLong(data)

    fun getLong(path: String): Long {
        return getSection(path).long
    }

    val message: String
        get() = Extra.toChatMessage(string)
    val messages: ArrayList<String>
        get() {
            val list = ArrayList<String>()
            for (text in stringList) {
                list.add(Extra.toChatMessage(text))
            }
            return list
        }

    fun getMessages(path: String): List<String> {
        return getSection(path).messages
    }

    fun isRoot(): Boolean {
        return !this::father.isInitialized
    }

    private fun createSection(key: String): ConfigSection {
        log("Criando Secao: §a$key")
        val section = ConfigSection(key, "")
        section.father = this
        map[key] = section


        return section
    }

    fun getSection(nextPath: String): ConfigSection {
        var path = nextPath
        if (path.isEmpty()) {
            return this
        }
        path = getPath(path)
        val spliter = ConfigUtil.SECTION_SEPARATOR
        if (path.contains(spliter)) {

            val split = path.split(spliter).toMutableList()
            if (split.isEmpty()) {
                log("Esta vazio")
                return createSection(path.replace(".", "$"));
            }
            val key = split[0]
            split.removeAt(0)
            val restPath = split.joinToString(spliter, limit = 10)
            log("Preparando Secao: §a$key§f dependentes: §a$restPath")

            val section = getSection(key);
            return section.getSection(restPath)
        } else {
            if (map.containsKey(path)) {
                log("Cache §2$path")
                return map[path]!!
            } else {
                return createSection(path);
            }
        }
    }

    val set: Set<Map.Entry<String, ConfigSection>>
        get() = map.entries
    val string: String
        get() = removeQuotes(Extra.toString(data))
            .replace("\\n", ConfigUtil.LINE_SEPARATOR)
            .replace("<br>", ConfigUtil.LINE_SEPARATOR)
            .replace("/n", ConfigUtil.LINE_SEPARATOR)

    fun getString(path: String): String {
        return getSection(path).string
    }

    val stringList: List<String>
        get() {
            val list = ArrayList<String>()
            for (item in list) {
                list.add(removeQuotes(Extra.toString(item)))
            }
            return list
        }

    fun getStringList(path: String): List<String> {
        return getSection(path).stringList
    }

    val value: Any
        get() = getValue<Any>(null)

    fun <T> getValue(claz: Class<T>?): Any {
        val data = (if (isMap()) toMap() else get())
        return StorageAPI.restore(claz, data)
    }

    fun toMap(): Map<String, Any> {
        val map: MutableMap<String, Any> = LinkedHashMap()
        for ((key, value) in set) {
            when {
                value.isList() -> {
                    map[key] = value.list
                }
                value.isMap() -> {
                    map[key] = value.toMap()
                }
                else -> {
                    map[key] = value.get()
                }
            }
        }
        return map
    }

    fun toSections(map: Map<Any, Any>) {
        for ((key1, value) in map) {
            val key = key1.toString()
            if (value is Map<*, *>) {
                val section = getSection(key)
                section.map.clear()
                section.toSections(value as Map<Any, Any>)
            } else if (value is List<*>) {
                getSection(key).set(value)
            } else {
                set(key, value)
            }
        }
    }

    fun set(path: String, value: Any?, vararg comments: String): ConfigSection {
        val section = getSection(path)
        if (value == null) {
            section.remove()
            return section
        }
        val dataSalved = StorageAPI.store(value.javaClass, value)
        section.set(dataSalved)
        section.setComments(*comments)
        return section
    }

    fun set(value: Any): ConfigSection {
        if (value is List<*>) {
            val list = value
            if (list.isNotEmpty()) {
                val first = list[0]!!
                if (first is Map<*, *>) {
                    var id = 1
                    for (item in list) {
                        getSection("" + id).set(item!!)
                        id++
                    }
                } else {
                    data = list
                }
            } else {
                data = list
            }
        } else if (value is Map<*, *>) {
            toSections(value as Map<Any, Any>)
        } else {
            data = value
        }
        return this
    }

    val values: Collection<ConfigSection>
        get() = map.values

    fun getValues(path: String): Collection<ConfigSection> {
        return getSection(path).values
    }

    fun message(path: String): String {
        return getSection(path).message
    }

    fun save(lines: MutableList<String>, spaceId: Int) {
        val space = getSpace(spaceId)
        for (comment in comments) {
            lines.add("$space${ConfigUtil.COMMENT} $comment")
        }
        when {
            isList() -> {
                lines.add("$space$key" + ConfigUtil.SECTION + (if (list.isEmpty()) ConfigUtil.EMPTY_LIST else "" ))
                for (text in list) {
                    lines.add("$space  ${ConfigUtil.LIST_ITEM} $text")
                }
            }
            isMap() -> {
                if (spaceId != -1) {

                    lines.add("$space$key" + ConfigUtil.SECTION)
                }
                for (section in map.values) {
                    section.save(lines, spaceId + 1)
                    for (spaceCreatorIndex in 0 until indent) {
                        lines.add("")
                    }
                }
            }
            else -> {
                if (spaceId == -1) return
                val tempData = get()
                lines.add("$space$key${ConfigUtil.SECTION} $tempData")
            }
        }
    }

    fun reload(lines: List<String>) {
        var spaceId = 0
        var path: ConfigSection = this
        val currentComments: MutableList<String> = ArrayList()

        for (line in lines) {
            val space = getSpace(spaceId)
            log("Linha: §e$line")
            when {
                isList(line) -> {
                    path.list.add(getList(line))
                    path.log("Adicionando item a lista")
                }
                isComment(line) -> {
                    currentComments.add(getComment(line))
                    path.log("Adicionando comentario")
                }
                line.trimStart().isEmpty() -> {
                    spaceId = 0
                    path = this
                    path.log("Linha vazia")
                }
                isSection(line) -> {
                    if (!path.isRoot() && !line.startsWith(space)) {
                        var time = getSpaceAmount(line)
                        if (time >= 2) {
                            time /= 2
                        }
                        while (time < spaceId && spaceId >= 0) {
                            path.log("§c<<")
                            path = path.father
                            spaceId--

                        }
                    }


                    path = path.getSection(getKey(line))
                    path.set(getValue(line))
                    path.comments.addAll(currentComments)
                    currentComments.clear()

                    if (path.data.toString().isEmpty()) {
                        spaceId++
                        path.father.log("§c>> §3"+path.key)
                    }else{
                        path.log("Info: §a"+path.data)
                        path = path.father

                    }
                    // nao desencadeia apenas muda o percurso
                }
            }


        }
    }

    fun remove(path: String) {
        getSection(path).remove()
    }

    fun remove() {
        if (isRoot()) return
        father.map.remove(key)
    }

    fun isList(): Boolean {
        return data is List<*>
    }

    fun isMap(): Boolean {
        return data is Map<*, *>
    }

    fun setComments(vararg comments: String) {
        if (comments.isNotEmpty()) {
            this.comments.clear()
            for (value in comments) {
                this.comments.add(Extra.toString(value))
            }
        }
    }
}