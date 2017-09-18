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

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import net.eduard.api.setup.ScoreAPI.FakeOfflinePlayer;
@SuppressWarnings("unchecked")
public final class StorageAPI {
	/**
	 * Type = Class <br>
	 * 
	 * @param type
	 *            Tipo do mapa
	 * @return Tipo do Valor do Mapa
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
	 * Type = Class <br>
	 * 
	 * @param type
	 *            Tipo do Lista ou Mapa
	 * @return Tipo do Item/Key da Lista ou Mapa
	 */
	public static Class<?> getTypeKey(Type type) {
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;

			Type[] types = parameterizedType.getActualTypeArguments();

			return (Class<?>) types[0];

		}
		return null;
	}
	public static boolean isMap(Class<?> claz) {
		return Map.class.isAssignableFrom(claz);
	}
	public static boolean isList(Class<?> claz) {
		return List.class.isAssignableFrom(claz);
	}
	public static boolean isString(Class<?> claz) {
		return String.class.isAssignableFrom(claz);
	}
	public static boolean isPrimiteOrWrapper(Class<?> claz) {
		try {
			claz.getField("TYPE").get(0);
			return true;
		} catch (Exception e) {
			return claz.isPrimitive();
		}
	}
	public static boolean isStorable(Object object) {
		return object instanceof Storable;
	}
	public static boolean isStorable(Class<?> claz) {
		return Storable.class.isAssignableFrom(claz);
	}
	public static boolean isVariable(Class<?> claz) {
		return Variable.class.isAssignableFrom(claz);
	}
	public static boolean isVariable(Object object) {
		return object instanceof Variable;
	}
	public static interface Variable {

		public Object get(Object object);
		public Object save(Object object);

	}

	/**
	 * Copiavel
	 * 
	 * @author Eduard-PC
	 * @param <E>
	 *
	 */
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
	 * Armazenavel
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
		public default String alias() {
			return getClass().getSimpleName();
		}
		public default Integer toInt(Object object) {

			return ObjectAPI.toInteger(object);
		}
		public default String toStr(Object object) {
			return ObjectAPI.toString(object);
		}
		public default Double toDouble(Object object) {
			return ObjectAPI.toDouble(object);
		}
		public default Float toFloat(Object object) {
			return ObjectAPI.toFloat(object);
		}
		public default String toMsg(Object object) {
			return ObjectAPI.toChatMessage(toStr(object));
		}
	}

	public static String STORE_KEY = "=";
	public static String REFER_KEY = "@";
	private static Map<Class<?>, String> aliases = new LinkedHashMap<>();
	private static Map<Class<?>, Storable> storages = new LinkedHashMap<>();
	private static Map<Class<?>, Variable> variables = new LinkedHashMap<>();
	private static Map<Integer, Object> objects = new LinkedHashMap<>();
	private static List<Refer> references = new ArrayList<>();
	private static List<String> packages = new ArrayList<>();

	static {
		
		// fac1: 321
		// fac2: 212
	}
	public static void init() {
		register(Vector.class, new Storable() {

			@Override
			public Object restore(Map<String, Object> map) {
				return new Vector();
			}

			@Override
			public void store(Map<String, Object> map, Object object) {

			}
			@Override
			public String alias() {
				return "Vector";
			}
		});
		register(Enchantment.class, new Variable() {

			@SuppressWarnings("deprecation")
			@Override
			public Object save(Object object) {
				if (object instanceof Enchantment) {
					Enchantment enchantment = (Enchantment) object;
					return enchantment.getId();

				}
				return null;
			}

			@SuppressWarnings("deprecation")
			@Override
			public Object get(Object object) {
				if (object instanceof String) {
					String string = (String) object;
					return Enchantment.getById(ObjectAPI.toInt(string));

				}
				return null;
			}
		});
		register(PotionEffectType.class, new Variable() {

			@Override
			public Object get(Object object) {
				if (object instanceof String) {

					String string = (String) object;
					String[] split = string.split(";");
					return PotionEffectType.getByName(split[1]);
				}
				return null;
			}

			@SuppressWarnings("deprecation")
			@Override
			public Object save(Object object) {
				if (object instanceof PotionEffectType) {
					PotionEffectType potionEffectType = (PotionEffectType) object;
					return potionEffectType.getName() + ";"
							+ potionEffectType.getId();

				}
				return null;
			}

		});
		register(PotionEffect.class, new Variable() {

			@Override
			public Object save(Object object) {
				return null;
			}

			@Override
			public Object get(Object object) {
				return new PotionEffect(PotionEffectType.SPEED, 20, 0);
			}
		});
		register(OfflinePlayer.class, new Variable() {

			@Override
			public Object get(Object object) {
				if (object instanceof String) {
					String id = (String) object;
					String[] split = id.split(";");
					return new FakeOfflinePlayer(split[0],
							UUID.fromString(split[1]));

				}
				return null;
			}

			@Override
			public Object save(Object object) {
				if (object instanceof OfflinePlayer) {
					OfflinePlayer p = (OfflinePlayer) object;
					return p.getName() + ";" + p.getUniqueId().toString();

				}
				return null;
			}

		});
		register(Location.class, new Storable() {

			@Override
			public Object restore(Map<String, Object> map) {
				return new Location(Bukkit.getWorlds().get(0), 1, 1, 1);
			}

			@Override
			public void store(Map<String, Object> map, Object object) {
			}
			@Override
			public String alias() {
				return "Location";
			}

		});

		register(Chunk.class, new Variable() {

			@Override
			public Object get(Object object) {
				if (object instanceof String) {
					String string = (String) object;
					String[] split = string.split(";");
					return Bukkit.getWorld(split[0]).getChunkAt(
							ObjectAPI.toInt(split[1]), ObjectAPI.toInt(split[2]));

				}
				return null;
			}

			@Override
			public Object save(Object object) {
				if (object instanceof Chunk) {
					Chunk chunk = (Chunk) object;
					return chunk.getWorld().getName() + ";" + chunk.getX() + ";"
							+ chunk.getZ();
				}

				return null;
			}

		});
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
		register(World.class, new Variable() {
			@Override
			public Object get(Object object) {
				if (object instanceof String) {
					String world = (String) object;
					return Bukkit.getWorld(world);

				}
				return null;
			}

			@Override
			public Object save(Object object) {
				if (object instanceof World) {
					World world = (World) object;
					return world.getName();

				}
				return null;
			}
		});
		register(ItemStack.class, new Storable() {
			@Override
			public Object restore(Map<String, Object> map) {
				int id = toInt(map.get("id"));
				int amount = toInt(map.get("amount"));
				int data = toInt(map.get("data"));
				@SuppressWarnings("deprecation")
				ItemStack item = new ItemStack(id, amount, (short) data);
				String name = ObjectAPI.toChatMessage((String) map.get("name"));
				if (!name.isEmpty()) {
					ItemAPI.setName(item, name);
				}
				List<String> lore = ObjectAPI
						.toMessages((List<Object>) map.get("lore"));
				if (!lore.isEmpty()) {
					ItemAPI.setLore(item, lore);
				}
				String enchants = (String) map.get("enchants");
				if (!enchants.isEmpty()) {
					if (enchants.contains(", ")) {
						String[] split = enchants.split(", ");
						for (String enchs : split) {
							String[] sub = enchs.split("-");
							@SuppressWarnings("deprecation")
							Enchantment ench = Enchantment
									.getById(ObjectAPI.toInt(sub[0]));
							Integer level = ObjectAPI.toInt(sub[1]);
							item.addUnsafeEnchantment(ench, level);

						}
					} else {
						String[] split = enchants.split("-");
						@SuppressWarnings("deprecation")
						Enchantment ench = Enchantment
								.getById(ObjectAPI.toInt(split[0]));
						Integer level = ObjectAPI.toInt(split[1]);
						item.addUnsafeEnchantment(ench, level);

					}
				}
				return item;
			}
			@Override
			public String alias() {
				return "Item";
			}
			@SuppressWarnings("deprecation")
			@Override
			public void store(Map<String, Object> map, Object object) {
				if (object instanceof ItemStack) {
					ItemStack item = (ItemStack) object;
					map.remove("durability");
					map.remove("meta");
					map.remove("type");
					map.put("id", item.getTypeId());
					map.put("data", item.getDurability());
					map.put("amount", item.getAmount());
					map.put("name", ItemAPI.getName(item));
					map.put("lore", ItemAPI.getLore(item));
					String enchants = "";
					if (item.getItemMeta().hasEnchants()) {
						StringBuilder str = new StringBuilder();
						int id = 0;
						for (Entry<Enchantment, Integer> entry : item
								.getEnchantments().entrySet()) {
							if (id > 0)
								str.append(", ");
							Enchantment enchantment = entry.getKey();
							str.append(enchantment.getId() + "-"
									+ entry.getValue());
							id++;
						}
						enchants = str.toString();
					}
					map.put("enchants", enchants);
				}

			};
		});
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
	public static int getIdByObject(Object object) {
		for (Entry<Integer, Object> entry : objects.entrySet()) {
			if (entry.getValue().equals(object)) {
				return entry.getKey();
			}
		}
		int id = newId();
		objects.put(id, object);
		// System.out.println(
		// "Getting " + object.getClass().getName() + REFER_KEY + id);
		return id;
	}
	public static void updateReferences() {
		Iterator<Refer> loop = references.iterator();
		while (loop.hasNext()) {
			Refer next = loop.next();
			next.updateReference();
			loop.remove();
		}
	}

	public static void register(Class<?> claz, Variable var) {
		variables.put(claz, var);
	}
	public static void register(Class<?> claz, Storable storable) {
		storages.put(claz, storable);
		aliases.put(claz,
				storable == null ? claz.getSimpleName() : storable.alias());
	}
	public static void register(String pack) {
		if (!packages.contains(pack)) {
			packages.add(pack);
		}

	}
	public static void register(Package pack) {
		register(pack.getName());
	}
	public static void registerPackage(Class<?> claz) {
		register(claz.getPackage());
	}
	public static Storable register(Class<? extends Storable> claz) {
		Storable store = null;
		try {
			if (!Modifier.isAbstract(claz.getModifiers())) {
				store = claz.newInstance();
			}
			register(claz, store);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return store;
	}
	public static void addObject(int id, Object object) {
		objects.put(id, object);
	}
	public static void removeObject(int id) {
		objects.remove(id);
	}
	public static void removeObject(Object object) {
		objects.remove(object);
	}
	public static void unregister(Class<?> claz) {
		storages.remove(claz);
		variables.remove(claz);
	}

	public static Storable getStore(Class<?> claz) {
		return storages.get(claz);

	}
	public static String getAlias(Class<?> claz) {
		return aliases.get(claz);

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
	public static List<String> invalidClasses = new ArrayList<>();
	public static void autoRegisterClass(Class<?> claz) {
		if (isRegistred(claz))
			return;
		if (isStorable(claz)) {
			register((Class<? extends Storable>) claz);
		}

	}
	public static void autoRegisterAlias(String alias) {
		if (isRegistred(alias))
			return;
		System.out.println(alias);
		Class<?> claz = null;
		for (String pack : packages) {
			String name = pack + "." + alias;
			if (invalidClasses.contains(name))
				continue;
			try {
				claz = Class.forName(name);
				break;
			} catch (Exception e) {
				invalidClasses.add(name);
			}
		}
		if (claz != null) {
			if (!isRegistred(claz)) {
				if (isStorable(claz)) {
					register((Class<? extends Storable>) claz);
				}
			}
		}

	}

	public static Object restoreObject(Type type, Object value) {
		try {
			Class<?> claz = (Class<?>) type;
			if (claz.isEnum()) {
				return RefAPI.getValue(claz, value.toString().toUpperCase());
			}
			if (claz.isArray()) {
				Class<?> arrayType = claz.getComponentType();
				if (isPrimiteOrWrapper(arrayType) || isString(arrayType)
						|| getStore(arrayType) != null) {

					Map<Object, Object> valueMap = (Map<Object, Object>) value;
					Map<Object, Object> map = restoreMap(valueMap.get("array"),
							Integer.class, arrayType);
					Integer size = ObjectAPI.toInt(valueMap.get("size"));
					Object newArray = Array.newInstance(arrayType, size);
					for (Entry<Object, Object> entry : map.entrySet()) {
						Array.set(newArray, ObjectAPI.toInt(entry.getKey()),
								entry.getValue());
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
				String fieldTypeName = ObjectAPI.toTitle(claz.getSimpleName());
				value = RefAPI.getResult(ObjectAPI.class, "to" + fieldTypeName,
						RefAPI.getParameters(Object.class), value);
				if (value instanceof String) {
					value = ObjectAPI.toChatMessage((String) value);
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
	public static Object restoreField(Object instance, Object value,
			Field field) {
		// if (isPrimiteOrWrapper(field.getType()) && value == null) {
		// String fieldTypeName = ObjectAPI
		// .toTitle(field.getType().getSimpleName());
		// try {
		// return ObjectAPI.getResult(ObjectAPI.class, "to" + fieldTypeName,
		// ObjectAPI.getParameters(Object.class), value);
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
						newList.add(ObjectAPI
								.toInt(item.toString().split(REFER_KEY)[1]));
					}
					references.add(new Refer(field, instance, value));
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
								ObjectAPI.toInt(entry.getValue().toString()
										.split(REFER_KEY)[1]));
					}
					references.add(new Refer(field, instance, map));
				} else {
					return restoreMap(value, getTypeKey(type),
							getTypeValue(type));
				}
			} else if (isRefer) {
				references.add(new Refer(field, instance, value));
			} else
				return restoreObject(type, value);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	public static Object restoreValue(Object value) {
		if (value instanceof Map) {

			Map<String, Object> map = (Map<String, Object>) value;
			if (map.containsKey(STORE_KEY)) {
				String text = (String) map.get(STORE_KEY);
				String[] split = text.split(REFER_KEY);
				String alias = split[0];
				Class<?> claz = null;
				Storable store = null;
				autoRegisterAlias(alias);
				Integer id = ObjectAPI.toInt(split[1]);
				claz = getClassByAlias(alias);
				store = getStore(claz);
				Object instance = null;
				if (claz != null) {
					instance = store.restore(map);
					if (instance == null) {
						try {
							instance = claz.newInstance();
						} catch (Exception e) {
							try {
								instance = store.getClass().newInstance();
							} catch (Exception e2) {
								return null;
							}
						}
					}
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
								Object fieldRestored = restoreField(instance,
										fieldMapValue, field);
								field.set(instance, fieldRestored);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if (isStorable(claz)) {
							try {
								for (Method method : claz.getMethods()) {
									if (method.getName().equals("restore")) {
										method.invoke(instance, map);
										break;
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
						claz = claz.getSuperclass();
					}
					objects.put(id, instance);
					return instance;
				}
			}
		}

		return value;
	}

	public static Map<Object, Object> restoreMap(Object object,
			Class<?> keyType, Class<?> valueType) {
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
				if (isPrimiteOrWrapper(arrayType) || isString(arrayType)
						|| getStore(arrayType) != null) {
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
					if (isStorable(claz)) {
						try {
							for (Method method : claz.getDeclaredMethods()) {
								if (method.getName().equals("store")) {
									method.invoke(value, map, value);
									break;
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					claz = claz.getSuperclass();
				}
				if (!(value instanceof Storable)) {
					store.store(map, value);
				}
				return map;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}
	public static Object storeList(List<Object> list, Class<?> listType,
			boolean asReference) {

		try {
			List<Object> newList = new ArrayList<>();
			if (getStore(listType) != null) {
				if (asReference) {
					for (Object item : list) {
						newList.add(getAlias(listType) + REFER_KEY
								+ getIdByObject(item));
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
				return storeList((List<Object>) result, getTypeKey(type),
						field.isAnnotationPresent(Reference.class));

			}
			if (isMap(claz)) {
				// ParameterizedType ptype = (ParameterizedType) type;
				return storeMap((Map<Object, Object>) result, getTypeKey(type),
						getTypeValue(type),
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

	public static Object storeMap(Map<Object, Object> map, Class<?> typeKey,
			Class<?> typeValue, boolean asRefer) {
		Map<String, Object> newMap = new HashMap<>();
		try {

			for (Entry<Object, Object> entry : map.entrySet()) {
				String key = storeValue(entry.getKey()).toString();
				Object value = entry.getValue();
				if (getStore(typeValue) != null) {
					if (asRefer) {
						value = getAlias(typeValue) + REFER_KEY
								+ getIdByObject(value);
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

	@Target({java.lang.annotation.ElementType.FIELD})
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
						newMap.put(entry.getKey(),
								getObjectById(entry.getValue()));
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
					field.set(instance, (Integer) reference);
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
	
}
