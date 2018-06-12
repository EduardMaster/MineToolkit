package net.eduard.api.lib.trade;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Shield sistema de key Automatizado
 * @version 1.0
 * @since 6.0
 * @author Eduard
 *
 */
public class Shield {

	private String owner;
	private String plugin;
	private String host;
	private UUID key;
	private long use;
	private long sale;
	private double price;

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		while (true) {
			try {
				Shield shield = new Shield();
				Scanner scanOwner = new Scanner(System.in);

				System.out.println("Digite Nome do Dono do Plugin");
				shield.owner = scanOwner.nextLine();

				Scanner scanPlugin = new Scanner(System.in);
				System.out.println("Digite Nome do Plugin");
				shield.plugin = scanPlugin.nextLine();

				Scanner scanPreco = new Scanner(System.in);
				System.out.println("Digite Preço do Plugin");
				shield.price = scanPreco.nextDouble();

				Scanner scanUse = new Scanner(System.in);
				System.out.println("Digite Tempo de Uso (Dias)");

				shield.use = scanUse.nextLong();

				Scanner scanHost = new Scanner(System.in);
				System.out.println("Digite Ip do Servidor");
				shield.host = scanHost.nextLine();

				shield.sale = System.currentTimeMillis();
				shield.key = UUID.randomUUID();
				System.out.println("Key gerada: " + shield.key);
				System.out.println("Key online: " + shield.getOnline());
				File file = new File("");
				shield.write(file);
				break;
			} catch (Exception e) {
				System.out.println("Reiniciando o Programa de criação de Keys");
			}

		}

	}
	public String getOffline() {
		return ob(code(getOnline()));
	}
	public String getOnline() {
		StringBuilder a = new StringBuilder();
		a.append("owner=" + owner);
		a.append(" ");
		a.append("plugin=" + plugin);
		a.append(" ");
		a.append("host=" + host);
		a.append(" ");
		a.append("key=" + ob(code("" + key)));
		a.append(" ");
		a.append("use=" + use);
		a.append(" ");
		a.append("sale=" + sale);
		a.append(" ");
		a.append("price=" + price);
		return a.toString();
	}
	public void write(File file) {
		try {
			Files.write(file.toPath(), getOffline().getBytes());
		} catch (Exception e) {
		}
	}
	private static Shield read(String text) {
		Shield shield = new Shield();
		String[] vars = text.split(" ");
		shield.owner = vars[0].split("=")[1];
		shield.plugin = vars[1].split("=")[1];
		shield.host = vars[2].split("=")[1];
		shield.key = UUID.fromString(code(deob(vars[3].split("=")[1])));
		shield.use = Long.valueOf(vars[4].split("=")[1]);
		shield.sale = Long.valueOf(vars[5].split("=")[1]);
		shield.price = Double.valueOf(vars[6].split("=")[1]);
		return shield;
	}
	@SuppressWarnings("unused")
	private static Shield read(File file) {
		try {
			String text = Files
					.readAllLines(file.toPath(), Charset.defaultCharset())
					.get(0);
			return read(code(deob(text)));
		} catch (Exception e) {
		}

		return null;
	}
	public static List<Shield> getShields(String url) {
		try {
			List<Shield> shields = new ArrayList<>();
			URLConnection connect = new URL(url).openConnection();
			connect.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			Scanner scan = new Scanner(connect.getInputStream());
			while (scan.hasNext()) {
				shields.add(read(scan.nextLine()));
			}
			scan.close();
			return shields;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public long toDays(long value) {
		return value / 24 / 60 / 1000;
	}
	public long fromDays(long value) {
		return value * 24 * 60 * 1000;
	}
	public static boolean test(JavaPlugin plugin) {
		return checkOnline(plugin,
				"https://eduarddev.000webhostapp.com/arquivos.txt");
	}
	public static String getServerIp() {
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
	public static boolean checkOnline(JavaPlugin plugin, String url) {
		String SERVER_IP = getServerIp();
		String BUKKIT_IP = Bukkit.getIp();
		// if (BUKKIT_IP.equals("25.5.161.183")) {
		// return true;
		// }
		if (BUKKIT_IP.equals("localhost")) {
			Bukkit.getConsoleSender().sendMessage(
					" §aPlugin foi liberado para testes em LocalHost");
			return false;
		}

		// license.key

		// license.yml
		// key: tua key vai aqui
		//

		Bukkit.getConsoleSender()
				.sendMessage("- §eSistema de verificacao de Keys por Site §f-");

		List<Shield> keys = getShields(url);
		Bukkit.getConsoleSender().sendMessage(" §a" + keys.size() + " §fKeys!");
		Bukkit.getConsoleSender().sendMessage(
				" §aIp: §f" + BUKKIT_IP + " §8| §aIp: §f" + SERVER_IP);

		File file = new File(plugin.getDataFolder(), "licence.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.addDefault("COMPRADOR", "Nome do Comprador");
		config.addDefault("KEY", "{KEY}");
		config.options().copyDefaults(true);
		try {
			config.save(file);
		} catch (Exception e) {
		}
		String COMPRADOR = config.getString("COMPRADOR");
		String KEY = config.getString("KEY");
		Bukkit.getConsoleSender().sendMessage(" §aDono: §f" + COMPRADOR);
		Bukkit.getConsoleSender().sendMessage(" §aKey: §f" + KEY);

		String error = "§eProblemas: §c";
		String first = error;

		for (Shield license : keys) {
			if (!(license.getOwner().equals(COMPRADOR))) {
				continue;
			}
			if (!(license.getPlugin().equals(plugin.getName()))) {
				continue;
			}
			if (!license.getKey().toString().equals(KEY)) {
				error += "Key Invalida!";
				break;
			}
			if (!license.getHost().equals(SERVER_IP)) {

			} else if (!license.getHost().equals(BUKKIT_IP)) {
				error += "Ip da Host Invalida!";
				break;
			}

			long before = license.getSale();
			long time = license.getUse();
			long now = System.currentTimeMillis();
			// Bukkit.getConsoleSender().sendMessage("T " + time);
			// Bukkit.getConsoleSender().sendMessage("B " + before);
			// Bukkit.getConsoleSender().sendMessage("N " + now);
			boolean finish = ((before + (time * 24 * 60 * 60 * 1000)) < now);
			Bukkit.getConsoleSender().sendMessage(
					" §aPlugin comprado em: §f" + new Date(before));
			Bukkit.getConsoleSender()
					.sendMessage(" §aTempo da compra: §f" + time + " §adias!");
			if (finish) {
				error += "Tempo da compra foi expirado! §aRenove-a!";
				break;
			} else {
				Bukkit.getConsoleSender()
						.sendMessage(" §aPlugin ativado com sucesso!");
				return true;
			}

		}
		if (error.equals(first)) {
			error += "Não foi encontrado nenhuma Key no Site!";
		}
		Bukkit.getConsoleSender().sendMessage(error);
		Bukkit.getConsoleSender().sendMessage(" §cPlugin desativado!");
		Bukkit.getPluginManager().disablePlugin(plugin);
		return false;

	}

	public static boolean check(JavaPlugin plugin) {

		return false;
	}

	private static String ob(String str) {
		String build = "";
		for (int i = 0; i < str.length(); i++) {
			build = build.equals("")
					? "" + (str.charAt(i) + str.length() * str.length())
					: build + ";"
							+ (str.charAt(i) + str.length() * str.length());
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
	public UUID getKey() {
		return key;
	}
	public void setKey(UUID key) {
		this.key = key;
	}
	public long getUse() {
		return use;
	}
	public void setUse(long use) {
		this.use = use;
	}
	public long getSale() {
		return sale;
	}
	public void setSale(long sale) {
		this.sale = sale;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

}
