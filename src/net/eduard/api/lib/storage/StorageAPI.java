package net.eduard.api.lib.storage;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.eduard.api.lib.Extra;
import net.eduard.api.lib.core.Mine;

/**
 * Sistema automatico de Armazenamento em Mapas que podem ser usados em JSON,
 * SQL, YAML<br>
 * Sistema de copiar Objetos <br>
 * 
 * 
 * 
 * @author Eduard
 * @version 1.3
 * @since Lib v1.0
 *
 */
@SuppressWarnings("unchecked")
public final class StorageAPI {
	@Target({ java.lang.annotation.ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Reference {

	}
	private static boolean enabled = false;

	public static void log(String msg) {
		if (enabled)
			System.out.println("[StorageAPI] " + msg);
	}

	public static class Refer {

		private Field field;
		private Object instance;
		private Object reference;

		public void updateReference() {
			try {
				if (reference instanceof Map) {
					Map<Object, Integer> map = (Map<Object, Integer>) reference;
					Map<Object, Object> newMap = new HashMap<>();
					for (Entry<Object, Integer> entry : map.entrySet()) {
						newMap.put(entry.getKey(), getObjectById(entry.getValue()));
					}
					// System.out.println("field " + field + " inst " + instance);
					field.set(instance, newMap);
				} else if (reference instanceof List) {
					List<Integer> list = (List<Integer>) reference;
					List<Object> newList = new ArrayList<>();
					for (Integer id : list) {
						newList.add(getObjectById(id));
					}
					field.set(instance, newList);
				} else {
					field.set(instance, getObjectById((Integer) reference));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public Refer(Field field, Object instance, Object reference) {
			this.field = field;
			this.instance = instance;
			this.reference = reference;
		}

	}

	/**
	 * 
	 * @param object
	 *            Objeto
	 * @return Se o object implementa {@link Storable}
	 * 
	 */
	public static boolean isStorable(Object object) {
		return object instanceof Storable;
	}

	/**
	 * 
	 * @param claz
	 *            Classe
	 * @return Se a claz § um {@link Storable}
	 * 
	 */
	public static boolean isStorable(Class<?> claz) {
		return Storable.class.isAssignableFrom(claz);
	}

	@Target({ java.lang.annotation.ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Variable {

	}

	public static String storeInline(Object obj) {
		Class<? extends Object> c = obj.getClass();
		StringBuilder b = new StringBuilder();
		for (Field field : c.getDeclaredFields()) {
			field.setAccessible(true);
			if (Modifier.isTransient(field.getModifiers())) {
				continue;
			}

			try {
				Object fieldValue = field.get(obj);
				if (fieldValue == null) {
					b.append("-;");

				} else {
					if (Extra.isList(field.getType())) {
						int index = 0;
						for (Object object : (List<Object>) fieldValue) {
							if (index > 0) {
								b.append(",");
							} else
								index++;
							b.append(object);
						}

					} else if (Extra.isMap(field.getType())) {
						int index = 0;
						for (Entry<Object, Object> entrada : ((Map<Object, Object>) fieldValue).entrySet()) {
							if (index > 0) {
								b.append(",");
							} else
								index++;
							b.append(entrada.getKey() + "=" + entrada.getValue());
						}
					} else if (field.isAnnotationPresent(Reference.class)) {

						b.append(getAlias(field.getType()) + REFER_KEY + getIdByObject(fieldValue));
					} else if (isStorable(field.getType())) {
						Storable store = getStore(field.getType());
						if (store.saveInline() || field.isAnnotationPresent(Variable.class)) {
							b.append(store.store(fieldValue));
							continue;
						} else {
							b.append(fieldValue);
						}
					} else {
						b.append(fieldValue);
					}
					b.append(";");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
		return b.toString();
	}

	public static <E> E restoreInline(String line, Class<E> type) {
		String[] split = line.split(";");
		E resultadoFinal = null;
		try {
			resultadoFinal = (E) type.newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		int index = 0;
		for (Field field : type.getDeclaredFields()) {
			if (Modifier.isTransient(field.getModifiers())) {
				continue;
			}
			field.setAccessible(true);

			try {
				Object fieldFinalValue = split[index];

				if (fieldFinalValue.equals("-")) {
					fieldFinalValue = null;
				} else if (fieldFinalValue.toString().isEmpty()) {

					fieldFinalValue = new ArrayList<>();
				}

				else if (Extra.isList(field.getType())) {

					Class<?> typeKey = Extra.getTypeKey(field.getGenericType());
					String[] subSplit = fieldFinalValue.toString().split(",");
					List<Object> list = new ArrayList<>();
					for (String pedaco : subSplit) {
						if (pedaco.isEmpty())
							continue;
						list.add(transform(pedaco, typeKey));
					}
					fieldFinalValue = list;
				} else if (Extra.isMap(field.getType())) {

					Class<?> typeKey = Extra.getTypeKey(field.getGenericType());
					Class<?> typeValue = Extra.getTypeValue(field.getGenericType());
					String[] subSplit = fieldFinalValue.toString().split(",");
					Map<Object, Object> mapa = new HashMap<>();
					for (String pedaco : subSplit) {
						String[] corteNoPedaco = pedaco.split("=");
						Object chave = transform(corteNoPedaco[0], typeKey);
						Object value = transform(corteNoPedaco[1], typeValue);
						mapa.put(chave, value);
					}
					fieldFinalValue = mapa;
				} else if (field.isAnnotationPresent(Reference.class)) {
					Refer refer = new Refer(field, resultadoFinal,
							Mine.toInt(fieldFinalValue.toString().split(REFER_KEY)[1]));
					references.add(refer);
					fieldFinalValue = null;
				} else if (Extra.isWrapper(field.getType())) {
					fieldFinalValue = transform(fieldFinalValue, field.getType());
				} else {
					int length = index + field.getType().getDeclaredFields().length;
					StringBuilder b = new StringBuilder();
					while (index < length) {
						b.append(split[index] + ";");
						index++;
					}
					fieldFinalValue = restoreInline(b.toString(), field.getType());
				}
				field.set(resultadoFinal, fieldFinalValue);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			index++;
		}

		return resultadoFinal;

	}

	public static String STORE_KEY = "=";
	public static String REFER_KEY = "@";
	private static Map<Class<?>, String> aliases = new LinkedHashMap<>();
	private static Map<Class<?>, Storable> storages = new LinkedHashMap<>();
	public static Map<Integer, Object> objects = new LinkedHashMap<>();
	private static List<Refer> references = new ArrayList<>();

	public static Object getObjectById(int id) {
		return objects.get(id);
	}

	private static int randomId() {
		return new Random().nextInt(10000);
	}

	public static int newId() {
		int id = 0;
		do {
			id = randomId();
		} while (objects.containsKey(id));
		return id;
	}

	/**
	 * Atualiza as Referencias criadas e usadas na config
	 */
	public static void updateReferences() {
		Iterator<Refer> loop = references.iterator();
		while (loop.hasNext()) {
			Refer next = loop.next();
			next.updateReference();
			loop.remove();
		}
	}

	public static int getIdByObject(Object object) {
		for (Entry<Integer, Object> entry : objects.entrySet()) {
			if (entry.getValue().equals(object)) {
				return entry.getKey();
			}
		}
		int id = newId();
		objects.put(id, object);
		log("== " + object.getClass().getSimpleName() + "@" + id);
		return id;
	}

	public static void register(Class<?> claz, Storable storable) {
		storages.put(claz, storable);
		aliases.put(claz, storable == null ? claz.getSimpleName() : storable.alias());
	}

	public static void registerPackage(Class<?> clazzForPackage) {
		registerPackage(clazzForPackage, clazzForPackage.getName());
	}

	public static void registerPackage(Class<?> clazzPlugin, String pack) {
		log("PACKAGE " + pack);

		List<Class<?>> classes = Extra.getClasses(clazzPlugin, pack);
		for (Class<?> claz : classes) {
			getStore(claz);
		}
	}

	public static Storable register(Class<? extends Storable> claz) {
		Storable store = null;
		try {
			if (!Modifier.isAbstract(claz.getModifiers())) {
				store = claz.newInstance();
			} else {
			}
			log("CLASSE " + claz.getName());
			register(claz, store);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return store;
	}

	public static void registerClasses(Class<?> claz) {
		log("CLASSES OF " + claz.getName());
		for (Class<?> clazz : claz.getDeclaredClasses()) {
			// System.out.println(clazz.getName());
			try {
				if (isStorable(clazz)) {
					register((Class<? extends Storable>) clazz);
				}

			} catch (Exception e) {
			}

		}
	}

	public static void addObject(int id, Object object) {
		objects.put(id, object);
		log("-> " + aliases.get(object.getClass()) + "@" + id);
	}

	public static void removeObject(int id) {
		objects.remove(id);
		log("<- " + id);
	}

	public static void removeObject(Object object) {
		objects.remove(object);
		log("<- " + aliases.get(object.getClass()));
	}

	public static void unregister(Class<?> claz) {
		storages.remove(claz);
		log("<- " + claz.getName());
	}

	public static Storable getStore(Class<?> claz) {
		Storable store = storages.get(claz);
		if (store == null) {
			for (Entry<Class<?>, Storable> loop : storages.entrySet()) {
				if (loop.getKey().isAssignableFrom(claz)) {
					store = loop.getValue();
				}
				if (claz.isAssignableFrom(loop.getKey())) {
					store = loop.getValue();
				}
			}
		}
		if (store == null) {
			if (isStorable(claz) && !claz.isAnonymousClass()) {
				register((Class<? extends Storable>) claz);
			}
		}
		return store;

	}

	public static String getAlias(Class<?> claz) {
		String alias = null;
		if (claz == null) {
			return "Empty";
		}
		Storable store = getStore(claz);
		if (store != null) {
			alias = store.alias();
		}
		if (alias == null) {
			alias = aliases.getOrDefault(claz, claz.getSimpleName());
		}
		return alias;

	}

	public static Class<?> getClassByAlias(String alias) {
		for (Entry<Class<?>, String> entry : aliases.entrySet()) {
			if (entry.getValue().equals(alias)) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static boolean isRegistred(String alias) {
		return aliases.containsValue(alias);
	}

	public static boolean isRegistred(Class<?> claz) {
		return storages.containsKey(claz);
	}

	public static Object restoreObject(Type type, Object value) {
		try {
			Class<?> claz = (Class<?>) type;
			if (claz.isEnum()) {
				return Extra.getValue(claz, value.toString().toUpperCase());
			}

			if (claz.isArray()) {
				Class<?> arrayType = claz.getComponentType();
				if (Extra.isWrapper(arrayType) || getStore(arrayType) != null) {

					Map<Object, Object> valueMap = (Map<Object, Object>) value;
					Map<Object, Object> map = restoreMap(valueMap.get("array"), Integer.class, arrayType);
					Integer size = Extra.toInt(valueMap.get("size"));
					Object newArray = Array.newInstance(arrayType, size);
					for (Entry<Object, Object> entry : map.entrySet()) {
						Array.set(newArray, Extra.toInt(entry.getKey()), entry.getValue());
					}
					return newArray;
				}

			}
			// for (Entry<Class<?>, Variable> entry : variables.entrySet()) {
			// if (entry.getKey().isAssignableFrom(claz)) {
			//
			// return entry.getValue().get(value);
			// }
			// if (entry.getKey().equals(claz)) {
			//
			// return entry.getValue().get(value);
			// }
			// }

			if (Extra.isWrapper(claz)) {
				return transform(value, claz);
			}
			Storable store = getStore(claz);
			if (store != null) {
				if (store.saveInline()) {
					return store.restore(value);
				}
				return restoreValue(value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object restoreField(Object instance, Object value, Field field) {
		// if (isPrimiteOrWrapper(field.getType()) && value == null) {
		// String fieldTypeName = ObjectMine
		// .toTitle(field.getType().getSimpleName());
		// try {
		// return ObjectMine.getResult(ObjectMine.class, "to" + fieldTypeName,
		// ObjectMine.getParameters(Object.class), value);
		// } catch (Exception e) {
		// return null;
		// }
		// }
		if (value == null)
			return null;
		try {
			field.setAccessible(true);
			Type type = field.getGenericType();
			Class<?> claz = field.getType();
			boolean isRefer = field.isAnnotationPresent(Reference.class);
			if (Extra.isList(claz)) {
				if (isRefer) {
					List<Object> oldList = (List<Object>) value;
					List<Integer> newList = new ArrayList<>();
					for (Object item : oldList) {
						newList.add(Extra.toInt(item.toString().split(REFER_KEY)[1]));
					}
					references.add(new Refer(field, instance, newList));
				} else {
					return restoreList(value, Extra.getTypeKey(type));
				}

			} else if (Extra.isMap(claz)) {
				Class<?> mapKey = Extra.getTypeKey(type);
				// Class<?> mapValue = Extra.getTypeValue(type);
				if (isRefer) {
					Map<String, Object> oldMap = (Map<String, Object>) value;
					Map<Object, Integer> newMap = new HashMap<>();
					for (Entry<String, Object> entry : oldMap.entrySet()) {

						newMap.put(restoreObject(mapKey, entry.getKey()),
								Extra.toInt(entry.getValue().toString().split(REFER_KEY)[1]));
					}
					references.add(new Refer(field, instance, newMap));
				} else {
					return restoreMap(value, Extra.getTypeKey(type), Extra.getTypeValue(type));
				}
			} else if (isRefer) {
				references.add(new Refer(field, instance, Extra.toInt(value.toString().split(REFER_KEY)[1])));
			} else
				return restoreObject(type, value);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Reconstroi um Objeto baseado em outro objeto de preferencia Mapa
	 * 
	 * @param value
	 *            Objeto antigo
	 * @return Objeto novo
	 */
	public static Object restoreValue(Object value) {
		if (value instanceof Map) {

			Map<String, Object> map = (Map<String, Object>) value;
			if (map.containsKey(STORE_KEY)) {
				String text = (String) map.get(STORE_KEY);
				String[] split = text.split(REFER_KEY);
				String alias = split[0];
				Class<?> claz = null;
				Storable store = null;
				// autoRegisterAlias(alias);
				Integer id = 0;
				try {
					id = Extra.toInt(split[1]);
				} catch (Exception e) {
				}
				// System.out.println("ID "+id + " do "+alias);
				if (id == 0) {
					id = newId();
				}
				// if (objects.containsKey(id)) {
				// id = newId();
				// }
				// StorageAPI.updateReferences();
				claz = getClassByAlias(alias);
				store = getStore(claz);
				// System.out.println("Aliase "+alias+" -- "+store.alias());
				Object instance = null;
				if (claz != null) {
					try {
						// System.out.println("Restaurando primeira vez");
						instance = Extra.getResult(store, "restore", Extra.getParameters(Map.class), map);

					} catch (Exception ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}

					if (instance == null)
						try {
							instance = Extra.getNew(claz);
						} catch (Exception e1) {
						}
					if (instance == null)
						try {
							instance = Extra.getNew(store);
						} catch (Exception ex) {
							return null;
						}
					objects.put(id, instance);

					while (!claz.equals(Object.class)) {

						for (Field field : claz.getDeclaredFields()) {
							if (Modifier.isStatic(field.getModifiers()))
								continue;
							if (Modifier.isTransient(field.getModifiers()))
								continue;
							field.setAccessible(true);
							try {
								Object fieldMapValue = map.get(field.getName());
								if (fieldMapValue == null)
									continue;
								Object fieldRestored = restoreField(instance, fieldMapValue, field);
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
			}
		}

		return value;
	}

	public static Map<Object, Object> restoreMap(Object object, Class<?> keyType, Class<?> valueType) {
		Map<Object, Object> newMap = new HashMap<>();
		if (object instanceof Map) {
			Map<String, Object> map = (Map<String, Object>) object;
			for (Entry<String, Object> entry : map.entrySet()) {
				Object key = restoreObject(keyType, entry.getKey());
				Object value = restoreObject(valueType, entry.getValue());
				newMap.put(key, value);
			}
		}
		return newMap;
	}

	public static List<Object> restoreList(Object object, Class<?> listType) {
		List<Object> newList = new ArrayList<>();
		if (object instanceof List) {
			List<Object> list = (List<Object>) object;
			for (Object item : list) {
				newList.add(restoreObject(listType, item));
			}
		} else if (object instanceof Map) {
			Map<String, Object> _map = (Map<String, Object>) object;
			for (Object item : _map.values()) {
				newList.add(restoreObject(listType, item));
			}

		}
		return newList;

	}

	public static Object storeValue(Object value) {
		if (value == null)
			return null;

		try {
			Class<? extends Object> claz = value.getClass();
			if (claz.isEnum()) {
				return value.toString();
			}
			if (Extra.isWrapper(claz)) {
				if (Extra.isString(claz)) {
					return value.toString().replaceAll("§", "&");
				}
				return value;
			}
			if (claz.isArray()) {
				Class<?> arrayType = value.getClass().getComponentType();
				if (Extra.isWrapper(arrayType) || Extra.isString(arrayType) || getStore(arrayType) != null) {
					int size = Array.getLength(value);
					Map<Object, Object> map = new HashMap<>();
					map.put("size", size);
					Map<Object, Object> newMap = new HashMap<>();
					map.put("array", newMap);
					for (int i = 0; i < size; i++) {
						Object obj = Array.get(value, i);
						if (obj != null)
							newMap.put(i, storeValue(obj));
					}
					return map;
				}
				return value.toString();
			}

			Map<String, Object> map = new LinkedHashMap<>();
			String alias = null;
			Storable store = null;
			// if (value instanceof CraftWorld) {
			// System.out.println("Craftworld aqui");
			// System.out.println(claz);
			//
			// System.out.println(getStore(claz));
			// System.out.println(getAlias(claz));
			// }
			store = getStore(claz);
			alias = getAlias(claz);

			if (store != null) {
				if (store.saveInline()) {

					return store.store(value);
				}
				map.put(STORE_KEY, alias + REFER_KEY + getIdByObject(value));
				while (!claz.equals(Object.class)) {

					for (Field field : claz.getDeclaredFields()) {
						field.setAccessible(true);
						if (Modifier.isStatic(field.getModifiers()))
							continue;
						if (Modifier.isTransient(field.getModifiers()))
							continue;
						Object fieldResult = storeField(value, field);
						if (fieldResult != null)
							map.put(field.getName(), fieldResult);

					}
					// if (isStorable(claz)) {
					// Extra.getResult(claz, "store",
					// Extra.getParameters(Map.class, Object.class),
					// map, value);
					// }
					claz = claz.getSuperclass();
				}
				if (!(value instanceof Storable)) {
					store.store(map, value);
				} else {
					((Storable) value).store(map, value);
				}
				return map;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}

	public static Object storeList(List<Object> list, Class<?> listType, boolean asReference) {

		try {
			List<Object> newList = new ArrayList<>();
			if (getStore(listType) != null) {
				if (asReference) {
					for (Object item : list) {
						newList.add(getAlias(listType) + REFER_KEY + getIdByObject(item));
					}
					return newList;
				} else {
					Map<String, Object> map = new LinkedHashMap<>();
					int id = 1;
					for (Object item : list) {
						map.put(getAlias(listType) + id, storeValue(item));
						id++;
					}
					return map;
				}

			} else {
				for (Object item : list) {
					newList.add(storeValue(item));
				}
				return newList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	public static Object storeField(Object object, Field field) {

		Object result = null;
		try {
			field.setAccessible(true);
			Type type = field.getGenericType();
			Class<?> claz = field.getType();
			Storable store = getStore(claz);
			result = field.get(object);
			if (result == null)
				return null;

			boolean isVar = field.isAnnotationPresent(Variable.class);
			boolean isRef = field.isAnnotationPresent(Reference.class);

			if (Extra.isList(claz)) {
				return storeList((List<Object>) result, Extra.getTypeKey(type), isRef);

			}
			if (Extra.isMap(claz)) {
				// ParameterizedType ptype = (ParameterizedType) type;
				return storeMap((Map<Object, Object>) result, Extra.getTypeKey(type), Extra.getTypeValue(type), isRef);
			}
			if (isRef) {
				return getAlias(claz) + REFER_KEY + getIdByObject(result);
			}
			if (isVar) {

				return store.store(object);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return storeValue(result);
	}

	public static Object storeMap(Map<Object, Object> map, Class<?> typeKey, Class<?> typeValue, boolean asRefer) {
		Map<String, Object> newMap = new HashMap<>();
		try {

			for (Entry<Object, Object> entry : map.entrySet()) {
				String key = storeValue(entry.getKey()).toString();
				Object value = entry.getValue();
				if (getStore(typeValue) != null) {
					if (asRefer) {
						value = getAlias(typeValue) + REFER_KEY + getIdByObject(value);
					} else {
						value = storeValue(value);
					}
				} else {
					value = storeValue(value);
				}

				newMap.put(key, value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return newMap;
	}

	public static Object transform(Object object, Class<?> type) throws Exception {
		String fieldTypeName = Extra.toTitle(type.getSimpleName());
		Object value = Extra.getResult(Extra.class, "to" + fieldTypeName, Extra.getParameters(Object.class), object);
		if (value instanceof String) {
			value = Extra.toChatMessage((String) value);
		}
		return value;
	}



	public static Object storeData(Object data, Class<?> claz, Field field) {
		Object stored = null;
		Class<?> keyType = null;
		Class<?> valueType = null;
		Class<?> arrayType = null;
		boolean isReference = false;
		boolean isVariable = false;
		Type type = null;
		if (claz != null) {
			arrayType = claz.getComponentType();
		}
		if (field != null) {
			field.setAccessible(true);
			type = field.getGenericType();
			isReference = field.isAnnotationPresent(Reference.class);
			isVariable = field.isAnnotationPresent(Variable.class);
			keyType = Extra.getTypeKey(type);
			valueType = Extra.getTypeValue(type);
			if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers()))
				return null;
		}
		if (data != null) {
			Storable store = getStore(claz);
			String alias = getAlias(claz);

			if (data.getClass().isAnonymousClass()) {
				log("- INVALID " + data.getClass().getSimpleName());
				return null;
			}

			if (claz.isEnum()) {
				stored = data.toString();
				log("+ ENUM " + stored);
			} else if (Extra.isWrapper(claz)) {
				stored = data;
				if (Extra.isString(claz)) {
					stored = data.toString().replaceAll("§", "&");
				}
				log("+ (" + claz.getSimpleName() + ") " + stored);
			} else if (claz.isArray()) {

				store = getStore(arrayType);
				alias = getAlias(arrayType);
				log("+ ARRAY " + arrayType.getSimpleName());
				int arraySize = Array.getLength(data);
				List<Object> newList = new ArrayList<>();
				for (int index = 0; index < arraySize; index++) {
					newList.add(storeData(Array.get(data, index), arrayType, field));
				}
				stored = newList;
				if (store != null) {
					if (!store.saveInline() && !isVariable) {
						Map<String, Object> map = new LinkedHashMap<>();
						for (int index = 0; index < newList.size(); index++) {
							map.put(alias + index, newList.get(index));
						}
						stored = map;
					}
				}

			} else if (Extra.isList(claz)) {
				if (data instanceof List) {
					log("+ LIST " + keyType);
					if (keyType == null) {
						return data;
					}

					store = getStore(keyType);
					alias = getAlias(keyType);

					List<Object> list = (List<Object>) data;
					List<Object> newList = new ArrayList<>();
					for (Object item : list) {
						newList.add(storeData(item, keyType, field));
					}
					stored = newList;
					if (store != null) {
						if (!store.saveInline() && !isVariable) {
							Map<String, Object> map = new LinkedHashMap<>();
							for (int index = 0; index < newList.size(); index++) {
								map.put(alias + index, newList.get(index));
							}
							stored = map;
						}
					}

				}
			} else if (Extra.isMap(claz)) {

				if (data instanceof Map) {
					log("+ MAP " + keyType + " " + valueType);
					if (keyType == null || valueType == null) {
						return data;
					}

					Map<Object, Object> map = (Map<Object, Object>) data;
					Map<String, Object> newMap = new HashMap<>();
					try {

						for (Entry<Object, Object> entry : map.entrySet()) {
							Object key = storeData(entry.getKey(), keyType, field);
							Object value = storeData(entry.getValue(), valueType, field);
							newMap.put(key.toString(), value);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					stored = newMap;
				}

			} else if (store != null) {

				if (store.saveInline() || isVariable) {
					stored = store.store(data);
					log("+ INLINE " + stored);
				} else {
					if (isReference) {
						stored = alias + REFER_KEY + getIdByObject(data);
						log("+ REF " + stored);
					} else {
						Map<String, Object> map = new LinkedHashMap<>();
						map.put(STORE_KEY, alias + REFER_KEY + getIdByObject(data));
						log("+ OBJECT " + alias);
						while (!claz.equals(Object.class)) {

							for (Field variable : claz.getDeclaredFields()) {
								variable.setAccessible(true);
								Object variableValue = null;
								try {
									variableValue = variable.get(data);
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								variableValue = storeData(variableValue, variable.getType(), variable);
								if (variableValue != null)
									map.put(variable.getName(), variableValue);

							}
							claz = claz.getSuperclass();
						}
						if (!(data instanceof Storable)) {
							store.store(map, data);
						} else {
							((Storable) data).store(map, data);
						}
						stored = map;
					}
				}
			} else {
				stored = data.toString();
			}
		}
		return stored;
	}

	public static Object restoreData(Object data, Class<?> claz, Field field) {
		Object restored = null;

		Class<?> keyType = null;
		Class<?> valueType = null;
		Class<?> arrayType = null;
		boolean isReference = false;
		boolean isVariable = false;
		int id = 0;
		Storable store = null;
		String alias = null;
		Object instance = null;
		Type type = null;
		if (field != null) {
			field.setAccessible(true);
			if (claz == null) {
				claz = field.getType();
			}
			isReference = field.isAnnotationPresent(Reference.class);
			isVariable = field.isAnnotationPresent(Variable.class);
			type = field.getGenericType();
			keyType = Extra.getTypeKey(type);
			valueType = Extra.getTypeValue(type);
			if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers()))
				return null;

		}
		if (data instanceof Map) {

			Map<String, Object> map = (Map<String, Object>) data;
			if (map.containsKey(STORE_KEY)) {
				String text = (String) map.get(STORE_KEY);
				String[] split = text.split(REFER_KEY);
				alias = split[0];
				id = Extra.toInt(split[1]);
				if (id == 0) {
					id = newId();
				}
				claz = getClassByAlias(alias);
				store = getStore(claz);
				instance = store.restore(map);
				if (instance == null) {
					instance = store.newInstance();
				}
				log(text);
			}
		}

		if (data != null) {
			if (claz == null) {

				return null;
			}
			if (store == null || alias == null) {
				store = getStore(claz);
				alias = getAlias(claz);
			}

			if (claz.isEnum()) {
				try {
					restored = Extra.getValue(claz, data.toString().toUpperCase());
				} catch (Exception e) {
					e.printStackTrace();
				}
				log("++ ENUM " + restored);
			} else if (claz.isArray()) {
				Object array = null;
				arrayType = claz.getComponentType();
				log("++ ARRAY " + arrayType.getSimpleName());
				if (data instanceof List) {
					List<Object> list = (List<Object>) data;
					array = Array.newInstance(arrayType, list.size());
					int index = 0;
					for (Object item : list) {
						Array.set(array, index, restoreData(item, arrayType, field));
						index++;
					}

				} else if (data instanceof Map) {
					Map<String, Object> mapa = (Map<String, Object>) data;
					array = Array.newInstance(arrayType, mapa.size());
					int index = 0;
					for (Object item : mapa.values()) {
						Array.set(array, index, restoreData(item, arrayType, field));
						index++;
					}
				} else if (data.getClass().isArray()) {
					restored = data;
				}
				restored = array;

			} else if (Extra.isList(claz)) {
				log("++ LIST " + keyType.getSimpleName());
				if (isReference) {
					if (data instanceof List) {
						List<Object> oldList = (List<Object>) data;
						List<Integer> newList = new ArrayList<>();
						for (Object item : oldList) {
							newList.add(Extra.toInt(item.toString().split(REFER_KEY)[1]));
						}
						restored = newList;
					}
				} else {
					List<Object> newList = new ArrayList<>();
					if (data instanceof List) {
						List<Object> list = (List<Object>) data;
						for (Object item : list) {
							newList.add(restoreData(item, keyType, field));
						}
						restored = newList;
					} else if (data instanceof Map) {
						Map<String, Object> mapa = (Map<String, Object>) data;
						for (Object item : mapa.values()) {
							newList.add(restoreData(item, keyType, field));
						}
						restored = newList;
					}
				}

			} else if (Extra.isMap(claz)) {
				log("++ MAP " + keyType.getSimpleName() + " " + valueType.getSimpleName());
				if (isReference) {
					if (data instanceof Map) {
						Map<String, Object> oldMap = (Map<String, Object>) data;
						Map<Object, Integer> newMap = new HashMap<>();
						for (Entry<String, Object> entry : oldMap.entrySet()) {
							Object key = restoreData(entry.getKey(), keyType, field);
							newMap.put(key, Extra.toInt(entry.getValue().toString().split(REFER_KEY)[1]));
						}
						restored = newMap;
					}
				} else {
					Map<Object, Object> newMap = new HashMap<>();
					if (data instanceof Map) {
						Map<String, Object> map = (Map<String, Object>) data;
						for (Entry<String, Object> entry : map.entrySet()) {
							Object key = restoreData(entry.getKey(), keyType, field);
							Object value = restoreData(entry.getValue(), valueType, field);
							newMap.put(key, value);
						}
						restored = newMap;
					}
				}

			} else if (Extra.isWrapper(claz)) {
				try {
					restored = transform(data, claz);
				} catch (Exception e) {
					e.printStackTrace();
				}
				log("++ (" + claz.getSimpleName() + ") " + restored);
			} else if (store != null) {

				if (store.saveInline() || isVariable) {
					restored = store.restore(data);
					log("++ INLINE " + data);
				} else {
					if (isReference) {
						log("++ REF " + data);
						return restored = Extra.toInt(data.toString().split(REFER_KEY)[1]);
					}
					if (data instanceof Map) {
						Map<String, Object> map = (Map<String, Object>) data;
						log("++ OBJECT " + claz.getSimpleName());
						if (instance == null) {
							log("-- OBJECT NULL");
							return null;
						}
						objects.put(id, instance);
						while (!claz.equals(Object.class)) {

							for (Field variavel : claz.getDeclaredFields()) {
								boolean isVariableReference = variavel.isAnnotationPresent(Reference.class);
								try {
									Object fieldMapValue = map.get(variavel.getName());
									if (fieldMapValue == null)
										continue;

									Object fieldRestored = restoreData(fieldMapValue, variavel.getType(), variavel);
									if (!isVariableReference) {
										variavel.set(instance, fieldRestored);

									} else {
										if (fieldRestored == null)
											continue;
										references.add(new Refer(variavel, instance, fieldRestored));
									}

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
						restored = instance;
					}
				}

			} else {
			}
		}
		return restored;

	}

}
