package net.eduard.api.tutorial.nivel_5;

import java.sql.ResultSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import net.eduard.api.setup.manager.DBManager;

public class UsarWarpsDB extends DBManager {
 
	public UsarWarpsDB(String user, String pass, String host,
			String database) {
		super(user, pass, host, database);
		update("create table if not exists warps(name varchar(11) not null unique,"
				+ "x double, y double , z double , yaw double , pitch double,world varchar(11)");

	}
	public void setWarp(Location location, String name) {
		name = name.toLowerCase();
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		float pitch = location.getPitch();
		float yaw = location.getYaw();
		String world = location.getWorld().getName();
		if (hasWarp(name)) {
			update("update warps set x = ?, y = ?, z = ? , yaw = ? , pitch =  ?,world = ? where name = ?",
					x, y, z, yaw, pitch, world, name);
		} else {
			update("insert into warps values (?,?,?,?,?,?,?)", name, x, y, z,
					yaw, pitch, world);
		}
	}
	public Location getWarp(String name) {
		name = name.toLowerCase();
		Location location = null;
		try {
			ResultSet rs = select("select * from warps where name = ?", name);
			if (rs.next()) {
				double x = rs.getDouble("x");
				double y = rs.getDouble("y");
				double z = rs.getDouble("z");
				float yaw = rs.getFloat("yaw");
				float pitch = rs.getFloat("pitch");
				World world = Bukkit.getWorld(rs.getString("world"));
				location = new Location(world, x, y, z, yaw, pitch);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;

		//
	}
	public boolean hasWarp(String name) {
		return contains("select name from warps where name = ?", name.toLowerCase());
	}

}
