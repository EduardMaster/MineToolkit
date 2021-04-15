package net.eduard.api.lib.database.api

import java.lang.reflect.Field
import java.sql.Connection
import java.sql.ResultSet


interface DatabaseTable<T : Any> {
    val engine : DatabaseEngine
    val name : String
    var connection : Connection
    val tableClass : Class<*>
    var newInstance : () -> T
    val columns : MutableMap<Field,DatabaseColumn<*>>
    val elements : MutableMap<Any,T>
    val primaryColumn: DatabaseColumn<*>?
        get() = columns.values.firstOrNull { it.isPrimary }

    val primaryName get() = primaryColumn?.name?:"ID"
    fun updateReferences()
    fun reload()
    fun insert(data : T)
    fun update(data : T)
    fun delete(data : T)
    fun selectAll() : List<T>
    fun createReferences()
    fun createCollumns()
    fun delete()
    fun deleteReferences()
    fun findByColumn(columnName :String, columnValue : Any , cachedData : T? = null) : T?
    fun findByPrimary(primaryValue : Any, cachedData : T? = null) : T?
    fun updateCache(data : T, query : ResultSet)
    fun select(where: String , columnOrder : String, ascending : Boolean, limit : Int): List<T>


}