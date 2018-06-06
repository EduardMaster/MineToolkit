package net.eduard.api.lib.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.eduard.api.lib.core.Mine;

public class CooldownManager extends EffectManager {

	private Map<UUID, TimeManager> playersInCooldown = new HashMap<>();
	private String onCooldownMessage = "§6Voce esta em Cooldown!";
	private String overCooldownMessage = "§6Voce saiu do Cooldown!";
	private String startCooldownMessage = "§6Voce usou a Habilidade!";

	public CooldownManager() {
	}

	public String getOnCooldownMessage() {
		return onCooldownMessage;
	}

	public void setOnCooldownMessage(String onCooldownMessage) {
		this.onCooldownMessage = onCooldownMessage;
	}

	public String getStartCooldownMessage() {
		return startCooldownMessage;
	}

	public void setStartCooldownMessage(String startCooldownMessage) {
		this.startCooldownMessage = startCooldownMessage;
	}

	public boolean cooldown(Player player) {
		if (onCooldown(player)) {
			sendOnCooldown(player);
			return false;
		}
		setOnCooldown(player);
		sendStartCooldown(player);
		return true;
	}

	public CooldownManager stopCooldown(Player player) {
		UUID id = player.getUniqueId();
		playersInCooldown.get(id).getTask().cancel();
		playersInCooldown.remove(id);
		return this;
	}

	public boolean onCooldown(Player player) {
		return getResult(player) > 0;
	}

	public CooldownManager sendOnCooldown(Player player) {
		player.sendMessage(onCooldownMessage);
		return this;

	}

	public CooldownManager sendStartCooldown(Player player) {
		player.sendMessage(startCooldownMessage);
		return this;
	}

	public CooldownManager setOnCooldown(Player player) {
		if (onCooldown(player)) {
			stopCooldown(player);
		}

		TimeManager cd = new TimeManager() {
			@Override
			public void run() {
				sendOverCooldown(player);
			}
		};
		cd.setTime(getTime());
		cd.delay(getPlugin());
		playersInCooldown.put(player.getUniqueId(), cd);
		return this;

	}

	public CooldownManager sendOverCooldown(Player player) {
		player.sendMessage(overCooldownMessage);
		return this;
	}

	public long getResult(Player player) {
		if (playersInCooldown.containsKey(player.getUniqueId())) {
			long now = Mine.getNow();
			TimeManager cd = playersInCooldown.get(player.getUniqueId());
			Long before = cd.getStartTime();
			long cooldown = cd.getTime() * 50;
			long calc = before + cooldown;
			long result = calc - now;
			if (result <= 0) {
				return 0;
			}
			return result / 50;
		}
		return 0;
	}

	public int getCooldown(Player player) {

		return (int) ((getResult(player) / 20));
	}

	public String getOverCooldownMessage() {
		return overCooldownMessage;
	}

	public CooldownManager setOverCooldownMessage(String overCooldownMessage) {
		this.overCooldownMessage = overCooldownMessage;
		return this;
	}
}
