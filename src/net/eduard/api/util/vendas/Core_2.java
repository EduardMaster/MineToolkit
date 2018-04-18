package net.eduard.api.util.vendas;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class Core_2 {

	public static void test(JavaPlugin plugin) {
		int port = Bukkit.getPort();
		String name = plugin.getName();
		String host = Bukkit.getIp();
		String server = getServerIp();
		

		Bukkit.getConsoleSender().sendMessage(
				"§aIP SERVER = " + host + " | IP CONECAO = " + server + " | PORTA "+port);
		if (host.equals("localhost")) {
			Bukkit.getConsoleSender()
					.sendMessage("§aPlugin liberado para tester em localhost");
		} else {
			int resultado = 0;
			resultado = getResult(name, host, port);
			if (resultado <= 0) {
				resultado = getResult(name, server, port);
			}
			if (resultado == -2) {
				Bukkit.getConsoleSender().sendMessage("§eSistema de Keys Corrompido avise-o Eduard");
				Bukkit.getPluginManager().disablePlugin(plugin);
				throw new Error("Autenticacao falha");
			}
			else	if (resultado == -1) {
				Bukkit.getConsoleSender().sendMessage("§eSistema de MySQL Corrompido avise-o Eduard");
				Bukkit.getPluginManager().disablePlugin(plugin);
				throw new Error("MySQL falha");
			}
			else if (resultado == 0) {
				Bukkit.getConsoleSender().sendMessage(
						"§cVoce nao comprou este Plugin: Entre em contato com Eduard!");
				Bukkit.getPluginManager().disablePlugin(plugin);
				throw new Error("Plugin invalido");
			} else if (resultado == 1) {
				Bukkit.getConsoleSender()
						.sendMessage("§aPlugin ativado com sucesso!");
			} else if (resultado == 2) {
				Bukkit.getConsoleSender().sendMessage(
						"§aPlugin expirado entre em contato com Eduard e reative o Plugin!");
				Bukkit.getPluginManager().disablePlugin(plugin);
				throw new Error("Plugin expirado");
			} else if (resultado == 100) {
				Bukkit.getConsoleSender().sendMessage(
						"§aPlugin sendo testado no Servidor do Eduard");
			}
		}
	}

	private static String getServerIp() {
		try {
			URLConnection connect = new URL("http://checkip.amazonaws.com/")
					.openConnection();
			connect.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			Scanner scan = new Scanner(connect.getInputStream());
			StringBuilder sb = new StringBuilder();
			while (scan.hasNext()) {
				sb.append(scan.next());
			}
			scan.close();
			return sb.toString();

		} catch (Exception ex) {

			String ip = null;
			return ip;
		}
	}

	
	
//	public class Vendas{
//		
//		private int id;
//		private String nome;
//		private String info;
//		
//		
//		
//		
//	}
	
	

	private static int getResult(String plugin, String host, int port) {
		try {
			String web = "https://eduarddev.000webhostapp.com/comprou.php";
			
//			String local = "http://localhost/Site/MeuSite/comprou.php";
			URLConnection connect = new URL(web + "?plugin=" + plugin + "&host="
					+ host + "&porta=" + port).openConnection();
//			connect.addRequestProperty("User-Agent",
//					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			Scanner scan = new Scanner(connect.getInputStream());
			StringBuilder sb = new StringBuilder();
			while (scan.hasNext()) {
				String text = scan.next();
				sb.append(text);
			}
			scan.close();
			return Integer.valueOf(sb.toString());

		} catch (Exception ex) {
//			ex.printStackTrace();
		}
		return 100;
	}

}
