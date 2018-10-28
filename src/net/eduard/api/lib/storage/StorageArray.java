package net.eduard.api.lib.storage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StorageArray extends StorageBase {

	private Class<?> arrayType;

	public StorageArray(StorageInfo info) {
		super(info);
		setArrayType(getType().getComponentType());
	}

	@Override
	public Object restore(Object data) {

		Object array = null;
		Class<?> arrayType = getType();
		StorageInfo clone = getInfo().clone();
		clone.setType(arrayType);
		StorageObject store = new StorageObject(clone);
		if (data instanceof List) {
			List<?> list = (List<?>) data;
			array = Array.newInstance(arrayType, list.size());
			int index = 0;

			for (Object item : list) {
				Array.set(array, index, store.restore(item));
				index++;
			}

		} else if (data instanceof Map) {
			Map<?, ?> mapa = (Map<?, ?>) data;
			array = Array.newInstance(arrayType, mapa.size());
			int index = 0;
			for (Object item : mapa.values()) {
				Array.set(array, index, store.restore(item));
				index++;
			}
		}

		return array;
	}

	@Override
	public Object store(Object data) {
		StorageInfo clone = getInfo().clone();
		clone.setType(arrayType);
		StorageObject store = new StorageObject(clone);
//		System.out.println("Array porra "+data);
//		Storable store = getStore(getType());
//		String alias = getAlias(getType());
		int arraySize = Array.getLength(data);
		List<Object> newList = new ArrayList<>();
//		
		for (int index = 0; index < arraySize; index++) {
			newList.add(store.store(Array.get(data, index)));
		}
		if (store.isStorable()) {
			if (!store.isInline()) {
				Map<String, Object> map = new LinkedHashMap<>();
				for (int index = 0; index < newList.size(); index++) {
					map.put(getAlias() + index, newList.get(index));
				}
				return map;
			}
		}
//		if (store != null) {
//			if (!isInline()) {
//				
//			}
//		}
		return null;
	}

	public Class<?> getArrayType() {
		return arrayType;
	}

	public void setArrayType(Class<?> arrayType) {
		this.arrayType = arrayType;
	}

}
