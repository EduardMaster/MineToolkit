package net.eduard.api.setup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * 
 * API de Reflection para Minecraft
 * 
 * @author Eduard
 *
 */
public final class RexAPI {
	

	public static String mEntityPlayer = "#mEntityPlayer";
	public static String cCraftPlayer = "#cCraftPlayer";
	public static String sPacketTitle = "#sProtocolInjector$PacketTitle";
	public static String sAction = "#sProtocolInjector$PacketTitle$Action";
	public static String sPacketTabHeader = "#sProtocolInjector$PacketTabHeader";
	public static String pPlayOutChat = "#pPlayOutChat";
	public static String pPlayOutTitle = "#pPlayOutTitle";
	public static String pPlayOutWorldParticles = "#pPlayOutWorldParticles";
	public static String pPlayOutPlayerListHeaderFooter = "#pPlayOutPlayerListHeaderFooter";
	public static String pPlayOutNamedEntitySpawn = "#pPlayOutNamedEntitySpawn";
	public static String pPlayInClientCommand = "#pPlayInClientCommand";
	public static String cEnumTitleAction = "#cEnumTitleAction";
	public static String pEnumTitleAction2 = "#pPlayOutTitle$EnumTitleAction";
	public static String mEnumClientCommand = "#mEnumClientCommand";
	public static String mEnumClientCommand2 = "#pPlayInClientCommand$EnumClientCommand";
	public static String mChatSerializer = "#mChatSerializer";
	public static String mIChatBaseComponent = "#mIChatBaseComponent";
	public static String mEntityHuman = "#mEntityHuman";
	public static String mNBTTagCompound = "#mNBTTagCompound";
	public static String mNBTBase = "#mNBTBase";
	public static String mNBTTagList = "#mNBTTagList";
	public static String pPacket = "#p";
	public static String cItemStack = "#cinventory.CraftItemStack";
	public static String mItemStack = "#mItemStack";
	public static String bItemStack = "#bItemStack";
	public static String bBukkit = "#bBukkit";
	public static String mChatComponentText = "#mChatComponentText";
	public static String mMinecraftServer = "#mMinecraftServer";
	static {
		RefAPI.newReplacer("#v", getVersion());
	}
	/**
	 * Envia o pacote para o jogador
	 * 
	 * @param player
	 *            Jogador
	 * @param packet
	 *            Pacote
	 * @throws Exception
	 */
	public static void sendPacket(Object packet, Player player)
			throws Exception {

		RefAPI.getResult(getConnection(player), "sendPacket", RefAPI.getParameters(pPacket),
				packet);
	}
	public static Plugin getPlugin(String plugin) {
		return Bukkit.getPluginManager().getPlugin(plugin);
	}

	public static int getCurrentTick() throws Exception {
		return (int) RefAPI.getValue(RexAPI.mMinecraftServer, "currentTick");
	}
	/**
	 * Pega o TPS do servidor uma expecie de calculador de LAG
	 * @return TPS em forma de DOUBLE
	 */
	public static Double getTPS() {
		try {
			return Double.valueOf(
					Math.min(20.0D, Math.round(getCurrentTick() * 10) / 10.0D));
		} catch (Exception e) {
		}

		return 0D;
	}

	/**
	 * Envia o pacote para o jogador
	 * 
	 * @param player
	 *            Jogador
	 * @param packet
	 *            Pacote
	 * @throws Exception
	 */
	public static void sendPacket(Player player, Object packet)
			throws Exception {
		sendPacket(packet, player);
	}

