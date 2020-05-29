package net.eduard.api.lib.database.api;

import net.eduard.api.lib.database.api.entity.SQLColumn;
import net.eduard.api.lib.database.api.entity.SQLRecord;
import net.eduard.api.lib.database.api.entity.SQLTable;

import java.util.Map;

public interface SQLQueryBuilder {

    SQLOption option();

    StringBuilder builder();

    StringBuilder newQuery();

    default StringBuilder space() {
        return builder().append(" ");
    }


    default String createTable(SQLTable table) {

        newQuery().append(option().createTable());

        space().append(option().name(table.getTableName()));
        builder().append(" (");
        for (SQLColumn column : table.getColumns()) {
            builder().append(option().name(column.getName()));

            if (column.getSqlType() == null) {
                column.setSqlType(option().sqlTypeOf(column.getJavaType(), column.getSize()));
            }
            space().append(column.getSqlType());
            boolean canHaveDefault = true;
            if (!column.isNullable()) {
                space().append(option().notNull());

            }
            if (column.isUnique()) {
                space().append(option().unique());
                canHaveDefault = false;
            }

            if (column.isPrimary()) {
                space().append(option().primaryKey());


                if (Number.class.isAssignableFrom(column.getJavaType())) {
                    space().append(option().autoIncrement());
                }
                canHaveDefault = false;
            }
            if (canHaveDefault) {
                space().append(option().defaults());


                if (column.getDefaultValue() == null) {

                    if (column.isNullable()) {
                        space().append("NULL");
                    } else {
                        space().append("'0'");
                    }
                } else {
                    space().append(option().data(option().convertToSQL(column.getDefaultValue())));
                }
            }




                builder().append(",");

            }
            builder().deleteCharAt(builder().length() - 1);

            builder().append(")");
            return builder().toString();

        }

        default
        String deleteTable (SQLTable table){
            return newQuery().append(option().deleteTable()).append(option().name(table.getTableName())).toString();
        }

        default
        String clearTable (SQLTable table){
            return newQuery().append(option().clearTable()).append(option().name(table.getTableName())).toString();
        }

        default
        String insertRecord (SQLTable table, Object instance){
            newQuery().append(option().insertData()).append(option().name(table.getTableName()));
            space();
            StringBuilder header = new StringBuilder();

            StringBuilder values = new StringBuilder();
            SQLRecord record = new SQLRecord(table, instance);
            for (Map.Entry<SQLColumn, Object> entry : record.getData().entrySet()) {
                SQLColumn column = entry.getKey();
                Object value = entry.getValue();
                header.append(option().name(column.getName()));
                header.append(",");
                values.append(option().data(value.toString()));
                values.append(",");
            }

            values.charAt(values.length() - 1);
            header.charAt(values.length() - 1);

            builder().append("(");
            builder().append(header.toString());
            builder().append(") VALUES (");
            builder().append(values.toString());
            builder().append(")");

            return builder().toString();


        }


    }
