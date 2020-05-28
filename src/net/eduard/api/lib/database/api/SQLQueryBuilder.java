package net.eduard.api.lib.database.api;

import net.eduard.api.lib.database.api.entity.SQLColumn;
import net.eduard.api.lib.database.api.entity.SQLRecord;
import net.eduard.api.lib.database.api.entity.SQLTable;

import javax.persistence.Column;
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
        builder().append("(");
        for (SQLColumn column : table.getColumns()) {
            builder().append(option().name(column.getName()));

            space().append(column.getSqlType());
            if (!column.isNullable()) {
                space().append(option().notNull());
                space().append(option().defaults());
                space().append(option().data(option().convertToSQL(column.getDefaultValue())));
            }
            if (column.isUnique()) {
                space().append(option().unique());
            }
            if (column.isPrimary()) {
                space().append(option().primaryKey());
            }


        }
        builder().append(")");
        return builder().toString();

    }

    default String deleteTable(SQLTable table) {
        return newQuery().append(option().deleteTable()).append(option().name(table.getTableName())).toString();
    }

    default String clearTable(SQLTable table) {
        return newQuery().append(option().clearTable()).append(option().name(table.getTableName())).toString();
    }

    default String insertRecord(SQLTable table, Object instance) {
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
