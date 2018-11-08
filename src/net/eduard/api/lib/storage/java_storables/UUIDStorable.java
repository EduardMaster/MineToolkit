package net.eduard.api.lib.storage.java_storables;

import java.util.UUID;

import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.StorageAttributes;

@StorageAttributes(inline = true)
public class UUIDStorable implements Storable {

	@Override
	public Object restore(Object object) {
		try {
			return UUID.fromString(object.toString());
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Object store(Object object) {
		return object.toString();
	}

}
