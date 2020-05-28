package net.eduard.api.lib.database.mysql;

import net.eduard.api.lib.database.api.SQLOption;

public class MySQLOption implements SQLOption {
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
