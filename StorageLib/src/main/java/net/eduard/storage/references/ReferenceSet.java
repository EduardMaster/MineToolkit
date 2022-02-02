package net.eduard.storage.references;

import net.eduard.api.lib.storage.StorageAPI;
import net.eduard.storage.api.StorageInfo;

import java.util.List;
import java.util.Set;

public class ReferenceSet extends ReferenceBase<Set<Object>> {
	private final Set<Object> realSet;
	public ReferenceSet(StorageInfo info, Set<Object> references, Set<Object> realSet) {
		super(info, null);
		setRestore(references);
		this.realSet = realSet;
	}

	@Override
	public void update() {
		for (Object key : getRestore()) {
			realSet.add(StorageAPI.getObjectByKey(getInfo().getType(), key ));
		}
	}



}
