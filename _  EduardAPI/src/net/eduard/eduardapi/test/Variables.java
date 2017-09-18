package net.eduard.eduardapi.test;

public final class Variables {
	public static void simpleClans() {
//		ExtraAPI.addReplacer("$clan_label", new Replacer() {
//
//			@Override
//			public Object getText(Player p) {
//
//				ClanPlayer clan = SimpleClans.getInstance().getClanManager()
//						.getClanPlayer(p);
//				if (clan == null) {
//					return "";
//				}
//				if (clan.getClan() == null) {
//					return "";
//				}
//
//				return clan.getClan().getTagLabel();
//			}
//		});
//		ExtraAPI.addReplacer("$clan_name", new Replacer() {
//
//			@Override
//			public Object getText(Player p) {
//
//				ClanPlayer clan = SimpleClans.getInstance().getClanManager()
//						.getClanPlayer(p);
//				if (clan == null) {
//					return "";
//				}
//				if (clan.getClan() == null) {
//					return "";
//				}
//
//				return clan.getClan().getName();
//			}
//		});
//		ExtraAPI.addReplacer("$clan_tag", new Replacer() {
//
//			@Override
//			public Object getText(Player p) {
//
//				ClanPlayer clan = SimpleClans.getInstance().getClanManager()
//						.getClanPlayer(p);
//				if (clan == null) {
//					return "";
//				}
//				if (clan.getClan() == null) {
//					return "";
//				}
//
//				return  clan.getClan().getTag();
//			}
//		});
//		ExtraAPI.addReplacer("$clan_color", new Replacer() {
//
//			@Override
//			public Object getText(Player p) {
//
//				ClanPlayer clan = SimpleClans.getInstance().getClanManager()
//						.getClanPlayer(p);
//				if (clan == null) {
//					return "";
//				}
//				if (clan.getClan() == null) {
//					return "";
//				}
//
//				return  clan.getClan().getColorTag();
//			}
//		});
//		ExtraAPI.addReplacer("$clan_name", new Replacer() {
//
//			@Override
//			public Object getText(Player p) {
//
//				ClanPlayer clan = SimpleClans.getInstance().getClanManager()
//						.getClanPlayer(p);
//				if (clan == null) {
//					return "";
//				}
//				if (clan.getClan() == null) {
//					return "";
//				}
//
//				return  clan.getClan().getName();
//			}
//		});
	}
	public static void hardFacs() {
//		if (API.hasPlugin("HardFacs")) {
//			ExtraAPI.addReplacer("$fac_money", new Replacer() {
//
//				@Override
//				public Object getText(Player p) {
//					DecimalFormat decimal = new DecimalFormat("#,##0.00");
//					return decimal.format(FPlayers.i.get(p).getFaction().money);
//				}
//			});
//			ExtraAPI.addReplacer("$fac_p_chat", new Replacer() {
//
//				@Override
//				public Object getText(Player p) {
//					return FPlayers.i.get(p).getChatTag();
//				}
//			});
//			ExtraAPI.addReplacer("$fac_zone", new Replacer() {
//
//				@Override
//				public Object getText(Player p) {
//					FPlayer player = FPlayers.i.get(p);
//					Faction zone = Board.getFactionAt(player.getLastStoodAt());
//
//					return zone.getColorTo(player) + zone.getTag();
//					// return Board.getTerritoryAccessAt(new FLocation(fp)).
//				}
//			});
//			ExtraAPI.addReplacer("$fac_p_power", new Replacer() {
//
//				@Override
//				public Object getText(Player p) {
//
//					return FPlayers.i.get(p).getPowerRounded();
//				}
//			});
//			ExtraAPI.addReplacer("$fac_p_maxpower", new Replacer() {
//
//				@Override
//				public Object getText(Player p) {
//
//					return FPlayers.i.get(p).getPowerMaxRounded();
//				}
//			});
//			ExtraAPI.addReplacer("$fac_power", new Replacer() {
//
//				@Override
//				public Object getText(Player p) {
//
//					return FPlayers.i.get(p).getFaction().getPowerRounded();
//				}
//			});
//			ExtraAPI.addReplacer("$fac_maxpower", new Replacer() {
//
//				@Override
//				public Object getText(Player p) {
//
//					return FPlayers.i.get(p).getFaction().getPowerMaxRounded();
//				}
//			});
//
//			ExtraAPI.addReplacer("$fac_tag", new Replacer() {
//
//				@Override
//				public Object getText(Player p) {
//					Faction f = FPlayers.i.get(p).getFaction();
//					if (f != null & !f.getComparisonTag().equals("ZonaLivre"))
//						return f.getTag();
//					return "§7Sem Facção";
//				}
//			});
//			ExtraAPI.addReplacer("$fac_comptag", new Replacer() {
//
//				@Override
//				public Object getText(Player p) {
//
//					return FPlayers.i.get(p).getFaction().getComparisonTag();
//				}
//			});
//			ExtraAPI.addReplacer("$fac_desc", new Replacer() {
//
//				@Override
//				public Object getText(Player p) {
//					Faction f = FPlayers.i.get(p).getFaction();
//					if (f != null)
//						return f.getDescription();
//					return "";
//				}
//			});
//			ExtraAPI.addReplacer("$fac_online", new Replacer() {
//
//				@Override
//				public Object getText(Player p) {
//
//					return FPlayers.i.get(p).getFaction()
//							.getFPlayersWhereOnline(true).size();
//				}
//			});
//			ExtraAPI.addReplacer("$fac_players", new Replacer() {
//
//				@Override
//				public Object getText(Player p) {
//
//					return FPlayers.i.get(p).getFaction().getFPlayers().size();
//				}
//			});
//			ExtraAPI.addReplacer("$fac_claims", new Replacer() {
//
//				@Override
//				public Object getText(Player p) {
//
//					return FPlayers.i.get(p).getFaction().getClaims().size();
//				}
//			});
	}
}
