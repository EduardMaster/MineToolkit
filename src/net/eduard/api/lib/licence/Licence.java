package net.eduard.api.lib.licence;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.eduard.api.lib.modules.BungeeConfigs;
import net.eduard.api.lib.modules.Configs;
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
public class Licence {

	public static void main(String[] args) {
		
	}

	private static String site = "https://eduarddev.000webhostapp.com/teste/verify.php?";

	private static PluginActivationStatus test(String plugin, String owner, String key) {
		try {
			String link = site + "key=" + key + "&plugin=" + plugin + "&owner=" + owner;
			URLConnection connect = new URL(link).openConnection();
			System.out.println(link);
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
			e.printStackTrace();
			return PluginActivationStatus.PLUGIN_EXPIRED;
		}
	}

	private static enum PluginActivationStatus {

		INVALID_KEY("§cNao foi encontrado esta Licensa no Sistema."),
		WRONG_KEY("§cEsta Licensa nao bate com a do Sistema."),
		KEY_TO_WRONG_PLUGIN("§cA Licensa usada nao eh para este plugin."),
		KEY_TO_WRONG_OWNER("§cA Licensa usada nao eh para este Dono"),
		INVALID_IP("§cEste IP usado nao corresponde a Licensa"), INVALID_PORT("§cEsta Porta nao correponde a da Licensa."),
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
			Configs config = new Configs("license.yml", plugin);
			config.add("key", "INSIRA_KEY");
			config.add("owner", "INSIRA_Dono");
			config.saveDefault();
			String key = config.getString("key");
			String owner = config.getString("owner");
			Bukkit.getScheduler().runTaskAsynchronously(plugin, (Runnable) () ->

			{
				PluginActivationStatus result = Licence.test(pluginName, owner, key);
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
			BungeeConfigs config = new BungeeConfigs("license.yml", plugin);
			config.add("key", "INSIRA_KEY");
			config.add("owner", "INSIRA_Dono");
			config.saveConfig();
			String key = config.getString("key");
			String owner = config.getString("onwer");
			BungeeCord.getInstance().getScheduler().runAsync(plugin, () -> {

				PluginActivationStatus result = Licence.test(pluginName, owner, key);
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
