package net.eduard.api.test.conversor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import net.eduard.api.lib.Extra;
import net.eduard.api.lib.manager.DBManager;

public class ConversorDeIconomy {

	public static void main(String[] args) throws URISyntaxException, IOException {
		DBManager db = new DBManager();
		DBManager.setDebug(false);
		db.openConnection();
		db.createTable("contas", "name varchar(16), coins double");
		URL file = ConversorDeIconomy.class.getResource("accounts.mini");
		Path path = Paths.get(file.toURI());
		List<String> lines = java.nio.file.Files.readAllLines(path);
		long inicio = System.currentTimeMillis();

		StringBuilder b = new StringBuilder();
		{
			b.append("INSERT INTO CONTAS VALUES ");

			boolean first = true;
			for (String line : lines) {
				if (!first) {
					b.append(", ");
				} else
					first = false;
				String[] split = line.split(" ");
				String name = split[0];
				// System.out.println(name);
				Double dinheiro = Extra.toDouble(split[1].split(":")[1]);
				// System.out.println(d);
				b.append("(DEFAULT,");
				b.append("'");
				b.append(name);
				b.append("',");

				b.append("'");
				b.append(dinheiro);
				b.append("')");
				// db.insert("contas", name,dinheiro);
			}
			b.append(";");
			try {
				PreparedStatement st = db.getConnection().prepareStatement(b.toString());
				st.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		{
		

//			boolean first = true;
//			for (String line : lines) {
//				
//				if (!first) {
//					b.append(", ");
//				} else
//					first = false;
//				String[] split = line.split(" ");
//				String name = split[0];
				// System.out.println(name);
//				Double dinheiro = Extra.toDouble(split[1].split(":")[1]);
				//b.append("UPDATE CONTAS SET coins = '"+dinheiro+"' where name = '"+name + "'; ");
				// System.out.println(d);
//				b.append("(DEFAULT,");
//				b.append("'");
//				b.append(name);
//				b.append("',");
//
//				b.append("'");
//				b.append(dinheiro);
//				b.append("')");
				// db.insert("contas", name,dinheiro);
//				break;
//			}
//			try {
//				b.append("UPDATE `contas` SET `coins` = '02' WHERE `contas`.`ID` = 1724;");
//				b.append("UPDATE `contas` SET `coins` = '02' WHERE `contas`.`ID` = 1724;");
//				PreparedStatement st = db.getConnection().prepareStatement(b.toString());
//				st.executeUpdate();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		{
//			db.deleteTable("contas");
		}
//		{
//			for (String line : lines) {
//				String[] split = line.split(" ");
//				String name = split[0];
//				// System.out.println(name);
//				Double dinheiro = Extra.toDouble(split[1].split(":")[1]);
//				db.change("contas", "coins = ?", "name = ?", dinheiro+1, name);
//			}
//		}
		long fim = System.currentTimeMillis();
		System.out.println("dif " + (fim - inicio));
		db.closeConnection();

	}

}
