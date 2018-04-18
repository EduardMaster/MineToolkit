package net.eduard.api.setup;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

/**
 * API para a utilização do Vault com um formato mais simples de entender <br>
 * 
 * @author Eduard
 * @version 1.0
 * @since 1.0
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
				.replace('&', '§');
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
	 * Pega o Controlador das Permissões
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
	 * @return Se sim ou não
	 */
	public static boolean hasVault() {
		return Bukkit.getPluginManager().getPlugin("Vault") != null;
	}

	/**
	 * Testa se tem algum plugin de Economia
	 * 
	 * @return Se sim ou não
	 */
	public static boolean hasEconomy() {
		return economy != null;
	}

	/**
	 * Testa se tem algum plugin de Chat
	 * 
	 * @return Se sim ou não
	 */
	public static boolean hasChat() {
		return chat != null;
	}

	/**
	 * Teste se tem plugin de permissões
	 * 
	 * @return Se sim ou não
	 */
	public static boolean hasPermission() {
		return permission != null;
	}

	/**
	 * Tenta Ativar o Sistema de Chat
	 * 
	 * @return Se ativou ou não
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
	 * @return Se ativou ou não
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
	 * Tenta ativar o Sistema de Permissões
	 * 
	 * @return Se Ativou ou não
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
	 * Força o ativamento do Vault
	 */
	public static void setupVault() {

		setupEconomy();
		setupChat();
		setupPermissions();
	}

	/**
	 * Se tiver o vault ele é ativado automaticamente
	 */
	static {
		if (hasVault()) {
			setupVault();
		}
	}
}
