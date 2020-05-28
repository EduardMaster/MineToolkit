package net.eduard.api.lib.database.sqlite;

import net.eduard.api.lib.database.api.SQLOption;

public class SQLiteOption implements SQLOption {

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
