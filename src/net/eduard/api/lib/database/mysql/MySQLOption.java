package net.eduard.api.lib.database.mysql;

import net.eduard.api.lib.database.api.IOption;

public class MySQLOption implements IOption {
    @Override
    public String primaryKey() {
        return "PRIMARY KEY";
    }

    @Override
    public String autoIncrement() {
        return "AUTO_INCREMENT";
    }

    @Override
    public String varText(int size) {
        return "VARCHAR (" +size+" )";
    }
}
