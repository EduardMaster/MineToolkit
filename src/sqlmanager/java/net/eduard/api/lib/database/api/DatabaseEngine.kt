package net.eduard.api.lib.database.api

import java.sql.Connection

interface DatabaseEngine {
    val connection: Connection
    val types: MutableMap<Class<*>, String>
    val tables : MutableMap<Class<*> , DatabaseTable<*>>

    fun <T> getTable(
        clz: Class<T>
    ): DatabaseTable<T>


    fun <T> deleteTable(clz: Class<T>)
    fun <T> clearTable(clz: Class<T>)
    fun deleteTable(tableName: String)
    fun clearTable(tableName: String)
    fun <T> createTable(clz: Class<T>)

    fun convertToSQL(value : Any?) : String
    fun convertToJava(string: String, column: DatabaseColumn<*>): Any?
}