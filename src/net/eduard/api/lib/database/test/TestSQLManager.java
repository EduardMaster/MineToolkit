package net.eduard.api.lib.database.test;

import net.eduard.api.lib.database.DBManager;
import net.eduard.api.lib.database.api.SQLManager;

public class TestSQLManager {
    public static void main(String[] args) {
        DBManager dbManager = new DBManager("root", "", "localhost");
        dbManager.openConnection();
        SQLManager manager = new SQLManager(dbManager.getConnection());
        System.out.println(manager);
        dbManager.closeConnection();
    }
}
