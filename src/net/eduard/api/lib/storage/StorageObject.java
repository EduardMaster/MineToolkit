package net.eduard.api.lib.storage;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.references.ReferenceValue;

public class StorageObject extends StorageBase {

    public StorageObject(StorageInfo info) {
        super(info);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object restore(Object data) {
        Class<?> claz = getType();
        if (data == null) {
            debug(">> DATA NULL");
            return null;
        }

        int id = 0;
        String alias = null;
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

                    } catch (Exception e) {
                        e.printStackTrace();
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

            } else {
            }


        }
        if (claz == null) {
            debug(">> CLASS NULL ");
            return null;
        }
        if (claz.isEnum()) {
            debug(">> ENUM " + data);
            return new StorageEnum(getInfo().clone()).restore(data);
        }
        if (isReference()) {

            debug(">> REFERENCE " + data);
            return StorageAPI.getObjectIdByReference(data.toString());


        }
        if (Extra.isList(claz)) {
            debug(">> LIST " + data);
            return new StorageList(getInfo().clone()).restore(data);
        }
        if (Extra.isMap(claz)) {
            debug(">> MAP " + data);
            return new StorageMap(getInfo().clone()).restore(data);
        }


        alias = StorageAPI.getAlias(claz);

        Storable store = StorageAPI.getStore(claz);

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


        if (isInline()) {
            if (store != null) {
                instance = store.restore(data.toString());

            }
            if (instance == null) {
                debug(">> INLINE  " + data);
                instance = new StorageInline(getInfo().clone()).restore(data);
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
            } catch (Exception e) {

            }
        }
        if (instance == null) {
            // Sem instancia não tem como retornar objeto
            return null;
        }

        Map<?, ?> map = (Map<?, ?>) data;

        if (isIndentifiable())
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
                    StorageObject storage = new StorageObject(getInfo().clone());
                    storage.setField(field);
                    storage.setType(field.getType());
                    storage.updateByType();
                    storage.updateByStoreClass();
                    storage.updateByField();

                    debug(">> VARIABLE " + field.getName() + " " + field.getType().getSimpleName());
                    Object restoredValue = storage.restore(fieldMapValue);
                    if (storage.isReference()) {
                        if (restoredValue != null) {
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
            Storable storable = (Storable) instance;
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
    public Object store(Object data) {
        if (data == null)
            return null;
        Class<?> claz = data.getClass();

        String alias = StorageAPI.getAlias(claz);
        if (claz.isEnum()) {
            debug("<< ENUM " + data);
            return new StorageEnum(getInfo().clone()).store(data);

        }
        if (claz.isArray()) {
            debug("<< ARRAY " + data);
            return new StorageArray(getInfo().clone()).store(data);
        }
        if (Extra.isList(claz)) {
            debug("<< LIST " + new ArrayList<>((List<?>) data));

            return new StorageList(getInfo().clone()).store((List) data);
        }
        if (Extra.isMap(claz)) {
            return new StorageMap(getInfo().clone()).store(data);
        }
        Storable store = getStore(claz);


        /* Se caso a Storable for nula ainda sim vai continuar
        if (store == null) {
            return data;
        }
         */

        if (isReference()) {

            String text = alias + StorageAPI.REFER_KEY + StorageAPI.getIdByObject(data);
            debug("<< REFERENCE " + text);
            return text;
        }
        Class<?> wrapper = Extra.getWrapper(claz);
        if (wrapper != null) {
            return data;
        }
        if (isInline()) {

            Object result = null;
            if (store != null) {
                result = store.store(data);
                debug("<< INLINE CUSTOM " + result);
            }
            if (result == null) {
                debug("<< INLINE " + data);
                return new StorageInline(getInfo().clone()).store(data);
            } else {
                return result;
            }
        }

        try {
            Map<String, Object> map = new LinkedHashMap<>();
            if (isIndentifiable()) {
                debug("<< INDENTIFIABLE " + claz);
                map.put(StorageAPI.STORE_KEY, alias + StorageAPI.REFER_KEY + StorageAPI.getIdByObject(data));
            } else {
                map.put(StorageAPI.STORE_KEY, alias);
            }
            while (!claz.equals(Object.class)) {
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

                    StorageObject storage = new StorageObject(getInfo().clone());
                    storage.setField(field);
                    storage.setType(field.getType());
                    storage.updateByType();
                    storage.updateByStoreClass();
                    storage.updateByField();

                    debug("<< VARIABLE " + field.getName() + " " + fieldValue.getClass().getSimpleName());
                    Object fieldResult = storage.store(fieldValue);

                    if (fieldResult != null)
                        map.put(field.getName(), fieldResult);

                }
                claz = claz.getSuperclass();
            }
            if (data instanceof Storable) {
                Storable dataStorable = (Storable) data;
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
