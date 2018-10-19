package net.eduard.api.test;

import org.bukkit.Location;
import org.bukkit.World;

import net.eduard.api.EduardAPI;
import net.eduard.api.lib.ConfigAPI;
import net.eduard.api.lib.Mine;

public class TestLocationStorable {

	public static void test() {
		World wordlVasio = Mine.newEmptyWorld("mundovasio");
		
		ConfigAPI c = new ConfigAPI("testloc.yml", EduardAPI.getInstance());
		c.set("loc", new Location(wordlVasio,0,5,0));
		c.saveConfig();
		
		c.reloadConfig();
		System.out.println(c.get("loc"));
		
	}
	public static void init() {
		Mine.TIME.timer(20, ()->test());
	
	}
}
