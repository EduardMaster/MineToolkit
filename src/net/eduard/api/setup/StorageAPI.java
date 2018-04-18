package net.eduard.api.setup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

/**
 * Sistema automatico de Armazenamento em Mapas que podem ser usados em JSON,
 * SQL, YAML<br>
 * <br>
 * 
 * @see Esta classe é muito boa também {@link AutoBase}
 * 
 * @author Eduard
 * @version 1.0
 *
 */
@SuppressWarnings("unchecked")
public final class StorageAPI {
	private static boolean enabled;

	public static void log(String msg) {
		if (enabled)
			System.out.println("[StorageAPI] " + msg);
	}

	/**
	 * 
	 * @param type
	 *            Variavel (Classe)
	 * @return Tipo 2 de type, caso type seja um {@link ParameterizedType}
	 * 
	 */
	public static Class<?> getTypeValue(Type type) {
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			Type[] types = parameterizedType.getActualTypeArguments();
			if (types[1] instanceof Class) {
				return (Class<?>) types[1];
			}

		}
		return null;
	}

	/**
	 * 
	 * @param type
	 *            Variavel {@link Type} (Classe/Tipo)
	 * @return Tipo 1 de type, caso type seja um {@link ParameterizedType}
	 * 
	 */
	public static Class<?> getTypeKey(Type type) {
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;

			Type[] types = parameterizedType.getActualTypeArguments();

			return (Class<?>) types[0];

		}
		return null;
	}

	/**
	 * 
	 * @param claz
	 *            Classe
	 * @return Se a claz é um {@link Map} (Mapa)
	 * 
	 */
	public static boolean isMap(Class<?> claz) {
		return Map.class.isAssignableFrom(claz);
	}

	/**
	 * 
	 * @param claz
	 *            Classe
	 * @return Se a claz é uma {@link List} (Lista)
	 * 
	 */
	public static boolean isList(Class<?> claz) {
		return List.class.isAssignableFrom(claz);
	}

	/**
	 * 
	 * @param claz
	 *            Classe
	 * @return Se a claz é um {@link String} (Texto)
	 * 
	 */
	public static boolean isString(Class<?> claz) {
		return String.class.isAssignableFrom(claz);
	}

	/**
	 * 
	 * @param claz
	 *            Classe
	 * @return Se a claz é do tipo Primitivo ou Wrapper (Envolocro)
	 * 
	 */
	public static boolean isPrimiteOrWrapper(Class<?> claz) {
		try {
			claz.getField("TYPE").get(0);
			return true;
		} catch (Exception e) {
			return claz.isPrimitive();
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
	 * @return Se a claz é um {@link Storable}
	 * 
	 */
	public static boolean isStorable(Class<?> claz) {
		return Storable.class.isAssignableFrom(claz);
	}

	/**
	 * 
	 * @param claz
	 *            Classe
	 * @return Se a claz é um {@link Variable}
	 * 
	 */
	public static boolean isVariable(Class<?> claz) {
		return Variable.class.isAssignableFrom(claz);
	}

	/**
	 * 
	 * @param object
	 * @return
	 */
	public static boolean isVariable(Object object) {
		return object instanceof Variable;
	}

	public static interface Variable {

		public Object get(Object object);

		public Object save(Object object);

	}

	public static interface Copyable {
		public default <E> E copy(E copied) {
			E result = StorageAPI.copy(copied);
			if (result instanceof Copyable) {
				Copyable copy = (Copyable) result;
				copy.onCopy();

			}
			return result;
		}

		public default Object copy() {
			return copy(this);
		}

		public default void onCopy() {
		}
	}

	public static <E> E copy(E object) {
		Class<? extends Object> claz = object.getClass();
		if (isList(claz)) {
			List<Object> list = (List<Object>) object;
			List<Object> newList = new ArrayList<>();
			for (Object item : list) {
				newList.add(copy(item));
			}
			object = (E) newList;

		} else if (isMap(claz)) {
			Map<Object, Object> map = (Map<Object, Object>) object;
			Map<Object, Object> newMap = new HashMap<>();
			for (Entry<Object, Object> entry : map.entrySet()) {
				newMap.put(copy(entry.getKey()), copy(entry.getValue()));
			}
			object = (E) newMap;

		} else if (claz.isArray()) {
			Object[] array = (Object[]) object;
			object = (E) Arrays.copyOf(array, array.length);
		} else if (isPrimiteOrWrapper(claz) || isString(claz)) {

		} else {
			try {
				E newInstance = (E) object.getClass().newInstance();
				while (!claz.equals(Object.class)) {
					for (Field field : claz.getDeclaredFields()) {
						if (Modifier.isStatic(field.getModifiers()))
							continue;
						if (Modifier.isTransient(field.getModifiers()))
							continue;
						field.setAccessible(true);
						try {
							Object value = field.get(object);
							if (value != null) {
								field.set(newInstance, copy(value));
							}
							field.setAccessible(true);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					claz = claz.getSuperclass();
				}
				object = newInstance;
			} catch (Exception e) {
			}

		}
		return object;
	}

	public static <E> E clone(E object) {
		try {
			Method cloneMethod = null;
			try {
				cloneMethod = object.getClass().getMethod("clone");
			} catch (Exception e) {
				try {
					cloneMethod = object.getClass().getDeclaredMethod("clone");
				} catch (Exception e2) {
				}
			}
			if (cloneMethod != null) {
				cloneMethod.setAccessible(true);
				return (E) cloneMethod.invoke(object);
			}
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * Armazenavel<br>
	 * Caso use os metodos restore e store por favor certifique-se de rodar o seu
	 * pai antes<br>
	 * Ex: super.restore(map) ou super.store(map,object);<br>
	 * Certifique-se de que haja um Construtor vasio new SeuObjeto();
	 * 
	 * 
	 * 
	 * @author Eduard-PC
	 *
	 */
	public static interface Storable {

		/**
		 * Cria um Objeto pelo Mapa
		 * 
		 * @param map
		 *            Mapa
		 * @return Objeto
		 */
		public Object restore(Map<String, Object> map);

		/**
		 * Salva o Objeto no Mapa
		 * 
		 * @param map
		 *            Mapa
		 * @param object
		 *            Objeto
		 */
		public void store(Map<String, Object> map, Object object);

		public default Object newInstance(Map<String, Object> map) {
			return restore(map);
		}

		public default String alias() {
			return getClass().getSimpleName();
		}
	}

	public static String STORE_KEY = "=";
	public static String REFER_KEY = "@";
	private static Map<Class<?>, String> aliases = new LinkedHashMap<>();
	private static Map<Class<?>, Storable> storages = new LinkedHashMap<>();
	private static Map<Class<?>, Variable> variables = new LinkedHashMap<>();
	private static Map<Integer, Object> objects = new LinkedHashMap<>();
	private static List<Refer> references = new ArrayList<>();

	static {
		register(UUID.class, new Variable() {

			@Override
			public Object get(Object object) {

				return UUID.fromString(object.toString());
			}

			@Override
			public Object save(Object object) {
				return object.toString();
			}
		});
		// fac1: 321
		// fac2: 212
	}

	public static Object getObjectById(int id) {
		return objects.get(id);
	}

	private static int randomId() {
		return new Random().nextInt(10000);
	}

	private static int newId() {
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

	public static void register(Class<?> claz, Variable var) {
		variables.put(claz, var);
	}

	public static void register(Class<?> claz, Storable storable) {
		storages.put(claz, storable);
		aliases.put(claz, storable == null ? claz.getSimpleName() : storable.alias());
	}

	public static void registerPackage(Class<?> clazzPlugin,String pack) {
		log("PACKAGE " + pack);

		List<Class<?>> classes = Extra.getClasses(clazzPlugin, pack);
		for (Class<?> claz : classes) {
			autoRegisterClass(claz);
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
	public static void autoRegisterClass(Class<?> claz) {

		if (isRegistred(claz))
			return;
		if (isStorable(claz) && !claz.isAnonymousClass()) {
			// log("AUTO " + claz.getName());
			register((Class<? extends Storable>) claz);
		}

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
		variables.remove(claz);
		log("<- " + claz.getName());
	}

	public static Storable getStore(Class<?> claz) {
		return storages.get(claz);

	}

	public static String getAlias(Class<?> claz) {
		String alias = null;
		if (isStorable(claz)) {
			Storable store = getStore(claz);
			if (store != null) {
				alias = store.alias();
			}
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

	// public static List<String> invalidClasses = new ArrayList<>();


	// public static void autoRegisterAlias(String alias) {
	// if (isRegistred(alias))
	// return;
	// System.out.println(alias);
	// Class<?> claz = null;
	// for ()
	// for (String pack : packages) {
	// String name = pack + "." + alias;
	// if (invalidClasses.contains(name))
	// continue;
	// try {
	// claz = Class.forName(name);
	// break;
	// } catch (Exception e) {
	// invalidClasses.add(name);
	// }
	// }
	// if (claz != null) {
	// if (!isRegistred(claz)) {
	// if (isStorable(claz)) {
	// register((Class<? extends Storable>) claz);
	// }
	// }
	// }

	// }

	public static Object restoreObject(Type type, Object value) {
		try {
			Class<?> claz = (Class<?>) type;
			if (claz.isEnum()) {
				return Extra.getValue(claz, value.toString().toUpperCase());
			}

			if (claz.isArray()) {
				Class<?> arrayType = claz.getComponentType();
				if (isPrimiteOrWrapper(arrayType) || isString(arrayType) || getStore(arrayType) != null) {

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
			for (Entry<Class<?>, Variable> entry : variables.entrySet()) {
				if (entry.getKey().isAssignableFrom(claz)) {

					return entry.getValue().get(value);
				}
				if (entry.getKey().equals(claz)) {

					return entry.getValue().get(value);
				}
			}

			if (isPrimiteOrWrapper(claz) || isString(claz)) {
				String fieldTypeName = Extra.toTitle(claz.getSimpleName());
				value = Extra.getResult(Extra.class, "to" + fieldTypeName, Extra.getParameters(Object.class), value);
				if (value instanceof String) {
					value = Extra.toChatMessage((String) value);
				}
				return value;
			}
			autoRegisterClass(claz);
			if (getStore(claz) != null) {
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
			if (isList(claz)) {
				if (isRefer) {
					List<Object> _list = (List<Object>) value;
					List<Integer> newList = new ArrayList<>();
					for (Object item : _list) {
						newList.add(Extra.toInt(item.toString().split(REFER_KEY)[1]));
					}
					references.add(new Refer(field, instance, newList));
				} else {
					return restoreList(value, getTypeKey(type));
				}

			} else if (isMap(claz)) {
				Class<?> mapKey = getTypeKey(type);
				// Class<?> mapValue = getTypeValue(type);
				if (isRefer) {
					Map<String, Object> _map = (Map<String, Object>) value;
					Map<Object, Integer> map = new HashMap<>();
					for (Entry<String, Object> entry : _map.entrySet()) {

						map.put(restoreObject(mapKey, entry.getKey()),
								Extra.toInt(entry.getValue().toString().split(REFER_KEY)[1]));
					}
					references.add(new Refer(field, instance, map));
				} else {
					return restoreMap(value, getTypeKey(type), getTypeValue(type));
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
						// Cuidado para não implementar isso aqui sem motivo
						// System.out.println("Restaurando ultima vez "+storable.alias());
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
		List<Object> list = new ArrayList<>();
		if (object instanceof List) {
			List<Object> _list = (List<Object>) object;
			for (Object item : _list) {
				list.add(restoreObject(listType, item));
			}
		} else if (object instanceof Map) {
			Map<String, Object> _map = (Map<String, Object>) object;
			for (Object item : _map.values()) {
				list.add(restoreObject(listType, item));
			}

		}
		return list;

	}

	public static Object storeValue(Object value) {
		if (value == null)
			return null;

		try {
			Class<? extends Object> claz = value.getClass();
			if (claz.isEnum()) {
				return value.toString();
			}
			if (isPrimiteOrWrapper(claz)) {
				return value;
			}
			if (claz.isArray()) {
				Class<?> arrayType = value.getClass().getComponentType();
				if (isPrimiteOrWrapper(arrayType) || isString(arrayType) || getStore(arrayType) != null) {
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
			if (isString(claz)) {
				return value.toString().replaceAll("§", "&");
			}
			for (Entry<Class<?>, Variable> entry : variables.entrySet()) {
				if (entry.getKey().isAssignableFrom(claz)) {
					return entry.getValue().save(value);
				}
				if (entry.getKey().equals(claz)) {
					return entry.getValue().save(value);
				}
			}

			Map<String, Object> map = new LinkedHashMap<>();
			String alias = null;
			Storable store = null;
			autoRegisterClass(claz);
			store = getStore(claz);
			alias = getAlias(claz);

			if (store != null) {
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
			result = field.get(object);
			if (result == null)
				return null;

			if (isList(claz)) {
				return storeList((List<Object>) result, getTypeKey(type), field.isAnnotationPresent(Reference.class));

			}
			if (isMap(claz)) {
				// ParameterizedType ptype = (ParameterizedType) type;
				return storeMap((Map<Object, Object>) result, getTypeKey(type), getTypeValue(type),
						field.isAnnotationPresent(Reference.class));
			}
			if (field.isAnnotationPresent(Reference.class)) {
				return getAlias(claz) + REFER_KEY + getIdByObject(result);
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

	@Target({ java.lang.annotation.ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Reference {

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

	public static void registerPackage(Class<?> clazzForPackage) {
		registerPackage(clazzForPackage, clazzForPackage.getName());
	}

}
