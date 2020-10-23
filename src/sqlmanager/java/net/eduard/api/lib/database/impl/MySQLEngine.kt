package net.eduard.api.lib.database.impl

import net.eduard.api.lib.database.api.DatabaseColumn
import net.eduard.api.lib.database.api.DatabaseEngine
import net.eduard.api.lib.database.api.DatabaseTable
import net.eduard.api.lib.modules.Extra
import java.sql.Connection
import java.sql.SQLException
import java.sql.Time
import java.sql.Timestamp
import java.util.*

class MySQLEngine(override val connection: Connection) : DatabaseEngine {


    override val types: MutableMap<Class<*>, String> = mutableMapOf(
        String::class.java to "VARCHAR",
        Int::class.java to "INT",
        Integer::class.java to "INT",
        Double::class.java to "DOUBLE",
        Float::class.java to "FLOAT",
        Boolean::class.java to "TINYINT",
        Long::class.java to "BIGINT"
    )

    override fun <T> getTable(name: String, clz: Class<T>): DatabaseTable<T> {
        return MySQLTable(name, connection, clz, this)
    }

    override fun <T> deleteTable(clz: Class<T>) {
        val table = MySQLTable<T>(clz.simpleName, connection, clz, this)
        deleteTable(table.tableName)
    }

    override fun deleteTable(tableName: String) {
        try {

            val prepare = connection.prepareStatement(
                "DROP TABLE $tableName"
            )
            val query = prepare.executeUpdate()


        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }

    override fun <T> clearTable(clz: Class<T>) {
        val table = MySQLTable<T>(clz.simpleName, connection, clz, this)
        clearTable(table.tableName)
    }

    override fun clearTable(tableName: String) {
        try {

            val prepare = connection.prepareStatement(
                "TRUNCATE TABLE $tableName"
            )
            val query = prepare.executeUpdate()


        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }

    override fun <T> createTable(tableName: String, clz: Class<T>) {
        try {
            val builder = StringBuilder("CREATE TABLE IF NOT EXISTS $tableName (")

            for (field in clz.declaredFields) {
                val column = DatabaseColumn(field, this)
                builder.append(",")
                builder.append(column.name)
                builder.append(" ")
                if (column.isNullable) {
                    builder.append("NULL")
                } else builder.append("NOT NULL")
                builder.append(" ")
                if (column.isNumber) {
                    builder.append("AUTO_INCREMENT")
                }
                builder.append(" ")
                if (column.isPrimary) {
                    builder.append("PRIMARY KEY")
                }

            }
            builder.append(")")

            val prepare = connection.prepareStatement(
                builder.toString()


            )
            prepare.executeUpdate()


        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }

    override fun convertToSQL(valor: Any): String {
        var value = valor
        if (value == null) {
            return "NULL"
        }

        if (javaClass == Boolean::class.java) {
            value = if (value as Boolean) 1 else 0
        } else if (javaClass == Date::class.java) {
            value = java.sql.Date((value as Date).time)
        } else if (value is Calendar) {
            value = Timestamp(value.timeInMillis)
        } else if (value is Timestamp || value is java.sql.Time) {
        } else if (value is UUID) {
            value = value.toString()
        }
        return "" + value
    }

    fun data(any: Any): String {
        return "'$any'"
    }

    fun defaultFor(javaClass: Class<*>): String? {
        if (Time::class.java == javaClass) {
            return data(Time(System.currentTimeMillis()).toString())
        } else if (Timestamp::class.java == javaClass || Calendar::class.java == javaClass) {
            return "CURRENT_TIMESTAMP()"
        } else if (Date::class.java == javaClass || java.sql.Date::class.java == javaClass) {
            return data(java.sql.Date(System.currentTimeMillis()).toString())
        }
        return data("0")
    }

    fun sqlTypeOf(javaClass: Class<*>, size: Int): String {
        var javaClass = javaClass
        val wrapper = Extra.getWrapper(javaClass)
        if (wrapper != null) {
            javaClass = wrapper
        }
        if (String::class.java == javaClass) {
            return "VARCHAR($size)"
        } else if (Int::class.java == javaClass) {
            return "INTEGER"
        } else if (Double::class.java == javaClass) {
            return "DOUBLE"
        } else if (Byte::class.java == javaClass) {
            return "TINYINT"
        } else if (Short::class.java == javaClass) {
            return "SHORTINT"
        } else if (Long::class.java == javaClass) {
            return "BIGINT"
        } else if (Boolean::class.java == javaClass) {
            return "TINYINT(1)"
        } else if (Char::class.java == javaClass) {
            return "CHAR"
        } else if (Number::class.java == javaClass) {
            return "NUMERIC"
        } else if (Float::class.java == javaClass) {
            return "FLOAT"
        } else if (UUID::class.java == javaClass) {
            return "VARCHAR(40)"
        } else if (Timestamp::class.java == javaClass) {
            return "TIMESTAMP"
        } else if (Calendar::class.java == javaClass) {
            return "TIMESTAMP"
        } else if (java.sql.Date::class.java == javaClass) {
            return "DATE"
        } else if (Date::class.java == javaClass) {
            return "DATE"
        } else if (Time::class.java == javaClass) {
            return "TIME"
        }
        return "VARCHAR($size)"
    }

    fun convertToJava(value: Any, javaClass: Class<*>, column: DatabaseColumn): Any? {
        if (javaClass.isEnum) {
            try {
                return javaClass.getDeclaredField(value.toString())[0]
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
        if (javaClass == Calendar::class.java) {
            if (value is Timestamp) {
                val calendario = Calendar.getInstance()
                calendario.timeInMillis = value.time
                return calendario
            }
        } else if (javaClass == Date::class.java) {
            if (value is java.sql.Date) {
                return Date(value.time)
            }
        } else if (javaClass == UUID::class.java) {
            return UUID.fromString(value.toString())
        } else {
            /*
            if (column.isInline()) {
                return net.eduard.api.lib.storage.StorageAPI.restoreInline(javaClass, value)
            }
            if (column.isJson()) {
                return if (column.isList()) {
                    val list: List<*> =
                        net.eduard.api.lib.storage.StorageAPI.getGson().fromJson(
                            value.toString(),
                            MutableList::class.java
                        )
                    net.eduard.api.lib.storage.StorageAPI.restoreField(column.getField(), list)
                } else {
                    val map: Map<*, *> =
                        net.eduard.api.lib.storage.StorageAPI.getGson().fromJson(
                            value.toString(),
                            MutableMap::class.java
                        )
                    net.eduard.api.lib.storage.StorageAPI.restoreField(column.getField(), map)
                }

             */

        }
        return value
    }

}