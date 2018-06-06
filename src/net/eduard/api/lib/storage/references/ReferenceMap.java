package net.eduard.api.lib.storage.references;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ReferenceMap extends ReferenceAbstract{
	private Map<Object,Integer> map;
	public ReferenceMap(Map<Object,Integer> map,Field field, Object instance) {
		super(field, instance);
		setMap(map);
	}

	@Override
	public void update() {
		Map<Object,Object>newMap = new HashMap<>();
		for (Entry<Object, Integer> entry : map.entrySet()) {
			newMap.put(entry.getKey(), getObjectById(entry.getValue()));
		}
		
		try {
			getField().set(getInstance(), newMap);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public Map<Object,Integer> getMap() {
		return map;
	}

	public void setMap(Map<Object,Integer> map) {
		this.map = map;
	}

}
