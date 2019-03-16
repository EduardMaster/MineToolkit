package net.eduard.api.lib.old;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.eduard.api.lib.Mine;

/**
 * API principal com métodos importantes
 * @version 1.0
 * @since 0.9
 * @author Eduard
 * @deprecated Versão atual {@link Mine}
 *
 */
public final class Principal {
	public static ArrayList<Location> createFloor(Location location, int size, Material material) {
		return createFloor(location, size, material, 0);
	}

	public static Player getPlayer(String name) {
		return Bukkit.getPlayerExact(name);
	}

	public static int getDayInTicks() {
		return 20 * getDayInSeconds();
	}

	public static void goToSpawn(Entity entity) {
		entity.teleport(entity.getWorld().getSpawnLocation());
	}

	public static int getDayInLong() {
		return 50 * getDayInTicks();
	}

	public static int getDayInSeconds() {
		return 86400;
	}

	public static void broadcast(String message) {
		for (Player p : getOnlinePlayers()) {
			p.sendMessage(message);
		}
		Bukkit.getConsoleSender().sendMessage(message);
	}

	public static ArrayList<Location> createFloor(Location location, int size, Material material, int data) {
		ArrayList<Location> locations = getBlocksSquared(location, size);
		for (Location location1 : locations) {
			location1.getBlock().setType(material);

			location1.getBlock().setData((byte) data);
		}
		return locations;
	}

	public static Location getBlockLocation(Location location) {
		return new Location(location.getWorld(), (int) location.getX(), (int) location.getY(), (int) location.getZ());
	}

