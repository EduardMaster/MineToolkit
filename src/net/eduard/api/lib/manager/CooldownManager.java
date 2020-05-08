package net.eduard.api.lib.manager;

import java.util.HashMap;
import java.util.Map;

import net.eduard.api.lib.storage.Storable;
import org.bukkit.entity.Player;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.player.FakePlayer;

public class CooldownManager extends EffectManager  {
	private String msgOnCooldown;
	private String msgOverCooldown;
	private String msgStartCooldown;
	@Storable.StorageAttributes(reference = true)
	private Map<FakePlayer, TimeManager> cooldowns = new HashMap<>();

	public CooldownManager() {

	}

	public CooldownManager(int time) {
		setTime(time);
		msgOnCooldown = "§6Voce esta em Cooldown!";
		msgOverCooldown = "§6Voce saiu do Cooldown!";
		msgStartCooldown = "§6Voce usou a Habilidade!";
	}

	public String getOnCooldownMessage() {
		return msgOnCooldown;
	}

	public void setOnCooldownMessage(String onCooldownMessage) {
		this.msgOnCooldown = onCooldownMessage;
	}

	public String getStartCooldownMessage() {
		return msgStartCooldown;
	}

	public void setStartCooldownMessage(String startCooldownMessage) {
		this.msgStartCooldown = startCooldownMessage;
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
		FakePlayer fake = new FakePlayer(player);
		TimeManager cd = cooldowns.get(fake);
		cd.stopTask();
		cooldowns.remove(fake);
		return this;
	}

	public boolean onCooldown(Player player) {
		return getResult(player) > 0;
	}

	public CooldownManager sendOnCooldown(Player player) {
		if (msgOnCooldown != null)
			player.sendMessage(msgOnCooldown);
		return this;

	}

	public CooldownManager sendStartCooldown(Player player) {
		if (msgStartCooldown != null)
			player.sendMessage(msgStartCooldown);
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
		cd.asyncDelay();
		cooldowns.put(new FakePlayer(player), cd);
		return this;

	}

	public CooldownManager sendOverCooldown(Player player) {
		if (msgOnCooldown != null)
			player.sendMessage(msgOverCooldown);
		return this;
	}

	public long getResult(Player player) {
		FakePlayer fake = new FakePlayer(player);
		if (cooldowns.containsKey(fake)) {
			long now = Mine.getNow();
			TimeManager cd = cooldowns.get(fake);
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
		return msgOverCooldown;
	}

	public CooldownManager setOverCooldownMessage(String overCooldownMessage) {
		this.msgOverCooldown = overCooldownMessage;
		return this;
	}
}
