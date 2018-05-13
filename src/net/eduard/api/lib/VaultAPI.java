package net.eduard.api.lib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

/**
 * API para a utiliza��o do Vault com um formato mais simples de entender <br>
 * 
 * @author Eduard
 * @version 2.0
 * @since Lib v1.0 <br> EduardAPI 4.0
 *
 */
public final class VaultAPI {

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
				.replace('&', '�');
	}

	/**
	 * Controle de permiss�es
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
	 * Pega o Controlador das Permiss�es
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
	 * Testa se o Vault esta instalado no Servidor
	 * 
	 * @return Se sim ou n�o
	 */
	public static boolean hasVault() {
		return Bukkit.getPluginManager().getPlugin("Vault") != null;
	}

	/**
	 * Testa se tem algum plugin de Economia
	 * 
	 * @return Se sim ou n�o
	 */
	public static boolean hasEconomy() {
		return economy != null;
	}

	/**
	 * Testa se tem algum plugin de Chat
	 * 
	 * @return Se sim ou n�o
	 */
	public static boolean hasChat() {
		return chat != null;
	}

	/**
	 * Teste se tem plugin de permiss�es
	 * 
	 * @return Se sim ou n�o
	 */
	public static boolean hasPermission() {
		return permission != null;
	}

	/**
	 * Tenta Ativar o Sistema de Chat
	 * 
	 * @return Se ativou ou n�o
	 */
	private static boolean setupChat() {

		RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.chat.Chat.class);
		if (chatProvider != null) {
			chat = chatProvider.getProvider();
		}

		return (chat != null);
	}

	/**
	 * Tenta Ativar o Sistema de Economia
	 * 
	 * @return Se ativou ou n�o
	 */
	private static boolean setupEconomy() {

		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

		return (economy != null);
	}

	/**
	 * Tenta ativar o Sistema de Permiss�es
	 * 
	 * @return Se Ativou ou n�o
	 */
	private static boolean setupPermissions() {

		RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}

	/**
	 * For�a o ativamento do Vault
	 */
	public static void setupVault() {

		setupEconomy();
		setupChat();
		setupPermissions();
	}

	/**
	 * Se tiver o vault ele � ativado automaticamente
	 */
	static {
		if (hasVault()) {
			setupVault();
		}
	}
}