	public static ArrayList<Block> getBlocks(ArrayList<Location> locations) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		for (Location location : locations) {
			blocks.add(location.getBlock());
		}
		return blocks;
	}

	public static ArrayList<Location> getBlocksBox(Location location, int size, int high) {
		return getBlocksBox(location, size, high, 0);
	}

	public static ArrayList<Location> getBlocksBox(Location location, int size, int high, int low) {
		return getLocations(getHighLocation(location.clone(), high, size), getLowLocation(location.clone(), low, size));
	}

	public static ArrayList<Location> getBlocksSquared(Location location, int size) {
		return getBlocksBox(location, size, 0);
	}

	public static boolean getChance(double chance) {
		Random random = new Random();
		return random.nextDouble() <= chance;
	}

	public static Location getHighLocation(Location loc, int high, int size) {
		loc.add(size, high, size);
		return loc;
	}

	public static ArrayList<Location> getLocations(ArrayList<Block> blocks) {
		ArrayList<Location> locations = new ArrayList<Location>();
		for (Block block : blocks) {
			locations.add(block.getLocation());
		}
		return locations;
	}

	public static ArrayList<Location> getLocations(Block block1, Block block2) {
		return getLocations(block1.getLocation(), block2.getLocation());
	}

	public static ArrayList<Location> getLocations(Location location1, Location location2) {
		Location min = getMinLocation(location1, location2);
		Location max = getMaxLocation(location1, location2);
		ArrayList<Location> locations = new ArrayList<Location>();
		for (double x = min.getX(); x <= max.getX(); x += 1.0D) {
			for (double y = min.getY(); y <= max.getY(); y += 1.0D) {
				for (double z = min.getZ(); z <= max.getZ(); z += 1.0D) {
					Location loc = new Location(min.getWorld(), x, y, z);
					if (!locations.contains(loc)) {
						locations.add(loc);
					}
				}
			}
		}
		return locations;
	}

	public static Location getLowLocation(Location loc, int low, int size) {
		loc.subtract(size, low, size);
		return loc;
	}

	public static Location getMaxLocation(Location loc1, Location loc2) {
		double x = Math.max(loc1.getX(), loc2.getX());
		double y = Math.max(loc1.getY(), loc2.getY());
		double z = Math.max(loc1.getZ(), loc2.getZ());
		return new Location(loc1.getWorld(), x, y, z);
	}

	public static String getMessage(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static Location getMinLocation(Location loc1, Location loc2) {
		double x = Math.min(loc1.getX(), loc2.getX());
		double y = Math.min(loc1.getY(), loc2.getY());
		double z = Math.min(loc1.getZ(), loc2.getZ());
		return new Location(loc1.getWorld(), x, y, z);
	}

	public static ArrayList<Player> getOnlinePlayers() {
		ArrayList<Player> list = new ArrayList<Player>();
		try {
			Class<?> bukkit = Class.forName("org.bukkit.Bukkit");
			Method getOnlinePlayers = bukkit.getMethod("getOnlinePlayers", new Class[0]);
			Object object = getOnlinePlayers.invoke(bukkit, new Object[0]);
			if ((object instanceof ArrayList)) {
				ArrayList<?> players = (ArrayList<?>) object;
				for (Object obj : players)
					if ((obj instanceof Player)) {
						Player p = (Player) obj;
						list.add(p);
					}
			} else {
				Player p;
				if ((object instanceof Collection)) {
					Collection<?> players = (Collection<?>) object;
					for (Object obj : players) {
						if ((obj instanceof Player)) {
							p = (Player) obj;
							list.add(p);
						}
					}
				} else if ((object instanceof Player[])) {
					Player[] players = (Player[]) object;
					for (Player player : players) {

						list.add(player);

					}
				}
			}
		} catch (Exception localException) {
		}

		return list;
	}

	public static double getRandomDouble(double minValue, double maxValue) {
		double min = Math.min(minValue, maxValue);
		double max = Math.max(minValue, maxValue);
		Random random = new Random();
		return min + (max - min) * random.nextDouble();
	}

	public static int getRandomInt(int minValue, int maxValue) {
		int min = Math.min(minValue, maxValue);
		int max = Math.max(minValue, maxValue);
		Random random = new Random();
		return min + random.nextInt(max - min + 1);
	}

	public static String getText(int size, String text) {
		return text.length() > size ? text.substring(0, size) : text;
	}

	public static String getText(String text) {
		return getText(16, text);
	}

	public static boolean isSimiliarLocation(Location location1, Location location2) {
		return getBlockLocation(location1).equals(getBlockLocation(location2));
	}

	public static boolean toBoolean(Object obj) {
		if (obj == null)
			return false;
		if ((obj instanceof Boolean))
			return ((Boolean) obj).booleanValue();
		try {
			return Boolean.valueOf(obj.toString()).booleanValue();
		} catch (Exception e) {
		}
		return false;
	}

	public static byte toByte(Object obj) {
		if (obj == null)
			return 0;
		if ((obj instanceof Byte)) {
			return ((Byte) obj).byteValue();
		}

		if ((obj instanceof Number)) {
			Number number = (Number) obj;
			return number.byteValue();
		}
		try {
			return Byte.valueOf(obj.toString()).byteValue();
		} catch (Exception e) {
		}
		return 0;
	}

	public static double toDouble(Object obj) {
		if (obj == null)
			return 0.0D;
		if ((obj instanceof Double))
			return ((Double) obj).doubleValue();
		if ((obj instanceof Number)) {
			Number number = (Number) obj;
			return number.doubleValue();
		}
		try {
			return Double.valueOf(obj.toString()).doubleValue();
		} catch (Exception e) {
		}
		return 0.0D;
	}

	public static float toFloat(Object obj) {
		if (obj == null)
			return 0.0F;
		if ((obj instanceof Float)) {
			return ((Float) obj).floatValue();
		}

		if ((obj instanceof Number)) {
			Number number = (Number) obj;
			return number.floatValue();
		}
		try {
			return Float.valueOf(obj.toString()).floatValue();
		} catch (Exception e) {
		}
		return 0.0F;
	}

	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		if ((obj instanceof Integer))
			return ((Integer) obj).intValue();
		if ((obj instanceof Number)) {
			Number number = (Number) obj;
			return number.intValue();
		}
		try {
			return Integer.valueOf(obj.toString()).intValue();
		} catch (Exception e) {
		}
		return 0;
	}

	public static long toLong(Object obj) {
		if (obj == null)
			return 0L;
		if ((obj instanceof Byte)) {
			return ((Long) obj).longValue();
		}

		if ((obj instanceof Number)) {
			Number number = (Number) obj;
			return number.longValue();
		}
		try {
			return Long.valueOf(obj.toString()).longValue();
		} catch (Exception e) {
		}
		return 0L;
	}

	public static short toShort(Object obj) {
		if (obj == null)
			return 0;
		if ((obj instanceof Short)) {
			return ((Short) obj).shortValue();
		}

		if ((obj instanceof Number)) {
			Number number = (Number) obj;
			return number.shortValue();
		}
		try {
			return Short.valueOf(obj.toString()).shortValue();
		} catch (Exception e) {
		}
		return 0;
	}

	public static String toString(Object obj) {
		if (obj == null)
			return "";
		if ((obj instanceof String))
			return (String) obj;
		return obj.toString();
	}

	public Vector getVector(Location loc1, Location loc2) {
		double distance = loc1.distance(loc2);
		double x = (1.0D + 0.07D * distance) * (loc1.getX() - loc2.getX()) / distance;
		double y = (1.0D + 0.07D * distance) * (loc1.getY() - loc2.getY()) / distance;
		double z = (1.0D + 0.07D * distance) * (loc1.getZ() - loc2.getZ()) / distance;
		return new Vector(x, y, z);
	}
}
