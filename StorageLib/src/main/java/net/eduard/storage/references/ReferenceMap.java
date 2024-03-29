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
		/**
		 * Dia 13/11/2021 Ta com erro na recuperacao do HashMap Referenciado
		 */
		for (Entry<Object,Object> entry : getRestore().entrySet()){
			Object realKey = entry.getKey();

			// Desativando suporte a Key de HashMap ser Referencia
			//realKey = StorageAPI.getObjectByKey(keyInfo.getType(), entry.getKey());

			Object realValue = StorageAPI.getObjectByKey(valueInfo.getType(), entry.getValue());
			realMap.put(realKey, realValue);

		}

	}


}
