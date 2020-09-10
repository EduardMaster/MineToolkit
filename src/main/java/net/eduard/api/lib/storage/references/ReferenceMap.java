package net.eduard.api.lib.storage.references;

import java.util.Map;
import java.util.Map.Entry;

import net.eduard.api.lib.storage.StorageAPI;

public class ReferenceMap extends ReferenceBase<Map<Object,Integer>>{

	public ReferenceMap(Map<Object,Integer> map, Object instance) {
		super(map,null, instance);

	}


	public void update() {
		@SuppressWarnings("unchecked")
		Map<Object,Object>newMap =  (Map<Object, Object>) getInstance();
		for (Entry<Object, Integer> entry : getRestore().entrySet()) {
			newMap.put(entry.getKey(), StorageAPI.getObjectById(entry.getValue()));
		}
		
		
	}


}
