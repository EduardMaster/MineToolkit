package net.eduard.api.server;

import net.eduard.api.server.chat.ChatManager;
import net.eduard.api.server.clans.ClanManager;
import net.eduard.api.server.economy.EconomyManager;
import net.eduard.api.server.factions.FactionManager;
import net.eduard.api.server.party.PartyManager;
import net.eduard.api.server.permissions.PermissionsManager;
import net.eduard.api.server.vips.VipManager;

public class Central {
	private static ChatManager chat;
	private static VipManager vips;
	private static EconomyManager economy;
	private static ClanManager clans;
	private static FactionManager factions;
	private static PartyManager party;
	private static PermissionsManager permissions;

	public static VipManager getVips() {
		return vips;
	}

	public static void setVips(VipManager vips) {
		Central.vips = vips;
	}

	public static ChatManager getChat() {
		return chat;
	}

	public static void setChat(ChatManager chat) {
		Central.chat = chat;
	}

	public static EconomyManager getEconomy() {
		return economy;
	}

	public static void setEconomy(EconomyManager economy) {
		Central.economy = economy;
	}

	public static ClanManager getClans() {
		return clans;
	}

	public static void setClans(ClanManager clans) {
		Central.clans = clans;
	}

	public static FactionManager getFactions() {
		return factions;
	}

	public static void setFactions(FactionManager factions) {
		Central.factions = factions;
	}

	public static PartyManager getParty() {
		return party;
	}

	public static void setParty(PartyManager party) {
		Central.party = party;
	}

	public static PermissionsManager getPermissions() {
		return permissions;
	}

	public static void setPermissions(PermissionsManager permissions) {
		Central.permissions = permissions;
	}

}
