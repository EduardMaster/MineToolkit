package net.eduard.api.lib.database.impl

import net.eduard.api.lib.database.*
import net.eduard.api.lib.database.annotations.TableName
import net.eduard.api.lib.database.api.DatabaseColumn
import net.eduard.api.lib.database.api.DatabaseEngine
import net.eduard.api.lib.modules.Extra
import java.lang.Exception
import java.sql.Connection
import java.sql.PreparedStatement
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

    override fun clearCache() {
        for (table in tables.values){
            for (element in table.elements.values){
                for (column in table.columns.values){
                    if (column.isReference){
                        column.field.set(element, null)
                    }
                }
            }
            table.references.clear()
            table.elements.clear()
            table.columns.clear()
            table.columnsContraintsCreated.clear()
            table.columnsCreated.clear()
        }
        tables.clear()
        tablesByName.clear()
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
            val table = tables[clz] as MySQLTable<T>
            return table
        }
        val tableName: String = if (clz.isAnnotationPresent(
                TableName::class.java
            )
        ) (clz.getAnnotation(TableName::class.java).value) else (clz.simpleName)

        if (tablesByName.containsKey(tableName)){
            val table = tablesByName[tableName]!! as MySQLTable<T>
            if (table.tableClass == String::class.java) {
                // erro encontrado dia 05/10 sobre modificar o tableClass de classes normais
                // ai a tabela ficava com table class incompativel e bugava tudo
                // agora com o if para só modificar se é String.class ou seja se nunca foi modificado
                table.tableClass = clz
            }
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
        var prepare : PreparedStatement? = null
        try {
            val table = getTable(tableName)
            table.delete()
            prepare = connection.prepareStatement(
                "DROP TABLE $tableName"
            )
            prepare.executeUpdate()
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }finally {
            prepare?.close()
        }
    }

    override fun <T : Any> clearTable(clz: Class<T>) {
        val table = getTable(clz)
        clearTable(table.name)
    }

    override fun clearTable(tableName: String) {
        var prepare : PreparedStatement? = null
        try {
            val table = getTable(tableName)
            table.deleteReferences()
            prepare = connection.prepareStatement(
                "TRUNCATE TABLE $tableName"
            )
            prepare.executeUpdate()
            table.createReferences()
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }finally {
            prepare?.close()
        }
    }


    override fun <T : Any> createTable(clz: Class<T>) {
        val table = getTable(clz)
        if (table.created){
            return
        }
        val tableName = table.name
        var prepare : PreparedStatement? = null
        try {
            val builder = StringBuilder("CREATE TABLE IF NOT EXISTS `$tableName` (")
            log("Criando tabela $tableName")
            for ((field, column) in table.columns) {
                log("Coluna: ${field.name}")
                builder.append("`" + column.name + "`")
                builder.append(" ")
                builder.append(column.customType)
                if (!column.isNumber && column.size>0) {
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
            log("Inserindo: $builder")
            prepare = connection.prepareStatement(
                builder.toString()
            )
            prepare.executeUpdate()
            table.created=true
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }finally {
            prepare?.close()
        }
        table.createCollumns()
    }

    override fun convertToSQL(valor: Any?): String {
        var value: Any = valor ?: return "NULL"
        if (value is Boolean){
            value = if (value) 1 else 0
        }

        val method = customTypes[value::class.java] as CustomType<Any>?
        return method?.saveMethod?.invoke(value) ?: "$value"
    }
    override fun convertToSQL(valor: Any?, column: DatabaseColumn<*>): String {
        var value: Any = valor ?: return "NULL"
        if (value is Boolean){
            value = if (value) 1 else 0
        }
        val method = customTypes[column.javaType] as CustomType<Any>?
        return method?.saveMethod?.invoke(value) ?: "$value"

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
        val method = customTypes[column.javaType] as CustomType<Any>?
        return method?.reloadMethod?.invoke(data) ?: data

    }


}