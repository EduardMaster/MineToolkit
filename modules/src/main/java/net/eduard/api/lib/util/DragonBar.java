package net.eduard.api.lib.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
/**
 * Sistema de Ender Dragon virtual com nome alterado
 * @author Internet
 *
 */
public class DragonBar {
	private static DragonBar instance;
	private final Map<String, Dragon> dragonMap = new HashMap<>();

	public static DragonBar getInstance() {
		if (instance == null) {
			instance = new DragonBar();
		}
		return instance;
	}

	public void setStatus(Player player, String text, int percent,
			boolean reset) throws Exception {
		if (percent <= 0) {
			percent = 1;
		} else if (percent > 100) {
			throw new IllegalArgumentException(
					"percent cannot be greater than 100, percent = " + percent);
		}

		Dragon dragon;

		if ((this.dragonMap.containsKey(player.getName())) && (!reset)) {
			dragon = this.dragonMap.get(player.getName());
		} else {
			dragon = new Dragon(text,
					player.getLocation().add(0.0D, -200.0D, 0.0D), percent);
			Object mobPacket = dragon.getSpawnPacket();
			sendPacket(player, mobPacket);
			this.dragonMap.put(player.getName(), dragon);
		}

		if (text.equals("")) {
			Object destroyPacket = dragon.getDestroyPacket();
			sendPacket(player, destroyPacket);
			this.dragonMap.remove(player.getName());
		} else {
			dragon.setName(text);
			dragon.setHealth(percent);
			Object metaPacket = dragon.getMetaPacket(dragon.getWatcher());
			Object teleportPacket;
			try {
				teleportPacket = dragon.getTeleportPacket(
						player.getLocation().add(0.0D, +100.0D, 0.0D));
			} catch (Exception e) {
				teleportPacket = dragon.getTeleportPacket2(
						player.getLocation().add(0.0D, +100.0D, 0.0D));
			}
			sendPacket(player, metaPacket);
			sendPacket(player, teleportPacket);
		}
	}

	public static void sendPacketRadius(Location loc, int radius,
			Object packet) {
		for (Player p : loc.getWorld().getPlayers()) {
			if (loc.distanceSquared(p.getLocation()) < radius * radius) {
				sendPacket(p, packet);
			}
		}
	}

	public static void sendPacket(List<Player> players, Object packet) {
		for (Player p : players) {
			sendPacket(p, packet);
		}
	}

