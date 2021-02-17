package net.eduard.api.lib.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

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
			Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", new Class[] { getNMSClass("Packet") })
					.invoke(playerConnection, packet);
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
			Object packet = Objects.requireNonNull(classe).newInstance();
			Class<?> clas = packet.getClass();
			Field team_name = getField(clas, "a");
			Field display_name = getField(clas, "b");
			Field prefix2 = getField(clas, "c");
			Field suffix2 = getField(clas, "d");
			Field members = getField(clas, "g");
			Field param_int = getField(clas, "h");
			Field pack_option = getField(clas, "i");
			setField(packet, Objects.requireNonNull(team_name), order + name);
			setField(packet, Objects.requireNonNull(display_name), player.getName());
			setField(packet, Objects.requireNonNull(prefix2), prefix);
			setField(packet, Objects.requireNonNull(suffix2), suffix);
			setField(packet, Objects.requireNonNull(members), Collections.singletonList(player.getName()));
			setField(packet, Objects.requireNonNull(param_int), 0);
			setField(packet, Objects.requireNonNull(pack_option), 1);
			for (Player ps : Bukkit.getOnlinePlayers())
				sendPacket(ps, packet);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}
}