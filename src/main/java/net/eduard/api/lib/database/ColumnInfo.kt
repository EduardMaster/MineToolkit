package net.eduard.api.lib.database

import java.sql.Date
import java.util.*

/**
 * Informações de uma Coluna de uma Tabela
 *
 * @author Eduard
 */
class ColumnInfo {
    var id = 0
    var name = ""
    var value: Any? = null
    var valueString : String? = null
    var type = 0
    var typeName: String? = null
    var className: String? = null
    fun get(): Any? {
        return value
    }

    val date: Date?
        get() = value as Date?

    val int: Int
        get() = value as Int

    val double: Double
        get() = value as Double

    val long: Long
        get() = value as Long

    val uuid: UUID
        get() = UUID.fromString(toString())

    @get:Throws(ClassNotFoundException::class)
    val classType: Class<*>
        get() = Class.forName(className)

    override fun toString(): String {
        return "" + value
    }

}