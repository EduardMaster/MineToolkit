package net.eduard.api.lib.database.impl

import net.eduard.api.lib.database.api.DatabaseColumn
import net.eduard.api.lib.database.api.DatabaseEngine
import net.eduard.api.lib.database.api.DatabaseTable
import net.eduard.api.lib.database.deserialization
import net.eduard.api.lib.database.serialization
import net.eduard.api.lib.modules.Extra
import java.lang.Exception
import java.sql.Connection
import java.sql.SQLException

class MySQLEngine(override val connection: Connection) : DatabaseEngine {
    override val tables: MutableMap<Class<*>, MySQLTable<*>> = mutableMapOf()
    private val tablesByName = mutableMapOf<String, DatabaseTable<*>>()

    companion object {
        var logEnabled = false
    }

    fun log(str: String) {
        if (logEnabled)
            println("MySQLEngine: $str")
    }

    override fun updateReferences() {
        for (table in tables.values) {
            table.updateReferences()
        }
    }

    override val types: MutableMap<Class<*>, String> = mutableMapOf(
        String::class.java to "VARCHAR",
        Int::class.java to "INT",
        Integer::class.java to "INTEGER",
        Double::class.java to "DOUBLE",
        Float::class.java to "FLOAT",
        Boolean::class.java to "TINYINT",
        Long::class.java to "BIGINT"
    )

    override fun <T> getTable(clz: Class<T>): MySQLTable<T> {
        if (tables.containsKey(clz)) {
            return tables[clz] as MySQLTable<T>
        }
        val table= MySQLTable<T>(connection, clz, this)
        tables[clz] = table
        table.reload()
        return table
    }

    override fun <T> deleteTable(clz: Class<T>) {
        val table = getTable(clz)
        deleteTable(table.name)
    }

    fun getTable(tableName: String): DatabaseTable<*> {
        if (tablesByName.containsKey(tableName)) {
            return tablesByName[tableName]!!
        }
        val table = tables.values.first { it.name.equals(tableName, true) }
        tablesByName[tableName.toLowerCase()] = table
        return table
    }

    override fun deleteTable(tableName: String) {
        try {
            val table = getTable(tableName)
            table.delete()
            val prepare = connection.prepareStatement(
                "DROP TABLE $tableName"
            )
             prepare.executeUpdate()


        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }

    override fun <T> clearTable(clz: Class<T>) {
        val table = getTable(clz)
        clearTable(table.name)
    }

    override fun clearTable(tableName: String) {
        try {

            val prepare = connection.prepareStatement(
                "TRUNCATE TABLE $tableName"
            )
            prepare.executeUpdate()
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }


    override fun <T> createTable(clz: Class<T>) {
        val table = getTable(clz)
        val tableName = table.name

        try {
            val builder = StringBuilder("CREATE TABLE IF NOT EXISTS $tableName (")
            log("Criando tabela $tableName")

            for ((field, column) in table.columns) {
                log("Coluna: ${field.name}")
                builder.append(column.name)
                builder.append(" ")
                builder.append(column.customType)
                if (!column.isNumber) {
                    builder.append("(${column.size})")
                }
                builder.append(" ")

                if (column.isNullable || column.isConstraint) {
                    builder.append("NULL")
                } else builder.append("NOT NULL")
                builder.append(" ")
                if (column.isNumber && column.isPrimary) {
                    builder.append("AUTO_INCREMENT")
                    builder.append(" ")
                }
                if (column.isUnique) {
                    builder.append("UNIQUE")
                    builder.append(" ")
                }

                if (!column.isConstraint && column.isPrimary) {
                    builder.append("PRIMARY KEY")
                    builder.append(" ")
                }
                builder.append(",")
            }
            builder.deleteCharAt(builder.length - 1)
            builder.append(")")
            log("Query:$builder")

            val prepare = connection.prepareStatement(
                builder.toString()


            )
            prepare.executeUpdate()


        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }


    override fun convertToSQL(valor: Any?): String {
        val value: Any = valor ?: return "NULL"
        return serialization[value::class.java]?.invoke(value)?:"$value"

    }


    override fun convertToJava(data: String, column: DatabaseColumn<*>): Any? {
        val javaClass = column.javaType

        if (Extra.isWrapper(javaClass)) {
            val wrapper = Extra.getWrapper(javaClass)
            return Extra.transform(data, wrapper)
        }
        if (javaClass.isEnum) {
            try {
                return javaClass.getDeclaredField(data)[0]
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return deserialization[javaClass]?.invoke(data) ?: return data

    }


}