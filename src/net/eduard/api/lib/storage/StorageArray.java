package net.eduard.api.lib.storage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StorageArray extends StorageBase {

	public StorageArray(Class<?> arrayType, boolean asReference) {
		super(arrayType, asReference);
	}

	@Override
	public Object restore(Object data) {

		Object array = null;
		Class<?> arrayType = getType();
		if (data instanceof List) {
			List<?> list = (List<?>) data;
			array = Array.newInstance(arrayType, list.size());
			int index = 0;
			for (Object item : list) {
				Array.set(array, index, new StorageObject(arrayType, isReference()).restore(item));
				index++;
			}

		} else if (data instanceof Map) {
			Map<?, ?> mapa = (Map<?, ?>) data;
			array = Array.newInstance(arrayType, mapa.size());
			int index = 0;
			for (Object item : mapa.values()) {
				Array.set(array, index, new StorageObject(arrayType, isReference()).restore(item));
				index++;
			}
		} 

		return array;
	}

	@Override
	public Object store(Object data) {

		Storable store = getStore(getType());
		String alias = getAlias(getType());
		int arraySize = Array.getLength(data);
		List<Object> newList = new ArrayList<>();
		for (int index = 0; index < arraySize; index++) {
			newList.add(new StorageObject(getType(), isReference()).store(Array.get(data, index)));
		}
		if (store != null) {
			if (!store.saveInline()) {
				Map<String, Object> map = new LinkedHashMap<>();
				for (int index = 0; index < newList.size(); index++) {
					map.put(alias + index, newList.get(index));
				}
				return map;
			}
		}
		return newList;
	}

}
