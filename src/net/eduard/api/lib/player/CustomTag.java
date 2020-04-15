package net.eduard.api.lib.player;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Sistema de customizar tags dos jogadores
 * 
 * @author Internet
 *
 *
 */

public class CustomTag {

	private static Class<?> getNMSClass(String name) {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try {
			return Class.forName("net.minecraft.server." + version + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", new Class[] { getNMSClass("Packet") })
					.invoke(playerConnection, new Object[] { packet });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void setField(Object packet, Field field, Object value) {
		field.setAccessible(true);
		try {
			field.set(packet, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		field.setAccessible(!field.isAccessible());
	}

	private static Field getField(Class<?> classs, String fieldname) {
		try {
			return classs.getDeclaredField(fieldname);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Seta um tag customizada para o jogador
	 * @param player Jogador
	 * @param prefix Prefixo
	 * @param suffix Suffixo
	 * @param order Ordem do tab
	 */
	public static void sendNameTag(Player player, String prefix, String suffix, String order) {
		String name = UUID.randomUUID().toString().substring(0, 15);
		Class<?> classe = getNMSClass("PacketPlayOutScoreboardTeam");

		try {
			Object packet = classe.newInstance();
			Class<?> clas = packet.getClass();
			Field team_name = getField(clas, "a");
			Field display_name = getField(clas, "b");
			Field prefix2 = getField(clas, "c");
			Field suffix2 = getField(clas, "d");
			Field members = getField(clas, "g");
			Field param_int = getField(clas, "h");
			Field pack_option = getField(clas, "i");
			setField(packet, team_name, order + name);
			setField(packet, display_name, player.getName());
			setField(packet, prefix2, prefix);
			setField(packet, suffix2, suffix);
			setField(packet, members, Arrays.asList(new String[] { player.getName() }));
			setField(packet, param_int, Integer.valueOf(0));
			setField(packet, pack_option, Integer.valueOf(1));
			for (Player ps : Bukkit.getOnlinePlayers())
				sendPacket(ps, packet);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}