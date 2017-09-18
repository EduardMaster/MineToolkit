package net.eduard.api.tutorial;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CriarMensagems {
public static String mensagem = "§6Que mensagem foda!";
// A cada 10 segundos | 20 ticks vezes 10 = 20 segundos
public static int tempoQueRepete = 20*10;

public CriarMensagems(JavaPlugin plugin) {
	// Nosso repetidor
	new BukkitRunnable() {
		
		@Override
		public void run() {
			Bukkit.broadcastMessage(mensagem);
		}
	}.runTaskTimer(plugin, tempoQueRepete, tempoQueRepete);
}
}
