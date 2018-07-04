package net.eduard.api.test.conversor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.eduard.api.lib.Extra;
import net.eduard.api.lib.manager.DBManager;

public class ConversorDeIconomy2 {

	public static void main(String[] args) throws URISyntaxException, IOException {
		DBManager db = new DBManager();
		DBManager.setDebug(false);
//		db.openConnection();
//		db.createTable("contas", "name varchar(16), coins double");
		URL file = ConversorDeIconomy2.class.getResource("accounts.mini");
		Path path = Paths.get(file.toURI());
		
//		URL file2 = ConversorDeIconomy2.class.getResource("players.dat");
		Path path2 = Paths.get("C:\\Users\\Eduard\\Desktop\\teste\\players.dat");
		
		List<String> lines = java.nio.file.Files.readAllLines(path);
		long inicio = System.currentTimeMillis();
		List<String> result = new ArrayList<>();
			for (String line : lines) {
				String[] split = line.split(" ");
				String name = split[0];
				// System.out.println(name);
				Double dinheiro = Extra.toDouble(split[1].split(":")[1]);
				String lei = name+" "+dinheiro;
				result.add(lei);
				System.out.println(lei);
			}
		Files.write(path2,result,Charset.forName("UTF-8"));
		long fim = System.currentTimeMillis();
		System.out.println("dif " + (fim - inicio));
		db.closeConnection();

	}

}
