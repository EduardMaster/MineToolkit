package net.eduard.api.lib.storage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StorageArray extends StorageBase {


	@Override
	public Object restore(StorageInfo info,Object data) {

		Object array = null;
		Class<?> arrayType = info.getType();
		StorageInfo arrayInfo = info.clone();
		arrayInfo.setType(arrayType);
		arrayInfo.updateByType();
		arrayInfo.updateByStoreClass();
		if (data instanceof List) {
			List<?> list = (List<?>) data;
			array = Array.newInstance(arrayType, list.size());
			int index = 0;

			for (Object item : list) {
				Array.set(array, index, StorageAPI.STORE_OBJECT.restore(arrayInfo,item));
				index++;
			}

		} else if (data instanceof Map) {
			Map<?, ?> mapa = (Map<?, ?>) data;
			array = Array.newInstance(arrayType, mapa.size());
			int index = 0;
			for (Object item : mapa.values()) {
				Array.set(array, index, StorageAPI.STORE_OBJECT.restore(arrayInfo,item));
				index++;
			}
		}

		return array;
	}

	@Override
	public Object store(StorageInfo info, Object data) {
		Class<?> arrayType = info.getType();
		StorageInfo arrayInfo = info.clone();
		arrayInfo.setType(arrayType);
		arrayInfo.updateByType();
		arrayInfo.updateByStoreClass();
		int arraySize = Array.getLength(data);
		List<Object> newList = new ArrayList<>();
		for (int index = 0; index < arraySize; index++) {
			newList.add(StorageAPI.STORE_OBJECT.store(info,Array.get(data, index)));
		}


		return newList;
	}


}
