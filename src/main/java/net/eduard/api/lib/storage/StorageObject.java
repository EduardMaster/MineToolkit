package net.eduard.api.lib.storage;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.references.ReferenceValue;
import org.bukkit.Bukkit;

public class StorageObject extends StorageBase<Object, Object> {

    StorageObject() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object restore(StorageInfo info, Object data) {
        Class<?> claz = info.getType();
        if (data == null) {
            debug(">> DATA NULL");
            return null;
        }

        int id = 0;
        String alias;
        if (data instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) data;
            if (map.containsKey(StorageAPI.STORE_KEY)) {
                String text = (String) map.get(StorageAPI.STORE_KEY);
                debug(">> RESTORING TYPE AND ALIAS BY : " + text);
                if (text.contains(StorageAPI.REFER_KEY)) {
                    String[] split = text.split(StorageAPI.REFER_KEY);
                    alias = split[0];
                    try {
                        id = Extra.toInt(split[1]);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {
                    alias = text;
                }

                Class<?> tempClass = StorageAPI.getClassByAlias(alias);
                if (tempClass != null) {
                    debug(">> RESTORED TYPE " + tempClass.getSimpleName());
                    claz = tempClass;
                }
                debug(">> RESTORED ALIAS " + alias);
                debug(">> RESTORED ID " + id);

            }


        }
        if (claz == null) {
            debug(">> CLASS NULL ");
            return null;
        }
        if (claz.isEnum()) {
            debug(">> ENUM " + data);

            return StorageAPI.STORE_ENUM.restore(info, data.toString());
        }
        if (Extra.isList(claz)) {
            debug(">> LIST " + data);
            return StorageAPI.STORE_LIST.restore(info, data);
        }
        if (Extra.isMap(claz)) {
            debug(">> MAP " + data);
            return StorageAPI.STORE_MAP.restore(info, data);
        }

        if (info.isReference()) {

            debug(">> REFERENCE " + data);
            return StorageAPI.getObjectIdByReference(data.toString());


        }


        alias = StorageAPI.getAlias(claz);

        Storable<?> store = StorageAPI.getStore(claz);


        Class<?> wrapper = Extra.getWrapper(claz);

        if (wrapper != null) {
            try {
                debug(">> RESTORING DATA FROM " + data);
                Object result = StorageAPI.transform(data, wrapper);

                debug(">> DATA RESTORED " + data);
                return result;
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        Object instance = null;


		/* mesmo se a Storable esteja nula vai continuar recarregando da config
		if (store == null) {
			debug(">> NULL STORABLE ");
			return null;
		}
		*/


        if (info.isInline()) {
            if (store != null) {
                instance = store.restore(data.toString());

            }
            if (instance == null) {
                debug(">> INLINE  " + data);
                instance = StorageAPI.STORE_INLINE.restore(info, data.toString());
            } else {
                debug(">> INLINE CUSTOM " + data);
            }


            return instance;
        }
        if (id == 0) {
            id = StorageAPI.newId();
        }
        if (store != null) {
            instance = store.newInstance();
        } else {
            try {
                debug(">> NEW INSTANCE");
                instance = claz.newInstance();

            } catch (Exception ex) {
                ex.printStackTrace();
                debug(">> NEW INSTANCE FAIL");
            }
        }

        if (instance == null) {
            // Sem instancia não tem como retornar objeto
            return null;
        }else{
            if (!instance.getClass().isAssignableFrom(claz)){
                debug(" NEED CREATE newInstance() METHOD IN STORABLE");
                return null;
            }
        }

        Map<?, ?> map = (Map<?, ?>) data;

        if (info.isIndentifiable())
            StorageAPI.registerObject(id, instance);
        while (!claz.equals(Object.class)) {

            for (Field field : claz.getDeclaredFields()) {

                if (Modifier.isStatic(field.getModifiers()))
                    continue;
                if (Modifier.isTransient(field.getModifiers()))
                    continue;
                if (Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                if (Modifier.isNative(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);

                try {

                    Object fieldMapValue = map.get(field.getName());

                    if (fieldMapValue == null)
                        continue;
                    StorageInfo infoField = info.clone();
                    infoField.setField(field);
                    infoField.setType(field.getType());
                    infoField.updateByType();
                    infoField.updateByStoreClass();
                    infoField.updateByField();

                    debug(">> VARIABLE " + field.getName() + " " + field.getType().getSimpleName());
                    Object restoredValue = StorageAPI.STORE_OBJECT.restore(infoField, fieldMapValue);
                    if (infoField.isReference()) {
                        if (restoredValue != null && (!(restoredValue instanceof List || restoredValue instanceof Map))) {
                            StorageAPI.newReference(new ReferenceValue((Integer) restoredValue, field, instance));
                            continue;
                        }
                    }

                    try {
                        if (restoredValue != null)
                            field.set(instance, restoredValue);

                    } catch (Exception e) {
                        e.printStackTrace();
                        //debug(">> FAILED TO SET VARIABLE " + field.getName() + " USING " + restoredValue);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            claz = claz.getSuperclass();
        }

        if (instance instanceof Storable) {
            Storable<?> storable = (Storable<?>) instance;
            storable.restore((Map<String, Object>) map);

        }
        if (store != null) {
            Object returned = store.restore((Map<String, Object>) map);
            if (returned != null) {
                instance = returned;
            }
        }
        return instance;
    }

    @Override
    public Object store(StorageInfo info, Object data) {
        if (data == null)
            return null;
        Class<?> claz = data.getClass();

        String alias = StorageAPI.getAlias(claz);
        if (claz.isEnum()) {
            debug("<< ENUM " + data);

            return StorageAPI.STORE_ENUM.store(info, (Enum<?>) data);

        }
        if (claz.isArray()) {
            debug("<< ARRAY " + data);
            return StorageAPI.STORE_ARRAY.store(info, data);
        }
        if (Extra.isList(claz)) {
            debug("<< LIST " + new ArrayList<>((List<?>) data));

            return StorageAPI.STORE_LIST.store(info, (List<?>) data);
        }
        if (Extra.isMap(claz)) {
            return StorageAPI.STORE_MAP.store(info, (Map<?, ?>) data);
        }
        Storable<Object> store = (Storable<Object>) getStore(claz);


        /* Se caso a Storable for nula ainda sim vai continuar
        if (store == null) {
            return data;
        }
         */

        if (info.isReference()) {

            String text = alias + StorageAPI.REFER_KEY + StorageAPI.getIdByObject(data);
            debug("<< REFERENCE " + text);
            return text;
        }
        Class<?> wrapper = Extra.getWrapper(claz);
        if (wrapper != null) {
            return data;
        }
        if (info.isInline()) {

            Object result = null;
            if (store != null) {
                result = store.store(data);
                debug("<< INLINE CUSTOM " + result);
            }
            if (result == null) {
                debug("<< INLINE " + data);
                return StorageAPI.STORE_INLINE.store(info, data);
            } else {
                return result;
            }
        }

        try {
            Map<String, Object> map = new LinkedHashMap<>();
            if (info.isIndentifiable()) {
                debug("<< INDENTIFIABLE " + claz);
                map.put(StorageAPI.STORE_KEY, alias + StorageAPI.REFER_KEY + StorageAPI.getIdByObject(data));
            } else {
                map.put(StorageAPI.STORE_KEY, alias);
            }

            List<Class<?>> classes = new ArrayList<>();
            while (!claz.equals(Object.class)) {
                classes.add(claz);
                claz = claz.getSuperclass();
            }
            Collections.reverse(classes);

            //
            /*
            classes.stream()
                    .flatMap(aClass ->
                            Arrays.stream(aClass.getDeclaredFields())
                                    .peek(field -> {
                                        field.setAccessible(true);
                                    })
                    )
                    .filter(field -> !Modifier.isStatic(field.getModifiers())
                            && !Modifier.isTransient(field.getModifiers())
                            && !Modifier.isFinal(field.getModifiers())
                            && !Modifier.isNative(field.getModifiers()))
                    .forEach(field -> {


                    });



             */
            //

            for (Class<?> clz : classes) {
                claz = clz;
                for (Field field : claz.getDeclaredFields()) {

                    field.setAccessible(true);

                    if (Modifier.isStatic(field.getModifiers()))
                        continue;
                    if (Modifier.isTransient(field.getModifiers()))
                        continue;
                    if (Modifier.isFinal(field.getModifiers())) {
                        continue;
                    }
                    if (Modifier.isNative(field.getModifiers())) {
                        continue;
                    }
                    Object fieldValue = field.get(data);
                    if (fieldValue == null) {
                        continue;
                    }

                    StorageInfo infoField = info.clone();
                    infoField.setField(field);
                    infoField.setType(field.getType());
                    infoField.updateByType();
                    infoField.updateByStoreClass();
                    infoField.updateByField();

                    debug("<< VARIABLE " + field.getName() + " " + fieldValue.getClass().getSimpleName());
                    Object fieldResult = StorageAPI.STORE_OBJECT.store(infoField, fieldValue);

                    if (fieldResult != null)
                        map.put(field.getName(), fieldResult);

                }

            }
            if (data instanceof Storable) {
                Storable<Object> dataStorable = (Storable<Object>) data;
                dataStorable.store(map, data);

            } else if (store != null) {
                store.store(map, data);
            }
            return map;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
