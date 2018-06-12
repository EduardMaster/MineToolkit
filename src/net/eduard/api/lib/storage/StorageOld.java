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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.eduard.api.lib.Extra;
@SuppressWarnings("unchecked")
@Deprecated
public class StorageOld extends StorageAPI {
	@Target({ java.lang.annotation.ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Variable {

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
//					references.add(new Refer(field, instance, newList));
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
//					references.add(new Refer(field, instance, newMap));
				} else {
					return restoreMap(value, Extra.getTypeKey(type), Extra.getTypeValue(type));
				}
			} else if (isRefer) {
//				references.add(new Refer(field, instance, Extra.toInt(value.toString().split(REFER_KEY)[1])));
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
					StorageAPI.addObject(id, instance);

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
						StorageAPI.addObject(id, instance);
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
//										references.add(new Refer(variavel, instance, fieldRestored));
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
