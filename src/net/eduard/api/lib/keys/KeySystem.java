package net.eduard.api.lib.keys;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.eduard.api.lib.core.BungeeConfig;
import net.eduard.api.lib.core.ConfigAPI;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Sistema de verificacao de Compra do Plugin para Bungee ou Bukkit
 * 
 * @version 1.0
 * @author Eduard
 *
 */
public class KeySystem {

	private static String site = "https://eduarddev.000webhostapp.com/plugins-shop/verify.php?";

	private static PluginActivationStatus test(String plugin, String owner, String key) {
		try {

			URLConnection connect = new URL(site + "key=" + key + "&plugin=" + plugin + "&owner=" + owner)
					.openConnection();
			connect.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			Scanner scan = new Scanner(connect.getInputStream());
			StringBuilder b = new StringBuilder();
			while (scan.hasNext()) {
				String text = scan.next();
				b.append(text);
			}
			scan.close();
			if (b.toString().isEmpty()) {
				return PluginActivationStatus.PLUGIN_ACTIVATED;
			}
			return PluginActivationStatus.valueOf(b.toString().toUpperCase().replace(" ", "_"));
		} catch (Exception e) {
			return PluginActivationStatus.SITE_OFFLINE;
		}
	}

	private static enum PluginActivationStatus {

		INVALID_KEY("§cNão foi encontrado esta Key no Sistema."), WRONG_KEY("§cEsta key não bate com a do Sistema."),
		KEY_TO_WRONG_PLUGIN("§cA key usada não é para este plugin."),
		KEY_TO_WRONG_OWNER("§cA key usada não é para este Dono"),
		SITE_OFFLINE("§aSite offline portanto liberado para testes.", true),
		INVALID_IP("§cEste IP usado não corresponde a Key"), INVALID_PORT("§cEsta Porta não correponde a Key."),
		PLUGIN_EXPIRED("§cO plugin expirou."), PLUGIN_ACTIVATED("§aPlugin ativado com sucesso.", true);

		private String message;
		private boolean active;

		private PluginActivationStatus() {

		}

		private PluginActivationStatus(String message) {
			setMessage(message);
		}

		private PluginActivationStatus(String message, boolean active) {
			setMessage(message);
			setActive(active);
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}
	}

	public static class BukkitTester {

		public static void test(JavaPlugin plugin, Runnable activation) {

			String pluginName = plugin.getName();
			Bukkit.getConsoleSender().sendMessage("§aAutenticando o plugin " + pluginName);
			ConfigAPI config = new ConfigAPI("license.yml", plugin);
			config.add("key", "INSIRA_KEY");
			config.add("owner", "INSIRA_Dono");
			config.saveDefault();
			String key = config.getString("key");
			String owner = config.getString("onwer");
			Bukkit.getScheduler().runTaskAsynchronously(plugin, (Runnable) () ->

			{
				PluginActivationStatus result = KeySystem.test(pluginName, owner, key);
				Bukkit.getConsoleSender().sendMessage(result.getMessage());
				if (!result.isActive()) {
					Bukkit.getPluginManager().disablePlugin(plugin);
				} else {
					activation.run();
				}

			});

		}

	}

	public static class BungeeTester {

		public static void test(Plugin plugin, Runnable activation) {
			String pluginName = plugin.getDescription().getName();
			BungeeCord.getInstance().getConsole()
					.sendMessage(new TextComponent("§aAutenticando o plugin " + pluginName));
			BungeeConfig config = new BungeeConfig("license.yml", plugin);
			config.add("key", "INSIRA_KEY");
			config.add("owner", "INSIRA_Dono");
			config.saveConfig();
			String key = config.getString("key");
			String owner = config.getString("onwer");
			BungeeCord.getInstance().getScheduler().runAsync(plugin, () -> {

				PluginActivationStatus result = KeySystem.test(pluginName, owner, key);
				BungeeCord.getInstance().getConsole().sendMessage(new TextComponent(result.getMessage()));
				if (!result.isActive()) {
					BungeeCord.getInstance().getPluginManager().unregisterListeners(plugin);
					BungeeCord.getInstance().getPluginManager().unregisterCommands(plugin);
				} else {
					activation.run();
				}

			});

		}
	}

}
