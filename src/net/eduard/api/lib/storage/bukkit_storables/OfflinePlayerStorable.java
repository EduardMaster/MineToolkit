package net.eduard.api.lib.storage.bukkit_storables;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

import net.eduard.api.lib.modules.FakePlayer;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.StorageAttributes;
@StorageAttributes(inline=true)
public class OfflinePlayerStorable implements Storable {
	
	@Override
	public Object newInstance() {
		return new FakePlayer();
	}

	@Override
	public Object restore(Object object) {
		if (object instanceof String) {
			String id = (String) object;
			if (id.contains(";")) {
				String[] split = id.split(";");
				return new FakePlayer(split[0], UUID.fromString(split[1]));

			}
		}
		return null;
	}

	@Override
	public Object store(Object object) {
		if (object instanceof OfflinePlayer) {
			OfflinePlayer p = (OfflinePlayer) object;
			return p.getName() + ";" + p.getUniqueId().toString();

		}
		return null;
	}

}
