package net.eduard.api.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.eduard.api.EduardAPI;
import net.eduard.api.lib.Extra;
import net.eduard.api.lib.FakePlayer;
import net.eduard.api.lib.Scoreboards;
import net.eduard.api.lib.Extra.KeyType;
import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.game.DisplayBoard;
import net.eduard.api.lib.storage.manager.TimeManager;

public class Testes {

	public static class ExplicandoHashMap {

		public static void main(String[] args) {
			int meuDinheiro = 100;
			int seuDinheiro = 200;
			// NOME "KEY"
			// VALOR "VALUE"

			System.out.println("Meu dinheiro é " + meuDinheiro);
			System.out.println("Seu dinheiro é " + seuDinheiro);
			HashMap<String, Integer> contas = new HashMap<>();
			HashMap<String, String> amigoSecreto = new HashMap<>();

			amigoSecreto.put("Eduard", "Gabriel");
			amigoSecreto.put("Gabriel", "Beta");
			amigoSecreto.put("Beta", "Eduard");

			String amigoSecredoDoBeta = amigoSecreto.get("Beta");
			System.out.println(amigoSecredoDoBeta);

			contas.put("Eduard", 1000);
			contas.put("Gabriel", 2000);
			contas.put("Beta", 1);
			System.out.println("O dinheiro do Eduard é " + contas.get("Eduard"));

		}

	}

	public void teste4() {
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(322, 1, (short) 1);
		item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
		item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("teste");
		meta.setLore(Arrays.asList("affs", "tensolandia"));
		item.setItemMeta(meta);
//		System.out.println(Mine.saveItem(item));
//		System.out.println(Mine.reloadItem(Mine.saveItem(item)));

	}

	public void test() {
//		World world = Bukkit.getWorld("world");
//		Location high = new Location(world, 0, 0, 0);
//		Location low = new Location(world, 100, 100, 100);
//		Schematic schematic = new Schematic();
//		schematic.copy(low, high);
//		File file = new File(EduardAPI.getInstance().getDataFolder(), "teste.txt");
//		schematic.save(file);

//		Schematic schema = new Schematic();
//		schema.reload(file);

	}

	public static void simpleClans() {
		// ExtraMine.addReplacer("$clan_label", new Replacer() {
		//
		// @Override
		// public Object getText(Player p) {
		//
		// ClanPlayer clan = SimpleClans.getInstance().getClanManager()
		// .getClanPlayer(p);
		// if (clan == null) {
		// return "";
		// }
		// if (clan.getClan() == null) {
		// return "";
		// }
		//
		// return clan.getClan().getTagLabel();
		// }
		// });
		// ExtraMine.addReplacer("$clan_name", new Replacer() {
		//
		// @Override
		// public Object getText(Player p) {
		//
		// ClanPlayer clan = SimpleClans.getInstance().getClanManager()
		// .getClanPlayer(p);
		// if (clan == null) {
		// return "";
		// }
		// if (clan.getClan() == null) {
		// return "";
		// }
		//
		// return clan.getClan().getName();
		// }
		// });
		// ExtraMine.addReplacer("$clan_tag", new Replacer() {
		//
		// @Override
		// public Object getText(Player p) {
		//
		// ClanPlayer clan = SimpleClans.getInstance().getClanManager()
		// .getClanPlayer(p);
		// if (clan == null) {
		// return "";
		// }
		// if (clan.getClan() == null) {
		// return "";
		// }
		//
		// return clan.getClan().getTag();
		// }
		// });
		// ExtraMine.addReplacer("$clan_color", new Replacer() {
		//
		// @Override
		// public Object getText(Player p) {
		//
		// ClanPlayer clan = SimpleClans.getInstance().getClanManager()
		// .getClanPlayer(p);
		// if (clan == null) {
		// return "";
		// }
		// if (clan.getClan() == null) {
		// return "";
		// }
		//
		// return clan.getClan().getColorTag();
		// }
		// });
		// ExtraMine.addReplacer("$clan_name", new Replacer() {
		//
		// @Override
		// public Object getText(Player p) {
		//
		// ClanPlayer clan = SimpleClans.getInstance().getClanManager()
		// .getClanPlayer(p);
		// if (clan == null) {
		// return "";
		// }
		// if (clan.getClan() == null) {
		// return "";
		// }
		//
		// return clan.getClan().getName();
		// }
		// });
	}

