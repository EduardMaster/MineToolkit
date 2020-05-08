package net.eduard.api.lib.modules;

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
 * @author Eduard
 * @version 1.3
 * @see Extra
 */
@SuppressWarnings("unchecked")
public interface Copyable {
    /**
     * Anotação que marca que uma variavel não é para ser copiada
     *
     * @author Eduard
     * @version 1.0
     */
    @Target({java.lang.annotation.ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface NotCopyable {

    }

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

     default <E> E copy(E object) {
        if (object == null)
            return null;

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
                debug("COPYING " + object.getClass().getSimpleName());
                E newInstance = (E) object.getClass().newInstance();

                while (!claz.equals(Object.class)) {
                    for (Field field : claz.getDeclaredFields()) {

                        if (Modifier.isStatic(field.getModifiers()))
                            continue;

                        field.setAccessible(true);
                        try {
                            Object value = field.get(object);
                            if (!field.isAnnotationPresent(NotCopyable.class)) {
                                field.set(newInstance, copy(value));
                            }

                        } catch (Exception e) {
                            debug("VARIABLE FAILED " + field.getName());
                        }

                    }
                    claz = claz.getSuperclass();
                }
                object = newInstance;
            } catch (Exception e) {
                debug("FAIL " + claz.getSimpleName());
//				e.printStackTrace();
            }

        }
        return object;
    }

}
