package net.eduard.api.lib.storage.references;

import java.util.List;

import net.eduard.api.lib.storage.StorageAPI;
import net.eduard.api.lib.storage.api.StorageInfo;

public class ReferenceList extends ReferenceBase<List<Object>> {
	private final List<Object> realList;
	public ReferenceList(StorageInfo info, List<Object> references, List<Object> realList) {
		super(info, null);
		setRestore(references);
		this.realList = realList;
	}

	@Override
	public void update() {
		for (Object key : getRestore()) {
			realList.add(StorageAPI.getObjectByKey(getInfo().getType(), key ));
		}

	}



}
