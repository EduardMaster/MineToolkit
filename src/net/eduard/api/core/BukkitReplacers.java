package net.eduard.api.core;

import org.bukkit.Statistic;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.modules.VaultAPI;

/**
 * Registrando os replacers mais usados na Displayboard
 * 
 * @author Eduard
 *
 */
public class BukkitReplacers {

	public BukkitReplacers() {

		if (Mine.hasPlugin("Vault")) {
			Mine.addReplacer("$player_group", p -> VaultAPI.getPermission().getPrimaryGroup(p));
			Mine.addReplacer("$player_prefix", p -> Mine.toChatMessage(VaultAPI.getChat().getPlayerPrefix(p)));
			Mine.addReplacer("$player_suffix", p -> Mine.toChatMessage(Mine.toChatMessage(VaultAPI.getChat().getPlayerPrefix(p))));
			Mine.addReplacer("$group_prefix", p -> Mine.toChatMessage(
					VaultAPI.getChat().getGroupPrefix("null", VaultAPI.getPermission().getPrimaryGroup(p))));
			Mine.addReplacer("$group_suffix", p -> Mine.toChatMessage(
					VaultAPI.getChat().getGroupSuffix("null", VaultAPI.getPermission().getPrimaryGroup(p))));
			Mine.addReplacer("$player_money", p -> {
				if (VaultAPI.hasVault() && VaultAPI.hasEconomy()) {

					return Extra.MONEY.format(VaultAPI.getEconomy().getBalance(p));

				}
				return "0.00";
			});
			Mine.addReplacer("$player_balance", p -> {
				if (VaultAPI.hasVault() && VaultAPI.hasEconomy()) {

					return Extra.formatMoney(VaultAPI.getEconomy().getBalance(p));

				}
				return "0.00";
			});
		}

		Mine.addReplacer("$players_online", p -> Mine.getPlayers().size());
		Mine.addReplacer("$player_world", p -> p.getWorld().getName());
		Mine.addReplacer("$player_displayname", Player::getDisplayName);
		Mine.addReplacer("$player_name", HumanEntity::getName);
		Mine.addReplacer("$player_health", Damageable::getHealth);
		Mine.addReplacer("$player_maxhealth", Damageable::getMaxHealth);
		Mine.addReplacer("$player_level", Player::getLevel);
		Mine.addReplacer("$player_xp", p -> Extra.MONEY.format(p.getTotalExperience()));
		Mine.addReplacer("$player_kills", p -> p.getStatistic(Statistic.PLAYER_KILLS));
		Mine.addReplacer("$player_deaths", p -> p.getStatistic(Statistic.DEATHS));
		Mine.addReplacer("$player_kdr", p -> {
			int kill = p.getStatistic(Statistic.PLAYER_KILLS);
			int death = p.getStatistic(Statistic.DEATHS);
			if (kill == 0)
				return 0;
			if (death == 0)
				return 0;
			return kill / death;
		});
		Mine.addReplacer("$player_kill/death", p -> {
			int kill = p.getStatistic(Statistic.PLAYER_KILLS);
			int death = p.getStatistic(Statistic.DEATHS);
			if (kill == 0)
				return 0;
			if (death == 0)
				return 0;
			return kill / death;
		});

		Mine.addReplacer("$player_x", p -> p.getLocation().getX());
		Mine.addReplacer("$player_y", p -> p.getLocation().getY());
		Mine.addReplacer("$player_z", p -> p.getLocation().getZ());

	}

	// Strings
	

	
}
