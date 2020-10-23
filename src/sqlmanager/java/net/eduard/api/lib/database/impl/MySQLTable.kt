package net.eduard.api.lib.database.impl

import net.eduard.api.lib.database.annotations.TableName
import net.eduard.api.lib.database.api.DatabaseColumn
import net.eduard.api.lib.database.api.DatabaseEngine
import net.eduard.api.lib.database.api.DatabaseTable
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException

class MySQLTable<T >(override var name: String,
                                      override var connection: Connection
                                      , override val tableClass: Class<*>,
                                      override val engine: DatabaseEngine



) : DatabaseTable<T>{
    override val columns: MutableMap<Field, DatabaseColumn> = mutableMapOf()
    init{
        for (field in tableClass.declaredFields){
            if (Modifier.isStatic(field.modifiers)) {
                continue
            }
            if (Modifier.isFinal(field.modifiers)) {
                continue
            }
            if (Modifier.isTransient(field.modifiers)) {
                continue
            }
            columns[field] = DatabaseColumn(field, engine)
        }
    }

    val tableName :String= if (tableClass.isAnnotationPresent(TableName::
    class.java)) tableClass.getAnnotation(TableName::class.java).value else name

    override var newInstance: () -> T = { tableClass.newInstance() as T }

    override fun selectAll(): List<T> {
        val list = mutableListOf<T>()
        try {

            val prepare = connection.prepareStatement(
                "SELECT * FROM $name")
            val query = prepare.executeQuery()
            while (query.next()){
                val newData =newInstance.invoke()
                updateCache(newData,query)
                list.add(newData)
            }

        }catch (ex : SQLException){
            ex.printStackTrace()
        }
        return list
    }

    override fun findByPrimary(primaryValue: Any): T? {

        try {
            val prepare = connection.prepareStatement(
                "SELECT * FROM $name WHERE ${primaryColumn?.name ?:"ID"} = ?")
            prepare.setString(1,""+primaryValue)
            val query = prepare.executeQuery()
            if (query.next()){
               val newData =newInstance.invoke()
                updateCache(newData,query)
                return newData
            }

        }catch (ex : SQLException){
            ex.printStackTrace()
        }

        return null

    }

    override fun select(columnOrder: String, ascending: Boolean): List<T> {
        val list = mutableListOf<T>()
        try {

            val prepare = connection.prepareStatement(
                "SELECT * FROM $name ORDER BY $columnOrder "+ (if (ascending) "ASC" else "DESC"))
            val query = prepare.executeQuery()
            while (query.next()){
                val newData =newInstance.invoke()
                updateCache(newData,query)
                list.add(newData)
            }

        }catch (ex : SQLException){
            ex.printStackTrace()
        }
        return list
    }

    override fun updateCache(data: T, query : ResultSet) {

    }

    override fun insert(data: T) {
        TODO("Not yet implemented")
    }

    override fun update(data: T) {
        TODO("Not yet implemented")
    }

    override fun delete(data: T) {
        TODO("Not yet implemented")
    }

}