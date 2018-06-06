package net.eduard.api.lib;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Sistema de copiar objetos
 * 
 * @see Extra
 * @author Eduard
 *
 */
@SuppressWarnings("unchecked")
public interface Copyable {

	@Target({ java.lang.annotation.ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface NoCopyable {

	}

	public default Object copy() {
		return copy(this);
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
				Object value = cloneMethod.invoke(object);
				return (E) value;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}

	default void onCopy() {
	}

	public default <E> E copy(E object) {

		Class<? extends Object> claz = object.getClass();
		// System.out.println("classe a ser copiada " + object + " e array? " +
		// claz.isArray());
		if (Extra.isList(claz)) {
			List<Object> list = (List<Object>) object;
			List<Object> newList = new ArrayList<>();
			for (Object item : list) {
				newList.add(copy(item));
			}
			object = (E) newList;

		} else if (Extra.isMap(claz)) {
			Map<Object, Object> map = (Map<Object, Object>) object;
			Map<Object, Object> newMap = new HashMap<>();
			for (Entry<Object, Object> entry : map.entrySet()) {
				newMap.put(copy(entry.getKey()), copy(entry.getValue()));
			}
			object = (E) newMap;
		} else if (Extra.isCloneable(claz)) {
			object = clone(object);
		} else if (claz.isArray()) {
			int len = Array.getLength(object);
			Object newArray = Array.newInstance(object.getClass().getComponentType(), len);
			for (int index = 0; index < len; index++) {
				Array.set(newArray, index, Array.get(object, index));
			}
		} else if (Extra.isWrapper(claz) || Extra.isString(claz)) {

		} else {
			try {

				E newInstance = (E) object.getClass().newInstance();
				while (!claz.equals(Object.class)) {
					for (Field field : claz.getDeclaredFields()) {
						if (Modifier.isStatic(field.getModifiers()))
							continue;
						if (field.isAnnotationPresent(NoCopyable.class))
							continue;

						field.setAccessible(true);
						try {
							Object value = field.get(object);
							if (value != null) {
								field.set(newInstance, copy(value));
							}

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

}
