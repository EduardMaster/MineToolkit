package net.eduard.api.setup;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
/**
 * API de Reflection simples
 * @author Eduard-PC
 *
 */
public final class RefAPI {
	private static Map<String, String> replacers = new LinkedHashMap<>();
	public static String getReplacer(String key) {
		return replacers.get(key);
	}
	public static void newReplacer(String key, String replacer) {
		replacers.put(key, replacer);
	}
	static {
		replacers.put("#s", "org.spigotmc.");
		replacers.put("#a", "net.eduard.api.");
		replacers.put("#e", "net.eduard.eduardapi.");
		replacers.put("#k", "net.eduard.api.kits.");
		replacers.put("#p", "#mPacket");
		replacers.put("#m", "net.minecraft.server.#v.");
		replacers.put("#c", "org.bukkit.craftbukkit.#v.");
		replacers.put("#s", "org.bukkit.");
	}
	public static void setValue(Object object, String name, Object value)
			throws ClassNotFoundException, IllegalArgumentException,
			IllegalAccessException {
		Field field = getField(object, name);
		field.set(object, value);
	}
	public static Field getField(Object object, String name)
			throws ClassNotFoundException {
		Class<?> claz = get(object);
		for (Field field : claz.getDeclaredFields()) {
			if (field.getName().equals(name)) {
				field.setAccessible(true);
				return field;
			}
		}
		for (Field field : claz.getFields()) {
			if (field.getName().equals(name)) {
				field.setAccessible(true);
				return field;
			}
		}
		return null;
	}

	public static Method getMethod(Object object, String name,
			Object... parameters) throws ClassNotFoundException {
		Class<?> claz = get(object);
		Class<?>[] objects = getParameters(parameters);
		for (Method method : claz.getDeclaredMethods()) {
			if (method.getName().equals(name)
					&& equals(method.getParameterTypes(),objects)) {
				method.setAccessible(true);
				return method;
			}
		}

		for (Method method : claz.getMethods()) {
			if (method.getName().equals(name)
					&& equals(method.getParameterTypes(),objects)) {
				method.setAccessible(true);
				return method;
			}
		}
		return null;
	}
	public static boolean equals(Class<?>[] firstArray,
			Class<?>[] secondArray) {
		if (firstArray.length == secondArray.length) {
			for (int i = 0; i < secondArray.length; i++) {
				if (!firstArray[i].equals(secondArray[i])) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	public static Class<?>[] getParameters(Object... parameters)
			throws ClassNotFoundException {
		Class<?>[] objects = new Class<?>[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			objects[i] = get(parameters[i]);
		}
		return objects;

	}
	public static Constructor<?> getConstructor(Object object,
			Object... parameters) throws ClassNotFoundException {
		Class<?> claz = get(object);
		Class<?>[] objects = getParameters(parameters);
		for (Constructor<?> constructor : claz.getDeclaredConstructors()) {
			if (equals(constructor.getParameterTypes(),objects)) {
				constructor.setAccessible(true);
				return constructor;
			}
		}
		for (Constructor<?> constructor : claz.getConstructors()) {
			if (equals(constructor.getParameterTypes(),objects)) {
				constructor.setAccessible(true);
				return constructor;
			}
		}
		return null;
	}
	public static Object getNew(Object object, Object... values)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			ClassNotFoundException {
		return getConstructor(object, values).newInstance(values);
	}
	public static Object getNew(Object object, Object[] parameters,
			Object... values) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, ClassNotFoundException {
		return getConstructor(object, parameters).newInstance(values);
	}

	public static Object getValue(Object object, String name)
			throws IllegalArgumentException, IllegalAccessException,
			ClassNotFoundException {
		return getField(object, name).get(object);
	}
	public static Object getResult(Object object, String name, Object... values)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, ClassNotFoundException {

		return getMethod(object, name, values).invoke(object, values);
	}
	public static Object getResult(Object object, String name,
			Object[] parameters, Object... values)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, ClassNotFoundException {

		return getMethod(object, name, parameters).invoke(object, values);
	}
	public static Class<?> get(Object object) throws ClassNotFoundException {
		if (object instanceof Class) {
			return (Class<?>) object;
		}
		if (object instanceof String) {
			String string = (String) object;
			if (string.startsWith("#")) {
				for (Entry<String, String> entry : replacers.entrySet()) {
					string = string.replace(entry.getKey(), entry.getValue());
				}
				return Class.forName(string);
			}

		}
		try {
			return (Class<?>) object.getClass().getField("TYPE").get(0);
		} catch (Exception e) {
		}
		return object.getClass();
	}

}
