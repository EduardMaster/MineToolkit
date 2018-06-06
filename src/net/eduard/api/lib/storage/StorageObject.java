package net.eduard.api.lib.storage;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.World;

import net.eduard.api.lib.Extra;
import net.eduard.api.lib.storage.StorageAPI.Reference;

public class StorageObject extends StorageAbstract {

	public StorageObject(Class<?> type, boolean asReference) {
		super(type, asReference);
	}

	@Override
	public Object restore(Object data) {
		int id = 0;
		if (getType() == null) {
			if (data instanceof Map) {
				Map<?, ?> map = (Map<?, ?>) data;
				if (map.containsKey(StorageAPI.STORE_KEY)) {
					String text = (String) map.get(StorageAPI.STORE_KEY);
					String[] split = text.split(StorageAPI.REFER_KEY);
					String alias = split[0];
					setType(StorageAPI.getClassByAlias(alias));

					try {
						id = Extra.toInt(split[1]);
					} catch (Exception e) {
					}

				}
			} else
				return data;

		}
		if (Extra.isWrapper(getType())) {
			Class<?> wrapper = Extra.getWrapper(getType());
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
		if (store.saveInline()) {
			return store.restore(data);
		}
		if (id == 0) {
			id = StorageAPI.newId();
		}
		Object instance = store.newInstance();
		Map<?, ?> map = (Map<?, ?>) data;

		StorageAPI.objects.put(id, instance);
		Class<?> claz = getType();
		while (!claz.equals(Object.class)) {

			for (Field field : claz.getDeclaredFields()) {
				boolean isReference = field.isAnnotationPresent(Reference.class);
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
						return new StorageList(Extra.getTypeKey(field.getGenericType()), isReference)
								.restore(fieldMapValue);
					}
					if (Extra.isMap(field.getType())) {
						return new StorageMap(Extra.getTypeKey(field.getGenericType()),
								Extra.getTypeValue(field.getGenericType()), isReference).restore(fieldMapValue);
					}

					Object fieldRestored = new StorageObject(field.getType(), isReference).restore(fieldMapValue);
					if (field.getType() == World.class) {
						System.out.println("é world "+fieldRestored);
					}
					field.set(instance, fieldRestored);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			claz = claz.getSuperclass();
		}

		if (instance instanceof Storable) {
			Storable storable = (Storable) instance;
			storable.restore(map);
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
					boolean isReference = field.isAnnotationPresent(Reference.class);
					if (Modifier.isStatic(field.getModifiers()))
						continue;
					if (Modifier.isTransient(field.getModifiers()))
						continue;
					Object value = field.get(data);
					if (value == null) {
						continue;
					}
					if (Extra.isList(field.getType())) {
						return new StorageList(Extra.getTypeKey(field.getGenericType()), isReference).store(value);
					}
					if (Extra.isMap(field.getType())) {
						return new StorageMap(Extra.getTypeKey(field.getGenericType()), Extra.getTypeValue(getType()),
								isReference).store(value);
					}

					Object fieldResult = new StorageObject(field.getType(), isReference).store(value);
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
