package net.eduard.api.lib.database.api

import java.sql.Connection

interface DatabaseEngine {
    val connection: Connection
    val types: MutableMap<Class<*>, String>

    fun <T> getTable(
        name: String,
        clz: Class<T>
    ): DatabaseTable<T>


    fun <T> deleteTable(clz: Class<T>)
    fun <T> clearTable(clz: Class<T>)
    fun deleteTable(tableName: String)
    fun clearTable(tableName: String)
    fun <T> createTable(tableName: String, clz: Class<T>)

    fun  convertToSQL(value : Any) : String

}