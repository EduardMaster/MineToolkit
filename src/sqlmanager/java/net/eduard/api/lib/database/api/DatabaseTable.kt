package net.eduard.api.lib.database.api

import java.lang.reflect.Field
import java.sql.Connection
import java.sql.ResultSet


interface DatabaseTable<T> {
    val engine : DatabaseEngine
    var name : String
    var connection : Connection
    val tableClass : Class<*>
    var newInstance : () -> T
    val columns : MutableMap<Field,DatabaseColumn>
    val primaryColumn: DatabaseColumn?
        get() = columns.values.firstOrNull { it.isPrimary }

    fun insert(data : T)
    fun update(data : T)
    fun delete(data : T)
    fun selectAll() : List<T>
    fun findByPrimary(primaryValue : Any) : T?
    fun updateCache(data : T, query : ResultSet)
    fun select(columnOrder : String, ascending : Boolean): List<T>


}