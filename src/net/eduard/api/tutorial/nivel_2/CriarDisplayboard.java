package net.eduard.api.tutorial.nivel_2;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.eduard.api.EduardAPI;
import net.eduard.api.setup.game.DisplayBoard;

public class CriarDisplayboard implements Listener {
	public static HashMap<Player, DisplayBoard> scores = new HashMap<>();

	@EventHandler
	public void event(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		DisplayBoard score = new DisplayBoard();
		scores.put(p, score);
		p.setScoreboard(score.getScore());
	}

	@EventHandler
	public void event(AsyncPlayerChatEvent e) {
		// Player p = e.getPlayer();
		e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
	}

	public static void ligar() {
		Bukkit.getPluginManager().registerEvents(new CriarDisplayboard(), EduardAPI.getInstance());
		new BukkitRunnable() {

			@Override
			public void run() {
				atualizar();
			}
		}.runTaskTimer(EduardAPI.getInstance(), 1, 1);

	}

	public static void atualizar() {
		for (Entry<Player, DisplayBoard> entry : scores.entrySet()) {
			Player p = entry.getKey();
			DisplayBoard score = entry.getValue();

			score.empty(15);
			score.set(14, "§aTestão " + p.getExhaustion());
			score.empty(13);
			score.set(12, "§cTestão " + p.getExhaustion());
			score.empty(11);
			score.set(10, "§bTestão " + p.getExhaustion());
			score.empty(9);

		}
	}

}
