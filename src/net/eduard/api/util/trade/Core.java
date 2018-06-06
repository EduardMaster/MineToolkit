package net.eduard.api.util.trade;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.eduard.api.lib.core.ConfigAPI;
/**
 * Ultimo sistema de verificacao de Compra do Plugin
 * @version 1.0
 * @since EduardAPI 6.1
 * @author Eduard
 *
 */
public class Core {

	public static boolean ativado = false;

	public static void verificarCompra(JavaPlugin plugin, Runnable ligando) {

		new BukkitRunnable() {

			@Override
			public void run() {
				if (ativado == false) {
					if (plugin.isEnabled()) {
						Bukkit.getPluginManager().disablePlugin(plugin);
					}
				}
			}
		}.runTaskTimerAsynchronously(plugin, 40, 100);
		new BukkitRunnable() {

			@Override
			public void run() {
				try {
					String nomePlugin = plugin.getName();
					Bukkit.getConsoleSender().sendMessage("§aAutenticando o plugin "+nomePlugin);
					ConfigAPI config = new ConfigAPI("licença.yml", plugin);
					config.add("key", "KEY_INSERE_AQUI");
					String key = config.getString("key");
					config.saveDefault();
					URLConnection connect = new URL("https://eduarddev.000webhostapp.com/plugins/verify.php?key=" + key)
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
					
					if (b.toString().equals("key")) {
						Bukkit.getConsoleSender().sendMessage(
								"§cNao foi encontrado a key no nosso sistema, entre em contato com Eduard");
					} else if (b.toString().equals("ip")) {
						Bukkit.getConsoleSender()
								.sendMessage("§cEste servidor nao possui o Ip correto da Key do plugin.");
					} else if (!b.toString().equals(nomePlugin)) {
						Bukkit.getConsoleSender().sendMessage("§cEsta key não foi feita para este plugin.");
					} else {
						ativado = true;
						ligando.run();
						Bukkit.getConsoleSender().sendMessage("§aPlugin ativado com sucesso.");
						return;
					}

				} catch (Exception e) {
					Bukkit.getConsoleSender().sendMessage("§cFalha no sistema.");
					e.printStackTrace();
				}
				Bukkit.getPluginManager().disablePlugin(plugin);

			}
		}.runTaskAsynchronously(plugin);

	}

}
