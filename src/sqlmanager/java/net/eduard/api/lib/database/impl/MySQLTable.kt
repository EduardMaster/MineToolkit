package net.eduard.api.lib.database.impl

import net.eduard.api.lib.database.annotations.TableName
import net.eduard.api.lib.database.api.DatabaseColumn
import net.eduard.api.lib.database.api.DatabaseEngine
import net.eduard.api.lib.database.api.DatabaseTable
import java.lang.Exception
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException

class MySQLTable<T>(
    override var connection: Connection
    , override val tableClass: Class<*>,
    override val engine: DatabaseEngine


) : DatabaseTable<T> {
    override val name: String = if (tableClass.isAnnotationPresent(
            TableName:: class.java )
    ) tableClass.getAnnotation(TableName::class.java).value else tableClass.simpleName
    override val columns: MutableMap<Field, DatabaseColumn> = linkedMapOf()

    init {
        for (field in tableClass.declaredFields) {
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


    override var newInstance: () -> T = { tableClass.newInstance() as T }

    override fun selectAll(): List<T> {
        val list = mutableListOf<T>()
        try {

            val prepare = connection.prepareStatement(
                "SELECT * FROM $name"
            )
            val query = prepare.executeQuery()
            while (query.next()) {
                val newData = newInstance.invoke()
                updateCache(newData, query)
                list.add(newData)
            }

        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return list
    }

    override fun findByColumn(columnName: String, columnValue: Any): T? {
        try {
            val prepare = connection.prepareStatement(
                "SELECT * FROM $name WHERE $columnName = ? LIMIT 1"
            )
            prepare.setString(1, "" + columnValue)
            val query = prepare.executeQuery()
            if (query.next()) {
                val newData = newInstance.invoke()
                updateCache(newData, query)
                return newData
            }

        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return null
    }

    override fun findByPrimary(primaryValue: Any): T? {
        return findByColumn(primaryName, primaryValue)

    }

    override fun select(where: String, columnOrder: String, ascending: Boolean, limit: Int): List<T> {
        val list = mutableListOf<T>()
        try {

            val prepare = connection.prepareStatement(
                "SELECT * FROM $name WHERE $where ORDER BY $columnOrder "
                        + (if (ascending) "ASC" else "DESC") + " LIMIT $limit"
            )
            val query = prepare.executeQuery()
            while (query.next()) {
                val newData = newInstance.invoke()
                updateCache(newData, query)
                list.add(newData)
            }

        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return list
    }

    override fun updateCache(data: T, query: ResultSet) {

        for ((field, column) in columns) {
            val str = query.getString(column.name)
            val converted = engine.convertToJava(str, column)
            try {
                field.set(data, converted)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

    }

    override fun insert(data: T) {

        try {
            val builder = StringBuilder("INSERT INTO $name VALUES (")

            for (column in columns.values) {
                builder.append("?,")
            }

            builder.deleteCharAt(builder.length - 1)
            builder.append(")")
            val prepare = connection
                .prepareStatement(builder.toString())
            var id = 1
            for ((field, column) in columns) {
                val fieldValue = field.get(data)
                if (fieldValue == null || (column.isNumber && column.isPrimary)) {
                    prepare.setObject(id, null)
                } else {
                    prepare.setString(id, "" + fieldValue)
                }
                id++
            }
            prepare.executeUpdate()


        } catch (ex: SQLException) {
            ex.printStackTrace()
        }


    }


    override fun update(data: T) {

        try {
            val builder = StringBuilder("UPDATE $name SET ")

            for (column in columns.values) {
                builder.append("${column.name} = ?,")
            }

            builder.deleteCharAt(builder.length - 1)
            builder.append(")")
            val prepare = connection
                .prepareStatement(builder.toString())
            var id = 1
            for ((field, column) in columns) {
                val fieldValue = field.get(data)
                if (fieldValue == null || (column.isNumber && column.isPrimary)) {
                    prepare.setObject(id, null)
                } else {
                    prepare.setString(id, "" + fieldValue)
                }
                id++
            }
            prepare.executeUpdate()


        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }

    override fun delete(data: T) {
        try {

            val prepare = connection.prepareStatement(
                "DELETE FROM $name WHERE ${primaryColumn?.name ?: "ID"} = ?"
            )
            if (primaryColumn != null) {
                prepare.setString(1, "" + primaryColumn!!.field.get(data))
            } else {
                prepare.setString(1, "1")
            }
            prepare.executeUpdate()


        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }


}