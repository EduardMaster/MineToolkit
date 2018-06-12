package net.eduard.api.lib.storage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.eduard.api.lib.Extra;
import net.eduard.api.lib.storage.references.ReferenceList;

public class StorageList extends StorageBase {

	private Class<?> listType;

	public StorageList(Class<?> listType, boolean asReference) {
		super(null, null, List.class, asReference);
		setListType(listType);
	}

	public StorageList(Field field, Object instance, boolean asReference) {
		super(field, instance, List.class, asReference);
		setListType(Extra.getTypeKey(field.getGenericType()));
	}

	@Override
	public Object restore(Object data) {

		if (data == null)
			return null;

		if (isReference()) {
			if (data instanceof List) {
				List<?> oldList = (List<?>) data;
				List<Integer> newList = new ArrayList<>();
				for (Object item : oldList) {
					newList.add((Integer) new StorageObject(getListType(), true).restore(item));
				}
				StorageAPI.newReference(new ReferenceList(newList, getField(), getInstance()));
				debug("Restoring refereced list");
			}
			return null;

		}

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
		return newList;

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
		List<?> list = (List<?>) data;
		Storable store = getStore(listType);
		for (Object item : list) {
			newList.add(new StorageObject(listType, isReference()).store(item));
			;
		}
		if (store != null) {
			// String alias = getAlias(listType);
			if (!store.saveInline() && !isReference()) {
				Map<String, Object> map = new LinkedHashMap<>();
				for (int index = 1; index <= newList.size(); index++) {
					map.put("" + index, newList.get(index));
				}
				return map;
			}
		}
		return newList;

	}

}
