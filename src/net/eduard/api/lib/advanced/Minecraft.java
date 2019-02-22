package net.eduard.api.lib.advanced;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.Mine;

public interface Minecraft {

	/**
	 * Envia um Pacote de dados para o jogador
	 * @param packet Pacote
	 * @param player Jogador
	 */
	public void sendPacket(Object packet, Player player);
	/**
	 * Envia um Pacote de dados para o jogador
	 * @param player Jogador
	 * @param packet Pacote
	 */
	public default void sendPacket(Player player,Object packet) {
		sendPacket(packet, player);
	}
	/**
	 * Inicia automaticamente uma istancia da versão atual do servidor
	 * @return Instancia nova feita com Reflection
	 */
	public static Minecraft newInstance() {
		try {
			return (Minecraft) Class.forName("net.eduard.api.lib.advanced.Minecraft_" + Mine.getVersion())
					.newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void sendActionBar(Player player, String message);

	public default void sendPacketsToAll(Object... packets) {
		for (Player player : Mine.getPlayers()) {
			for (Object packet : packets) {
				sendPacket(packet, player);
			}
		}
	}

	public default void sendPacketsToOthers(Player player, Object... packets) {
		for (Player playerLoop : Mine.getPlayers()) {
			if (playerLoop.canSee(player) && !player.equals(playerLoop)) {
				for (Object packet : packets) {
					sendPacket(packet, player);
				}
			}
		}
	}

	public void setHeadSkin(ItemStack head, String texture, String signature);

	public void performRespawn(Player player);

	public void setPlayerSkin(Player player, String newSkin);

	public void setPlayerName(Player player, String newName);

	public void respawnPlayer(Player playerToRespawn);

	public void removeFromTab(Player playerRemoved);

	public void addToTab(Player playerToAdd);

}
