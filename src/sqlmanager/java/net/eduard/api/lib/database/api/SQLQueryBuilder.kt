package net.eduard.api.lib.database.api

interface SQLQueryBuilder {
    fun option(): SQLOption
    fun findRecord(
        table: SQLTable,
        column: SQLColumn,
        columnValue: Any?
    ): String? {
        return StringBuilder().append(option().selectData()).append(option().name(table.tableName))
            .append(option().where())
            .append(option().name(column.name))
            .append(option().equalsTo()).append(
                option().data(
                    option()
                        .convertToSQL(columnValue, table.primaryKey.javaType, table.primaryKey)
                )
            ).toString()
    }

    fun findRecords(table: SQLTable): String? {
        return StringBuilder().append(option()
            .selectData()).append(option().name(table.tableName)).toString()
    }

    fun deleteRecord(record: SQLRecord): String? {
        val table = record.table
        return StringBuilder().append(option().deleteData()).append(option().name(table.tableName))
            .append(option().where())
            .append(option().name(table.primaryKey.name))
            .append(option().equalsTo()).append(
                option().data(
                    option()
                        .convertToSQL(
                            record.primaryKeyValue,
                            table.primaryKey.javaType,
                            table.primaryKey
                        )
                )
            ).toString()
    }

    fun createTable(table: SQLTable): String? {
        val builder = StringBuilder()
        builder.append(option().createTable())
        builder.append(option().name(table.tableName))
        builder.append(" (")
        for (column in table.columns) {
            builder.append(option().name(column.name))
            if (column.sqlType == null) {
                column.sqlType = option()
                    .sqlTypeOf(column.javaType, column.size)
            }
            builder.append(" ")
            builder.append(column.sqlType)
            var canHaveDefault = true
            if (!column.isNullable) {
                builder.append(option().notNull())
            }
            if (column.isUnique) {
                builder.append(option().unique())
                canHaveDefault = false
            }
            if (column.isPrimary) {
                builder.append(option().primaryKey())
                if (column.isAutoIncrement) {
                    builder.append(option().autoIncrement())
                }
                canHaveDefault = false
            }
            if (canHaveDefault) {
                builder.append(option().defaults())
                if (column.defaultValue == null) {
                    if (column.isNullable) {
                        builder.append("NULL")
                    } else {
                        builder.append(option().defaultFor(column.javaType))
                    }
                } else {
                    builder.append(
                        option().data(
                            option()
                                .convertToSQL(
                                    column.defaultValue, table.primaryKey
                                        .javaType, column
                                )
                        )
                    )
                }
            }
            builder.append(",")
        }
        builder.deleteCharAt(builder.length - 1)
        builder.append(")")
        return builder.toString()
    }

    fun deleteTable(table: SQLTable): String? {
        return StringBuilder().append(option().deleteTable())
            .append(option().name(table.tableName)).toString()
    }

    fun clearTable(table: SQLTable): String? {
        return StringBuilder().append(option().clearTable())
            .append(option().name(table.tableName)).toString()
    }

    fun updateRecord(record: SQLRecord): String? {
        val table = record.table
        val builder = StringBuilder()
        builder.append(option().updateData()).append(option().name(table.tableName))
        builder.append(option().updateDataSet())
        for ((column, value) in record.data) {
            if (column.isPrimary) continue
            builder.append(option().name(column.name))
            builder.append(option().equalsTo())
            if (column.isNullable && value == null) {
                builder.append(option().nullable())
            } else {
                builder.append(
                    option().data(
                        option()
                            .convertToSQL(value, column.javaType, column)
                    )
                )
            }
            builder.append(",")
        }
        val primaryKeyValue = record.primaryKeyValue
        builder.deleteCharAt(builder.length - 1)
        builder.append(option().where())
            .append(option().name(table.primaryKey.name))
            .append(option().equalsTo()).append(
                option().data(
                    option()
                        .convertToSQL(primaryKeyValue, table.primaryKey.javaType, table.primaryKey)
                )
            )
        return builder.toString()
    }

    fun insertRecord(record: SQLRecord): String? {
        val table = record.table
        val builder = StringBuilder()
        builder.append(option().insertData()).append(option().name(table.tableName))
        val header = StringBuilder()
        val values = StringBuilder()
        for ((column, value) in record.data) {
            if (column.isPrimary && column.isAutoIncrement) continue
            header.append(option().name(column.name))
            header.append(",")
            if (value == null && column.isNullable) {
                values.append(option().nullable())
            } else {
                values.append(option().data(option().convertToSQL(value, column.javaType, column)))
            }
            values.append(",")
        }
        values.deleteCharAt(values.length - 1)
        header.deleteCharAt(header.length - 1)
        builder.append(" ( ")
        builder.append(header.toString())
        builder.append(" ) VALUES ( ")
        builder.append(values.toString())
        builder.append(" ) ")
        return builder.toString()
    }
}