	public static void hardFacs() {
		// if (Mine.hasPlugin("HardFacs")) {
		// ExtraMine.addReplacer("$fac_money", new Replacer() {
		//
		// @Override
		// public Object getText(Player p) {
		// DecimalFormat decimal = new DecimalFormat("#,##0.00");
		// return decimal.format(FPlayers.i.get(p).getFaction().money);
		// }
		// });
		// ExtraMine.addReplacer("$fac_p_chat", new Replacer() {
		//
		// @Override
		// public Object getText(Player p) {
		// return FPlayers.i.get(p).getChatTag();
		// }
		// });
		// ExtraMine.addReplacer("$fac_zone", new Replacer() {
		//
		// @Override
		// public Object getText(Player p) {
		// FPlayer player = FPlayers.i.get(p);
		// Faction zone = Board.getFactionAt(player.getLastStoodAt());
		//
		// return zone.getColorTo(player) + zone.getTag();
		// // return Board.getTerritoryAccessAt(new FLocation(fp)).
		// }
		// });
		// ExtraMine.addReplacer("$fac_p_power", new Replacer() {
		//
		// @Override
		// public Object getText(Player p) {
		//
		// return FPlayers.i.get(p).getPowerRounded();
		// }
		// });
		// ExtraMine.addReplacer("$fac_p_maxpower", new Replacer() {
		//
		// @Override
		// public Object getText(Player p) {
		//
		// return FPlayers.i.get(p).getPowerMaxRounded();
		// }
		// });
		// ExtraMine.addReplacer("$fac_power", new Replacer() {
		//
		// @Override
		// public Object getText(Player p) {
		//
		// return FPlayers.i.get(p).getFaction().getPowerRounded();
		// }
		// });
		// ExtraMine.addReplacer("$fac_maxpower", new Replacer() {
		//
		// @Override
		// public Object getText(Player p) {
		//
		// return FPlayers.i.get(p).getFaction().getPowerMaxRounded();
		// }
		// });
		//
		// ExtraMine.addReplacer("$fac_tag", new Replacer() {
		//
		// @Override
		// public Object getText(Player p) {
		// Faction f = FPlayers.i.get(p).getFaction();
		// if (f != null & !f.getComparisonTag().equals("ZonaLivre"))
		// return f.getTag();
		// return "§7Sem Facção";
		// }
		// });
		// ExtraMine.addReplacer("$fac_comptag", new Replacer() {
		//
		// @Override
		// public Object getText(Player p) {
		//
		// return FPlayers.i.get(p).getFaction().getComparisonTag();
		// }
		// });
		// ExtraMine.addReplacer("$fac_desc", new Replacer() {
		//
		// @Override
		// public Object getText(Player p) {
		// Faction f = FPlayers.i.get(p).getFaction();
		// if (f != null)
		// return f.getDescription();
		// return "";
		// }
		// });
		// ExtraMine.addReplacer("$fac_online", new Replacer() {
		//
		// @Override
		// public Object getText(Player p) {
		//
		// return FPlayers.i.get(p).getFaction()
		// .getFPlayersWhereOnline(true).size();
		// }
		// });
		// ExtraMine.addReplacer("$fac_players", new Replacer() {
		//
		// @Override
		// public Object getText(Player p) {
		//
		// return FPlayers.i.get(p).getFaction().getFPlayers().size();
		// }
		// });
		// ExtraMine.addReplacer("$fac_claims", new Replacer() {
		//
		// @Override
		// public Object getText(Player p) {
		//
		// return FPlayers.i.get(p).getFaction().getClaims().size();
		// }
		// });

	}

	/**
	 * oque desconbri nos teste é que a causa do flicker na scoreboard a o metodo
	 * scoerboard.resetScore(fake) que remove o antigo fakeofflineplayer para ser
	 * colocado o outro
	 * 
	 * @author Eduard-PC
	 *
	 */
	@SuppressWarnings("deprecation")
	public static class ScoreboardTestes {
		public static Map<Player, Scoreboard> scores = new HashMap<>();

		public static Map<Player, DisplayBoard> boards = new HashMap<>();

		public static void teste10() {
			new BukkitRunnable() {

				@Override
				public void run() {
					for (Player p : Bukkit.getOnlinePlayers()) {
						if (!boards.containsKey(p)) {
							DisplayBoard board = new DisplayBoard("Teste");
							boards.put(p, board);
							board.apply(p);
						}
						DisplayBoard board = boards.get(p);

						// board.setLine("", "§a", "", 15);
						board.setLine("", "§aVida: ", "" + p.getHealth(), 14);
						// board.setLine("", "§aVida Maxima: ", ""+p.getMaxHealth(), 13);
						board.setLine("", "§aExaultao ", "" + p.getExhaustion(), 12);
						// board.setLine("", "§b", "", 11);
						// for (int i = 15; i > 0; i--) {
						// board.setLine( Extra.newKey(KeyType.LETTER, 5)+" ",
						// Extra.newKey(KeyType.LETTER, 5), " "+ Extra.newKey(KeyType.LETTER, 5), i);
						// }
						board.removeEntries();
					}

				}
			}.runTaskTimer(EduardAPI.getInstance(), 20, 20);
		}

