package net.eduard.api.manager;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.modules.Replacer;
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
			Mine.addReplacer("$player_group", new Replacer() {

				@Override
				public Object getText(Player p) {
					return VaultAPI.getPermission().getPrimaryGroup(p);
				}
			});
			Mine.addReplacer("$player_prefix", new Replacer() {

				@Override
				public Object getText(Player p) {
					return Mine.toChatMessage(VaultAPI.getChat().getPlayerPrefix(p));
				}
			});
			Mine.addReplacer("$player_suffix", new Replacer() {

				@Override
				public Object getText(Player p) {
					return Mine.toChatMessage(Mine.toChatMessage(VaultAPI.getChat().getPlayerPrefix(p)));
				}
			});
			Mine.addReplacer("$group_prefix", new Replacer() {

				@Override
				public Object getText(Player p) {
					return Mine.toChatMessage(
							VaultAPI.getChat().getGroupPrefix("null", VaultAPI.getPermission().getPrimaryGroup(p)));
				}
			});
			Mine.addReplacer("$group_suffix", new Replacer() {

				@Override
				public Object getText(Player p) {
					return Mine.toChatMessage(
							VaultAPI.getChat().getGroupSuffix("null", VaultAPI.getPermission().getPrimaryGroup(p)));
				}
			});
			Mine.addReplacer("$player_money", new Replacer() {

				@Override
				public Object getText(Player p) {
					if (VaultAPI.hasVault() && VaultAPI.hasEconomy()) {

						return Extra.MONEY.format(VaultAPI.getEconomy().getBalance(p));

					}
					return "0.00";
				}
			});
			Mine.addReplacer("$player_balance", new Replacer() {

				@Override
				public Object getText(Player p) {
					if (VaultAPI.hasVault() && VaultAPI.hasEconomy()) {

						return Extra.formatMoney(VaultAPI.getEconomy().getBalance(p));

					}
					return "0.00";
				}
			});
			Mine.addReplacer("$player_op_balance", new Replacer() {

				@Override
				public Object getText(Player p) {
					if (VaultAPI.hasVault() && VaultAPI.hasEconomy()) {

						return format(VaultAPI.getEconomy().getBalance(p), 0);

					}
					return "0.00";
				}
			});

		}

		Mine.addReplacer("$players_online", new Replacer() {

			@Override
			public Object getText(Player p) {
				return Mine.getPlayers().size();
			}
		});
		Mine.addReplacer("$player_world", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getWorld().getName();
			}
		});
		Mine.addReplacer("$player_displayname", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getDisplayName();
			}
		});
		Mine.addReplacer("$player_name", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getName();
			}
		});
		Mine.addReplacer("$player_health", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getHealth();
			}
		});
		Mine.addReplacer("$player_maxhealth", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getMaxHealth();
			}
		});
		Mine.addReplacer("$player_level", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getLevel();
			}
		});
		Mine.addReplacer("$player_xp", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getTotalExperience();
			}
		});
		Mine.addReplacer("$player_kills", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getStatistic(Statistic.PLAYER_KILLS);
			}
		});
		Mine.addReplacer("$player_deaths", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getStatistic(Statistic.DEATHS);
			}
		});
		Mine.addReplacer("$player_kdr", new Replacer() {

			@Override
			public Object getText(Player p) {
				int kill = p.getStatistic(Statistic.PLAYER_KILLS);
				int death = p.getStatistic(Statistic.DEATHS);
				if (kill == 0)
					return 0;
				if (death == 0)
					return 0;
				return kill / death;
			}
		});
		Mine.addReplacer("$player_kill/death", new Replacer() {

			@Override
			public Object getText(Player p) {
				int kill = p.getStatistic(Statistic.PLAYER_KILLS);
				int death = p.getStatistic(Statistic.DEATHS);
				if (kill == 0)
					return 0;
				if (death == 0)
					return 0;
				return kill / death;
			}
		});

		Mine.addReplacer("$player_x", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getLocation().getX();
			}
		});
		Mine.addReplacer("$player_y", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getLocation().getY();
			}
		});
		Mine.addReplacer("$player_z", new Replacer() {

			@Override
			public Object getText(Player p) {
				return p.getLocation().getZ();
			}
		});

	}

	// Strings
	private static String[] c = new String[] { "K", "M", "B", "Q", "QQ", "S", "SS", "O", "N", "D" };

	/**
	 * No dia 28/07/2019 o ViniOtaku#0666 passou este Metodo de formatacao
	 * @param numero
	 * @param iteration
	 * @return Numero Formatado
	 */
	// Format
	public static String format(double numero, int iteration) {
		double d = ((long) numero / 100) / 10.0;
		boolean isRound = (d * 10) % 10 == 0;

		return (d < 1000 ? ((d > 99.9 || isRound || (!isRound && d > 9.99) ? (int) d : d + "") + " " + c[iteration])
				: format(d, iteration + 1));
	}
}
