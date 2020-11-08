package net.eduard.api.lib.game;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * API de Cooldown para Habilidades e Kits
 * 
 * @author Eduard
 * @version 1.0
 * @since Lib v1.0
 *
 */
@SuppressWarnings("unused")
public abstract class Cooldowns {
	public static abstract class CooldownEvent extends BukkitRunnable {

		public CooldownEvent(long cooldownTime) {
			setStartTime(System.currentTimeMillis());
			setCooldownTime(cooldownTime);
		}

		private long cooldownTime;
		private long startTime;

		public long getStartTime() {
			return startTime;
		}

		public void setStartTime(long startTime) {
			this.startTime = startTime;
		}

		public long getCooldownTime() {
			return cooldownTime;
		}

		public void setCooldownTime(long cooldownTime) {
			this.cooldownTime = cooldownTime;
		}

	}

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
	private final Map<UUID, CooldownEvent> cooldowns = new HashMap<>();

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
		ticks = seconds * 20;
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
		CooldownEvent event = new CooldownEvent(ticks) {

			@Override
			public void run() {
				removeFromCooldown(player);
			}

		};
		event.runTaskLater(getPlugin(), ticks);
		cooldowns.put(player.getUniqueId(), event);
	}

	public int getCooldown(Player player) {
		if (onCooldown(player)) {
			CooldownEvent cooldown = cooldowns.get(player.getUniqueId());
			int result = (int) ((-cooldown.getStartTime() + System.currentTimeMillis()) / 1000);
			return (int) (cooldown.getCooldownTime() - result);
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