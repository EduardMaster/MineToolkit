package net.eduard.api.lib.storage.bukkit_storables;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

import net.eduard.api.lib.FakePlayer;
import net.eduard.api.lib.storage.Storable;

public class OfflinePlayerStorable implements Storable {
	public boolean saveInline() {
		return true;
	}

	@Override
	public Object restore(Object object) {
		if (object instanceof String) {
			String id = (String) object;
			String[] split = id.split(";");
			return new FakePlayer(split[0], UUID.fromString(split[1]));

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
