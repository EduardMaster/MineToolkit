package net.eduard.api.lib.storage.api;

import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.StorageAPI;

public abstract class StorageBase<DataType, StorageType> {

	public abstract DataType restore(StorageInfo info, StorageType data);

	public abstract StorageType store(StorageInfo info, DataType data);

	public static void debug(String msg) {
		StorageAPI.debug(msg);
	}

	public Storable<?> getStore(Class<?> claz) {
		return StorageAPI.getStore(claz);
	}

	public String getAlias(Class<?> claz) {
		return StorageAPI.getAlias(claz);
	}
}
