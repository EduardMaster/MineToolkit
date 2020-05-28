package net.eduard.api.lib.database.sqlite;

import net.eduard.api.lib.database.api.SQLOption;
import net.eduard.api.lib.database.api.SQLQueryBuilder;
import net.eduard.api.lib.database.mysql.MySQLOption;

public class SQLiteQueryBuilder implements SQLQueryBuilder {
    private SQLiteOption option = new SQLiteOption();
    private StringBuilder builder = new StringBuilder();

    @Override
    public SQLOption option() {
        return option;
    }

    @Override
    public StringBuilder builder() {
        return builder;
    }

    @Override
    public StringBuilder newQuery() {
        this.builder = new StringBuilder();
        return builder;
    }
}
