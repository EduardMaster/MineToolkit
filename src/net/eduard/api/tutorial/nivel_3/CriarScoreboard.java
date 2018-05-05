package net.eduard.api.tutorial.nivel_3;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.eduard.api.EduardAPI;
import net.eduard.api.setup.Mine;
import net.eduard.api.setup.lib.FakePlayer;


public class CriarScoreboard {

	@SuppressWarnings({ "deprecation" })
	public static void build(Player p) {

		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = sb.registerNewObjective("score", "dummy");
		obj.setDisplayName("§6§lFACTION  ");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		FakePlayer line = new FakePlayer("   ");
		FakePlayer line0 = new FakePlayer("§0");
		FakePlayer line1 = new FakePlayer(" §fAlmas: §8✟§7");
		FakePlayer line2 = new FakePlayer(" §fCoins: §7");
		FakePlayer line3 = new FakePlayer("§1");
		FakePlayer line4 = new FakePlayer(" §fFacção: ");
		FakePlayer line8 = new FakePlayer("§a");
		FakePlayer line9 = new FakePlayer(" §fCash: §7");
		FakePlayer line11 = new FakePlayer("§9");

		FakePlayer line12 = new FakePlayer("  §7www.smashmc.com");

		Team l = sb.registerNewTeam("line");
		Team l1 = sb.registerNewTeam("line1");
		Team l4 = sb.registerNewTeam("line4");
		Team l2 = sb.registerNewTeam("line2");
		Team l9 = sb.registerNewTeam("line9");

		l.setSuffix("§7...");
		l1.setSuffix("§7Loading...");
		l4.setSuffix(Mine.cutText((""+p.getExhaustion()),16));
		l2.setSuffix("§7Loading...");
		l9.setSuffix("§7Loading...");

		l.addPlayer(line);
		l1.addPlayer(line1);
		l4.addPlayer(line4);
		l2.addPlayer(line2);
		l9.addPlayer(line9);

		obj.getScore(line.getName()).setScore(13);
		obj.getScore(line0.getName()).setScore(12);
		obj.getScore(line1.getName()).setScore(11);
		obj.getScore(line2.getName()).setScore(10);
		obj.getScore(line3.getName()).setScore(9);
		obj.getScore(line4.getName()).setScore(8);
		obj.getScore(line8.getName()).setScore(4);
		obj.getScore(line9.getName()).setScore(3);
		obj.getScore(line11.getName()).setScore(1);
		obj.getScore(line12.getName()).setScore(0);

		p.setScoreboard(sb);

	}

	public static void update(final Player p) {
//		Thread th = new Thread(new Runnable() {
//
//			@Override
//			public void run() {

				if (p.getScoreboard() != null) {
					if (p.getScoreboard().getObjective("score") != null) {
						Scoreboard sb = p.getScoreboard();
						Team l = sb.getTeam("line");
						Team l1 = sb.getTeam("line1");
						Team l4 = sb.getTeam("line4");
						Team l2 = sb.getTeam("line2");
						Team l9 = sb.getTeam("line9");

						l.setPrefix(Mine.cutText((""+p.getExhaustion()),16));
						l1.setSuffix("§7Loading...");
						l4.setSuffix(Mine.cutText((""+p.getExhaustion()),16));
						l2.setSuffix("§7Loading...");
						l9.setSuffix("§7Loading...");
//						FactionsMembro membro = me.smashfactions.Main.getPlugin().getManager().getMembro(p);
//						int cash = Cash.get(p.getName());
//						int coins = CoinsManager.get(p.getName());
//						int almas = Almas.get(p.getName());
//
//						FactionsClaim terreno = me.smashfactions.Main.getPlugin().getManager()
//								.pegarTerreno(p.getLocation().getChunk());
//						Faction dom = terreno.getFac();
//						if (dom == null) {
//
//							dom = me.smashfactions.Main.getPlugin().getManager().getZonaLivre();
//
//						}
//
//						String tag = "§7" + ChatColor.stripColor(dom.getNome());
//						if (dom.equals(me.smashfactions.Main.getPlugin().getManager().getZonaLivre())) {
//
//							tag = "" + dom.getTag();
//
//						} else if (dom.equals(me.smashfactions.Main.getPlugin().getManager().getZonaGuerra())) {
//
//							tag = "" + dom.getTag();
//
//						} else if (dom.equals(me.smashfactions.Main.getPlugin().getManager().getZonaProtegida())) {
//
//							tag = "" + dom.getTag();
//						}
//
//						DecimalFormat format = new DecimalFormat("#,###.#");
//
//						Team l = p.getScoreboard().getTeam("line");
//						l.setSuffix("   " + tag);
//
//						if (me.smashfactions.Main.getPlugin().getManager().temFacção(p)) {
//
//							Team l1 = p.getScoreboard().getTeam("line1");
//							Team l2 = p.getScoreboard().getTeam("line2");
//							Team l4 = p.getScoreboard().getTeam("line4");
//							Team l9 = p.getScoreboard().getTeam("line9");
//
//							l1.setSuffix("" + format.format(almas));
//							l2.setSuffix("" + format.format(coins));
//							l4.setSuffix("§7" + membro.getFaccao().getNome());
//							l9.setSuffix("§7" + format.format(cash));
//
//						} else {
//
//							Team l1 = p.getScoreboard().getTeam("line1");
//							Team l4 = p.getScoreboard().getTeam("line4");
//							Team l2 = p.getScoreboard().getTeam("line2");
//							Team l9 = p.getScoreboard().getTeam("line9");
//
//							l1.setSuffix("" + format.format(almas));
//							l4.setSuffix("§cNenhuma");
//							l2.setSuffix("" + format.format(coins));
//							l9.setSuffix("§7" + format.format(cash));
//
//						}
					}
				}
//			}
//		});
//		th.start();
	}

	public static void ligar() {
		new BukkitRunnable() {

			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					update(p);
				}
			}
		}.runTaskTimer(EduardAPI.getInstance(), 1, 1);
	}
}
