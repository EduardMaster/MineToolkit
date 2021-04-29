package net.eduard.api.lib.database.impl

import net.eduard.api.lib.database.DBManager
import net.eduard.api.lib.database.annotations.TableName
import net.eduard.api.lib.database.api.DatabaseColumn
import net.eduard.api.lib.database.api.DatabaseEngine
import net.eduard.api.lib.database.deserialization
import net.eduard.api.lib.database.serialization
import net.eduard.api.lib.modules.Extra
import java.lang.Exception
import java.sql.Connection
import java.sql.SQLException

class MySQLEngine(override val connection: Connection) : DatabaseEngine {
    override val tables: MutableMap<Class<*>, MySQLTable<*>> = mutableMapOf()
    private val tablesByName = mutableMapOf<String, MySQLTable<*>>()
    override fun <T : Any> updateCache(data: T) {
        val clz = data.javaClass
        val table = getTable(clz)
        table.findByPrimary(table.primaryColumn!!.field.get(data), data)
    }


    companion object {
        val logEnabled get() = DBManager.isDebugging
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



    override fun cacheInfo() {
        try{

            val showTableSql = "show tables"
            val tablesCreatedQuery = connection.prepareStatement(showTableSql).executeQuery()
            log(showTableSql)
            while (tablesCreatedQuery.next()) {
                val tableName = tablesCreatedQuery.getString(1)
                getTable(tableName).created = true
            }
            tablesCreatedQuery.close()


        }catch (ex : Exception){
            ex.printStackTrace()
        }
    }

    override fun getTable(tableName: String): MySQLTable<*> {
        if (tablesByName.containsKey(tableName)) {
            return tablesByName[tableName]!!
        }
        val table = MySQLTable<Any>(connection, String::class.java, this)
        //val table = tables.values.first { it.name.equals(tableName, true) }
        tablesByName[tableName] = table
        return table
    }
    override fun <T : Any> getTable(clz: Class<T>): MySQLTable<T> {
        if (tables.containsKey(clz)) {
            return tables[clz] as MySQLTable<T>
        }
        val tableName: String = if (clz.isAnnotationPresent(
                TableName::class.java
            )
        ) (clz.getAnnotation(TableName::class.java).value) else (clz.simpleName)
        if (tablesByName.containsKey(tableName)){
            val table = tablesByName[tableName]!! as MySQLTable<T>
            table.tableClass = clz
            tables[clz] = table
            table.reload()
            return table
        }
        val table = MySQLTable<T>(connection, clz, this)
        tables[clz] = table
        tablesByName[table.name] = table
        table.reload()
        return table
    }

    override fun <T : Any> deleteTable(clz: Class<T>) {
        val table = getTable(clz)
        deleteTable(table.name)
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

    override fun <T : Any> clearTable(clz: Class<T>) {
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


    override fun <T : Any> createTable(clz: Class<T>) {
        val table = getTable(clz)
        if (table.created){
            return
        }
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
            table.created=true
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        table.createCollumns()
    }


    override fun convertToSQL(valor: Any?): String {
        var value: Any = valor ?: return "NULL"
        if (value is Boolean){
            value = if (value) 1 else 0
        }
        return serialization[value::class.java]?.invoke(value) ?: "$value"

    }


    override fun convertToJava(data: String, column: DatabaseColumn<*>): Any? {

        if (column.isBoolean){
            return data == "1"
        }
        if (column.isWrapper) {
            val wrapper = column.wrapperType
            return Extra.transform(data, wrapper)
        }
        if (column.isEnum) {
            try {
                return column.javaType.getDeclaredField(data.toUpperCase()
                    .replace(" ","_"))[0]
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return deserialization[column.javaType]?.invoke(data) ?: data

    }


}