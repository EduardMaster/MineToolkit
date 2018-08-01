package net.eduard.api.lib.storage;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import net.eduard.api.lib.Extra;
import net.eduard.api.lib.storage.references.ReferenceValue;

public class StorageObject extends StorageBase {

	public StorageObject(Class<?> type, boolean asReference) {
		super(type, asReference);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object restore(Object data) {

		if (data == null)
			return null;
		int id = 0;

		if (data instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) data;
			if (map.containsKey(StorageAPI.STORE_KEY)) {
				String text = (String) map.get(StorageAPI.STORE_KEY);
				String[] split = text.split(StorageAPI.REFER_KEY);
				String alias = split[0];
				debug("Resgatando tipo pela alias: " + alias);
				setType(StorageAPI.getClassByAlias(alias));
			

				try {
					id = Extra.toInt(split[1]);
					debug("Resgatando id: " + id);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (getType() == null) {
			return data;
		}

		Class<?> wrapper = Extra.getWrapper(getType());
		if (wrapper != null) {
			try {
				return Extra.getResult(Extra.class, "to" + wrapper.getSimpleName(), new Object[] { Object.class },
						data);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (getType().isEnum()) {
			return new StorageEnum(getType()).restore(data);
		}

		Storable store = StorageAPI.getStore(getType());
		if (store == null) {
			return null;
		}
		if (isReference()) {
			if (data.toString().contains(StorageAPI.REFER_KEY)) {
				return Extra.toInt(data.toString().split(StorageAPI.REFER_KEY)[1]);
			}
			return 0;
		}
		if (store.saveInline()) {
			return store.restore(data);
		}
		if (id == 0) {
			id = StorageAPI.newId();
		}
		Object instance = store.restore((Map<String, Object>) data);
		if (instance == null) {
			instance = store.newInstance();
		}
		Map<?, ?> map = (Map<?, ?>) data;

		debug("Registring " + getAlias(getType()) + StorageAPI.REFER_KEY + id);
		StorageAPI.addObject(id, instance);
		Class<?> claz = getType();
		while (!claz.equals(Object.class)) {

			for (Field field : claz.getDeclaredFields()) {
				// debug("Class: " + field.getType().getSimpleName() + " field: " +
				// field.getName() + " atributes: "
				// + field.getGenericType());
				boolean isReference = field.isAnnotationPresent(Reference.class);
				// Class<?> keyType = Extra.getTypeKey(field.getGenericType());
				// Class<?> valueType = Extra.getTypeValue(field.getGenericType());
				if (Modifier.isStatic(field.getModifiers()))
					continue;
				if (Modifier.isTransient(field.getModifiers()))
					continue;
				field.setAccessible(true);
				try {
					Object fieldMapValue = map.get(field.getName());

					if (fieldMapValue == null)
						continue;
					if (Extra.isList(field.getType())) {
						Object fieldRestored = new StorageList(field, instance, isReference).restore(fieldMapValue);
						field.set(instance, fieldRestored);

					} else if (Extra.isMap(field.getType())) {
						Object fieldRestored = new StorageMap(field, instance, isReference).restore(fieldMapValue);
						field.set(instance, fieldRestored);

					} else {

						Object fieldRestored = new StorageObject(field.getType(), isReference).restore(fieldMapValue);
						if (isReference) {
							StorageAPI.newReference(new ReferenceValue((int) fieldRestored, field, instance));
							debug("Restoring refereced object");
							continue;
						}
						field.set(instance, fieldRestored);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			claz = claz.getSuperclass();
		}

		if (instance instanceof Storable) {
			Storable storable = (Storable) instance;
			storable.restore((Map<String, Object>) map);
		}
		return instance;
	}

	@Override
	public Object store(Object data) {
		if (data == null)
			return null;
		if (getType().isEnum()) {
			return new StorageEnum(getType()).store(data);
		}
		if (getType().isArray()) {
			return new StorageArray(getType().getComponentType(), isReference()).store(data);
		}
		Storable store = getStore(getType());
		if (store == null) {
			return data;
		}
		if (isReference()) {
			return store.alias() + StorageAPI.REFER_KEY + StorageAPI.getIdByObject(data);
		}
		if (store.saveInline()) {
			return store.store(data);
		}
		String alias = StorageAPI.getAlias(getType());
		try {
			Map<String, Object> map = new LinkedHashMap<>();
			Class<?> claz = getType();

			map.put(StorageAPI.STORE_KEY, alias + StorageAPI.REFER_KEY + StorageAPI.getIdByObject(data));
			while (!claz.equals(Object.class)) {

				for (Field field : claz.getDeclaredFields()) {
					field.setAccessible(true);
					// debug("Var2: " + field);
					boolean isReference = field.isAnnotationPresent(Reference.class);
					if (Modifier.isStatic(field.getModifiers()))
						continue;
					if (Modifier.isTransient(field.getModifiers()))
						continue;
					Object value = field.get(data);
					if (value == null) {
						continue;
					}
					Object fieldResult = null;
					if (Extra.isList(field.getType())) {
						fieldResult = new StorageList(Extra.getTypeKey(field.getGenericType()), isReference)
								.store(value);
					} else if (Extra.isMap(field.getType())) {
						fieldResult = new StorageMap(Extra.getTypeKey(field.getGenericType()),
								Extra.getTypeValue(field.getGenericType()), isReference).store(value);
					} else {

						fieldResult = new StorageObject(field.getType(), isReference).store(value);
					}
					if (fieldResult != null)
						map.put(field.getName(), fieldResult);

				}
				claz = claz.getSuperclass();
			}
			if (!(data instanceof Storable)) {
				store.store(map, data);
			} else {
				((Storable) data).store(map, data);
			}
			return map;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

}