	public static void sendPacket(Player p, Object packet) {
		try {
			Object nmsPlayer = getHandle(p);
			Field con_field = nmsPlayer.getClass().getField("playerConnection");
			Object con = con_field.get(nmsPlayer);
			Method packet_method = getMethod(con.getClass(), "sendPacket");
			Objects.requireNonNull(packet_method).invoke(con, packet);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	public static Class<?> getCraftClass(String ClassName) {
		String name = Bukkit.getServer().getClass().getPackage().getName();
		String version = name.substring(name.lastIndexOf('.') + 1) + ".";
		String className = "net.minecraft.server." + version + ClassName;
		Class<?> c = null;
		try {
			c = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return c;
	}

	public static Class<?> getBukkitClass(String ClassName) {
		String name = Bukkit.getServer().getClass().getPackage().getName();
		String version = name.substring(name.lastIndexOf('.') + 1) + ".";
		String className = "org.bukkit.craftbukkit." + version + ClassName;
		Class<?> c = null;
		try {
			c = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return c;
	}

	public static Object getHandle(Entity entity) {
		Object nms_entity = null;
		Method entity_getHandle = getMethod(entity.getClass(), "getHandle");
		try {
			nms_entity = Objects.requireNonNull(entity_getHandle).invoke(entity);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return nms_entity;
	}

	public static Object getHandle(Object entity) {
		Object nms_entity = null;
		Method entity_getHandle = getMethod(entity.getClass(), "getHandle");
		try {
			assert entity_getHandle != null;
			nms_entity = entity_getHandle.invoke(entity);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return nms_entity;
	}

	public static Field getField(Class<?> cl, String field_name) {
		try {
			return cl.getDeclaredField(field_name);
		} catch (SecurityException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Method getMethod(Class<?> cl, String method,
			Class<?>[] args) {
		for (Method mtd : cl.getMethods()) {
			if ((mtd.getName().equals(method))
					&& (ClassListEqual(args, mtd.getParameterTypes()))) {
				return mtd;
			}
		}
		return null;
	}

	public static Method getMethod(Class<?> cl, String method, Integer args) {
		for (Method m : cl.getMethods()) {
			if ((m.getName().equals(method)) && (args
					.equals(m.getParameterTypes().length))) {
				return m;
			}
		}
		return null;
	}

	public static Method getMethod(Class<?> cl, String method) {
		for (Method m : cl.getMethods()) {
			if (m.getName().equals(method)) {
				return m;
			}
		}
		return null;
	}

	public static void setValue(Object instance, String fieldName, Object value)
			throws Exception {
		Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(instance, value);
	}

	public static Object getValue(Object instance, String fieldName)
			throws Exception {
		Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(instance);
	}

	public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
		boolean equal = true;

		if (l1.length != l2.length)
			return false;
		for (int i = 0; i < l1.length; i++) {
			if (l1[i] != l2[i]) {
				equal = false;
				break;
			}
		}

		return equal;
	}
	public static class Dragon {

		private static final int MAX_HEALTH = 200;
		private int id;
		private final int x;
		private final int y;
		private final int z;
		private float health;
		private final boolean visible = false;
		private String name;
		private final Object world;
		private Object dragon;

		public Dragon(String name, Location loc, int percent) {
			this.name = name;
			this.x = loc.getBlockX();
			this.y = loc.getBlockY();
			this.z = loc.getBlockZ();
			this.health = (percent / 100.0F * MAX_HEALTH);
			this.world = getHandle(loc.getWorld());
		}

		public void setHealth(int percent) {
			this.health = (percent / 100.0F * 200.0F);
		}

		public void setName(String name) {
			this.name = name;
		}

		public Object getSpawnPacket()
				throws IllegalArgumentException, SecurityException,
				InstantiationException, IllegalAccessException,
				InvocationTargetException, NoSuchMethodException {
			Class<?> Entity = getCraftClass("Entity");
			Class<?> EntityLiving = getCraftClass("EntityLiving");
			Class<?> EntityEnderDragon = getCraftClass("EntityEnderDragon");
			this.dragon = EntityEnderDragon
					.getConstructor(new Class[]{getCraftClass("World")})
					.newInstance(this.world);

			Method setLocation = getMethod(EntityEnderDragon, "setLocation",
					new Class[]{Double.TYPE, Double.TYPE, Double.TYPE,
							Float.TYPE, Float.TYPE});
			int pitch = 0;
			int yaw = 0;
			Objects.requireNonNull(setLocation).invoke(this.dragon,
					this.x,
					this.y, this.z,
					pitch,
					yaw);

			Method setInvisible = getMethod(EntityEnderDragon, "setInvisible",
					new Class[]{Boolean.TYPE});
			Objects.requireNonNull(setInvisible).invoke(this.dragon,
					this.visible);

			Method setCustomName = getMethod(EntityEnderDragon, "setCustomName",
					new Class[]{String.class});
			Objects.requireNonNull(setCustomName).invoke(this.dragon, this.name);

			Method setHealth = getMethod(EntityEnderDragon, "setHealth",
					new Class[]{Float.TYPE});
			Objects.requireNonNull(setHealth).invoke(this.dragon,
					this.health);

			Field motX = getField(Entity, "motX");
			byte xvel = 0;
			assert motX != null;
			motX.set(this.dragon, xvel);

			Field motY = getField(Entity, "motX");
			byte yvel = 0;
			assert motY != null;
			motY.set(this.dragon, yvel);

			Field motZ = getField(Entity, "motX");
			byte zvel = 0;
			assert motZ != null;
			motZ.set(this.dragon, zvel);

			Method getId = getMethod(EntityEnderDragon, "getId", new Class[0]);
			this.id = (Integer) Objects.requireNonNull(getId).invoke(this.dragon, new Object[0]);

			Class<?> PacketPlayOutSpawnEntityLiving = getCraftClass(
					"PacketPlayOutSpawnEntityLiving");

			return PacketPlayOutSpawnEntityLiving
					.getConstructor(new Class[]{EntityLiving})
					.newInstance(this.dragon);
		}

		public Object getDestroyPacket() throws IllegalArgumentException,
				SecurityException, InstantiationException,
				IllegalAccessException, InvocationTargetException,
				NoSuchMethodException, NoSuchFieldException {
			Class<?> PacketPlayOutEntityDestroy = getCraftClass(
					"PacketPlayOutEntityDestroy");

			Object packet = PacketPlayOutEntityDestroy
					.getConstructor(new Class[0]).newInstance();

			Field a = PacketPlayOutEntityDestroy.getDeclaredField("a");
			a.setAccessible(true);
			a.set(packet, new int[]{this.id});

			return packet;
		}

		public Object getMetaPacket(Object watcher)
				throws IllegalArgumentException, SecurityException,
				InstantiationException, IllegalAccessException,
				InvocationTargetException, NoSuchMethodException {
			Class<?> DataWatcher = getCraftClass("DataWatcher");

			Class<?> PacketPlayOutEntityMetadata = getCraftClass(
					"PacketPlayOutEntityMetadata");

			return PacketPlayOutEntityMetadata.getConstructor(
					new Class[]{Integer.TYPE, DataWatcher, Boolean.TYPE})
					.newInstance(this.id, watcher,
							Boolean.TRUE);
		}

		public Object getTeleportPacket(Location loc)
				throws IllegalArgumentException, SecurityException,
				InstantiationException, IllegalAccessException,
				InvocationTargetException, NoSuchMethodException {
			Class<?> PacketPlayOutEntityTeleport = getCraftClass(
					"PacketPlayOutEntityTeleport");

			return PacketPlayOutEntityTeleport
					.getConstructor(new Class[]{Integer.TYPE, Integer.TYPE,
							Integer.TYPE, Integer.TYPE, Byte.TYPE, Byte.TYPE})
					.newInstance(this.id,
							loc.getBlockX() * 32,
							loc.getBlockY() * 32,
							loc.getBlockZ() * 32,
							(byte) ((int) loc.getYaw() * 256 / 360),
							(byte) ((int) loc.getPitch() * 256
									/ 360));
		}
		public Object getTeleportPacket2(Location loc)
				throws IllegalArgumentException, SecurityException,
				InstantiationException, IllegalAccessException,
				InvocationTargetException, NoSuchMethodException {
			Class<?> PacketPlayOutEntityTeleport = getCraftClass(
					"PacketPlayOutEntityTeleport");

			return PacketPlayOutEntityTeleport
					.getConstructor(new Class[]{Integer.TYPE, Integer.TYPE,
							Integer.TYPE, Integer.TYPE, Byte.TYPE, Byte.TYPE,Boolean.TYPE})
					.newInstance(this.id,
							loc.getBlockX() * 32,
							loc.getBlockY() * 32,
							loc.getBlockZ() * 32,
							(byte) ((int) loc.getYaw() * 256 / 360),
							(byte) ((int) loc.getPitch() * 256
									/ 360),true);
		}

		public Object getWatcher()
				throws IllegalArgumentException, SecurityException,
				InstantiationException, IllegalAccessException,
				InvocationTargetException, NoSuchMethodException {
			Class<?> Entity = getCraftClass("Entity");
			Class<?> DataWatcher = getCraftClass("DataWatcher");

			Object watcher = DataWatcher.getConstructor(new Class[]{Entity})
					.newInstance(this.dragon);

			Method a = getMethod(DataWatcher, "a",
					new Class[]{Integer.TYPE, Object.class});

			assert a != null;
			a.invoke(watcher, 0,
					(byte) (this.visible ? 0 : 32));
			a.invoke(watcher, 6,
					this.health);
			a.invoke(watcher,
					7, 0);
			a.invoke(watcher,
					8, (byte) 0);
			a.invoke(watcher, 10, this.name);
			a.invoke(watcher,
					11, (byte) 1);
			return watcher;
		}

	}

}
