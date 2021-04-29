package net.eduard.api.lib.database.api

import java.sql.Connection

interface DatabaseEngine {
    val connection: Connection
    val types: MutableMap<Class<*>, String>
    val tables : MutableMap<Class<*> , out DatabaseTable<*>>


    fun <T : Any> getTable(
        clz: Class<T >
    ): DatabaseTable<T>

    fun getTable(
        className : String
    ): DatabaseTable<*>
    fun cacheInfo()
    fun <T : Any> updateCache(data : T)
    fun <T : Any> deleteTable(clz: Class<T>)
    fun <T : Any> clearTable(clz: Class<T>)
    fun deleteTable(tableName: String)
    fun clearTable(tableName: String)
    fun <T : Any> createTable(clz: Class<T>)
    fun updateReferences()
    fun convertToSQL(value : Any?) : String
    fun convertToJava(string: String, column: DatabaseColumn<*>): Any?
}