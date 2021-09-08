package net.eduard.api.lib.database.impl

import net.eduard.api.lib.database.annotations.TableName
import net.eduard.api.lib.database.api.DatabaseColumn
import net.eduard.api.lib.database.api.DatabaseEngine
import net.eduard.api.lib.database.api.DatabaseTable
import net.eduard.api.lib.database.api.TableReference
import java.lang.Exception
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.sql.*

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
        ) (tableClass.getAnnotation(TableName::class.java).value)
        else (tableClass.simpleName)

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
        var prepare : PreparedStatement? = null
        var query : ResultSet? = null
        log("Selecionando tudo de $name")
        try {
            prepare = connection.prepareStatement(
                "SELECT * FROM $name"
            )
            query = prepare.executeQuery()
            while (query.next()) {
                val newData = newInstance.invoke()
                updateCache(newData, query)
                list.add(newData)
            }


        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        finally {
            query?.close()
            prepare?.close()
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
                var state : PreparedStatement? = null
                try {
                    state = connection.prepareStatement(
                        "ALTER TABLE $name " +
                                "DROP FOREIGN KEY ${column.foreignKeyName}"
                    )
                        state.executeUpdate()
                } catch (ex: SQLException) {
                    log("§cFalha ao deletar a Foreign key pois ela nao existe")
                }finally {
                    state?.close()
                }
            }
            //ALTER TABLE `party_user` DROP FOREIGN KEY `party`; ALTER TABLE `party_user` ADD CONSTRAINT `party` FOREIGN KEY (`party_id`) REFERENCES `partu`(`id`) ON DELETE SET NULL ON UPDATE SET NULL;
        }

    }



    override fun createReferences() {
        var state : PreparedStatement?= null
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
                    state = connection.prepareStatement(text)
                    state.executeUpdate()
                    columnsContraintsCreated.add(column.foreignKeyName)

                }
                //ALTER TABLE `party_user` DROP FOREIGN KEY `party`; ALTER TABLE `party_user` ADD CONSTRAINT `party` FOREIGN KEY (`party_id`) REFERENCES `partu`(`id`) ON DELETE SET NULL ON UPDATE SET NULL;
            }
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }finally {
            state?.close()
        }
    }

    override fun findByReference(reference: Any, cachedData: T?): T? {
        var referenceColumnName = primaryName
        var referenceId : Any = 1
        for (column in columns.values){
            if (column.javaType == reference.javaClass){
                referenceColumnName = column.name
                val primary = column.referenceTable.primaryColumn?:continue
                referenceId = primary.field.get(reference)
            }
        }
        return findByColumn(referenceColumnName,referenceId, cachedData);
    }

    override fun selectByReference(reference: Any): List<T> {
        val list = mutableListOf<T>()
        var prepare : PreparedStatement? = null
        var query : ResultSet? = null
        var referenceColumnName = primaryName
        var referenceId : Any = 1
        for (column in columns.values){
            if (column.javaType == reference.javaClass){
                referenceColumnName = column.name
                val primary = column.referenceTable.primaryColumn?:continue
                referenceId = primary.field.get(reference)
            }
        }
        try {
            val text = "SELECT * FROM $name WHERE $referenceColumnName = ?"
            prepare = connection.prepareStatement(
                text
            )
            val column = columns.values.first{it.name == referenceColumnName}
            prepare.setString(1, engine.convertToSQL(referenceId, column))
            log("Pegando: $text")
            query = prepare.executeQuery()

            while (query.next()) {
                val cachedData : T? = elements[query.getObject(primaryName)]
                val newData = cachedData ?: newInstance.invoke()
                updateCache(newData, query)
                list.add(newData)
            }

        } catch (ex: SQLException) {
            ex.printStackTrace()
        }finally {
            query?.close()
            prepare?.close()
        }
        return list
    }

    override fun findByColumn(columnName: String, columnValue: Any, cachedData: T?): T? {
        var prepare : PreparedStatement? = null
        var query : ResultSet? = null
        var result : T? = null
        try {
            val text = "SELECT * FROM $name WHERE $columnName = ? LIMIT 1"
            prepare = connection.prepareStatement(
                text
            )
            val column = columns.values.first{it.name == columnName}
            prepare.setString(1, engine.convertToSQL(columnValue, column))
            log("Encontrando: $text")
            query = prepare.executeQuery()

            if (query.next()) {
                val newData = cachedData ?: newInstance.invoke()
                updateCache(newData, query)
                result= newData
            }

        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        finally {
            prepare?.close()
            query?.close()
        }
        return result
    }

    override fun findByPrimary(primaryValue: Any, cachedData: T?): T? {
        return findByColumn(primaryName, primaryValue, cachedData)

    }


    override fun select(collums : String, where: String, columnOrder: String, ascending: Boolean, limit: Int): List<T> {
        var prepare : PreparedStatement? = null
        var query : ResultSet? = null
        val list = mutableListOf<T>()
        try {
            val collumsUsed = if (collums.isEmpty())"*" else collums
            var text =  "SELECT $collumsUsed FROM $name "
            if (where.isNotEmpty()) text+="WHERE $where"
            text+=" ORDER BY $columnOrder "+ (if (ascending) "ASC" else "DESC") + " LIMIT $limit"
            prepare = connection.prepareStatement(
               text
            )
            log("Selecionando: $text")
            query = prepare.executeQuery()
            while (query.next()) {
                val cachedData : T? = elements[query.getObject(primaryName)]
                val newData = cachedData?: newInstance.invoke()
                updateCache(newData, query)
                list.add(newData)
            }

        } catch (ex: SQLException) {
            ex.printStackTrace()
        }finally {
            prepare?.close()
            query?.close()
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
                val referenceInstance = column.referenceTable.elements[key]
                if (referenceInstance != null) {
                    field.set(data, referenceInstance)
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
        var prepare : PreparedStatement? = null
        try {

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
            prepare = connection
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
            log("Inserindo: $builder")
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

        }finally {
            prepare?.close()
        }


    }


    override fun update(data: T,vararg columnsNames : String) {
        var prepare : PreparedStatement? = null
        try {
            val builder = StringBuilder("UPDATE $name SET ")

            for (column in columns.values) {
                if (column.isPrimary && column.isNumber) continue
                if (columnsNames.isNotEmpty()&& !columnsNames.contains(column.name))continue
                builder.append("${column.name} = ?,")
            }
            builder.deleteCharAt(builder.length - 1)
            builder.append(" WHERE ")
            builder.append(primaryName)
            builder.append(" = ?")
            prepare = connection
                .prepareStatement(builder.toString())
            var id = 1
            for ((field, column) in columns) {
                if (column.isPrimary && column.isNumber) continue
                if (columnsNames.isNotEmpty()&& !columnsNames.contains(column.name))continue
                field.isAccessible = true
                val fieldValue = field.get(data)
                if (column.isConstraint && fieldValue != null) {
                    val primaryKey = column.referenceTable.primaryColumn!!
                        .field.get(fieldValue) ?: 0
                    prepare.setObject(id, primaryKey)
                    id++
                    continue
                }
                if (fieldValue == null || (column.isNumber &&
                            column.isPrimary)) {
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
            log("Atualizando: $builder")
            prepare.executeUpdate()
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }finally {
            prepare?.close()
        }
    }

    override fun delete(data: T) {
        var prepare : PreparedStatement? = null
        try {

            prepare = connection.prepareStatement(
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
        } finally {
            prepare?.close()
        }
    }

    override fun createCollumns() {
        var columnsState : PreparedStatement? = null
        var columnsQuery : ResultSet? = null
        try {

            val columnsSql = "SELECT * FROM information_schema.COLUMNS WHERE TABLE_NAME = ?"
            columnsState = connection.prepareStatement(columnsSql)
            columnsState.setString(1, name)
            columnsQuery = columnsState.executeQuery()
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
        }finally {
            columnsQuery?.close()
            columnsState?.close()
        }
    }


}