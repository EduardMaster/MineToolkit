package net.eduard.api.lib.manager;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.eduard.api.lib.BukkitConfig;
import net.eduard.api.lib.Mine;

public class CooldownConfig extends BukkitConfig {

	private String tag = "Cooldowns.";

	public CooldownConfig(JavaPlugin plugin) {
		super("cooldowns.yml", plugin);

	}

	public void addCooldown(Player player, String cooldown, long timeToWait) {
		set(tag + getId(player) + cooldown + ".before", System.currentTimeMillis());
		set(tag + getId(player) + cooldown + ".cooldown", timeToWait);
	}

	private String getId(Player player) {
		return player.getUniqueId() + ".";
	}

	public boolean inCooldown(Player player, String cooldown) {
		if (getCooldown(player, cooldown) == -1)
			return false;
		return true;

	}

	public String getTime(Player player, String cooldown) {
		return Mine.formatTime(getCooldown(player, cooldown));
	}

	public long getCooldown(Player player, String cooldown) {

		if (contains(tag + getId(player) + cooldown)) {
			long before = getBefore(player, cooldown);
			long now = System.currentTimeMillis();
			long wait = getDelay(player, cooldown);
			long result = -now + (before + wait);
			// -14 + 10 + 5
			// antes = 10
			// cd = 5
			// agora = 20
			// - agora + cd + antes = resultado

			return result > 0 ? result : -1;

		}
		return -1;

	}

	public long getDelay(Player player, String cooldown) {
		return getLong(tag + getId(player) + cooldown + ".cooldown");
	}

	public long getBefore(Player player, String cooldown) {
		return getLong(tag + getId(player) + cooldown + ".before");
	}

	public void removeCooldown(Player player, String cooldown) {
		remove(tag + getId(player) + cooldown);
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
