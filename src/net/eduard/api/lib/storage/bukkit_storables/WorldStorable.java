package net.eduard.api.lib.storage.bukkit_storables;

import org.bukkit.Bukkit;
import org.bukkit.World;

import net.eduard.api.lib.storage.Storable;

public class WorldStorable implements Storable {

	@Override
	public boolean saveInline() {
		return true;
	}

	@Override
	public Class<?> type() {
		return World.class;
	}

	@Override
	public Object restore(Object object) {
		if (object instanceof String) {
			String world = (String) object;
			return Bukkit.getWorld(world);

		}
		return null;
	}

	@Override
	public Object store(Object object) {
		if (object instanceof World) {
			World world = (World) object;
			return world.getName();

		}
		return null;
	}

}
