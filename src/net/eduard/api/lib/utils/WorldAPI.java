package net.eduard.api.lib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;


/**
 * API de controle e manipula§§o de Mundos e Localiza§§es e Cuboids (Uma expecie
 * de Bloco retangular)
 * 
 * @author Eduard
 * @version 1.0
 * @since Lib v1.0 <br> EduardAPI 5.2
 */
public final class WorldAPI {

	/**
	 * Efeito a fazer na localiza§§o
	 * 
	 * @author Eduard
	 *
	 */
	public static interface LocationEffect {

		boolean effect(Location location);
	}

	/**
	 * Ponto de dire§§o usado para fazer um RADAR
	 * 
	 * @author Eduard
	 *
	 */
	public static enum Point {
		N('N'), NE('/'), E('O'), SE('\\'), S('S'), SW('/'), W('L'), NW('\\');

		public final char asciiChar;

		private Point(char asciiChar) {
			this.asciiChar = asciiChar;
		}

		@Override
		public String toString() {
			return String.valueOf(this.asciiChar);
		}

		public String toString(boolean isActive, ChatColor colorActive, String colorDefault) {
			return (isActive ? colorActive : colorDefault) + String.valueOf(this.asciiChar);
		}
	}

	public static boolean equals(Location location1, Location location2) {

		return getBlockLocation1(location1).equals(getBlockLocation1(location2));
	}

	public static boolean equals2(Location location1, Location location2) {
		return location1.getBlock().getLocation().equals(location2.getBlock().getLocation());
	}

	public static List<Location> getLocations(Location location1, Location location2) {
		return getLocations(location1, location2, new LocationEffect() {

			@Override
			public boolean effect(Location location) {
				return true;
			}
		});
	}

	public static Location getHighLocation(Location loc, double high, double size) {

		loc.add(size, high, size);
		return loc;
	}

