package net.eduard.api.test;

import net.eduard.api.lib.manager.DBManager;

public class Testai {
	public static void main(String[] args) {
		DBManager db = new DBManager();
		db.openConnection();

		try {
			db.insert("accounts", "edu", "teste", "affs", 1, 1, 52345123, 1232341234);

		} catch (Exception e) {
			e.printStackTrace();
		}
		db.closeConnection();
	}

}
