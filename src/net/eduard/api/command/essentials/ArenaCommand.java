
package net.eduard.api.command.essentials;

import net.eduard.api.lib.storage.manager.CommandManager;

public class ArenaCommand extends CommandManager {

	public ArenaCommand() {
		super("arena");
	}
//	public CooldownMine cool;
//	/* 32 */ public static ArrayList<String> ArenaCogumeloa = new ArrayList<>();
//	/* 33 */ public static ArrayList<String> ArenaCogumelob = new ArrayList<>();
//	/* 34 */ public static ArrayList<String> ArenaPrismarinea = new ArrayList<>();
//	/* 35 */ public static ArrayList<String> ArenaPrismarineb = new ArrayList<>();
//	/*     */
//	/* 125 */ public static HashMap<UUID, Integer> CoolDown = new HashMap();
//	/*     */
//	/*     */ public boolean onCommand(CommandSender s, Command command,
//			String cmd, String[] args)
//	/*     */ {
//		/* 39 */ if (!(s instanceof Player)) {
//			/* 40 */ s.sendMessage("\u00A7cUse apenas comandos em jogo!");
//			/* 41 */ return false;
//			/*     */ }
//		/* 43 */ Player jogador = (Player) s;
//		/* 44 */ if (cmd.equalsIgnoreCase("Arena")) {
//			/* 45 */ guiKits1(jogador);
//			/*     */ }
//		/* 47 */ return false;
//		/*     */ }
//	/*     */
//	/*     */ public static void guiKits1(Player jogador)
//	/*     */ {
//		/* 52 */ Inventory inv = Bukkit.getServer().createInventory(jogador, 27,
//				"Selecione uma arena");
//		/*     */
//		/* 54 */ ItemStack Chest1 = new ItemStack(Material.HUGE_MUSHROOM_2, 1);
//		/* 55 */ ItemMeta meta1 = Chest1.getItemMeta();
//		/* 56 */ meta1.setDisplayName("\u00A7eArena cogumelo #1");
//		/*     */
//		/* 59 */ List loreList = new ArrayList();
//		/* 60 */ loreList.add(" ");
//		/* 61 */ loreList.add(
//				"\u00A77Jogadores na arena " + ArenaCogumeloa.size() + "/15");
//		/* 62 */ loreList.add(" ");
//		/* 63 */ loreList.add(" \u00A7cPvP Ativo em toda a arena.");
//		/*     */
//		/* 66 */ meta1.setLore(loreList);
//		/*     */
//		/* 68 */ Chest1.setItemMeta(meta1);
//		/* 69 */ inv.setItem(11, Chest1);
//		/*     */
//		/* 71 */ ItemStack Chest2 = new ItemStack(Material.HUGE_MUSHROOM_2, 2);
//		/* 72 */ ItemMeta meta2 = Chest2.getItemMeta();
//		/* 73 */ meta2.setDisplayName("\u00A7eArena cogumelo #2");
//		/*     */
//		/* 76 */ List loreList2 = new ArrayList();
//		/* 77 */ loreList2.add(" ");
//		/* 78 */ loreList2.add(
//				"\u00A77Jogadores na arena " + ArenaCogumelob.size() + "/15");
//		/* 79 */ loreList2.add(" ");
//		/* 80 */ loreList2.add(" \u00A7cPvP Ativo em toda a arena.");
//		/*     */
//		/* 82 */ meta2.setLore(loreList2);
//		/*     */
//		/* 84 */ Chest2.setItemMeta(meta2);
//		/* 85 */ inv.setItem(12, Chest2);
//		/*     */
//		/* 88 */ ItemStack Chest3 = new ItemStack(Material.PRISMARINE, 1);
//		/* 89 */ ItemMeta meta3 = Chest3.getItemMeta();
//		/* 90 */ meta3.setDisplayName("\u00A7eArena Prismarine #1");
//		/*     */
//		/* 93 */ List loreList3 = new ArrayList();
//		/* 94 */ loreList3.add(" ");
//		/* 95 */ loreList3.add(
//				"\u00A77Jogadores na arena " + ArenaPrismarinea.size() + "/15");
//		/* 96 */ loreList3.add(" ");
//		/* 97 */ loreList3.add(" \u00A7cPvP Ativo em toda a arena.");
//		/*     */
//		/* 99 */ meta3.setLore(loreList3);
//		/*     */
//		/* 101 */ Chest3.setItemMeta(meta3);
//		/* 102 */ inv.setItem(14, Chest3);
//		/*     */
//		/* 105 */ ItemStack Chest4 = new ItemStack(Material.PRISMARINE, 2);
//		/* 106 */ ItemMeta meta4 = Chest4.getItemMeta();
//		/* 107 */ meta4.setDisplayName("\u00A7eArena Prismarine #2");
//		/*     */
//		/* 110 */ List loreList4 = new ArrayList();
//		/* 111 */ loreList4.add(" ");
//		/* 112 */ loreList4.add(
//				"\u00A77Jogadores na arena " + ArenaPrismarineb.size() + "/15");
//		/* 113 */ loreList4.add(" ");
//		/* 114 */ loreList4.add(" \u00A7cPvP Ativo em toda a arena.");
//		/*     */
//		/* 116 */ meta4.setLore(loreList4);
//		/*     */
//		/* 118 */ Chest4.setItemMeta(meta4);
//		/* 119 */ inv.setItem(15, Chest4);
//		/*     */
//		/* 121 */ jogador.openInventory(inv);
//		/*     */ }
//	/*     */
//	/*     */ @EventHandler
//	/*     */ public void onPlayerClickInventry(InventoryClickEvent e)
//	/*     */ {
//		/* 130 */ final Player p = (Player) e.getWhoClicked();
//		/* 131 */ if ((e.getInventory().getTitle()
//				.equalsIgnoreCase("Selecione uma arena")) &&
//		/* 132 */ (e.getCurrentItem() != null)
//				&& (e.getCurrentItem().getTypeId() != 0)) {
//			/* 133 */ e.setCancelled(true);
//			/* 134 */ p.closeInventory();
//			/*     */
//			/* 137 */ if ((e.getSlot() == 11) || (e.getSlot() == 12)
//					|| (e.getSlot() == 14) || (e.getSlot() == 15)) {
//				/* 138 */ if (CoolDown.containsKey(p.getUniqueId())) {
//					/* 139 */ p.sendMessage(
//							"\u00A7cVoc\u00EA ainda n\u00E3o pode entrar na arena. (Tempo restante: "
//									+ CoolDown.get(p.getUniqueId()) + "s).");
//					/*     */
//					/* 141 */ e.setCancelled(true);
//					/*     */
//					/* 143 */ return;
//					/*     */ }
//				/*     */
//				/* 146 */ if (!CoolDown.containsKey(p.getUniqueId())) {
//					/* 147 */ CoolDown.put(p.getUniqueId(), Integer.valueOf(3));
//					/*     */
//					/* 149 */ Bukkit.getScheduler().scheduleSyncRepeatingTask(
//							Main.getInstance(), new Runnable() {
//								/*     */ public void run() {
//									/* 151 */ if (CoolDown
//											.containsKey(p.getUniqueId())) {
//										/* 152 */ CoolDown.put(
//												p.getUniqueId(),
//												Integer.valueOf(
//														((Integer) CoolDown
//																.get(p.getUniqueId()))
//																		.intValue()
//																- 1));
//										/*     */
//										/* 154 */ if (((Integer) CoolDown
//												.get(p.getUniqueId()))
//														.intValue() == 0)
//											/* 155 */ CoolDown
//													.remove(p.getUniqueId());
//										/*     */ }
//									/*     */ }
//								/*     */ }
//							/*     */ , 20L, 20L);
//					/*     */ }
//				/*     */ }
//			/*     */
//			/* 164 */ if (e.getSlot() == 11)
//			/*     */ {
//				/* 166 */ if (ArenaCogumeloa.size() >= 15) {
//					/* 167 */ p.sendMessage("\u00A7cArena lotada");
//					/* 168 */ return;
//					/*     */ }
//				/*     */
//				/* 171 */ ArenaCogumeloa.add(p.getName());
//				/* 172 */ PvPMine.Teleporte(p, "ArenaCogumelo1");
//				/* 173 */ p.sendMessage(" ");
//				/* 174 */ p.sendMessage(
//						"\u00A7aVoc\u00EA foi teleportado para a Arena Cogumelo #1");
//				/* 175 */ p.sendMessage(" ");
//				/* 176 */ p.playSound(p.getLocation(), Sound.LEVEL_UP, 5.0F,
//						5.0F);
//				/*     */
//				/* 178 */ p.setAllowFlight(false);
//				/* 179 */ e.setCancelled(true);
//				/*     */
//				/* 181 */ return;
//				/*     */ }
//			/*     */
//			/* 184 */ if (e.getSlot() == 12)
//			/*     */ {
//				/* 186 */ if (ArenaCogumelob.size() >= 15) {
//					/* 187 */ p.sendMessage("\u00A7cArena lotada");
//					/* 188 */ return;
//					/*     */ }
//				/*     */
//				/* 191 */ ArenaCogumelob.add(p.getName());
//				/* 192 */ PvPMine.Teleporte(p, "ArenaCogumelo2");
//				/* 193 */ p.sendMessage(" ");
//				/* 194 */ p.sendMessage(
//						"\u00A7aVoc\u00EA foi teleportado para a Arena Cogumelo #2");
//				/* 195 */ p.sendMessage(" ");
//				/* 196 */ p.playSound(p.getLocation(), Sound.LEVEL_UP, 5.0F,
//						5.0F);
//				/*     */
//				/* 199 */ p.setAllowFlight(false);
//				/* 200 */ e.setCancelled(true);
//				/* 201 */ return;
//				/*     */ }
//			/*     */
//			/* 204 */ if (e.getSlot() == 14)
//			/*     */ {
//				/* 206 */ if (ArenaPrismarinea.size() >= 15) {
//					/* 207 */ p.sendMessage("\u00A7cArena lotada");
//					/* 208 */ return;
//					/*     */ }
//				/*     */
//				/* 211 */ ArenaPrismarinea.add(p.getName());
//				/* 212 */ PvPMine.Teleporte(p, "ArenaPrismarine1");
//				/* 213 */ p.sendMessage(" ");
//				/* 214 */ p.sendMessage(
//						"\u00A7aVoc\u00EA foi teleportado para a Arena Prismarine #1");
//				/* 215 */ p.sendMessage(" ");
//				/* 216 */ p.playSound(p.getLocation(), Sound.LEVEL_UP, 5.0F,
//						5.0F);
//				/*     */
//				/* 219 */ p.setAllowFlight(false);
//				/* 220 */ e.setCancelled(true);
//				/* 221 */ return;
//				/*     */ }
//			/*     */
//			/* 224 */ if (e.getSlot() == 15)
//			/*     */ {
//				/* 226 */ if (ArenaPrismarineb.size() >= 15) {
//					/* 227 */ p.sendMessage("\u00A7cArena lotada");
//					/* 228 */ return;
//					/*     */ }
//				/*     */
//				/* 231 */ ArenaPrismarineb.add(p.getName());
//				/* 232 */ PvPMine.Teleporte(p, "ArenaPrismarine2");
//				/* 233 */ p.sendMessage(" ");
//				/* 234 */ p.sendMessage(
//						"\u00A7aVoc\u00EA foi teleportado para a Arena Prismarine #2");
//				/* 235 */ p.sendMessage(" ");
//				/* 236 */ p.playSound(p.getLocation(), Sound.LEVEL_UP, 5.0F,
//						5.0F);
//				/*     */
//				/* 239 */ p.setAllowFlight(false);
//				/* 240 */ e.setCancelled(true);
//				/* 241 */ return;
//				/*     */ }
//			/*     */ }
//		/*     */ }
//	/*     */
//
//	@EventHandler
//	/*     */ private void Arena(PlayerCommandPreprocessEvent e)
//	/*     */ {
//		/* 155 */ if ((ArenaCogumeloa.contains(e.getPlayer().getName()))
//				|| (ArenaCogumelob.contains(e.getPlayer().getName()))
//				|| (ArenaPrismarinea.contains(e.getPlayer().getName()))
//				|| (ArenaPrismarineb.contains(e.getPlayer().getName()))) {
//			/* 156 */ Player p = e.getPlayer();
//			/* 157 */ if ((!e.getMessage().toLowerCase().startsWith("/spawn"))
//					&& (!e.getMessage().toLowerCase().startsWith("/g"))
//					&& (!e.getMessage().toLowerCase().startsWith("/."))
//					&& (!e.getMessage().toLowerCase().startsWith("/f")))
//			/*     */ {
//				/* 160 */ p.sendMessage(
//						"\u00A7cVoc\u00EA n\u00E3o pode usar este comando na /arena!");
//				/* 161 */ e.setCancelled(true);
//				/*     */ }
//			/*     */ }
//		/*     */ }
//
//	@EventHandler
//	/*     */ public void morrer(PlayerRespawnEvent e) {
//		/* 90 */ Player jogador = e.getPlayer();
//		/*     */
//		/* 92 */ PvPMine.Teleporte(jogador, "Spawn01");
//		/*     */
//		/* 94 */ ArenaCogumeloa.remove(e.getPlayer().getName());
//		/* 95 */ ArenaCogumelob.remove(e.getPlayer().getName());
//		/* 96 */ ArenaPrismarinea.remove(e.getPlayer().getName());
//		/* 97 */ ArenaPrismarineb.remove(e.getPlayer().getName());
//		/*     */ }
//	/*     */
//	/*     */ @EventHandler
//	/*     */ public void onMoveEvent(PlayerMoveEvent e)
//	/*     */ {
//		/* 104 */ Player p = e.getPlayer();
//		/* 105 */ if (p.getLocation().getY() <= 0.0D)
//		/*     */ {
//			/* 107 */ PvPMine.Teleporte(p, "Spawn01");
//			/*     */
//			/* 109 */ ArenaCogumeloa.remove(e.getPlayer().getName());
//			/* 110 */ ArenaCogumelob.remove(e.getPlayer().getName());
//			/* 111 */ ArenaPrismarinea.remove(e.getPlayer().getName());
//			/* 112 */ ArenaPrismarineb.remove(e.getPlayer().getName());
//			/*     */ }
//		/*     */ }
//	/*     */
//	/*     */ @EventHandler
//	/*     */ public void DeathXP(PlayerDeathEvent Evento)
//	/*     */ {
//		/* 120 */ Player jogador = Evento.getEntity();
//		/* 121 */ if (jogador.hasPermission("FactionMania.forjar.vip")) {
//			/* 122 */ Evento.setKeepLevel(true);
//			/*     */
//			/* 124 */ ArenaCogumeloa.remove(jogador.getName());
//			/* 125 */ ArenaCogumelob.remove(jogador.getName());
//			/* 126 */ ArenaPrismarinea.remove(jogador.getName());
//			/* 127 */ ArenaPrismarineb.remove(jogador.getName());
//			/*     */ }
//		/*     */ }
//	@EventHandler
//	/*     */ public void Entrar(PlayerJoinEvent e)
//	/*     */ {
//		/* 27 */ Player jogador = e.getPlayer();
//		/*     */
//		/* 29 */ e.setJoinMessage(null);
//		/*     */
//		/* 31 */ PvPMine.Tab(jogador,
//				"\n\n\u00A76\u00A7lFACTION MANIA\n   \u00A77jogar.factionmania.tk\n",
//				/* 35 */ "\n\u00A76Team Speak: \u00A7fts.factionmania.tk\n\u00A76Twitter: \u00A7f@Faction_Mania\n\n\u00A76Adquira VIP e CASH acessando: \u00A7fwww.loja.factionmania.tk");
//		/*     */
//		/* 41 */ ArenaCogumeloa.remove(e.getPlayer().getName());
//		/* 42 */ ArenaCogumelob.remove(e.getPlayer().getName());
//		/* 43 */ ArenaPrismarinea.remove(e.getPlayer().getName());
//		/* 44 */ ArenaPrismarineb.remove(e.getPlayer().getName());
//		/*     */
//		/* 47 */ if (Main.Restart) {
//			/* 48 */ jogador.kickPlayer(
//					"\u00A7c\u00A7lFactionMania\n\n\u00A7cServidor reiniciandom aguarde...");
//			/*     */ }
//		/*     */
//		/* 52 */ Main.up.build(e.getPlayer());
//		/*     */
//		/* 54 */ for (Player Todos : Bukkit.getOnlinePlayers()) {
//			/* 55 */ if (ComandosGerais.Admin.contains(Todos.getName()))
//			/*     */ {
//				/* 57 */ jogador.hidePlayer(Todos);
//				/*     */ }
//			/*     */ else {
//				/* 60 */ jogador.showPlayer(Todos);
//				/*     */ }
//			/*     */
//			/*     */ }
//		/*     */
//		/* 65 */ File arquivo = new File("plugins/FactionUtils/Grupo.yml");
//		/* 66 */ Object Grupo = YamlConfiguration.loadConfiguration(arquivo);
//		/*     */
//		/* 68 */ if (((FileConfiguration) Grupo)
//				.get("Jogadores." + jogador.getName().toLowerCase()) == null) {
//			/* 69 */ PvPMine.SetGroup(jogador, "Membro");
//			/*     */
//			/* 71 */ PvPMine.ChatOn(jogador);
//			/*     */
//			/* 73 */ PvPMine.TpaOn(jogador);
//			/* 74 */ PvPMine.TellOn(jogador);
//			/* 75 */ PvPMine.SlimeOFF(jogador);
//			/*     */ }
//		/*     */
//		/* 79 */ PvPMine.AlmaSetNull(jogador);
//		/*     */
//		/* 81 */ for (Player Todos : Bukkit.getOnlinePlayers())
//			/* 82 */ if (ComandosGerais.Admin.contains(Todos.getName()))
//				/* 83 */ jogador.hidePlayer(Todos);
//		/*     */ }
//	/*     */
//	/*     */ @EventHandler
//	/*     */ public void Sair(PlayerQuitEvent e)
//	/*     */ {
//		/* 91 */ e.setQuitMessage(null);
//		/* 92 */ ArenaCogumeloa.remove(e.getPlayer().getName());
//		/* 93 */ ArenaCogumelob.remove(e.getPlayer().getName());
//		/* 94 */ ArenaPrismarinea.remove(e.getPlayer().getName());
//		/* 95 */ ArenaPrismarineb.remove(e.getPlayer().getName());
//		/*     */ }
}
