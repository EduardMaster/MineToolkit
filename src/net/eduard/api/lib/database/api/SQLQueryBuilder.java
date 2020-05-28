package net.eduard.api.lib.database.api;

public interface SQLQueryBuilder {

    IOption option();
    StringBuilder builder();

    default StringBuilder space(){
        return builder().append(" ");
    }

    default String createTable(TableData table){
        builder().append(option().createTable());

        space().append(option().name(table.getTableName()));
        builder().append("(");
        for (ColumnData column : table.getColumns()){
            builder().append(option().name(column.getName()));

            space().append(column.getSqlType());
            if (!column.isNullable()){
                space().append(option().notNull());
            }
            if (column.isUnique()){
                space().append("UNIQUE");
            }
            if (column.isPrimary()){
                space().append(option().primaryKey());
            }

        }
        builder().append(")");
        return builder().toString();

    }
    default String deleteTable(TableData table){
        return builder().append(option().deleteTable()).append(option().name(table.getTableName())).toString();
    }


}
