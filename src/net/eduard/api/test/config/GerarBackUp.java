package net.eduard.api.test.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import net.eduard.api.lib.manager.DBManager;

public class GerarBackUp {
	public static void main(String[] args) {
		DBManager db = new DBManager();
		db.setUseSQLite(true);
		db.setDatabase("F:\\Tudo\\Infos\\Trabalhos\\database.db");
		db.openConnection();
		File f = new File("F:\\\\Tudo\\\\Infos\\\\Trabalhos\\\\players.dat");
		try {
			f.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet q = db.select("select * from solaryeconomy");
		try {

			ResultSetMetaData m = q.getMetaData();
			StringBuilder g = new StringBuilder();
			for (int x = 1; x < m.getColumnCount(); x++) {
				System.out.println(m.getColumnName(x));
			}
			while (q.next()) {
				g.append(q.getString("name"));
				g.append(" ");
				g.append(q.getDouble("valor"));
				g.append('\n');
				StringBuilder b = new StringBuilder();
				for (int x = 1; x < m.getColumnCount(); x++) {
					b.append(" ");
					b.append(q.getObject(x));
				}
				System.out.println(b.toString());
			}
			try {
				Files.write(f.toPath(), g.toString().getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("foi");
		db.closeConnection();
	}
}
