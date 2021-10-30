package net.eduard.api.lib.database.api

import java.lang.reflect.Field
import java.sql.Connection
import java.sql.ResultSet


interface DatabaseTable<T : Any> {
    val engine : DatabaseEngine
    val name : String
    var connection : Connection
    var tableClass : Class<*>
    var newInstance : () -> T
    val columns : MutableMap<Field,DatabaseColumn<*>>
    val columnsCreated: MutableSet<String>
    val elements : MutableMap<Any,T>
    val primaryColumn: DatabaseColumn<*>?
        get() = columns.values.firstOrNull { it.isPrimary }

    val primaryName get() = primaryColumn?.name?:"ID"
    fun updateReferences()
    fun reload()
    fun insert(data : T)
    fun update(data : T , vararg columnsNames : String)
    fun delete(data : T)
    fun selectAll() : MutableList<T>
    fun createReferences()
    fun createCollumns()
    fun delete()
    fun deleteReferences()
    fun findByReference(reference : Any , cachedData : T? = null) : T?
    fun findByColumn(columnName :String, columnValue : Any , cachedData : T? = null) : T?
    fun findByPrimary(primaryValue : Any, cachedData : T? = null) : T?
    fun updateCache(data : T, query : ResultSet)
    fun select(collums : String, where: String , columnOrder : String, ascending : Boolean, limit : Int): MutableList<T>
    fun selectByReference(reference: Any) : MutableList<T>

    val columnsContraintsCreated: MutableSet<String>
    var created: Boolean
}