		public static HashMap<Player, Scoreboards> scoreboards = new HashMap<>();

		public static void teste0(TimeManager time) {
			time.timer(1, () -> {
				for (Player p : Mine.getPlayers()) {
					if (!scoreboards.containsKey(p)) {
						Scoreboards board = new Scoreboards("Teste");
						scoreboards.put(p, board);
						p.setScoreboard(board.getScoreboard());
					}
					Scoreboards board = scoreboards.get(p);
					for (int i = 15; i > 0; i--) {
						board.setLine(Extra.newKey(KeyType.LETTER, 5) + " ", Extra.newKey(KeyType.LETTER, 5),
								" " + Extra.newKey(KeyType.LETTER, 5), i);
					}

				}

			});
		}

		public static void teste1(TimeManager time) {
			time.timer(1, () -> {

				for (Player p : Mine.getPlayers()) {
					Scoreboard sc = null;
					Objective obj = null;
					if (!scores.containsKey(p)) {
						sc = Bukkit.getScoreboardManager().getNewScoreboard();
						obj = sc.registerNewObjective("objetivo", "dummy");
						obj.setDisplayName("§aTitulo");
						obj.setDisplaySlot(DisplaySlot.SIDEBAR);
						p.setScoreboard(sc);
						scores.put(p, sc);
						for (int i = 15; i > 0; i--) {
							Team team = sc.registerNewTeam("time" + i);
							FakePlayer fake = new FakePlayer(" " + Mine.randomInt(1000, 99999) + " ");
							team.addPlayer(fake);
							team.setSuffix(" " + Mine.randomInt(1000, 99999) + " ");
							team.setPrefix(" " + Mine.randomInt(1000, 99999) + " ");
							obj.getScore(fake).setScore(i);
						}
					} else {
						sc = scores.get(p);
						obj = sc.getObjective("objetivo");
					}
					for (Team team : sc.getTeams()) {
						Integer teamId = Mine.toInt(team.getName().replace("time", ""));
						OfflinePlayer fake = team.getPlayers().iterator().next();
						obj.getScore(fake).setScore(-1);
						Score score = obj.getScore(fake);

						if (score.getScore() == -1) {
							sc.resetScores(fake);
						}
						team.removePlayer(fake);

						fake = new FakePlayer(" " + Mine.randomInt(1000, 99999) + " ");
						obj.getScore(fake).setScore(teamId);
						team.addPlayer(fake);
						team.setSuffix(" " + Mine.randomInt(1000, 99999) + " ");
						team.setPrefix(" " + Mine.randomInt(1000, 99999) + " ");
						System.out.println("team id " + teamId);
					}
					for (OfflinePlayer fake : sc.getPlayers()) {
						Score score = obj.getScore(fake);
						if (score.getScore() == -1) {
							sc.resetScores(fake);
						}
					}
					Mine.console("§e" + sc.getPlayers().size());
					// obj.setDisplayName(Extra.newKey(KeyType.LETTER, 32));
				}
				// Mine.broadcast("Tamanho "+ Bukkit.getOfflinePlayers().length);
			});

		}

		public static void teste2(TimeManager time) {
			time.timer(1, () -> {

				for (Player p : Mine.getPlayers()) {
					Scoreboard sc = null;
					Objective obj = null;
					if (!scores.containsKey(p)) {
						sc = Bukkit.getScoreboardManager().getNewScoreboard();
						p.setScoreboard(sc);
						scores.put(p, sc);
					} else {
						sc = scores.get(p);
						obj = sc.getObjective("objetivo");
						if (obj != null) {
							for (OfflinePlayer entrada : sc.getPlayers()) {
								sc.resetScores(entrada);
							}
							obj.unregister();
						}
					}

					obj = sc.registerNewObjective("objetivo", "dummy");
					obj.setDisplayName("§aTitulo");
					obj.setDisplaySlot(DisplaySlot.SIDEBAR);
					Mine.console("§c" + sc.getPlayers().size());
					for (int i = 1; i <= 15; i++) {
						Team team = sc.getTeam("time" + i);
						if (team != null) {
							Set<OfflinePlayer> jogadores = team.getPlayers();
							for (OfflinePlayer jogador : jogadores) {
								team.removePlayer(jogador);
							}
							team.unregister();
						}
						team = sc.registerNewTeam("time" + i);

						FakePlayer fake = new FakePlayer(" " + Mine.randomInt(1000, 99999) + " ");
						team.addPlayer(fake);
						team.setSuffix(" " + Mine.randomInt(1000, 99999) + " ");
						team.setPrefix(" " + Mine.randomInt(1000, 99999) + " ");
						obj.getScore(fake).setScore(i);
					}
					Mine.console("§e" + sc.getPlayers().size());
					// obj.setDisplayName(Extra.newKey(KeyType.LETTER, 32));
				}
				// Mine.broadcast("Tamanho "+ Bukkit.getOfflinePlayers().length);
			});

		}

