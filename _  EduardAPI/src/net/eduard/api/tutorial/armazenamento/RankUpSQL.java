package net.eduard.api.tutorial.armazenamento;

import net.eduard.api.manager.DBManager;

public class RankUpSQL extends DBManager {

	public RankUpSQL(String user, String pass, String host, String database) {
		super(user, pass, host, database);
		update("create table if not exists ranks values(name varchar(11),prefix varchar(16),price double ,rankup varchar(11) )");
	}
	public void createRank(String name, String prefix, double price,
			String rankup) {
		update("update ranks values (?,?,?,?)", name.toLowerCase(), prefix,
				price, rankup.toLowerCase());
	}
	public void deleteRank(String name) {
		update("delete from ranks where name = ?", name.toLowerCase());
	}
	public boolean hasRank(String name) {
		return contains("select name from ranks where name = ?",
					name.toLowerCase());
	}
	
	
	
	

}
