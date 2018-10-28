package net.eduard.api.lib.storage.bukkit_storables;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.eduard.api.lib.storage.Storable;

public class LocationStorable implements Storable {


	@Override
	public Object newInstance() {
		return new Location(Bukkit.getWorlds().get(0), 1, 1, 1);
	}
	

}