		public static void teste3(TimeManager time) {
			time.timer(1, () -> {

				for (Player p : Mine.getPlayers()) {
					Scoreboard sc = null;
					Objective obj = null;
					if (!scores.containsKey(p)) {

						sc = Bukkit.getScoreboardManager().getNewScoreboard();
						obj = sc.registerNewObjective("objetivo", "dummy");
						obj.setDisplayName("§aTitulo");
						obj.setDisplaySlot(DisplaySlot.SIDEBAR);
						for (int i = 1; i <= 15; i++) {
							Team team = sc.registerNewTeam("time" + i);
							FakePlayer fake = new FakePlayer(ChatColor.values()[i - 1] + "§r");
							team.addPlayer(fake);
							team.setSuffix("1");

							obj.getScore(fake).setScore(i);
						}
						p.setScoreboard(sc);
						scores.put(p, sc);
					} else {
						sc = scores.get(p);
						obj = sc.getObjective("objetivo");
					}
					// obj.setDisplayName(Extra.newKey(KeyType.LETTER, 32));
					for (int i = 1; i <= 15; i++) {
						Team team = sc.getTeam("time" + i);
						Integer numero = Mine.toInt(team.getSuffix());
						numero++;
						team.setSuffix("" + numero);
						numero = Mine.toInt(team.getPrefix());
						numero--;
						team.setPrefix("" + numero);
						// team.setSuffix(Extra.newKey(KeyType.LETTER, 16));
						// team.setPrefix(Extra.newKey(KeyType.LETTER, 16));
						// Team team = sc.getTeam("time"+teamId);
						// team.removePlayer(entrada);
						// sc.resetScores(entrada);
						// if (entrada instanceof FakePlayer) {
						// Mine.broadcast("§cTeste");
						// }
						// Score score = obj.getScore(entrada);
						//
						// score.setScore(score.getScore()+1);

					}
					// for (int i = 15; i > 0; i--) {
					// // team =
					// // "§aA"+Mine.randomInt(100000, 9999999);
					// // OfflinePlayer offline =
					// Bukkit.getOfflinePlayer(Extra.newKey(KeyType.LETTER,
					// // 16));
					// Team team = sc.getTeam("time"+i);
					// FakePlayer fake = new FakePlayer(Extra.newKey(KeyType.LETTER,
					// 40));
					// team.addPlayer(fake);
					// obj.getScore(fake).setScore(i);
					// ;
					// }
					// Mine.console("§cTanto " + sc.getPlayers().size());
				}
				// Mine.broadcast("Tamanho "+ Bukkit.getOfflinePlayers().length);
			});

		}

