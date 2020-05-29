package net.eduard.api.lib.database.test;

import net.eduard.api.lib.database.DBManager;
import net.eduard.api.lib.database.api.SQLEngineType;
import net.eduard.api.lib.database.api.SQLManager;

public class TestSQLManager {
    public static void main(String[] args) {
        DBManager dbManager = new DBManager("root", "", "localhost");
        dbManager.openConnection();
        SQLManager manager = new SQLManager(dbManager.getConnection(), SQLEngineType.MYSQL);
        System.out.println(manager);
        manager.createTable(PlayerData.class);
        dbManager.closeConnection();
    }
}
