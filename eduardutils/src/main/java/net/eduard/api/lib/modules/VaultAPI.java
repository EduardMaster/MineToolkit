package net.eduard.api.lib.modules;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * API de utilização do Vault
 * 
 * @author Eduard
 * @version 2.0
 * @since Inicio
 *
 */
@SuppressWarnings("unused")
public final class VaultAPI {
	/**
	 * Força o ativamento do Vault
	 */
	public static void setupVault() {
		
		if (hasVault()) {
			setupEconomy();
			setupChat();
			setupPermissions();
		}
	}

	/**
	 *
	 * @param groups Lista de grupos
	 * @return Lista de Players com este grupo
	 */
	public static List<OfflinePlayer> getPlayersWithGroups(String... groups) {
		List<OfflinePlayer> newlist = new ArrayList<>();

		List<String> lista = Arrays.asList(groups);
		for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {

			for (String group : VaultAPI.getPermission().getPlayerGroups(null,player)){
				if (lista.contains(group)){
					newlist.add(player);
				}
			}
		}
		return newlist;
	}
	/**
	 * Testa se o Vault esta instalado no Servidor
	 * 
	 * @return Se sim ou nao
	 */
	public static boolean hasVault() {
		return Bukkit.getPluginManager().getPlugin("Vault") != null;
	}

	/*
	 * Se tiver o vault ele  ativado automaticamente
	 */
	static {
		if (hasVault()) {
			Bukkit.getConsoleSender().sendMessage("§bVaultAPI §aAtivando o suporte ao Vault");
			setupVault();
		}else {
			Bukkit.getConsoleSender().sendMessage("§bVaultAPI §aFalha ao ativar suporte ao Vault");
		}
	}
	/**
	 * Pega o prefixo no primeiro grupo do jogador
	 * 
	 * @param player
	 *            Jogador
	 * @return Prefixo do grupo
	 */
	@SuppressWarnings("deprecation")
	public static String getPlayerGroupPrefix(String player) {
		return VaultAPI.getChat()
				.getGroupPrefix("null",
						VaultAPI.getPermission().getPrimaryGroup("null", Bukkit.getOfflinePlayer(player)))
				.replace('&', ChatColor.COLOR_CHAR);
	}

	/**
	 * Controle de permissões
	 */

	@Deprecated
	private static Permission permission = null;

	/**
	 * Controle da Economia
	 */
	private static Economy economy = null;

	/**
	 * Controle do Chat (Bate Papo do Jogo)
	 */
	private static Chat chat = null;

	/**
	 * Pega o Controlador das permissões
	 * 
	 * @return Controlador
	 */
	public static Permission getPermission() {
		return permission;
	}

	/**
	 * Pega o Controlador das Chat
	 * 
	 * @return Controlador
	 */
	public static Chat getChat() {
		return chat;
	}

	/**
	 * Pega o Controlador da Economia
	 * 
	 * @return Controlador
	 */
	public static Economy getEconomy() {
		return economy;
	}



	/**
	 * Testa se tem algum plugin de Economia
	 * 
	 * @return Se sim ou nao
	 */
	public static boolean hasEconomy() {
		return economy != null;
	}

	/**
	 * Testa se tem algum plugin de Chat
	 * 
	 * @return Se sim ou nao
	 */
	public static boolean hasChat() {
		return chat != null;
	}

	/**
	 * net.eduard.curso.mongodb.MongoDBTeste se tem plugin de permissões
	 * 
	 * @return Se sim ou nao
	 */
	public static boolean hasPermission() {
		return permission != null;
	}

	/**
	 * Tenta Ativar o Sistema de Chat
	 *
	 */
	private static void setupChat() {

		RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.chat.Chat.class);
		if (chatProvider != null) {
			chat = chatProvider.getProvider();
		}

	}

	/**
	 * Tenta Ativar o Sistema de Economia
	 *
	 */
	private static void setupEconomy() {

		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

	}

	/**
	 * Tenta ativar o Sistema de permissões
	 *
	 */
	private static void setupPermissions() {

		RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
	}

	
}