		public static void teste4(TimeManager time) {
			time.timer(1, () -> {

				for (Player p : Mine.getPlayers()) {
					Scoreboard sc = null;
					Objective obj = null;
					if (!scores.containsKey(p)) {

						sc = Bukkit.getScoreboardManager().getNewScoreboard();
						obj = sc.registerNewObjective("objetivo", "dummy");
						obj.setDisplayName("§aTitulo");
						obj.setDisplaySlot(DisplaySlot.SIDEBAR);
						for (int i = 1; i <= 15; i++) {
							Team team = sc.registerNewTeam("time" + i);
							FakePlayer fake = new FakePlayer("linha" + i);
							team.addPlayer(fake);
							obj.getScore(fake).setScore(i);
						}
						p.setScoreboard(sc);
						scores.put(p, sc);
					} else {
						sc = scores.get(p);
						obj = sc.getObjective("objetivo");
					}
					// obj.setDisplayName(Extra.newKey(KeyType.LETTER, 32));
					for (int i = 1; i <= 15; i++) {
						Team team = sc.getTeam("time" + i);
						team.setSuffix(Extra.newKey(KeyType.LETTER, 16));
						team.setPrefix(Extra.newKey(KeyType.LETTER, 16));
					}
					int teamId = 15;
					System.out.println(sc.getEntries().size());
					for (OfflinePlayer entrada : sc.getPlayers()) {
						Team team = sc.getTeam("time" + teamId);
						team.removePlayer(entrada);
						sc.resetScores(entrada);

						//
						teamId--;
					}
					for (int i = 15; i > 0; i--) {
						// team =
						// "§aA"+Mine.randomInt(100000, 9999999);
						// OfflinePlayer offline = Bukkit.getOfflinePlayer(Extra.newKey(KeyType.LETTER,
						// 16));
						Team team = sc.getTeam("time" + i);
						FakePlayer fake = new FakePlayer(Extra.newKey(KeyType.LETTER, 16));
						team.addPlayer(fake);
						obj.getScore(fake).setScore(i);
						;
					}
					// Mine.console("§cTanto " + sc.getPlayers().size());
				}
				// Mine.broadcast("Tamanho "+ Bukkit.getOfflinePlayers().length);
			});

		}

		public static void teste5(TimeManager time) {

			time.timer(1, () -> {

				for (Player p : Mine.getPlayers()) {
					Scoreboard sc = null;
					Objective obj = null;
					if (!scores.containsKey(p)) {

						sc = Bukkit.getScoreboardManager().getNewScoreboard();
						obj = sc.registerNewObjective("objetivo", "dummy");
						obj.setDisplayName("§aTitulo");
						obj.setDisplaySlot(DisplaySlot.SIDEBAR);
						for (int i = 1; i <= 15; i++) {
							sc.registerNewTeam("time" + i);
						}
						p.setScoreboard(sc);
						scores.put(p, sc);
					} else {
						sc = scores.get(p);
						obj = sc.getObjective("objetivo");
					}
					obj.setDisplayName(Extra.newKey(KeyType.LETTER, 32));
					for (int i = 1; i <= 15; i++) {
						Team team = sc.getTeam("time" + i);
						team.setSuffix(Extra.newKey(KeyType.LETTER, 16));
						team.setPrefix(Extra.newKey(KeyType.LETTER, 16));
					}
					int teamId = 15;
					for (OfflinePlayer entrada : sc.getPlayers()) {
						Team team = sc.getTeam("time" + teamId);
						team.removePlayer(entrada);
						sc.resetScores(entrada);
						teamId--;
					}
					for (int i = 15; i > 0; i--) {
						// team =
						// "§aA"+Mine.randomInt(100000, 9999999);
						// OfflinePlayer offline = Bukkit.getOfflinePlayer(Extra.newKey(KeyType.LETTER,
						// 16));
						Team team = sc.getTeam("time" + i);
						FakePlayer fake = new FakePlayer(Extra.newKey(KeyType.LETTER, 40));
						team.addPlayer(fake);
						obj.getScore(fake).setScore(i);
						;
					}
					Mine.console("§cTanto " + sc.getPlayers().size());
				}
				// Mine.broadcast("Tamanho "+ Bukkit.getOfflinePlayers().length);
			});

			//// p.sendMessage("§cAtualizando");
			// if (scores.containsKey(p)) {
			// Scoreboard sc = scores.get(p);
			// Team team = sc.getTeam("linha1");
			// String texto = " "+Mine.randomInt(1000, 9999999);
			// StringBuilder str = new StringBuilder(texto);
			// while(str.length()<16) {
			// str.append(" ");
			// }
			// team.setSuffix(str.toString());
			// }else {
			// Scoreboard sc = Bukkit.getScoreboardManager().getNewScoreboard();
			// Objective obj = sc.registerNewObjective("objetivo", "dummy");
			// Team team = sc.registerNewTeam("linha1");
			// String texto = " "+Mine.randomInt(1000, 9999999);
			// StringBuilder str = new StringBuilder(texto);
			// while(str.length()<16) {
			// str.append(" ");
			// }
			// team.setSuffix(str.toString());
			//// obj.setDisplayName("§aTitulo "+ Mine.randomInt(1000, 9999999));
			// obj.setDisplayName("§aTitulo");
			// obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			//
			// obj.getScore(new FakePlayer("Num:" )).setScore(10);
			// team.addPlayer(new FakePlayer("Num:" ));
			// p.setScoreboard(sc);
			// scores.put(p, sc);
			// }

		}
	}

}
