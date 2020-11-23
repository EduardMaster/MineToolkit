package net.eduard.api.lib.storage.java_storables;

import java.util.UUID;

import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.annotations.StorageAttributes;


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
