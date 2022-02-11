package net.eduard.api.lib.config

import net.eduard.api.lib.hybrid.Hybrid
import java.util.LinkedHashMap
import net.eduard.api.lib.storage.StorageAPI
import net.eduard.api.lib.modules.Extra
import java.lang.StringBuilder
import java.util.ArrayList
import java.util.logging.Logger

/**
 * Interpretador de YAML proprio, Secao da [Config]
 *
 * @author Eduard
 */
class ConfigSection(var key: String, var data: Any) {
    var indent = 0
    var comments = mutableListOf<String>()

    companion object {
        var isDebug: Boolean = false
        val logger = Logger.getLogger("ConfigSection")
    }
    inline fun section(sectionName : String, setup : ConfigSection.() -> Unit): ConfigSection {
        return getSection(sectionName).apply(setup)
    }
    fun isRoot(): Boolean {
        return !this::father.isInitialized
    }

    fun isList(): Boolean {
        return data is List<*>
    }

    fun isMap(): Boolean {
        return data is Map<*, *>
    }

    val completeName: String get() = (if (!isRoot()) father.completeName + ConfigUtil.SECTION_SEPARATOR else "") + this.key

    lateinit var father: ConfigSection

    fun debug(message: String) {
        val quantidade = if (isMap()) map.size else (if (isList()) list.size else 0)
        if (isDebug)
            logger.info("[ConfigSection] ($completeName)[$quantidade] $message")
    }

    val map: MutableMap<String, ConfigSection>
        get() {
            if (data !is LinkedHashMap<*, *>) {
                data = LinkedHashMap<String, ConfigSection>()
                //debug("Criando o HashMap")
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


    operator fun get(path: String): Any {
        return getSection(path).data
    }


    fun getBoolean(path: String): Boolean {
        return getSection(path).boolean
    }


    fun getDouble(path: String): Double {
        return getSection(path).double
    }


    fun getFloat(path: String): Float {
        return getSection(path).float
    }

    val int: Int
        get() {
            if (data is Int) {
                return data as Int
            }
            data = Extra.toInt(data)
            return data as Int
        }

    val float: Float
        get() {
            if (data is Float) {
                return data as Float
            }
            data = Extra.toFloat(data)
            return data as Float
        }
    val double: Double
        get() {
            if (data is Double) {
                return data as Double
            }
            data = Extra.toDouble(data)
            return data as Double
        }

    val long: Long
        get() {
            if (data is Long) {
                return data as Long
            }
            data = Extra.toLong(data)
            return data as Long
        }
    val boolean: Boolean
        get() {
            if (data is Boolean) {
                return data as Boolean
            }
            data = Extra.toBoolean(data)
            return data as Boolean
        }

    fun getInt(path: String): Int {
        return getSection(path).int
    }

    val intList: List<Int> get() = this.list.map(Extra::toInt)


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
                //debug("Criando a lista")
                data = ArrayList<Any>()
            }
            return data as MutableList<Any>
        }


    fun getLong(path: String): Long {
        return getSection(path).long
    }

    val message: String
        get() = Extra.toChatMessage(string)

    val messages: List<String> get() = this.stringList


    fun getMessages(path: String): List<String> {
        return getSection(path).messages
    }


    private fun createSection(key: String): ConfigSection {
        //debug("Criando Secao: §a$key")
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
        path = ConfigUtil.getPath(path)
       // val spliter = ConfigUtil.SECTION_SEPARATOR
        val whereIsFinalPoint = path.indexOf('.')
        if (whereIsFinalPoint == -1) {
            val sectionFinded = map[path];
            if (sectionFinded != null) {
                return sectionFinded
            }
            return createSection(path);
        }
        val firstKey = path.substring(0, whereIsFinalPoint)
        //  val split = path.split(spliter)
        if (firstKey.isEmpty()) {
            return createSection(path.replace(".", "$"));
        }
        val lastKey = path.substring(whereIsFinalPoint + 1)
        /*
        val key = split[0]
        val builder = StringBuilder()
        var first = true;
        for (subKey in split) {
            if (first) {
                first = false
            } else {
                builder.append(subKey)
                builder.append(".")
            }
        }
        builder.deleteCharAt(builder.length - 1)
        val restPath = builder.toString()
        //split.removeAt(0)
        //val restPath = split.joinToString(spliter, limit = 10)

         */
        //debug("Preparando Secao: §a$firstKey§f dependentes: §a$lastKey")
        val section = getSection(firstKey);
        return section.getSection(lastKey)
    }

    val set: Set<Map.Entry<String, ConfigSection>>
        get() = map.entries
    val string: String
        get() = ConfigUtil.removeQuotes(Extra.toString(data))
    //.replace("\\n", ConfigUtil.LINE_SEPARATOR, false)
    // .replace("<br>", ConfigUtil.LINE_SEPARATOR, false)
    //.replace("/n", ConfigUtil.LINE_SEPARATOR, false)

    fun getString(path: String): String {
        return getSection(path).string
    }

    val stringList: List<String>
        get() = this.list.map {
            Extra.toChatMessage(ConfigUtil.removeQuotes(Extra.toString(it)))
        }


    fun getStringList(path: String): List<String> {
        return getSection(path).stringList
    }


