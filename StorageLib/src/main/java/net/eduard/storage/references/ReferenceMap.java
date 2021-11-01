package net.eduard.storage.references;

import net.eduard.api.lib.storage.StorageAPI;
import net.eduard.storage.api.StorageInfo;

import java.util.Map;
import java.util.Map.Entry;

public class ReferenceMap extends ReferenceBase<Map<Object,Object>> {

	private Map<Object,Object> realMap;
	private StorageInfo keyInfo;
	private StorageInfo valueInfo;
	public ReferenceMap(StorageInfo mapInfo, StorageInfo keyInfo, StorageInfo valueInfo, Map<Object,Object> references,
						Map<Object,Object> realMap) {
		super(mapInfo, null);
		setRestore(references);
		this.realMap = realMap;
	}

	@Override
	public void update() {
		for (Entry<Object,Object> entry : getRestore().entrySet()){
			Object realKey = StorageAPI.getObjectByKey(keyInfo.getType(), entry.getKey());
			Object realValue = StorageAPI.getObjectByKey(valueInfo.getType(), entry.getValue());
			realMap.put(realKey, realValue);

		}

	}


}
