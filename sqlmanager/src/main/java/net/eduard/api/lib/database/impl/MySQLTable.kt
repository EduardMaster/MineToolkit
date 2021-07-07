package net.eduard.api.lib.database.impl

import net.eduard.api.lib.database.annotations.TableName
import net.eduard.api.lib.database.api.DatabaseColumn
import net.eduard.api.lib.database.api.DatabaseEngine
import net.eduard.api.lib.database.api.DatabaseTable
import net.eduard.api.lib.database.api.TableReference
import org.bukkit.entity.Player
import java.lang.Exception
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class MySQLTable<T : Any>(
    override var connection: Connection,
    override var tableClass: Class<*>,
    override val engine: DatabaseEngine


) : DatabaseTable<T> {
    override var created = false
    override val elements = mutableMapOf<Any, T>()
    var referencesRemoved = false
    val references = mutableListOf<TableReference<T>>()
    fun log(str: String) {
        if (MySQLEngine.logEnabled)
            println("MySQLTable: $str")
    }

    override val name: String
        get() = if (tableClass.isAnnotationPresent(
                TableName::class.java
            )
        ) (tableClass.getAnnotation(TableName::class.java).value) else (tableClass.simpleName)

    override val columns: MutableMap<Field, DatabaseColumn<*>> = linkedMapOf()
    override val columnsCreated = mutableSetOf<String>()
    override val columnsContraintsCreated = mutableSetOf<String>()

    override var newInstance: () -> T = { tableClass.newInstance() as T }

    override fun reload() {
        if (tableClass == String::class.java) return
        columns.clear()
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
            val column = DatabaseColumn(this, field, engine)
            columns[field] = column

            field.isAccessible = true
        }
    }


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

    override fun delete() {
        deleteReferences()
        for (table in engine.tables.values) {
            for (column in table.columns.values) {
                if (column.isConstraint && column.javaType == tableClass) {
                    table.deleteReferences()
                }
            }
        }

    }

    override fun deleteReferences() {
        if (referencesRemoved) return
        referencesRemoved = true


        for (column in columns.values) {
            if (column.isConstraint) {
                try {
                    connection.prepareStatement(
                        "ALTER TABLE $name " +
                                "DROP FOREIGN KEY ${column.foreignKeyName}"
                    )
                        .executeUpdate()
                } catch (ex: SQLException) {
                    log("§cFalha ao deletar a Foreign key pois ela nao existe")
                }
            }
            //ALTER TABLE `party_user` DROP FOREIGN KEY `party`; ALTER TABLE `party_user` ADD CONSTRAINT `party` FOREIGN KEY (`party_id`) REFERENCES `partu`(`id`) ON DELETE SET NULL ON UPDATE SET NULL;
        }

    }

    fun calcLag(text: String, body: () -> Unit) {
        val start = System.currentTimeMillis()
        body()
        val end = System.currentTimeMillis()
        val dif = end - start
        println("MySQLEngine: $text -> ${dif}ms")

    }

    override fun createReferences() {

        try {
            val contraintsCreatedSql = "SELECT * FROM information_schema.table_constraints" +
                    " WHERE TABLE_NAME = ?"
            log(contraintsCreatedSql)
            val contraintsCreateState = connection.prepareStatement(
                contraintsCreatedSql
            )
            contraintsCreateState.setString(1, name)
            val contraintsCreateQuery = contraintsCreateState.executeQuery()
            while (contraintsCreateQuery.next()) {
                val constraint = contraintsCreateQuery.getString("CONSTRAINT_NAME")
                columnsContraintsCreated.add(constraint)
            }
            contraintsCreateQuery.close()


            for (column in columns.values) {
                if (column.isConstraint && !columnsContraintsCreated.contains(column.foreignKeyName)) {

                    val text = "ALTER TABLE $name ADD CONSTRAINT ${column.foreignKeyName} FOREIGN KEY" +
                            " (${column.name}) REFERENCES ${column.referenceTable.name}(${column.referenceTable.primaryName}) " +
                            "ON DELETE SET NULL ON UPDATE SET NULL"
                    log(text)

                    connection.prepareStatement(text)
                        .executeUpdate()
                columnsContraintsCreated.add(column.foreignKeyName)

                }
                //ALTER TABLE `party_user` DROP FOREIGN KEY `party`; ALTER TABLE `party_user` ADD CONSTRAINT `party` FOREIGN KEY (`party_id`) REFERENCES `partu`(`id`) ON DELETE SET NULL ON UPDATE SET NULL;
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }

    override fun findByReference(reference: Any, cachedData: T?): T? {
        var columnName = primaryName
        var columnValue : Any = 1
        for (column in columns.values){
            if (column.javaType == reference.javaClass){
                columnName = column.name
                val primary = column.referenceTable.primaryColumn?:continue
                columnValue = primary.field.get(reference)
            }
        }
        return findByColumn(columnName,columnValue, cachedData);
    }

    override fun findByColumn(columnName: String, columnValue: Any, cachedData: T?): T? {
        try {
            log("Selecionando 1 registro")
            val text = "SELECT * FROM $name WHERE $columnName = ? LIMIT 1"
            val prepare = connection.prepareStatement(
                text
            )
            val column = columns.values.first{it.name == columnName}
            prepare.setString(1, engine.convertToSQL(columnValue, column))
            val query = prepare.executeQuery()
            log("Query: $text")
            if (query.next()) {
                val newData = cachedData ?: newInstance.invoke()
                updateCache(newData, query)
                return newData
            }

        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return null
    }

    override fun findByPrimary(primaryValue: Any, cachedData: T?): T? {
        return findByColumn(primaryName, primaryValue, cachedData)

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
            if (column.isConstraint) {
                val key = query.getObject(column.name)
                if (key == null) {
                    field.set(data, null)
                    continue;
                }
                val value = column.referenceTable.elements[key]
                if (value != null) {
                    field.set(data, value)
                } else {

                    references.add(TableReference(column, data, key))
                }
                continue
            }
            var converted : Any? = str
            if (converted!=null)
                converted = engine.convertToJava(str, column)
            try {
                field.set(data, converted)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        if (primaryColumn != null) {
            elements[primaryColumn!!.field.get(data)] = data
        }

    }


    override fun updateReferences() {
        for (reference in references) {
            reference.update()
        }
        references.clear()
    }

    override fun insert(data: T) {

        try {
            log("Inserindo dado na tabela $name")
            val builder = StringBuilder("INSERT INTO $name (")
            for (column in columns.values) {
                builder.append(column.name + ",")
            }
            builder.deleteCharAt(builder.length - 1)
            builder.append(") VALUES (")

            for (column in columns.values) {
                builder.append("?,")
            }

            builder.deleteCharAt(builder.length - 1)
            builder.append(")")
            val prepare = connection
                .prepareStatement(builder.toString(), Statement.RETURN_GENERATED_KEYS)
            var id = 1
            for ((field, column) in columns) {

                val fieldValue = field.get(data)
                if (column.isConstraint && fieldValue != null) {
                    val primaryKey = column.referenceTable.primaryColumn!!.field.get(fieldValue) ?: 0
                    prepare.setObject(id, primaryKey)
                    id++
                    continue
                }
                if (fieldValue == null || (column.isNumber && column.isPrimary)) {
                    prepare.setObject(id, null)
                } else {
                    val converted = engine.convertToSQL(fieldValue, column)
                    prepare.setString(id, converted)
                }
                id++
            }
            log("Query: $builder")
            prepare.executeUpdate()
            val keys = prepare.generatedKeys
            if (keys != null) {
                if (keys.next() && primaryColumn != null && primaryColumn!!.isNumber) {
                    primaryColumn!!.field.set(data, keys.getInt(1))


                }
            }
            if (primaryColumn != null) {
                elements[primaryColumn!!.field.get(data)] = data
            }


        } catch (ex: SQLException) {
            ex.printStackTrace()
        }


    }


    override fun update(data: T) {

        try {
            val builder = StringBuilder("UPDATE $name SET ")

            for (column in columns.values) {
                if (column.isPrimary && column.isNumber) continue
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
                if (column.isPrimary && column.isNumber) continue
                field.isAccessible = true
                val fieldValue = field.get(data)
                if (column.isConstraint && fieldValue != null) {
                    val primaryKey = column.referenceTable.primaryColumn!!.field.get(fieldValue) ?: 0
                    prepare.setObject(id, primaryKey)
                    id++
                    continue
                }
                if (fieldValue == null || (column.isNumber && column.isPrimary)) {
                    prepare.setObject(id, null)
                } else {
                    val converted = engine.convertToSQL(fieldValue, column)
                    prepare.setString(id, converted)
                }
                id++
            }
            if (primaryColumn != null) {
                val primaryValue = primaryColumn!!.field.get(data)

                if (primaryValue != null) {
                    val converted = engine.convertToSQL(primaryValue,primaryColumn!!)
                    prepare.setObject(id, converted)
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
                prepare.setString(1, engine.convertToSQL(primaryColumn!!.field.get(data), primaryColumn!!))
                elements.remove(primaryColumn!!.field.get(data))
            } else {
                prepare.setString(1, "1")
                elements.remove(1)
            }
            prepare.executeUpdate()


        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }

    override fun createCollumns() {
        try {

            val columnsSql = "SELECT * FROM information_schema.COLUMNS WHERE TABLE_NAME = ?"
            val columnsState = connection.prepareStatement(columnsSql)
            columnsState.setString(1, name)
            val columnsQuery = columnsState.executeQuery()
            log(columnsSql)
            while (columnsQuery.next()) {
                val collumn = columnsQuery.getString("COLUMN_NAME")
                columnsCreated.add(collumn)
            }
            columnsQuery.close()

            var lastCollum = columnsCreated.lastOrNull()
            for (column in columns.values) {
                if (columnsCreated.contains(column.name)) continue
                val builder = StringBuilder()
                builder.append("ALTER TABLE $name ADD ${column.name} ")
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
                if (lastCollum != null) {
                    builder.append(" AFTER $lastCollum")
                }
                connection.prepareStatement(
                    builder.toString()
                ).executeUpdate()
                columnsCreated.add(column.name)
                lastCollum = column.name
                //ALTER TABLE `guriel_fases` ADD `teste` INT NOT NULL AFTER `tempo_online`;

            }

        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
    }

}