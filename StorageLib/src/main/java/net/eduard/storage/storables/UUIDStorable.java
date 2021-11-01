package net.eduard.storage.storables;

import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.annotations.StorageAttributes;

import java.util.UUID;


@StorageAttributes(inline = true)
public class UUIDStorable implements Storable<UUID> {

	public UUID restore(String object) {
		try {
			return UUID.fromString(object);
		} catch (Exception e) {
			return null;
		}
	}

	public String store(UUID object) {
		return object.toString();
	}

}
