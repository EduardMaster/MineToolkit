package net.eduard.api.setup.old;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * 
 * API de Reflection para Minecraft<br>
 * Array = Vetor -> String[] ou String... <br>
 * Constructor"cons" = Iniciador -> public RexAPI(){}; <br>
 * Parameters = Parametros -> Class[] ou Object[]
 * 
 * @version 1.0
 * @since Lib v1.0
 * @author Eduard
 *
 */
public class RexAPI {
	/**
	 * Tipo de geração de Key
	 * @author Eduard-PC
	 *
	 */
	public enum KeyType {
		/**
		 * ID UNICO
		 */
		UUID, 
		/**
		 * LETRAS
		 */
		LETTER, 
		/**
		 * NUMEROS
		 */
		NUMERIC, 
		/**
		 * NUMEROS E LETRAS
		 */
		ALPHANUMERIC;
	}

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

		getResult(getConnection(player), "sendPacket", getParameters(pPacket),
				packet);
	}
	public static Plugin getPlugin(String plugin) {
		return Bukkit.getPluginManager().getPlugin(plugin);
	}

	public static int getCurrentTick() throws Exception {
		return (int) RexAPI.getValue(RexAPI.mMinecraftServer, "currentTick");
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
		return getNew(mChatComponentText, text);

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
		return getResult(mChatSerializer, "a", component);
	}

	/**
	 * @param player
	 *            Jogador (CraftPlayer)
	 * @return EntityPlayer pelo metodo getHandle da classe CraftPlayer(Player)
	 * @exception Exception
	 */
	public static Object getHandle(Player player) throws Exception {
		return getResult(player, "getHandle");
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
		return getValue(getHandle(player), "playerConnection");
	}

	/**
	 * - Se for uma classe retorna a mesma<br>
	 * - Se for um String retorna uma classe baseada no Texto<Br>
	 * - Se for um objeto qualquer retorna object.getClass()<br>
	 * 
	 * @param object
	 *            Objeto
	 * @return Uma classe pelo objeto
	 * @throws Exception
	 */
	public static Class<?> getClass(Object object) throws Exception {
		if (object instanceof Class) {
			return (Class<?>) object;
		}
		if (object instanceof String) {
			String string = (String) object;
			if (string.startsWith("#")) {
				string = string.replace("#s", "org.spigotmc.");
				string = string.replace("#a", "net.eduard.api.");
				string = string.replace("#e", "net.eduard.eduardapi.");
				string = string.replace("#k", "net.eduard.api.kits.");
				string = string.replace("#p", "#mPacket");
				string = string.replace("#m", "net.minecraft.server.#v.");
				string = string.replace("#c", "org.bukkit.craftbukkit.#v.");
				string = string.replace("#b", "org.bukkit.");
				string = string.replace("#v", getVersion());
				// string = string.replace("#v2", getVersion2());

				return get(string);
			}

		}
		try {
			return (Class<?>) object.getClass().getField("TYPE").get(0);
		} catch (Exception e) {
		}
		return object.getClass();
	}
	/**
	 * Invoca um Metodo
	 * 
	 * @param object
	 *            Objeto
	 * @param name
	 *            Nome do Metodo
	 * @param objects
	 *            Parametros
	 * @return Valor do metodo
	 * @throws Exception
	 */
	public static Object getResult(Object object, String name,
			Object... objects) throws Exception {
		Method method = getMethod(object, name, objects);
		method.setAccessible(true);
		return method.invoke(object, objects);
	}
	/**
	 * Inicia um objeto
	 * 
	 * @param object
	 *            Classe
	 * @param objects
	 *            Parametros
	 * @return Novo Objeto iniciado
	 * @throws Exception
	 */
	public static Object getNew(Object object, Object... objects)
			throws Exception {
		Constructor<?> constructor = getConstructor(object, objects);
		constructor.setAccessible(true);
		return constructor.newInstance(objects);
	}
	/**
	 * Inicia um objeto
	 * 
	 * @param object
	 *            Classe
	 * @param parameters
	 *            Parametros
	 * @param objects
	 *            Valores
	 * @return Novo objeto iniciado
	 * @throws Exception
	 */
	public static Object getNew(Object object, Object[] parameters,
			Object... objects) throws Exception {
		Constructor<?> constructor = getConstructor(object, parameters);
		constructor.setAccessible(true);
		return constructor.newInstance(objects);
	}
	/**
	 * @param object
	 *            Classe
	 * @param objects
	 *            Parametros
	 * @return Construtor
	 * @throws Exception
	 */
	public static Constructor<?> getConstructor(Object object,
			Object... objects) throws Exception {
		try {
			return getClass(object)
					.getDeclaredConstructor(getParameters(objects));
		} catch (Exception ex) {
			return getClass(object).getConstructor(getParameters(objects));
		}
	}
	/**
	 * Invoca um Metodo
	 * 
	 * @param object
	 *            Classe
	 * @param name
	 *            Nome do Metodo
	 * @param parameters
	 *            Parametros
	 * @param objects
	 *            Valores
	 * @return Valor do metodo
	 * @throws Exception
	 */
	public static Object getResult(Object object, String name,
			Object[] parameters, Object... objects) throws Exception {
		Method method = getMethod(object, name, parameters);
		method.setAccessible(true);
		return method.invoke(object, objects);
	}
	/**
	 * @param object
	 *            Classe
	 * @param name
	 *            Nome da variavel
	 * @return Valor da variavel
	 * @throws Exception
	 */
	public static Object getValue(Object object, String name) throws Exception {
		Field field = getField(object, name);
		field.setAccessible(true);
		return field.get(object);
	}
	/**
	 * Modifica uma variavel
	 * 
	 * @param object
	 *            Classe
	 * @param name
	 *            Nome da Variavel
	 * @param value
	 *            Valor
	 * @throws Exception
	 */
	public static void setValue(Object object, String name, Object value)
			throws Exception {
		Field field = getField(object, name);
		field.setAccessible(true);
		field.set(object, value);
	}
	/**
	 * @param object
	 *            Classe
	 * @param name
	 *            Nome da variavel
	 * @return Uma Variavel
	 * @throws Exception
	 */
	public static Field getField(Object object, String name) throws Exception {
		try {
			return getClass(object).getDeclaredField(name);
		} catch (Exception ex) {
			return getClass(object).getField(name);
		}
	}
	/**
	 * @param object
	 *            Classe
	 * @param name
	 *            Nome do Metodo
	 * @param objects
	 *            Parametros
	 * @return Um Metodo
	 * @throws Exception
	 */
	public static Method getMethod(Object object, String name,
			Object... objects) throws Exception {
		try {
			return getClass(object).getDeclaredMethod(name,
					getParameters(objects));

		} catch (Exception ex) {
			return getClass(object).getMethod(name, getParameters(objects));
		}
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
	 * @param name
	 *            Nome
	 * @return Uma classe baseada no Nome
	 * @throws Exception
	 */
	public static Class<?> get(String name) throws Exception {
		return Class.forName(name);
	}
	/**
	 * @param objects
	 *            Valores"Parametros"
	 * @return Uma Array de Classes"Parametros" pela Array de Objetos"Valores"
	 * @throws Exception
	 */
	public static Class<?>[] getParameters(Object... objects) throws Exception {
		Class<?>[] parameters = new Class[objects.length];
		int index = 0;
		for (Object parameter : objects) {
			parameters[index] = getClass(parameter);
			index++;
		}
		return parameters;
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
				Object packet = getNew(sPacketTabHeader,
						getParameters(mIChatBaseComponent, mIChatBaseComponent),
						getIChatText(header), getIChatText(footer));
				sendPacket(packet, player);
				return;
			}

		} catch (Exception e) {
		}
		try {
			Object packet = getNew(pPlayOutPlayerListHeaderFooter,
					getParameters(mIChatBaseComponent), getIChatText(header));

			setValue(packet, "b", getIChatText(footer));
			sendPacket(packet, player);
		} catch (Exception e) {
		}
		try {
			Object packet = getNew(pPlayOutPlayerListHeaderFooter,
					getParameters(mIChatBaseComponent), getIChatText2(header));
			setValue(packet, "b", getIChatText2(footer));
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
			return (int) getResult(
					getValue(getConnection(player), "networkManager"),
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
				sendPacket(player, getNew(sPacketTitle,
						getValue(sAction, "TIMES"), fadeIn, stay, fadeOut));
				sendPacket(player, getNew(sPacketTitle,
						getParameters(sAction, mIChatBaseComponent),
						getValue(sAction, "TITLE"), getIChatText(title)));
				sendPacket(player, getNew(sPacketTitle,
						getParameters(sAction, mIChatBaseComponent),
						getValue(sAction, "SUBTITLE"), getIChatText(subTitle)));

				return;
			}

		} catch (Exception e) {
		}
		try {
			sendPacket(player, getNew(pPlayOutTitle, fadeIn, stay, fadeOut));
			sendPacket(player, getNew(pPlayOutTitle,
					getParameters(cEnumTitleAction, mIChatBaseComponent),
					getValue(cEnumTitleAction, "TITLE"), getIChatText(title)));
			sendPacket(player,
					getNew(pPlayOutTitle,
							getParameters(cEnumTitleAction,
									mIChatBaseComponent),
							getValue(cEnumTitleAction, "SUBTITLE"),
							getIChatText(subTitle)));
			return;
		} catch (Exception e) {
		}
		try {
			sendPacket(player, getNew(pPlayOutTitle, fadeIn, stay, fadeOut));
			sendPacket(player,
					getNew(pPlayOutTitle,
							getParameters(pEnumTitleAction2,
									mIChatBaseComponent),
							getValue(pEnumTitleAction2, "TITLE"),
							getIChatText2(title)));
			sendPacket(player,
					getNew(pPlayOutTitle,
							getParameters(pEnumTitleAction2,
									mIChatBaseComponent),
							getValue(pEnumTitleAction2, "SUBTITLE"),
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
			Object packet = getNew(pPlayOutChat,
					getParameters(mIChatBaseComponent, byte.class), component,
					(byte) 2);
			sendPacket(player, packet);
			return;
		} catch (Exception ex) {
		}
		try {
			Object component = getIChatText2(text);
			Object packet = getNew(pPlayOutChat,
					getParameters(mIChatBaseComponent, byte.class), component,
					(byte) 2);
			sendPacket(player, packet);
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage(
					"§bRexAPI §aNao foi possivel usar o 'setActionBar' pois o servidor esta na versao anterior a 1.8");

		}

	}
	/**
	 * Cria um texto baseados no Vetor de objetos
	 * @param objects Vetor de Objetos
	 * @return Texto
	 */
	public static String getText(Object... objects) {
		StringBuilder builder = new StringBuilder();
		for (Object object : objects) {
			builder.append(object);

		}
		return builder.toString();
	}
	/**
	 * @param player
	 *            Jogador
	 * @return Ping do jogador
	 */
	public static String getPing(Player player) {
		try {
			return getValue(getHandle(player), "ping").toString();
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

			Object object = getResult(bBukkit, "getOnlinePlayers");
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
			Object packet = getNew(pPlayInClientCommand,
					getValue(mEnumClientCommand, "PERFORM_RESPAWN"));
			getResult(getConnection(player), "a", packet);

		} catch (Exception ex) {
			try {
				Object packet = getNew(pPlayInClientCommand,
						getValue(mEnumClientCommand2, "PERFORM_RESPAWN"));
				getResult(getConnection(player), "a", packet);
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
			Object packet = getNew(pPlayOutNamedEntitySpawn,
					getParameters(mEntityHuman), getHandle(player));
			setValue(getValue(packet, "b"), "name", displayName);
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
			Object compound = getNew(mNBTTagCompound);
			Object getHandle = getResult(entity, "getHandle");
			getResult(getHandle, "c", compound);
			getResult(compound, "setByte", "NoAI", (byte) 1);
			getResult(getHandle, "f", compound);

		} catch (Exception e) {
		}

	}
	/**
	 * Pega o Ip do Jogador atual
	 * @param player Jogador
	 * @return Ip do Jogador
	 */
	public static String getIp(Player player) {
		return player.getAddress().getAddress().getHostAddress();
	}

	/**
	 * Gera uma nova Key
	 * @param type Tipo da Key
	 * @param maxSize Tamanho da Key
  	 * @return Key em forma de STRING
	 */
	public static String newKey(KeyType type, int maxSize) {

		String key = "";
		if (type == KeyType.UUID) {
			key = UUID.randomUUID().toString();
		} else if (type == KeyType.LETTER) {
			final StringBuffer buffer = new StringBuffer();
			String characters = "";
			characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
			final int charactersLength = characters.length();
			for (int i = 0; i < maxSize; ++i) {
				final double index = Math.random() * charactersLength;
				buffer.append(characters.charAt((int) index));
			}
			key = buffer.toString();
		} else if (type == KeyType.NUMERIC) {
			final StringBuffer buffer = new StringBuffer();
			String characters = "";
			characters = "0123456789";
			final int charactersLength = characters.length();
			for (int i = 0; i < maxSize; ++i) {
				final double index = Math.random() * charactersLength;
				buffer.append(characters.charAt((int) index));
			}
			key = buffer.toString();
		} else if (type == KeyType.ALPHANUMERIC) {
			final StringBuffer buffer = new StringBuffer();
			String characters = "";
			characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			final int charactersLength = characters.length();
			for (int i = 0; i < maxSize; ++i) {
				final double index = Math.random() * charactersLength;
				buffer.append(characters.charAt((int) index));
			}
			key = buffer.toString();
		}
		return key;

	}
	/**
	 * Pega o Ip do Coneção do Servidor
	 * @return Ip do Servidor
	 */
	public static String getServerIp() {
		try {
			URLConnection connect = new URL("http://checkip.amazonaws.com/")
					.openConnection();
			connect.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			Scanner scan = new Scanner(connect.getInputStream());
			StringBuilder sb = new StringBuilder();
			while (scan.hasNext()) {
				sb.append(scan.next());
			}
			scan.close();
			return sb.toString();

		} catch (Exception ex) {

			String ip = null;
			return ip;
		}
	}
	
	
}
