package net.eduard.api.lib.database.api;

import net.eduard.api.lib.database.api.entity.SQLColumn;
import net.eduard.api.lib.database.api.entity.SQLRecord;
import net.eduard.api.lib.database.api.entity.SQLTable;

import java.util.Map;

public interface SQLQueryBuilder {

    SQLOption option();


    default String findRecord(SQLTable table, SQLColumn column, Object columnValue) {

        return new StringBuilder().append(option().selectData()).append(option().name(table.getTableName()))
                .append(option().where())
                .append(option().name(column.getName()))
                .append(option().equalsTo()).append(option().data(option().convertToSQL(columnValue,table.getPrimaryKey().getJavaType()))).
                        toString();
    }


    default String findRecords(SQLTable table) {
        return new StringBuilder().append(option().selectData()).append(option().name(table.getTableName())).toString();
    }

    default String deleteRecord(SQLRecord record) {
        SQLTable table = record.getTable();

        return new StringBuilder().append(option().deleteData()).append(option().name(table.getTableName()))
                .append(option().where())
                .append(option().name(table.getPrimaryKey().getName()))
                .append(option().equalsTo()).append(option().data(option().convertToSQL(record.getPrimaryKeyValue(),table.getPrimaryKey().getJavaType() ))).toString();
    }

    default String createTable(SQLTable table) {
        StringBuilder builder = new StringBuilder();
        builder.append(option().createTable());
        builder.append(option().name(table.getTableName()));
        builder.append(" (");
        for (SQLColumn column : table.getColumns()) {
            builder.append(option().name(column.getName()));

            if (column.getSqlType() == null) {
                column.setSqlType(option().sqlTypeOf(column.getJavaType(), column.getSize()));
            }
            builder.append(" ");
            builder.append(column.getSqlType());
            boolean canHaveDefault = true;
            if (!column.isNullable()) {
                builder.append(option().notNull());

            }
            if (column.isUnique()) {
                builder.append(option().unique());
                canHaveDefault = false;
            }

            if (column.isPrimary()) {
                builder.append(option().primaryKey());


                if (column.isAutoIncrement()) {
                    builder.append(option().autoIncrement());
                }
                canHaveDefault = false;
            }
            if (canHaveDefault) {
                builder.append(option().defaults());


                if (column.getDefaultValue() == null) {

                    if (column.isNullable()) {
                        builder.append("NULL");
                    } else {
                        builder.append(option().defaultFor(column.getJavaType()));
                    }
                } else {
                    builder.append(option().data(option().convertToSQL(column.getDefaultValue(),table.getPrimaryKey().getJavaType())));
                }
            }


            builder.append(",");

        }
        builder.deleteCharAt(builder.length() - 1);

        builder.append(")");
        return builder.toString();

    }


    default String deleteTable(SQLTable table) {
        return new StringBuilder().append(option().deleteTable()).append(option().name(table.getTableName())).toString();
    }

    default String clearTable(SQLTable table) {
        return new StringBuilder().append(option().clearTable()).append(option().name(table.getTableName())).toString();
    }

    default String updateRecord(SQLRecord record) {
        SQLTable table = record.getTable();
        StringBuilder builder = new StringBuilder();
        builder.append(option().updateData()).append(option().name(table.getTableName()));

        builder.append(option().updateDataSet());
        for (Map.Entry<SQLColumn, Object> entry : record.getData().entrySet()) {

            SQLColumn column = entry.getKey();
            if (column.isPrimary()) continue;
            Object value = entry.getValue();
            builder.append(option().name(column.getName()));
            builder.append(option().equalsTo());
            builder.append(option().data(option().convertToSQL(value , column.getJavaType())));
            builder.append(",");
        }
        Object primaryKeyValue = record.getPrimaryKeyValue();

        builder.deleteCharAt(builder.length() - 1);
        builder.append(option().where())
                .append(option().name(table.getPrimaryKey().getName()))
                .append(option().equalsTo()).append(option().data(option().convertToSQL(primaryKeyValue, table.getPrimaryKey().getJavaType())));


        return builder.toString();


    }

    default String insertRecord(SQLRecord record) {
        SQLTable table = record.getTable();
        StringBuilder builder = new StringBuilder();
        builder.append(option().insertData()).append(option().name(table.getTableName()));
        StringBuilder header = new StringBuilder();

        StringBuilder values = new StringBuilder();
        for (Map.Entry<SQLColumn, Object> entry : record.getData().entrySet()) {

            SQLColumn column = entry.getKey();
            if (column.isPrimary() && column.isAutoIncrement()) continue;
            Object value = entry.getValue();
            header.append(option().name(column.getName()));
            header.append(",");
            if (value == null&&column.isNullable()){
                values.append(option().nullable());
            }else {
                values.append(option().data(option().convertToSQL(value, column.getJavaType())));
            }
            values.append(",");
        }

        values.deleteCharAt(values.length() - 1);
        header.deleteCharAt(header.length() - 1);

        builder.append(" ( ");
        builder.append(header.toString());
        builder.append(" ) VALUES ( ");
        builder.append(values.toString());
        builder.append(" ) ");

        return builder.toString();


    }


}
