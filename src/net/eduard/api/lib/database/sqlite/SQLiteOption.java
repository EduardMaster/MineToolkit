package net.eduard.api.lib.database.sqlite;

import net.eduard.api.lib.database.api.IOption;

public class SQLiteOption implements IOption {

    @Override
    public String primaryKey() {
        return "PRIMARY_KEY";
    }

    @Override
    public String autoIncrement() {
        return "AUTOINCREMENT";
    }

    @Override
    public String varText(int size) {
        return "TEXT";
    }
}
