package net.eduard.api.lib.database.api

import java.lang.reflect.Field
import java.sql.Connection
import java.sql.ResultSet


interface DatabaseTable<T> {
    val engine : DatabaseEngine
    val name : String
    var connection : Connection
    val tableClass : Class<*>
    var newInstance : () -> T
    val columns : MutableMap<Field,DatabaseColumn>
    val primaryColumn: DatabaseColumn?
        get() = columns.values.firstOrNull { it.isPrimary }

    val primaryName get() = primaryColumn?.name?:"ID"

    fun insert(data : T)
    fun update(data : T)
    fun delete(data : T)
    fun selectAll() : List<T>
    fun createReferences()
    fun findByColumn(columnName :String, columnValue : Any) : T?
    fun findByPrimary(primaryValue : Any) : T?
    fun updateCache(data : T, query : ResultSet)
    fun select(where: String , columnOrder : String, ascending : Boolean, limit : Int): List<T>


}