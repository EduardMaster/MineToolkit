package net.eduard.api.lib.database.mysql;

import net.eduard.api.lib.database.api.IOption;
import net.eduard.api.lib.database.api.SQLQueryBuilder;

public class MySQLQueryBuilder implements SQLQueryBuilder {
    private MySQLOption option= new MySQLOption();
    private StringBuilder builder = new StringBuilder();

    @Override
    public IOption option() {
        return option;
    }

    @Override
    public StringBuilder builder() {
        return builder;
    }
}
