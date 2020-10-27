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
import java.sql.Statement

class MySQLTable<T>(
    override var connection: Connection
    , override val tableClass: Class<*>,
    override val engine: DatabaseEngine


) : DatabaseTable<T> {


    fun log(str: String) {
        if (MySQLEngine.logEnabled)
            println("MySQLTable: $str")
    }

    override val name: String = if (tableClass.isAnnotationPresent(
            TableName::class.java
        )
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
        log("Selecionando tudo de $name")
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
    override fun createReferences(){
        try{

            for (column in columns.values){
                if (column.isConstraint){

                    connection.
                    prepareStatement("ALTER TABLE ${name} DROP FOREIGN KEY IF EXISTS ${column.foreignKeyName}")
                        .executeUpdate()
                    connection.
                    prepareStatement("ALTER TABLE ${name} ADD CONSTRAINT ${column.foreignKeyName} FOREIGN KEY" +
                            " ${column.name} REFERENCES ${column.table.name}.${column.table.primaryName} " +
                            "ON DELETE SET NULL ON UPDATE SET NULL")
                        .executeUpdate()


                }
                //ALTER TABLE `party_user` DROP FOREIGN KEY `party`; ALTER TABLE `party_user` ADD CONSTRAINT `party` FOREIGN KEY (`party_id`) REFERENCES `partu`(`id`) ON DELETE SET NULL ON UPDATE SET NULL;
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }
    override fun findByColumn(columnName: String, columnValue: Any): T? {
        try {
            log("Selecionando 1 registro")
            val text = "SELECT * FROM $name WHERE $columnName = ? LIMIT 1"
            val prepare = connection.prepareStatement(
                text
            )
            prepare.setString(1, "" + columnValue)
            val query = prepare.executeQuery()
            log("Query: $text")
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
            log("Inserindo dado na tabela $name")
            val builder = StringBuilder("INSERT INTO $name VALUES (")

            for (column in columns.values) {
                builder.append("?,")
            }

            builder.deleteCharAt(builder.length - 1)
            builder.append(")")
            val prepare = connection
                .prepareStatement(builder.toString(), Statement.RETURN_GENERATED_KEYS)
            var id = 1
            for ((field, column) in columns) {
                field.isAccessible = true
                val fieldValue = field.get(data)
                if (fieldValue == null || (column.isNumber && column.isPrimary)) {
                    prepare.setObject(id, null)
                } else {
                    prepare.setString(id, "" + fieldValue)
                }
                id++
            }
            log("Query: $builder")
            prepare.executeUpdate()
            val keys = prepare.generatedKeys
            if (keys != null) {
                if (keys.next()) {
                    if (primaryColumn != null) {
                        if (primaryColumn!!.isNumber) {
                            primaryColumn!!.field.set(data, keys.getInt(1))
                        }
                    }
                }
            }

        } catch (ex: SQLException) {
            ex.printStackTrace()
        }


    }


    override fun update(data: T) {

        try {
            val builder = StringBuilder("UPDATE $name SET ")

            for (column in columns.values) {
                if (column.isPrimary&&column.isNumber)continue
                builder.append("${column.name} = ?,")
            }
            builder.deleteCharAt(builder.length - 1)
            builder.append(" WHERE ")
            builder.append(primaryName)
            builder.append(" = ?")
            val prepare = connection
                .prepareStatement(builder.toString())
            var id = 1
            for ((field, column) in columns) {
                if (column.isPrimary&&column.isNumber)continue
                field.isAccessible = true
                val fieldValue = field.get(data)
                if (fieldValue == null || (column.isNumber && column.isPrimary)) {
                    prepare.setObject(id, null)
                } else {
                    prepare.setString(id, "" + fieldValue)
                }
                id++
            }
            if (primaryColumn != null) {
                val primaryValue = primaryColumn!!.field.get(data)
                if (primaryValue != null) {
                    prepare.setObject(id, primaryValue)
                } else prepare.setInt(id, 1)
            } else
                prepare.setInt(id, 1)

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