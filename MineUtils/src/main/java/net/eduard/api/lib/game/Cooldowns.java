package net.eduard.api.lib.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * API de criação de Cooldown para Habilidades e Kits
 * <ul>
 *     <li>v1.1 Não usa mais CooldownEvent (Deletado) agora usa BukkitTask</li>
 * </ul>
 * @author Eduard
 * @version 1.1
 * @since EduardAPI v0.2
 *
 */
@SuppressWarnings("unused")
public abstract class Cooldowns {


	/**
	 * Tempo de Cooldown<br>
	 * Em forma de Ticks<br>
	 * Cada TICK = 1/20 de SEGUNDO
	 */
	private long ticks;

	/**
	 * Mapa contendo os Cooldowns em Andamento<br>
	 * KEY = UUID = Id do Jogador<br>
	 * VALUE = CooldownEvent = Evento do Cooldown<br>
	 */
	private final Map<UUID, BukkitTask> cooldowns = new HashMap<>();
	private final Map<UUID, Long> cooldownsStart = new HashMap<>();
	/**
	 * Metodo abstrato para oque vai acontecer quando sair do Cooldown
	 * 
	 * @param player
	 *            Jogador
	 */
	public abstract void onLeftCooldown(Player player);

	/**
	 * Metodo abstrato para oque vai acontecer quando come§ar o Cooldown
	 * 
	 * @param player
	 *            Jogador
	 */
	public abstract void onStartCooldown(Player player);

	/**
	 * Metodo abstrato para oque vai acontecer quando estiver ainda em Coodlwon
	 * 
	 * @param player Jogador
	 */
	public abstract void onInCooldown(Player player);

	/**
	 * Iniciando o Sistema de Cooldown
	 * 
	 * @param seconds
	 *            Segundos de Cooldown
	 */
	public Cooldowns(int seconds) {
		setTime(seconds);
	}

	/**
	 * Define o Tempo de Cooldown
	 * 
	 * @param seconds
	 *            Segundos
	 */
	public void setTime(int seconds) {
		ticks = seconds * 20L;
	}

	/**
	 * 
	 * @return Tempo de Cooldown em Ticks
	 */
	public long getTicks() {
		return ticks;
	}

	public void setTicks(long ticks) {
		this.ticks = ticks;
	}

	public void setOnCooldown(Player player) {
		removeFromCooldown(player);
		onStartCooldown(player);
		BukkitTask task = Bukkit.getScheduler().runTaskLaterAsynchronously(getPlugin(), () -> removeFromCooldown(player), ticks);
		cooldowns.put(player.getUniqueId(), task);
		cooldownsStart.put(player.getUniqueId() , System.currentTimeMillis());
	}

	public int getCooldown(Player player) {
		if (onCooldown(player)) {
			BukkitTask cooldown = cooldowns.get(player.getUniqueId());
			long start = cooldownsStart.get(player.getUniqueId());
			int result = (int) ((-start + System.currentTimeMillis()) / 1000);
			return (int) (start - result);
		}
		return -1;
	}

	public JavaPlugin getPlugin() {
		return JavaPlugin.getProvidingPlugin(getClass());
	}

	public boolean onCooldown(Player player) {
		return cooldowns.containsKey(player.getUniqueId());
	}

	public void removeFromCooldown(Player player) {
		if (onCooldown(player)) {
			onLeftCooldown(player);
			cooldowns.get(player.getUniqueId()).cancel();
			cooldowns.remove(player.getUniqueId());
		}
	}

	public boolean cooldown(Player player) {
		if (onCooldown(player)) {
			onInCooldown(player);
			return false;
		}
		setOnCooldown(player);
		return true;

	}
}