package net.eduard.api.lib.storage;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.references.ReferenceValue;

public class StorageObject extends StorageBase {

	public StorageObject(StorageInfo info) {
		super(info);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object restore(Object data) {
		Class<?> claz = getType();
		if (data == null) {
			debug(">> DATA NULL");
			return null;
		}
		int id = 0;
		String alias = null;
		if (data instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) data;
			if (map.containsKey(StorageAPI.STORE_KEY)) {
				String text = (String) map.get(StorageAPI.STORE_KEY);
				debug(">> DETECTION OF TYPE " + text);
				if (text.contains(StorageAPI.REFER_KEY)) {
					String[] split = text.split(StorageAPI.REFER_KEY);
					alias = split[0];
//					debug("Alias: " + alias);
//					System.out.println(StorageAPI.getAliases());
					claz = StorageAPI.getClassByAlias(alias);
//					debug("Class: " + getType());
					try {
						id = Extra.toInt(split[1]);
//						debug("Id: " + id);
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					claz = StorageAPI.getClassByAlias(text);
				}
				debug(">> TYPE " + claz);
				debug(">> ALIAS " + alias);
				debug(">> ID " + id);

			} else {
			}
		}
		if (claz == null) {
			debug(">> CLASS NULL ");
			return data;
		}
		alias = StorageAPI.getAlias(claz);
		Storable store = StorageAPI.getStore(claz);

		Class<?> wrapper = Extra.getWrapper(claz);

		if (wrapper != null) {
			try {
				debug(">> BASIC TYPE " + data);
				return Extra.getResult(Extra.class, "to" + wrapper.getSimpleName(), new Object[] { Object.class },
						data);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (Extra.isList(claz)) {
			debug(">> LIST " + data);
			return new StorageList(getInfo().clone()).restore(data);
		}
		if (Extra.isMap(claz)) {
			debug(">> MAP " + data);
			return new StorageMap(getInfo().clone()).restore(data);
		}
		if (claz.isEnum()) {
			debug(">> ENUM " + data);
			return new StorageEnum(getInfo().clone()).restore(data);
		}

		if (isReference()) {
			if (data.toString().contains(StorageAPI.REFER_KEY)) {
				debug(">> REFERENCE " + data);
				return (int) Extra.toInt(data.toString().split(StorageAPI.REFER_KEY)[1]);
			}
			return null;
		}

		if (store == null) {
			debug(">> NULL STORABLE ");
			return null;
		}
//		if (claz.equals(UUID.class)) {
//			debug("=========== "+isInline());
//		}
		if (isInline()) {
			Object result = store.restore(data);
			if (result == null) {
				debug(">> INLINE  " + data);
				return new StorageInline(getInfo().clone()).restore(data);
			} else {

				debug(">> INLINE CUSTOM " + result);
				return result;
			}
		}
		if (id == 0) {
			id = StorageAPI.newId();
		}
		Object instance = store.restore((Map<String, Object>) data);
		if (instance == null) {
			try {
				instance = store.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			debug(">> NEW INSTANCE");

		} else {
			debug(">> RESTORED INSTANCE");
		}
		Map<?, ?> map = (Map<?, ?>) data;

//		debug("^^ " + alias + StorageAPI.REFER_KEY + id);
		if (isIndentifiable())
			StorageAPI.registerObject(id, instance);
		while (!claz.equals(Object.class)) {

			for (Field field : claz.getDeclaredFields()) {
				// debug("Class: " + field.getType().getSimpleName() + " field: " +
				// field.getName() + " atributes: "
				// + field.getGenericType());
				// Class<?> keyType = Extra.getTypeKey(field.getGenericType());
				// Class<?> valueType = Extra.getTypeValue(field.getGenericType());
				if (Modifier.isStatic(field.getModifiers()))
					continue;
				if (Modifier.isTransient(field.getModifiers()))
					continue;
				if (Modifier.isFinal(field.getModifiers())) {
					continue;
				}
				field.setAccessible(true);

				try {

					Object fieldMapValue = map.get(field.getName());

					if (fieldMapValue == null)
						continue;
					StorageObject storage = new StorageObject(getInfo().clone());
					storage.setField(field);
					storage.setType(field.getType());
					storage.update(field, field.getType());

//					
//					if (Extra.isList(field.getType())) {
//						Object fieldRestored = new StorageList(getInfo().clone()).restore(fieldMapValue);
//						field.set(instance, fieldRestored);
//
//					} else if (Extra.isMap(field.getType())) {
//						Object fieldRestored = new StorageMap(field, instance, reference).restore(fieldMapValue);
//						field.set(instance, fieldRestored);
//
//					} else {

					Object restoredValue = storage.restore(fieldMapValue);
					if (storage.isReference()) {
						if (restoredValue != null) {
							if (restoredValue instanceof Integer) {
								Integer indetification = (Integer) restoredValue;
								StorageAPI.newReference(new ReferenceValue(indetification, field, instance));

								continue;
							}
						}
					}
					debug(">> " + field.getName() + " " + field.getType().getSimpleName());

//					if (fieldRestored == null)
//						continue;
					try {
						field.set(instance, restoredValue);

					} catch (Exception e) {
						debug(">> FAILED TO SET VARIABLE " + field.getName() + " TO " + restoredValue);
					}

//					}
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
		Class<?> claz = data.getClass();

		String alias = StorageAPI.getAlias(claz);
		if (claz.isEnum()) {
			debug("<< ENUM " + data);
			return new StorageEnum(getInfo().clone()).store(data);
//			return new StorageEnum(getField(), getInstance(), getType(), isReference(), isInline(), isIndentifiable())
//					.store(data);
		}
		if (claz.isArray()) {
			debug("<< ARRAY " + data);
			return new StorageArray(getInfo().clone()).store(data);
		}
		if (Extra.isList(claz)) {
			debug("<< LIST " + data);

			return new StorageList(getInfo().clone()).store(data);
		}
		if (Extra.isMap(claz)) {
			return new StorageMap(getInfo().clone()).store(data);
		}
		Storable store = getStore(claz);
		if (store == null) {
			store = getStore(getType());
		}
		if (store == null) {
			return data;
		}
//		if (claz.equals(UUID.class)) {
//			debug("=========== " + isInline() + "     " + store);
//		}
		if (isReference()) {

			String text = alias + StorageAPI.REFER_KEY + StorageAPI.getIdByObject(data);
			debug("<< REFERENCE " + text);
			return text;
		}
		if (isInline()) {
			Object result = store.store(data);
			if (result == null) {
				debug("<< INLINE " + data);
				return new StorageInline(getInfo().clone()).store(data);
			}
			debug("<< INLINE CUSTOM " + result);
			return result;
		}

		try {
			Map<String, Object> map = new LinkedHashMap<>();
			if (isIndentifiable()) {
				debug("<< INDENTIFIABLE " + claz);
				map.put(StorageAPI.STORE_KEY, alias + StorageAPI.REFER_KEY + StorageAPI.getIdByObject(data));
			} else {
				map.put(StorageAPI.STORE_KEY, alias);
			}
			while (!claz.equals(Object.class)) {
				for (Field field : claz.getDeclaredFields()) {
					field.setAccessible(true);

					if (Modifier.isStatic(field.getModifiers()))
						continue;
					if (Modifier.isTransient(field.getModifiers()))
						continue;
					if (Modifier.isFinal(field.getModifiers())) {
						continue;
					}
					Object fieldValue = field.get(data);
					if (fieldValue == null) {
						continue;
					}

					StorageObject storage = new StorageObject(getInfo().clone());
					storage.setField(field);
					storage.setType(field.getType());
					storage.update(field, fieldValue.getClass());
//					if (fieldValue.getClass().equals(UUID.class)) {
//						debug("********** " + storage.isInline());
//					}
					debug("<< VARIABLE " + field.getName() + " " + fieldValue.getClass().getSimpleName());
					Object fieldResult = storage.store(fieldValue);
//					if (Extra.isList(field.getType())) {
//						fieldResult = new StorageList(Extra.getTypeKey(field.getGenericType()), reference).store(value);
//					} else if (Extra.isMap(field.getType())) {
//						fieldResult = new StorageMap(Extra.getTypeKey(field.getGenericType()),
//								Extra.getTypeValue(field.getGenericType()), isReference).store(value);
//					} else {
//						if (StorageAPI) {
//							Mine.console("§dTenso " + value.getClass());
//							fieldResult = new StorageObject(value.getClass(), isReference).store(value);
//						} else {
//							fieldResult = new StorageObject(field.getType(), isReference).store(value);
//						}
//					}
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