	public static void copyWorldFolder(File source, File target) {
		try {
			List<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
			if (!ignore.contains(source.getName())) {
				if (source.isDirectory()) {
					if (!target.exists())
						target.mkdirs();
					String files[] = source.list();
					for (String file : files) {
						File srcFile = new File(source, file);
						File destFile = new File(target, file);
						copyWorldFolder(srcFile, destFile);
					}
				} else {
					InputStream in = new FileInputStream(source);
					OutputStream out = new FileOutputStream(target);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = in.read(buffer)) > 0)
						out.write(buffer, 0, length);
					in.close();
					out.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void deleteWorld(String name) {
		World world = Bukkit.getWorld(name);
		if (world != null) {
			for (Player p : world.getPlayers()) {
				try {
					p.teleport(
							Bukkit.getWorlds().get(0).getSpawnLocation().setDirection(p.getLocation().getDirection()));

				} catch (Exception e) {
					p.kickPlayer("§cRestarting Server!");
				}
			}

		}
		Bukkit.unloadWorld(name, true);
		deleteFolder(new File(Bukkit.getWorldContainer(), name.toLowerCase()));
	}

	public static void deleteFolder(File file) {
		if (file.exists()) {
			File files[] = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteFolder(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
	}

	public static void copyWorld(String worldName, String name) {
		World world = Bukkit.getWorld(worldName);
		world.save();
		copyWorldFolder(world.getWorldFolder(), getWorldFolder(name));
		WorldCreator copy = new WorldCreator(name);
		copy.copy(world);
		copy.createWorld();
	}

	public static World loadWorld(String name) {
		return new WorldCreator(name).createWorld();
	}

	public static File getWorldFolder(String name) {
		return new File(Bukkit.getWorldContainer(), name.toLowerCase());
	}

	public static Location getHighLocation(Location loc1, Location loc2) {

		double x = Math.max(loc1.getX(), loc2.getX());
		double y = Math.max(loc1.getY(), loc2.getY());
		double z = Math.max(loc1.getZ(), loc2.getZ());
		return new Location(loc1.getWorld(), x, y, z);
	}

	public static List<Location> getLocations(Location location1, Location location2, LocationEffect effect) {

		Location min = getLowLocation(location1, location2);
		Location max = getHighLocation(location1, location2);
		List<Location> locations = new ArrayList<>();
		for (double x = min.getX(); x <= max.getX(); x++) {
			for (double y = min.getY(); y <= max.getY(); y++) {
				for (double z = min.getZ(); z <= max.getZ(); z++) {
					Location loc = new Location(min.getWorld(), x, y, z);
					try {
						boolean r = effect.effect(loc);
						if (r) {
							try {
								locations.add(loc);
							} catch (Exception ex) {
							}
						}
					} catch (Exception ex) {
					}

				}
			}
		}
		return locations;

	}

	public static Location getLowLocation(Location loc, double low, double size) {

		loc.subtract(size, low, size);
		return loc;
	}

	public static Location getLowLocation(Location location1, Location location2) {
		double x = Math.min(location1.getX(), location2.getX());
		double y = Math.min(location1.getY(), location2.getY());
		double z = Math.min(location1.getZ(), location2.getZ());
		return new Location(location1.getWorld(), x, y, z);
	}

	public static Location getBlockLocation1(Location location) {

		return new Location(location.getWorld(), (int) location.getX(), (int) location.getY(), (int) location.getZ());
	}

	public static Location getBlockLocation2(Location location) {

		return location.getBlock().getLocation();
	}

	public static List<Location> getBox(Location playerLocation, double higher, double lower, double size,
			LocationEffect effect) {
		Location high = getHighLocation(playerLocation.clone(), higher, size);
		Location low = getLowLocation(playerLocation.clone(), lower, size);
		return getLocations(low, high, effect);
	}

	public static List<Location> setBox(Location playerLocation, double higher, double lower, double size,
			Material wall, Material up, Material down, boolean clearInside) {
		return getBox(playerLocation, higher, lower, size, new LocationEffect() {

			@Override
			public boolean effect(Location location) {

				if (location.getBlockY() == playerLocation.getBlockY() + higher) {
					location.getBlock().setType(up);
					return true;
				}
				if (location.getBlockY() == playerLocation.getBlockY() - lower) {
					location.getBlock().setType(down);
					return true;
				}

				if (location.getBlockX() == playerLocation.getBlockX() + size
						|| location.getBlockZ() == playerLocation.getBlockZ() + size
						|| location.getBlockX() == playerLocation.getBlockX() - size
						|| location.getBlockZ() == playerLocation.getBlockZ() - size) {
					location.getBlock().setType(wall);
					return true;
				}
				if (clearInside) {
					if (location.getBlock().getType() != Material.AIR)
						location.getBlock().setType(Material.AIR);
				}
				return false;
			}
		});
	}

	public static List<Location> getBox(Location playerLocation, double higher, double lower, double size) {
		return getBox(playerLocation, higher, lower, size, new LocationEffect() {

			@Override
			public boolean effect(Location location) {
				return true;
			}
		});
	}

	public static List<Location> getBox(Location playerLocation, double xHigh, double xLow, double zHigh, double zLow,
			double yLow, double yHigh) {
		Location low = playerLocation.clone().subtract(xLow, yLow, zLow);
		Location high = playerLocation.clone().add(xHigh, yHigh, zHigh);
		return getLocations(low, high);
	}

	public static Location getRandomPosition(Location location, int xVar, int zVar) {
		return getHighPosition(getRandomLocation(location, xVar, 0, zVar));

	}

	public static void unloadWorld(String name) {
		try {
			unloadWorld(Bukkit.getWorld(name));
		} catch (Exception ex) {
		}
	}

	public static double distanceX(Location loc1, Location loc2) {
		return loc1.getX() - loc2.getX();
	}

	public static double distanceZ(Location loc1, Location loc2) {
		return loc1.getZ() - loc2.getZ();
	}

	public static void unloadWorld(World world) {
		try {
			Bukkit.getServer().unloadWorld(world, false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	public static int getRandomInt(int minValue, int maxValue) {

		int min = Math.min(minValue, maxValue),
				max = Math.max(minValue, maxValue);
		return min + new Random().nextInt(max - min + 1);
	}
	public static Location getRandomLocation(Location location, int xVar, int yVar, int zVar) {
		int x = location.getBlockX();
		int z = location.getBlockZ();
		int y = location.getBlockY();
		int xR = getRandomInt(x - xVar, x + xVar);
		int zR = getRandomInt(z - zVar, z + zVar);
		int yR = getRandomInt(y - yVar, y + zVar);
		return new Location(location.getWorld(), xR, yR, zR);
	}

	public static Location getHighPosition(Location location) {
		return location.getWorld().getHighestBlockAt(location).getLocation();
	}

	public static Location getSpawn() {
		return Bukkit.getWorlds().get(0).getSpawnLocation();
	}

	public static Point getCompassPointForDirection(double inDegrees) {
		double degrees = (inDegrees - 180.0D) % 360.0D;
		if (degrees < 0.0D) {
			degrees += 360.0D;
		}

		if ((0.0D <= degrees) && (degrees < 22.5D))
			return Point.N;
		if ((22.5D <= degrees) && (degrees < 67.5D))
			return Point.NE;
		if ((67.5D <= degrees) && (degrees < 112.5D))
			return Point.E;
		if ((112.5D <= degrees) && (degrees < 157.5D))
			return Point.SE;
		if ((157.5D <= degrees) && (degrees < 202.5D))
			return Point.S;
		if ((202.5D <= degrees) && (degrees < 247.5D))
			return Point.SW;
		if ((247.5D <= degrees) && (degrees < 292.5D))
			return Point.W;
		if ((292.5D <= degrees) && (degrees < 337.5D))
			return Point.NW;
		if ((337.5D <= degrees) && (degrees < 360.0D)) {
			return Point.N;
		}
		return null;
	}

	public static ArrayList<String> getAsciiCompass(Point point, ChatColor colorActive, String colorDefault) {
		ArrayList<String> ret = new ArrayList<>();

		String row = "";
		row = row + Point.NW.toString(Point.NW == point, colorActive, colorDefault);
		row = row + Point.N.toString(Point.N == point, colorActive, colorDefault);
		row = row + Point.NE.toString(Point.NE == point, colorActive, colorDefault);
		ret.add(row);

		row = "";
		row = row + Point.W.toString(Point.W == point, colorActive, colorDefault);
		row = row + colorDefault + "+";
		row = row + Point.E.toString(Point.E == point, colorActive, colorDefault);
		ret.add(row);

		row = "";
		row = row + Point.SW.toString(Point.SW == point, colorActive, colorDefault);
		row = row + Point.S.toString(Point.S == point, colorActive, colorDefault);
		row = row + Point.SE.toString(Point.SE == point, colorActive, colorDefault);
		ret.add(row);

		return ret;
	}

	public static ArrayList<String> getAsciiCompass(double inDegrees, ChatColor colorActive, String colorDefault) {
		return getAsciiCompass(getCompassPointForDirection(inDegrees), colorActive, colorDefault);
	}

}
