package net.eduard.api.manager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.Mine.Replacer;

public class MassiveFactionReplacers {

	public MassiveFactionReplacers() {
		if (Mine.hasPlugin("Factions")) {
			Mine.addReplacer("$faction_tag", new Replacer() {

				@Override
				public Object getText(Player p) {

					MPlayer player = MPlayer.get(p);
					FactionColl c = FactionColl.get();
					Faction f = player.getFaction();
					if (c.getWarzone() == f || c.getSafezone() == f || c.getNone() == f) {
						return "§7Sem Facção";
					}
					if (player.getFaction() != null) {
						return player.getFaction().getTag();
					}
					return "";
				}
			});
			Mine.addReplacer("$faction_name", new Replacer() {

				@Override
				public Object getText(Player p) {

					MPlayer player = MPlayer.get(p.getUniqueId());
					FactionColl c = FactionColl.get();
					Faction f = player.getFaction();
					if (c.getWarzone() == f || c.getSafezone() == f || c.getNone() == f) {
						return "§7Sem Facção";
					}
//					Mine.console(""+player.getFaction());
					if (player.getFaction() != null) {
						return player.getFaction().getName();
					}
					return "";
				}
			});
			Mine.addReplacer("$faction_power", new Replacer() {

				@Override
				public Object getText(Player p) {

					MPlayer player = MPlayer.get(p);
					if (player.getFaction() != null) {
						return player.getFaction().getPowerRounded();
					}
					return "";
				}
			});
			Mine.addReplacer("$faction_max_power", new Replacer() {

				@Override
				public Object getText(Player p) {

					MPlayer player = MPlayer.get(p);
					if (player.getFaction() != null) {
						return player.getFaction().getPowerMaxRounded();
					}
					return "";
				}
			});
			Mine.addReplacer("$faction_players_online", new Replacer() {

				@Override
				public Object getText(Player p) {

					MPlayer player = MPlayer.get(p);
					if (player.getFaction() != null) {
						return player.getFaction().getMPlayersWhereOnline(true).size();
					}
					return "";
				}
			});
			Mine.addReplacer("$faction_max_players", new Replacer() {

				@Override
				public Object getText(Player p) {

					MPlayer player = MPlayer.get(p);
					if (player.getFaction() != null) {
						return player.getFaction().getMPlayers().size();
					}
					return "";
				}
			});
			Mine.addReplacer("$faction_claims", new Replacer() {

				@Override
				public Object getText(Player p) {

					MPlayer player = MPlayer.get(p);
					if (player.getFaction() != null) {
						return player.getFaction().getLandCount();
					}
					return "";
				}
			});
			Mine.addReplacer("$faction_header", new Replacer() {

				@Override
				public Object getText(Player p) {
					MPlayer player = MPlayer.get(p);
					String nome = p.getWorld().getName();
					if (nome.equalsIgnoreCase("mineracao") | nome.equalsIgnoreCase("mina")) {
						return "§7Mundo de Mineração";
					}
					if (nome.equalsIgnoreCase("loja")) {
						return "§eLoja";
					}
					if (nome.equalsIgnoreCase("vip")) {
						return "§6Area Vip";
					}
					if (nome.equalsIgnoreCase("guerra")) {
						return "§4Evento Guerra";
					}
					if (nome.equalsIgnoreCase("arena")) {
						return "§cArena";
					}
					Faction faction = BoardColl.get().getFactionAt(PS.valueOf(p.getLocation()));
					ChatColor cor = faction.getRelationTo(player).getColor();
					if (faction.equals(FactionColl.get().getSafezone())) {
						cor = ChatColor.GOLD;
					}
					if (faction.equals(FactionColl.get().getNone())) {
						cor = ChatColor.GREEN;
					}
					if (faction.equals(FactionColl.get().getWarzone())) {
						cor = ChatColor.DARK_RED;
					}
					return cor + faction.getName();
				}
			});
			Mine.addReplacer("$faction_player_power", new Replacer() {

				@Override
				public Object getText(Player p) {
					MPlayer player = MPlayer.get(p);
					return player.getPowerRounded();
				}
			});
			Mine.addReplacer("$faction_player_max_power", new Replacer() {

				@Override
				public Object getText(Player p) {
					MPlayer player = MPlayer.get(p);
					return player.getPowerMaxRounded();
				}
			});
		}

	}

}