	/**
	 * Envia o pacote para todos menos para o jogador
	 * 
	 * @param packet
	 *            Pacote
	 * @param target
	 *            Jogador
	 */
	public static void sendPackets(Object packet, Player target) {
		for (Player player : getPlayers()) {
			if (target == player)
				continue;
			try {
				sendPacket(packet, target);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Envia o pacote para todos jogadores
	 * 
	 * @param packet
	 *            Pacote
	 */
	public static void sendPackets(Object packet) {
		for (Player p : getPlayers()) {
			try {
				sendPacket(packet, p);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * @param text
	 *            Texto
	 * @return "{\"text\":\"" + text + "\"}"
	 */
	public static String getIComponentText(String text) {
		return ("{\"text\":\"" + text + "\"}");

	}

	/**
	 * Inicia um ChatComponentText"IChatBaseComponent" pelo cons(String) da
	 * classe ChatComponentText
	 * 
	 * @param text
	 *            Texto
	 * @return ChatComponentText iniciado
	 * 
	 */
	public static Object getIChatText2(String text) throws Exception {
		return RefAPI.getNew(mChatComponentText, text);

	}

	/**
	 * Inicia um IChatBaseComponent pelo metodo a(String) da classe
	 * ChatSerializer adicionando componente texto
	 * 
	 * @return IChatBaseComponent iniciado
	 * @param text
	 *            Texto
	 */
	public static Object getIChatText(String text) throws Exception {
		return getIChatBaseComponent(getIComponentText(text));
	}

	/**
	 * Inicia um IChatBaseComponent pelo metodo a(String) da classe
	 * ChatSerializer
	 * 
	 * @param component
	 *            Componente (Texto)
	 * @return IChatBaseComponent iniciado
	 */
	public static Object getIChatBaseComponent(String component)
			throws Exception {
		return RefAPI.getResult(mChatSerializer, "a", component);
	}

	/**
	 * @param player
	 *            Jogador (CraftPlayer)
	 * @return EntityPlayer pelo metodo getHandle da classe CraftPlayer(Player)
	 * @exception Exception
	 */
	public static Object getHandle(Player player) throws Exception {
		return RefAPI.getResult(player, "getHandle");
	}

	/**
	 * Retorna Um PlayerConnection pela variavel playerConnection da classe
	 * EntityPlayer <Br>
	 * Pega o EntityPlayer pelo metodo getHandle(player)
	 * 
	 * @param player
	 *            Jogador (CraftPlayer)
	 * @return Conexão do jogador
	 */
	public static Object getConnection(Player player) throws Exception {
		return RefAPI.getValue(getHandle(player), "playerConnection");
	}
	/**
	 * 
	 * @return Versão do Servidor
	 */
	public static String getVersion() {
		return Bukkit.getServer().getClass().getPackage().getName()
				.replace('.', ',').split(",")[3];
	}
	/**
	 * (Não funciona)
	 * 
	 * @return Versão do Servidor
	 */
	@Deprecated
	public static String getVersion2() {
		return Bukkit.getServer().getClass().getPackage().getName()
				.split("\\")[3];
	}

	
	/**
	 * Modifica a TabList do Jogador
	 * 
	 * @param player
	 *            Jogador
	 * @param header
	 *            Cabeçalho
	 * @param footer
	 *            Rodapé
	 */
	public static void setTabList(Player player, String header, String footer) {
		try {
			if (isAbove1_8(player)) {
				Object packet =RefAPI. getNew(sPacketTabHeader,
						RefAPI.getParameters(mIChatBaseComponent, mIChatBaseComponent),
						getIChatText(header), getIChatText(footer));
				sendPacket(packet, player);
				return;
			}

		} catch (Exception e) {
		}
		try {
			Object packet =RefAPI. getNew(pPlayOutPlayerListHeaderFooter,
					RefAPI.getParameters(mIChatBaseComponent), getIChatText(header));

			RefAPI.setValue(packet, "b", getIChatText(footer));
			sendPacket(packet, player);
		} catch (Exception e) {
		}
		try {
			Object packet = RefAPI.getNew(pPlayOutPlayerListHeaderFooter,
					RefAPI.getParameters(mIChatBaseComponent), getIChatText2(header));
			RefAPI.setValue(packet, "b", getIChatText2(footer));
			sendPacket(packet, player);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	/**
	 * @param player
	 *            Jogador
	 * @return Se o Jogador esta na versão 1.8 ou pra cima
	 */
	public static boolean isAbove1_8(Player player) {
		try {
			return (int) RefAPI.getResult(
					RefAPI.getValue(getConnection(player), "networkManager"),
					"getVersion") == 47;

		} catch (Exception ex) {
		}
		return false;
	}

	/**
	 * Envia um Title para o Jogador
	 * 
	 * @param player
	 *            Jogador
	 * @param title
	 *            Titulo
	 * @param subTitle
	 *            SubTitulo
	 * @param fadeIn
	 *            Tempo de Aparececimento (Ticks)
	 * @param stay
	 *            Tempo de Passagem (Ticks)
	 * @param fadeOut
	 *            Tempo de Desaparecimento (Ticks)
	 */
	public static void sendTitle(Player player, String title, String subTitle,
			int fadeIn, int stay, int fadeOut) {
		try {
			if (isAbove1_8(player)) {

				// sendPacket(player, getNew(PacketTitle, getParameters(Action,
				// int.class, int.class, int.class),
				// getValue(Action, "TIMES"), fadeIn, stay, fadeOut));
				sendPacket(player, RefAPI.getNew(sPacketTitle,
						RefAPI.getValue(sAction, "TIMES"), fadeIn, stay, fadeOut));
				sendPacket(player, RefAPI.getNew(sPacketTitle,
						RefAPI.getParameters(sAction, mIChatBaseComponent),
						RefAPI.getValue(sAction, "TITLE"), getIChatText(title)));
				sendPacket(player, RefAPI.getNew(sPacketTitle,
						RefAPI.getParameters(sAction, mIChatBaseComponent),
						RefAPI.getValue(sAction, "SUBTITLE"), getIChatText(subTitle)));

				return;
			}

		} catch (Exception e) {
		}
		try {
			sendPacket(player, RefAPI.getNew(pPlayOutTitle, fadeIn, stay, fadeOut));
			sendPacket(player, RefAPI.getNew(pPlayOutTitle,
					RefAPI.getParameters(cEnumTitleAction, mIChatBaseComponent),
					RefAPI.getValue(cEnumTitleAction, "TITLE"), getIChatText(title)));
			sendPacket(player,
					RefAPI.	getNew(pPlayOutTitle,
							RefAPI.getParameters(cEnumTitleAction,
									mIChatBaseComponent),
							RefAPI.getValue(cEnumTitleAction, "SUBTITLE"),
							getIChatText(subTitle)));
			return;
		} catch (Exception e) {
		}
		try {
			sendPacket(player, RefAPI.getNew(pPlayOutTitle, fadeIn, stay, fadeOut));
			sendPacket(player,
					RefAPI.	getNew(pPlayOutTitle,
							RefAPI.getParameters(pEnumTitleAction2,
									mIChatBaseComponent),
							RefAPI.	getValue(pEnumTitleAction2, "TITLE"),
							getIChatText2(title)));
			sendPacket(player,
					RefAPI.	getNew(pPlayOutTitle,
							RefAPI.getParameters(pEnumTitleAction2,
									mIChatBaseComponent),
							RefAPI.	getValue(pEnumTitleAction2, "SUBTITLE"),
							getIChatText2(subTitle)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Modifica a Action Bar do Jogador
	 * 
	 * @param player
	 *            Jogador
	 * @param text
	 *            Texto
	 */
	public static void sendActionBar(Player player, String text) {
		try {
			Object component = getIChatText(text);
			Object packet = RefAPI.getNew(pPlayOutChat,
					RefAPI.getParameters(mIChatBaseComponent, byte.class), component,
					(byte) 2);
			sendPacket(player, packet);
			return;
		} catch (Exception ex) {
		}
		try {
			Object component = getIChatText2(text);
			Object packet = RefAPI.getNew(pPlayOutChat,
					RefAPI.getParameters(mIChatBaseComponent, byte.class), component,
					(byte) 2);
			sendPacket(player, packet);
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage(
					"§bRexAPI §aNao foi possivel usar o 'setActionBar' pois o servidor esta na versao anterior a 1.8");

		}

	}
	/**
	 * @param player
	 *            Jogador
	 * @return Ping do jogador
	 */
	public static String getPing(Player player) {
		try {
			return RefAPI.getValue(getHandle(player), "ping").toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}
	/**
	 * @return Lista de jogadores do servidor
	 */
	public static List<Player> getPlayers() {
		List<Player> list = new ArrayList<>();
		try {

			Object object =RefAPI. getResult(bBukkit, "getOnlinePlayers");
			if (object instanceof Collection) {
				Collection<?> players = (Collection<?>) object;
				for (Object obj : players) {
					if (obj instanceof Player) {
						Player p = (Player) obj;
						list.add(p);
					}
				}
			} else if (object instanceof Player[]) {
				Player[] players = (Player[]) object;
				for (Player p : players) {
					list.add(p);
				}
			}
		} catch (Exception e) {
		}

		return list;
	}
	/**
	 * Força o Respawn do Jogador (Respawn Automatico)
	 * 
	 * @param player
	 *            Jogador
	 */
	public static void makeRespawn(Player player) {
		try {
			Object packet = RefAPI.getNew(pPlayInClientCommand,
					RefAPI.getValue(mEnumClientCommand, "PERFORM_RESPAWN"));
			RefAPI.getResult(getConnection(player), "a", packet);

		} catch (Exception ex) {
			try {
				Object packet = RefAPI.getNew(pPlayInClientCommand,
						RefAPI.getValue(mEnumClientCommand2, "PERFORM_RESPAWN"));
				RefAPI.getResult(getConnection(player), "a", packet);
			} catch (Exception e) {
			}

		}
	}
	/**
	 * Modifica o nome do Jogador para um Novo Nome e<br>
	 * Envia para Todos os outros Jogadores a alteração (Packet)
	 * 
	 * @param player
	 *            Jogador
	 * @param displayName
	 *            Novo Nome
	 */
	public static void changeName(Player player, String displayName) {

		try {
			Object packet = RefAPI.getNew(pPlayOutNamedEntitySpawn,
					RefAPI.	getParameters(mEntityHuman), getHandle(player));
			RefAPI.setValue(RefAPI.getValue(packet, "b"), "name", displayName);
			sendPackets(packet, player);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	/**
	 * Desabilita a Inteligencia da Entidade
	 * @param entity Entidade
	 */
	public static void disableAI(Entity entity) {
		try {
			// net.minecraft.server.v1_8_R3.Entity NMS = ((CraftEntity)
			// entidade).getHandle();
			// NBTTagCompound compound = new NBTTagCompound();
			// NMS.c(compound);
			// compound.setByte("NoAI", (byte) 1);
			// NMS.f(compound);
			Object compound = RefAPI.getNew(mNBTTagCompound);
			Object getHandle = RefAPI.getResult(entity, "getHandle");
			RefAPI.getResult(getHandle, "c", compound);
			RefAPI.getResult(compound, "setByte", "NoAI", (byte) 1);
			RefAPI.getResult(getHandle, "f", compound);

		} catch (Exception e) {
		}

	}

	
}
