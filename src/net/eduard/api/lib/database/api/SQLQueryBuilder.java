package net.eduard.api.lib.database.api;

public interface SQLQueryBuilder {

    SQLOption option();
    StringBuilder builder();

    default StringBuilder space(){
        return builder().append(" ");
    }

    default String createTable(SQLTable table){
        builder().append(option().createTable());

        space().append(option().name(table.getTableName()));
        builder().append("(");
        for (SQLColumn column : table.getColumns()){
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
    default String deleteTable(SQLTable table){
        return builder().append(option().deleteTable()).append(option().name(table.getTableName())).toString();
    }


}