    fun <T> getValue(claz: Class<T>? = null): Any {
        val data = (if (isMap()) toMap() else data)
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
                    if (value.data is String) {
                        value.data = Extra.toChatMessage(value.data as String)
                    }
                    map[key] = value.data
                }
            }
        }
        return map
    }

    fun toSections(map: Map<Any, Any>) {
        for ((key, value) in map) {
            val keyName = key.toString()
            if (value is Map<*, *>) {
                val section = getSection(keyName)
                section.map.clear()
                section.toSections(value as Map<Any, Any>)
            } else if (value is List<*>) {
                getSection(keyName).set(value)
            } else {
                set(keyName, value)
            }
        }
    }

    fun set(path: String, value: Any?, vararg comments: String): ConfigSection {
        val section = getSection(path)
        if (value == null) {
            section.remove()
            return section
        }
        val dataSalved: Any? = StorageAPI.store(value.javaClass, value)
        if (dataSalved == null) {
            Hybrid.instance.console.sendMessage("§b[ConfigAPI] §aSetando Dado (${value.javaClass.simpleName}) Nulo na secao: $path")
        } else {
            section.set(dataSalved)
        }
        section.setComments(*comments)
        return section
    }

    fun set(value: Any): ConfigSection {
        if (value is List<*>) {
            val list = value as List<*>
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
        val space = ConfigUtil.getSpace(spaceId)
        for (comment in comments) {
            lines.add("$space${ConfigUtil.CHAR_COMMENT} ${comment.trimStart()}")
        }
        when {
            isList() -> {
                lines.add("$space$key" + ConfigUtil.CHAR_SECTION + (if (list.isEmpty()) (" " + ConfigUtil.EMPTY_LIST) else ""))
                for (text in list) {
                    lines.add("$space  ${ConfigUtil.CHAR_LIST_ITEM} $text")
                }
            }
            isMap() -> {
                if (spaceId != -1) {

                    lines.add("$space$key" + ConfigUtil.CHAR_SECTION + (if (map.isEmpty()) (" " + ConfigUtil.EMPTY_SECTION) else ""))
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
                var tempData = data
                if (tempData is String) {
                    if (ConfigUtil.hasOnlySpaces(tempData)) {
                        tempData = ConfigUtil.STR1 + "" + ConfigUtil.STR1
                    }
                    if (tempData.startsWith(" ") or tempData.endsWith(" ")) {
                        tempData = ConfigUtil.STR1 + tempData + ConfigUtil.STR1
                    }
                    tempData = tempData.replace("\n", "<br>", false)
                }
                lines.add("$space$key${ConfigUtil.CHAR_SECTION} $tempData")
            }
        }
    }

    fun reload(lines: List<String>) {
        var spaceId = 0
        var path: ConfigSection = this
        val currentComments: MutableList<String> = ArrayList()

        for (line in lines) {
            debug("Lendo linha: \'${line}\'")
            when {
                ConfigUtil.isList(line) -> {
                    path.list.add(ConfigUtil.getList(line))
                    path.debug("Adicionando item a lista")
                }
                ConfigUtil.isComment(line) -> {
                    currentComments.add(ConfigUtil.getComment(line))
                    path.debug("Adicionando comentario")
                }
                line.trimStart().isEmpty() -> {
                    spaceId = 0
                    path = this
                    path.debug("Linha vazia")
                }
                ConfigUtil.isSection(line) -> {

                    var spaceTimes = ConfigUtil.getSpaceAmount(line)
                    if (spaceTimes >= 2) {
                        spaceTimes /= 2
                    }
                    while (spaceTimes < spaceId) {
                        path.debug("<<")
                        path = path.father
                        spaceId--
                    }



                    path = path.getSection(ConfigUtil.getKey(line))

                    val currenLine = line.trimStart().trimEnd()
                    if (currenLine.endsWith(ConfigUtil.CHAR_SECTION)) {
                        path.map
                    } else if (currenLine.endsWith(ConfigUtil.EMPTY_SECTION)) {
                        path.map
                    } else if (currenLine.endsWith(ConfigUtil.EMPTY_LIST)) {
                        path.list
                    } else {
                        val result =
                            currenLine.substringAfter(':')
                                .trimStart()
                                .removeSurrounding(ConfigUtil.STR1)
                                .removeSurrounding(ConfigUtil.STR2)
                                .replace("\\n", ConfigUtil.LINE_SEPARATOR, false)
                                .replace("<br>", ConfigUtil.LINE_SEPARATOR, false)
                                .replace("/n", ConfigUtil.LINE_SEPARATOR, false)

                        if (result == ConfigUtil.EMPTY_SECTION) {
                            path.map
                        } else if (result == ConfigUtil.EMPTY_LIST) {

                            path.list
                        } else {
                            path.data = result
                        }
                    }




                    path.comments.addAll(currentComments)
                    currentComments.clear()
                    if (path.isMap() || path.isList()) {
                        spaceId++
                        path.father.debug("§>> (${path.key})")
                        path.debug("Tentando ler Items da Lista ou SubSecoes")
                    } else {
                        path.debug("Info: \'${path.data}\'")
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

    fun setComments(vararg comments: String) {
        if (comments.isNotEmpty()) {
            this.comments.clear()
            for (value in comments) {
                this.comments.add(Extra.toString(value))
            }
        }
    }
}