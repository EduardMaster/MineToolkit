package net.eduard.api.lib.keys;

import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Protecao do Plugin (Protection)
 * @version 1.0
 * @since EduardAPI 5.0
 * @author Eduard
 *
 */
public class BukkitKeys1 {

	private String owner;
	private String plugin;
	private String host;
	private String port;
	private int timeToUse;
	private double price;
	private long time;

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getPlugin() {
		return plugin;
	}

	public void setPlugin(String plugin) {
		this.plugin = plugin;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public int getTimeToUse() {
		return timeToUse;
	}

	public void setTimeToUse(int timeToUse) {
		this.timeToUse = timeToUse;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public BukkitKeys1() {
		// TODO Auto-generated constructor stub
	}

	private static String divide = " ";

	public BukkitKeys1(String line) {
		String[] div = line.split(divide);

		this.plugin = div[0];
		this.owner = div[1];
		this.host = code(deob(div[2]));
		this.port = div[3];
		this.price = Double.valueOf(div[4]);
		this.timeToUse = Integer.valueOf(div[5]);
		this.time = Long.valueOf(div[6]);
	}

	private static String ob(String str) {
		String build = "";
		for (int i = 0; i < str.length(); i++) {
			build = build.equals("") ? "" + (str.charAt(i) + str.length() * str.length())
					: build + ";" + (str.charAt(i) + str.length() * str.length());
		}
		return build;
	}

	private static String deob(String str) {
		final String[] split = str.split(";");
		final int[] parse = new int[split.length];
		for (int i = 0; i < split.length; i++) {
			parse[i] = Integer.parseInt(split[i]) - split.length * split.length;
		}
		String build = "";
		for (int i = 0; i < split.length; i++) {
			build = build + (char) parse[i];
		}
		return build;
	}

	private static String code(String str) {
		int i = str.length();
		char[] a = new char[i];
		int i0 = i - 1;
		while (true) {
			if (i0 >= 0) {
				int i1 = str.charAt(i0);
				int i2 = i0 + -1;
				int i3 = (char) (i1 ^ 56);
				a[i0] = (char) i3;
				if (i2 >= 0) {
					i0 = i2 + -1;
					int i4 = str.charAt(i2);
					int i5 = (char) (i4 ^ 70);
					a[i2] = (char) i5;
					continue;
				}
			}
			return new String(a);
		}
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(plugin);
		builder.append(divide);
		builder.append(owner);
		builder.append(divide);
		builder.append(ob(code(host)));
		builder.append(divide);
		builder.append(port);
		builder.append(divide);
		builder.append(price);
		builder.append(divide);
		builder.append(timeToUse);
		builder.append(divide);
		builder.append(time);
		return builder.toString();
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);
		System.out.println("Escolha uma Opc§§o:");
		System.out.println("1: Gerar um Ip ofuscado");
		System.out.println("2: Gerar um Key nova");
		
		
		int opcao = Integer.valueOf(scan.nextLine());
		if (opcao == 1) {
			
		
			System.out.println("Digite um Numero de Ip");
			String ip = scan.nextLine();
			System.out.println(ob(code(ip)));

		} else if (opcao == 2) {

			while (true) {
				try {
					BukkitKeys1 venda = new BukkitKeys1();
					Scanner scanOwner = new Scanner(System.in);

					System.out.println("Digite Nome do Dono do Plugin");
					venda.owner = scanOwner.nextLine();

					Scanner scanPlugin = new Scanner(System.in);
					System.out.println("Digite Nome do Plugin");
					venda.plugin = scanPlugin.nextLine();

					Scanner scanPreco = new Scanner(System.in);
					System.out.println("Digite Pre§o do Plugin");
					venda.price = scanPreco.nextDouble();

					Scanner scanUse = new Scanner(System.in);
					System.out.println("Digite Tempo de Uso (Dias)");

					venda.timeToUse = scanUse.nextInt();

					Scanner scanHost = new Scanner(System.in);
					System.out.println("Digite Ip do Servidor");
					venda.host = scanHost.nextLine();

					Scanner scanPort = new Scanner(System.in);
					System.out.println("Digite Porta do Servidor");
					venda.port = scanPort.nextLine();

					venda.time = System.currentTimeMillis();
					System.out.println(venda.toString());
					break;
				} catch (Exception e) {
					System.out.println("Reiniciando o Programa de cria§§o de Keys");
				}

			}
		}
		
	}

	public static List<BukkitKeys1> getVendas(String url) {
		List<BukkitKeys1> vendas = new ArrayList<>();
		try {

			URLConnection connect = new URL(url).openConnection();
			connect.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			Scanner scan = new Scanner(connect.getInputStream());
			while (scan.hasNext()) {
				String line = scan.nextLine();
				if (line.isEmpty() | line.startsWith("|")) {
					continue;
				}
				vendas.add(new BukkitKeys1(line));
			}
			scan.close();

		} catch (Exception e) {
//			e.printStackTrace();
			return null;
		}
		return vendas;
	}

	public long toDays(long value) {
		return value / 24 / 60 / 1000;
	}

	public long fromDays(long value) {
		return value * 24 * 60 * 1000;
	}

	public static void test(JavaPlugin plugin) {
		checkOnline(plugin, "http://www.eduarddev.tk/protecao.txt");
	}

	public static String getServerIp() {
		try {
			URLConnection connect = new URL("http://checkip.amazonaws.com/").openConnection();
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

	public static void disablePlugin(JavaPlugin plugin, String error) {
		console("§cErro: §e" + error);
		Bukkit.getPluginManager().disablePlugin(plugin);
		throw new Error("Desativando Plugin");
	}

	public static void console(String message) {
		Bukkit.getConsoleSender().sendMessage(message);
	}

	public static void checkOnline(JavaPlugin plugin, String url) {
		console("§aEduard Developer");
		console("§bSite §fww.bit.ly/eduardsite");
		console("§bCanal §fwww.bit.ly/eduardsubscribe");
		console("§bSkype §flive:eduardkiller");
		console("§bDiscord §fwww.bit.ly/eduardwebchat");

		String SERVER_IP = getServerIp();
		String BUKKIT_IP = Bukkit.getIp();
		String PORTA = "" + Bukkit.getPort();
		// if (BUKKIT_IP.equals("25.5.161.183")) {
		// return true;
		// }

		// Bukkit.getConsoleSender().sendMessage("§aAutenticao do Plugin");
		Bukkit.getConsoleSender()
				.sendMessage("§aHOST: §f" + BUKKIT_IP + " §aINTERNET: §f" + SERVER_IP + " §aPORTA: §f" + PORTA);
		if (BUKKIT_IP.equals("localhost")) {
			Bukkit.getConsoleSender().sendMessage("§aPlugin foi liberado para testes em 'localhost'");
			return;
		}
		List<BukkitKeys1> keys = getVendas(url);
		if (keys == null) {
			
			Bukkit.getConsoleSender().sendMessage("§aVendas nao encontradas site esta indisponivel");
			return;
		}
		Bukkit.getConsoleSender().sendMessage("§aVendas encontradas: §f" + keys.size());

		String error = "";
		String first = error;

		for (BukkitKeys1 license : keys) {
			error = "";
			if (!(license.getPlugin().equals(plugin.getName()))) {
				continue;
			}
			if (
					
					(license.getHost().equals(SERVER_IP) || license.getHost().equals(BUKKIT_IP))
					&& license.getPort().equals(PORTA)
					
					
					) {
				long before = license.getTime();
				long time = license.getTimeToUse();
				long now = System.currentTimeMillis();
				// Bukkit.getConsoleSender().sendMessage("T " + time);
				// Bukkit.getConsoleSender().sendMessage("B " + before);
				// Bukkit.getConsoleSender().sendMessage("N " + now);
				boolean finish = ((before + (time * 24 * 60 * 60 * 1000)) < now);
				Bukkit.getConsoleSender().sendMessage(" §aPlugin comprado em: §f" + new Date(before));
				Bukkit.getConsoleSender().sendMessage(" §aTempo da compra: §f" + time + " §adias!");
				if (finish) {
					error += "Tempo da compra foi expirado! Renove-a!";
					break;
				} else {
					Bukkit.getConsoleSender().sendMessage("§aPlugin ativado com sucesso!");
					return;
				}

			} else {
				error += "Possui este plugin no site porem nao esta configurado o Ip corretamente!";
			}

		}
		if (error.equals(first)) {
			error += "Nao possui este plugin no Site";
		}
		disablePlugin(plugin, error);

	}

}