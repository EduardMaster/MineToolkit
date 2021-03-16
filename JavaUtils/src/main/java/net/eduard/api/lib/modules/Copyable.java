package net.eduard.api.lib.modules;

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
 * API de copiar objetos mais funcional que o clone() do implements Cloneable
 *
 * @author Eduard
 * @version 1.4
 *  Extra
 */
@SuppressWarnings("unchecked")
public interface Copyable {

    class CopyDebug {
        private static boolean debug = true;

        public static boolean isDebug() {
            return debug;
        }

        public static void setDebug(boolean debug) {
            CopyDebug.debug = debug;
        }
    }

    static void debug(String msg) {

        if (CopyDebug.isDebug())
            System.out.println("[Copyable] " + msg);
    }

    default Object copy() {
        return copy(this);
    }

    static <E> E clone(E object) {
        try {
            Method cloneMethod = null;
            try {
                cloneMethod = object.getClass().getMethod("clone");
            } catch (Exception e) {
                try {
                    cloneMethod = object.getClass().getDeclaredMethod("clone");
                } catch (Exception ignored) {
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

    default <E> E copy(E object) {
        return copyObject(object);
    }

    static <E> E copyObject(E object) {
        if (object == null)
            return null;

        Class<?> claz = object.getClass();

        if (Extra.isList(claz)) {
            List<Object> list = (List<Object>) object;
            List<Object> newList = new ArrayList<>();
            for (Object item : list) {
                newList.add(copyObject(item));
            }
            object = (E) newList;

        } else if (Extra.isMap(claz)) {
            Map<Object, Object> map = (Map<Object, Object>) object;
            Map<Object, Object> newMap = new HashMap<>();
            for (Entry<Object, Object> entry : map.entrySet()) {
                newMap.put(copyObject(entry.getKey()), copyObject(entry.getValue()));
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
        } else if (!Extra.isWrapper(claz)) {
            try {
                debug("COPYING " + object.getClass().getSimpleName());
                E newInstance = (E) Extra.getNew(object.getClass());

                while (!claz.equals(Object.class)) {
                    for (Field field : claz.getDeclaredFields()) {

                        if (Modifier.isStatic(field.getModifiers()))
                            continue;
                        if (Modifier.isTransient(field.getModifiers()))
                            continue;
                        field.setAccessible(true);
                        try {
                            Object value = field.get(object);
                            field.set(newInstance, copyObject(value));
                        } catch (Exception e) {
                            debug("VARIABLE FAILED " + field.getName());
                        }

                    }
                    claz = claz.getSuperclass();
                }
                object = newInstance;
            } catch (Exception e) {
                debug("FAIL " + claz.getSimpleName());
            }

        }
        return object;
    }

}
