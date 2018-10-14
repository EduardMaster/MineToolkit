package net.eduard.api.lib.storage;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.java_storables.TimeStampStorable;
import net.eduard.api.lib.storage.java_storables.UUIDStorable;
import net.eduard.api.lib.storage.references.ReferenceBase;
import net.eduard.api.lib.storage.references.ReferenceValue;

/**
 * Sistema automatico de Armazenamento em Mapas que podem ser usados em JSON,
 * SQL, YAML<br>
 * 
 * 
 * 
 * 
 * @author Eduard
 * @version 1.3
 * @since Lib v1.0
 *
 */
@SuppressWarnings("unchecked")
public class StorageAPI {

	private static boolean logging = false;
	public static String STORE_KEY = "=";
	public static String REFER_KEY = "@";
	private static Map<Class<?>, Storable> storages = new LinkedHashMap<>();
	private static Map<Integer, Object> objects = new LinkedHashMap<>();
	private static List<ReferenceBase> references = new ArrayList<>();

	public static void log(String msg) {
		if (logging)
			System.out.println("[StorageAPI] " + msg);
	}

	/**
	 * 
	 * @param object Objeto
	 * @return Se o object implementa {@link Storable}
	 * 
	 */
	public static boolean isStorable(Object object) {
		return object instanceof Storable;
	}

	/**
	 * 
	 * @param claz Classe
	 * @return Se a claz � um {@link Storable}
	 * 
	 */
	public static boolean isStorable(Class<?> claz) {
		return Storable.class.isAssignableFrom(claz);
	}

	public static String storeInline(Object obj) {
		Class<? extends Object> c = obj.getClass();
		StringBuilder b = new StringBuilder();
		for (Field field : c.getDeclaredFields()) {
			field.setAccessible(true);
			if (Modifier.isTransient(field.getModifiers())) {
				continue;
			}
			if (Modifier.isStatic(field.getModifiers()))
				continue;

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
						if (store.saveInline()) {
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
			if (Modifier.isStatic(field.getModifiers()))
				continue;
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
//					System.out.println("[Tenso antes] "+fieldFinalValue);
					int id = (int) new StorageObject(field.getType(), true).restore(fieldFinalValue);
					StorageAPI.newReference(new ReferenceValue(id, field, resultadoFinal));
//					System.out.println("[Tenso] "+id+" class "+field.getType());
					fieldFinalValue = null;
				} else if (Extra.getWrapper(field.getType()) != null) {
					fieldFinalValue = transform(fieldFinalValue, Extra.getWrapper(field.getType()));
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

	public static Object getObjectById(int id) {
		return objects.get(id);
	}

	private static int randomId() {
		return new Random().nextInt(Integer.MAX_VALUE);
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
		Iterator<ReferenceBase> loop = references.iterator();
		while (loop.hasNext()) {
			ReferenceBase next = loop.next();
			next.update();
			loop.remove();
		}
	}

	public static void newReference(ReferenceBase refer) {
		references.add(refer);
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
	}

	public static void registerPackage(Class<?> clazzForPackage) {
		registerPackage(clazzForPackage, clazzForPackage.getPackage().getName());
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
		log("-> " + getAlias(object.getClass()) + "@" + id);
	}

	public static void removeObject(int id) {
		objects.remove(id);
		log("<- " + id);
	}

	public static void removeObject(Object object) {
		objects.remove(object);
		log("<- " + getAlias(object.getClass()));
	}

	public static void disolveObject(Object object) {
		if (!isStorable(object)) {
			return;
		}
//		if (object.getClass().isPrimitive())
//			return;
//		if (object.getClass() == String.class)
//			return;
//		if (Extra.isWrapper(object.getClass()))
//			return;
//		if (Extra.isList(object.getClass()))
//			return;
//		if (Extra.isMap(object.getClass()))
//			return;
		objects.remove(object);
		for (Field field : object.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				Object fieldValue = field.get(object);
				if (fieldValue != null) {
					for (Entry<Integer, Object> entry : objects.entrySet()) {
						if (entry.getValue().equals(fieldValue)) {
							disolveObject(fieldValue);
							break;
						}
					}

				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void unregister(Class<?> claz) {
		storages.remove(claz);
		log("<- " + claz.getName());
	}

	public static Object transform(Object object, Class<?> type) throws Exception {
		String fieldTypeName = Extra.toTitle(type.getSimpleName());
		Object value = Extra.getResult(Extra.class, "to" + fieldTypeName, Extra.getParameters(Object.class), object);
		if (value instanceof String) {
			value = Extra.toChatMessage((String) value);
		}
		return value;
	}

	public static Storable getStore(Class<?> claz) {
		if (claz == null)
			return null;
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
		if (claz == null) {
			return "Empty";
		}
		Storable store = getStore(claz);
		if (store != null) {
			return store.alias();
		}
		return claz.getSimpleName();

	}

	public static Class<?> getClassByAlias(String alias) {
		if (alias == null)
			return null;
		for (Entry<Class<?>, Storable> entry : storages.entrySet()) {
			if (entry.getValue() == null) {
				System.out.println(entry.getKey());
				continue;
			}
			if (alias.equals(entry.getValue().alias())) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static boolean isRegistred(Class<?> claz) {
		return storages.containsKey(claz);
	}

	static {
		StorageAPI.register(UUID.class, new UUIDStorable());
		StorageAPI.register(Timestamp.class, new TimeStampStorable());
	}

}
