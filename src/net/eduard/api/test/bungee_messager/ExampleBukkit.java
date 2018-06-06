package net.eduard.api.test.bungee_messager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ExampleBukkit implements Listener {
	private BukkitMessager bukkitMessager;

	public ExampleBukkit(JavaPlugin plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		bukkitMessager = new BukkitMessager(plugin);
		bukkitMessager.enable();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		bukkitMessager.sendMessage(player, "cmd cash add "+player.getName()+" 5");
	}

	public BukkitMessager getBukkitMessager() {
		return bukkitMessager;
	}

	public void setBukkitMessager(BukkitMessager bukkitMessager) {
		this.bukkitMessager = bukkitMessager;
	}
}
