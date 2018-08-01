package net.eduard.api.test.java;

import net.eduard.api.lib.manager.DBManager;

public class AutobaseTester {
	public static void main(String[] args) {
		DBManager db = new DBManager();
		db.openConnection();
		MeuObjeto teste = new MeuObjeto();
		
		teste.createTable(db.getConnection());
//		teste.insert(db.getConnection());
//		teste.id = 29;
		teste.updateCache(db.getConnection());
//		teste.duplo = 10;
		teste.updateTable(db.getConnection());
//		List<MeuObjeto> listaBolada = teste.getAll(db.getConnection(), "id", false);
//		teste.delete(db.getConnection());
		db.closeConnection();
	}
}
