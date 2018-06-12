package net.eduard.api.lib.storage;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.eduard.api.lib.Extra;
import net.eduard.api.lib.storage.references.ReferenceMap;

public class StorageMap extends StorageBase {

	private Class<?> keyType;
	private Class<?> valueType;

	public StorageMap(Class<?> keyType, Class<?> valueType, boolean asReference) {
		super( Map.class, asReference);
		this.keyType = keyType;
		this.valueType = valueType;
		// debug("KeyType "+keyType);
		// debug("ValueType "+valueType);
	}

	public StorageMap(Field field, Object instance, boolean asReference) {
		super(field, instance, Map.class, asReference);
		this.keyType = Extra.getTypeKey(field.getGenericType());
		this.valueType = Extra.getTypeValue(field.getGenericType());
		// debug("KeyType "+keyType);
		// debug("ValueType "+valueType);
	}

	public Class<?> getKeyType() {
		return keyType;
	}

	@Override
	public void debug(String msg) {
		super.debug("MAP " + msg);
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
		if (data == null)
			return null;
		if (isReference()) {
			if (data instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> oldMap = (Map<String, Object>) data;
				Map<Object, Integer> newMap = new HashMap<>();
				for (Entry<String, Object> entry : oldMap.entrySet()) {

					newMap.put(new StorageObject(keyType, false).restore(entry.getKey()),
							(Integer) new StorageObject(valueType, true).restore(entry.getValue()));
				}
				StorageAPI.newReference(new ReferenceMap(newMap, getField(), getInstance()));
				debug("Restoring refereced map");
			}
			return null;
		}
		Map<Object, Object> newMap = new HashMap<>();
		if (data instanceof Map) {
			
			Map<?, ?> map = (Map<?, ?>) data;
			for (Entry<?, ?> entry : map.entrySet()) {

				Object key = new StorageObject(keyType, isReference()).restore(entry.getKey());
				Object value = new StorageObject(valueType, isReference()).restore(entry.getValue());
				debug("put " + key + " " + value);
				newMap.put(key, value);
			}
		} 
		return newMap;
	}

	@Override
	public Object store(Object data) {

		Map<String, Object> newMap = new HashMap<>();
		Map<?, ?> map = (Map<?, ?>) data;
		for (Entry<?, ?> entry : map.entrySet()) {
			String key = new StorageObject(keyType, isReference()).store(entry.getKey()).toString();
			Object value = new StorageObject(valueType, isReference()).store(entry.getValue());
			newMap.put(key, value);
		}
		return newMap;
	}

}
