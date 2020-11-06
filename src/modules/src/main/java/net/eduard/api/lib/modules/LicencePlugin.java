package net.eduard.api.lib.modules;


import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Scanner;

/**
 * Sistema de verificacao de Compra do Plugin para Bungee ou Bukkit
 * 
 * @version 1.0
 * @author Eduard
 *
 */
public class LicencePlugin {

	private static final String site = "link";

	private static PluginActivationStatus test(String plugin, String owner, String key) {
		try {
			String link = site + "key=" + key + "&plugin=" + plugin + "&owner=" + owner;
			URLConnection connect = new URL(link).openConnection();
			connect.setConnectTimeout(5000);
			connect.setReadTimeout(5000);
			//System.out.println(tag + "Verificando pelo link: " + link);
			connect.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			Scanner scan = new Scanner(connect.getInputStream());
			StringBuilder b = new StringBuilder();
			while (scan.hasNext()) {
				String text = scan.next();
				b.append(text);
			}
			scan.close();
			try {
				return PluginActivationStatus.valueOf(b.toString().toUpperCase().replace(" ", "_"));
			} catch (Exception ex) {
				ex.printStackTrace();
				return PluginActivationStatus.ERROR;
			}
		}catch(IOException ex){
			ex.printStackTrace();
			return PluginActivationStatus.SITE_OFF;
		} catch (Exception e) {
			e.printStackTrace();
			return PluginActivationStatus.ERROR;
		}
	}

	private enum PluginActivationStatus {

		INVALID_KEY("§cNao foi encontrado esta Licensa no Sistema."),
		WRONG_KEY("§cNao possui esta Licensa no Sistema."),
		KEY_TO_WRONG_PLUGIN("§cA Licensa usada nao serve para este plugin."),
		KEY_TO_WRONG_OWNER("§cA Licensa usada nao serve para este Dono"),
		INVALID_IP("§cEste IP usado nao corresponde ao IP da Licensa"),
		ERROR("§cO plugin nao ativou pois deu algum tipo de erro ao receber a resposta do Site"),
		SITE_OFF("§eO Sistema de licença nao respondeu, §aplugin ativado para testes", true),
		PLUGIN_ACTIVATED("§aPlugin ativado com sucesso, Licensa permitida.", true),
		FOR_TEST("§aO plugin foi liberado para testes no PC do Eduard", true);


		private String message;
		private boolean active;


		PluginActivationStatus(String message) {
			setMessage(message);
		}

		PluginActivationStatus(String message, boolean active) {
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
	private static class License{
		private String owner="DONO";
		private String key="KEY_RANDOM";


		public License(File file){
			if (file.exists()){
				reload(file);
			}else{
				save(file);
			}

		}


		public void save(File file){
			try {
				Files.write(file.toPath(),(owner+";"+key).getBytes( StandardCharsets.UTF_8));
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		public void reload(File file){
			try {
				String data = Files.readAllLines(file.toPath(),StandardCharsets.UTF_8).get(0);
				String[] split = data.split(";");
				owner = split[0];
				key = split[1];

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static class BukkitTester {

		public static void test(JavaPlugin plugin, Runnable activation) {

			String pluginName = plugin.getName();
			String tag = "§b[" + plugin.getName() + "] §f";
			Bukkit.getConsoleSender().sendMessage(tag + "§eFazendo autenticacao do Plugin no site");
			License license = new License(new File(plugin.getDataFolder(),"license.txt"));

			Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->

			{
				PluginActivationStatus result = LicencePlugin.test(pluginName, license.owner,license.key);

				Bukkit.getConsoleSender().sendMessage(tag + result.getMessage());
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
			ProxyServer.getInstance().getConsole()
					.sendMessage(new TextComponent("§aAutenticando o plugin " + pluginName));
			License license = new License(new File(plugin.getDataFolder(),"license.txt"));

			String tag = "§b[" + plugin.getDescription().getName() + "] §f";
			ProxyServer.getInstance().getScheduler().runAsync(plugin, () -> {

				PluginActivationStatus result = LicencePlugin.test(pluginName, license.owner, license.key);
				ProxyServer.getInstance().getConsole().sendMessage(new TextComponent(tag + result.getMessage()));
				if (!result.isActive()) {
					ProxyServer.getInstance().getPluginManager().unregisterListeners(plugin);
					ProxyServer.getInstance().getPluginManager().unregisterCommands(plugin);
				} else {
					activation.run();
				}

			});

		}
	}

}
