package net.eduard.api.lib.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StorageMap extends StorageAbstract {

	private Class<?> keyType;
	private Class<?> valueType;

	public StorageMap(Class<?> keyType, Class<?> valueType, boolean asReference) {
		super(Map.class, asReference);
		this.keyType = keyType;
		this.valueType = valueType;
	}

	public Class<?> getKeyType() {
		return keyType;
	}

	public void setKeyType(Class<?> keyType) {
		this.keyType = keyType;
	}

	public Class<?> getValueType() {
		return valueType;
	}

	public void setValueType(Class<?> valueType) {
		this.valueType = valueType;
	}

	@Override
	public Object restore(Object data) {
		Map<Object, Object> newMap = new HashMap<>();
		if (data instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) data;
			for (Entry<?, ?> entry : map.entrySet()) {
				Object key = new StorageObject(keyType, false).restore(entry.getKey());
				Object value = new StorageObject(keyType, isReference()).restore(entry.getValue());
				newMap.put(key, value);
			}
		}
		return null;
	}

	@Override
	public Object store(Object data) {
		
		Map<String, Object> newMap = new HashMap<>();
		Map<?,?> map = (Map<?,?>)data;
		for (Entry<?, ?> entry: map.entrySet()) {
			String key = new StorageObject(keyType, isReference()).store(entry.getKey()).toString();
			Object value = new StorageObject(valueType, isReference()).store(entry.getValue());
			newMap.put(key, value);
		}
		return null;
	}

}
