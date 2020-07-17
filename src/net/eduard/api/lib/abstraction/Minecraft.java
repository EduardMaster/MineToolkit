package net.eduard.api.lib.abstraction;

import net.eduard.api.lib.modules.MineReflect;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.modules.Mine;

/**
 * @author  Eduard
 */
public interface Minecraft {

	/**
	 * Envia um Pacote de dados para o jogador
	 * @param packet Pacote
	 * @param player Jogador
	 */
	void sendPacket(Object packet, Player player);
	/**
	 * Envia um Pacote de dados para o jogador
	 * @param player Jogador
	 * @param packet Pacote
	 */
	default void sendPacket(Player player, Object packet) {
		sendPacket(packet, player);
	}
	/**
	 * Inicia automaticamente uma istancia da versão atual do servidor
	 * @return Instancia nova feita com Reflection
	 */
	static Minecraft newInstance() {
		try {
			return (Minecraft) Class.forName("net.eduard.api.lib.abstraction.Minecraft_" + MineReflect.getVersion())
					.newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {

			e.printStackTrace();
		}
		return null;
	}

	void sendActionBar(Player player, String message);

	 void sendParticle(Player player, String name, Location location, int amount, float xOffset, float yOffset, float zOffset, float speed);

	default  void sendParticle(Player player, String name, Location location, int amount){
		sendParticle(player,name,location,amount,0,0,0,1);
	}


	default void sendPacketsToAll(Object... packets) {
		for (Player player : Mine.getPlayers()) {
			for (Object packet : packets) {
				sendPacket(packet, player);
			}
		}
	}

	default void sendPacketsToOthers(Player player, Object... packets) {
		for (Player playerLoop : Mine.getPlayers()) {
			if (playerLoop.equals(player))continue;
			if (playerLoop.canSee(player) ) {
				for (Object packet : packets) {
					sendPacket(packet, player);
				}
			}
		}
	}

	void setHeadSkin(ItemStack head, String texture, String signature);

	void performRespawn(Player player);

	void setPlayerSkin(Player player, String newSkin);

	void setPlayerName(Player player, String newName);

	void respawnPlayer(Player playerToRespawn);

	void removeFromTab(Player playerRemoved);

	void addToTab(Player playerToAdd);

}
