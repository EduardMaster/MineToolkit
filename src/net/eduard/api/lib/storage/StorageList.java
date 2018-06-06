package net.eduard.api.lib.storage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StorageList extends StorageAbstract {

	private Class<?> listType;

	public StorageList(Class<?> listType, boolean asReference) {
		super(List.class, asReference);

	}

	@Override
	public Object restore(Object data) {
		if (data == null)
			return null;
		List<Object> newList = new ArrayList<>();
		if (data instanceof List) {
			List<?> list = (List<?>) data;
			for (Object item : list) {
				newList.add(new StorageObject(listType, isReference()).restore(item));
			}
		} else if (data instanceof Map) {
			Map<?, ?> _map = (Map<?, ?>) data;
			for (Object item : _map.values()) {
				newList.add(new StorageObject(listType, isReference()).restore(item));
			}

		}
		return null;
	}

	public Class<?> getListType() {
		return listType;
	}

	public void setListType(Class<?> listType) {
		this.listType = listType;
	}

	@Override
	public Object store(Object data) {
		List<Object> newList = new ArrayList<>();
		List<?> list = (List<?>)data;
		Storable store = getStore(listType);
		for (Object item : list ) {
			newList.add(new StorageObject(listType, isReference()).store(item));;
		}
		if (store != null) {
//			String alias = getAlias(listType);
			if (!store.saveInline()) {
				Map<String, Object> map = new LinkedHashMap<>();
				for (int index = 1; index <= newList.size(); index++) {
					map.put(""+index, newList.get(index));
				}
				return map;
			}
		}
		return newList;
		
	}

}
