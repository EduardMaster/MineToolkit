package net.eduard.storage.impl;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.StorageAPI;
import net.eduard.storage.api.StorageBase;
import net.eduard.storage.api.StorageClassInfo;
import net.eduard.storage.api.StorageInfo;
import net.eduard.storage.references.ReferenceValue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class StorageObject extends StorageBase<Object, Object> {

    public StorageObject() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object restore(StorageInfo info, Object data) {
        Class<?> claz = info.getType();
        if (data == null) {
            debug(">> DATA NULL");
            return null;
        }
        debug(">> RAW DATA: " + data);
        String alias;
        if (data instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) data;
            if (map.containsKey(StorageAPI.STORE_KEY)) {
                alias = (String) map.get(StorageAPI.STORE_KEY);
                debug(">> RESTORING TYPE BY ALIAS: " + alias);
                Class<?> tempClass = StorageAPI.getClassByAlias(alias);
                if (tempClass != null) {
                    debug(">> TYPE FOUND");
                    claz = tempClass;
                }
            }
        }
        if (claz == null) {
            debug(">> TYPE NULL");
            return null;
        }

        if (claz.isEnum()) {
            debug(">> BY ENUM ");
            return StorageAPI.STORE_ENUM.restore(info, data.toString());
        }
        if (Extra.isList(claz)) {
            debug(">> BY LIST ");
            return StorageAPI.STORE_LIST.restore(info, data);
        }
        if (Extra.isMap(claz)) {
            debug(">> BY MAP ");
            return StorageAPI.STORE_MAP.restore(info, data);
        }
        if (Extra.isSet(claz)) {
            debug(">> BY SET ");
            return StorageAPI.STORE_SET.restore(info, data);
        }
        alias = StorageAPI.getAlias(claz);
        Storable<?> store = StorageAPI.getStore(claz);
        Class<?> wrapper = Extra.getWrapper(claz);
        if (wrapper != null) {
            try {
                Object result = StorageAPI.transform(data, wrapper);
                debug(">> BY " + wrapper.getSimpleName());

                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (info.isReference()) {
            debug(">> BY REFERENCE ");

            return StorageAPI.STORE_OBJECT.restore(info.getClassInfo().getIndex(), data);

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
                debug(">> BY CUSTOM INLINE");
                instance = store.restore(data.toString());
                if (instance == null) {
                    debug(">> CUSTOM INLINE FAIL");
                }
            }
            if (instance == null) {
                debug(">> BY AUTOMATIC INLINE");
                instance = StorageAPI.STORE_INLINE.restore(info, data.toString());
            }
            return instance;
        }
        Map<?, ?> map = (Map<?, ?>) data;
        if (store != null) {
            debug(">> NEW INSTANCE BY STORABLE");
            instance = store.newInstance();


            if (instance != null) {
                Object returned = store.restore((Map<String, Object>) map);
                if (returned != null) {
                    debug(">> RELOAD BY STORABLE");
                    return returned;
                }else{
                    debug(">> NEED CREATE restore(Map) METHOD IN STORABLE");
                }
            }else{
                debug(">> NEED CREATE newInstance() METHOD IN STORABLE");
            }
        } else {
            try {
                debug(">> NEW INSTANCE AUTOMATIC");
                instance = claz.newInstance();

            } catch (Exception ex) {
                ex.printStackTrace();
                debug(">> NEW INSTANCE AUTOMATIC FAIL");
            }
        }
        if (instance == null) {
            debug(">> NEW INSTANCE FAIL");
            // Sem instancia não tem como retornar objeto
            return null;
        }


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
                    infoField.updateByStorable();
                    infoField.updateByField();

                    debug(">> VARIABLE " + field.getName());
                    debug(">> VARIABLE TYPE " + infoField.getAlias());
                    Object restoredValue = StorageAPI.STORE_OBJECT.restore(infoField, fieldMapValue);

                    if (infoField.isReference() && restoredValue != null && (!infoField.isList())) {

                        Object object = StorageAPI.getObjectByKey(infoField.getType(), restoredValue);

                        if (object == null) {
                            StorageAPI.newReference(new ReferenceValue(infoField, instance, restoredValue));
                            continue;
                        }
                        field.set(instance, object);
                        continue;
                    }
                    if (infoField.isIndentifiable() && restoredValue != null) {
                        StorageAPI.getCacheOf(claz).put(restoredValue, instance);
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
        if (data == null) {
            debug("<< DATA NULL");
            return null;
        }
        Class<?> claz = data.getClass();
        String alias = info.getAlias();
        try {
            debug("<< DATA: " + data);
        }catch (Exception ex){
            // Pode acontecer de tentar printar o Objeto no console e causar um erro
        }
        debug("<< TYPE: " + alias);

        if (claz.isEnum()) {
            debug("<< AS ENUM");
            return StorageAPI.STORE_ENUM.store(info, (Enum<?>) data);
        }
        if (claz.isArray()) {
            debug("<< AS ARRAY");
            return StorageAPI.STORE_ARRAY.store(info, data);
        }
        if (Extra.isList(claz)) {
            debug("<< AS LIST");
            return StorageAPI.STORE_LIST.store(info, (List<?>) data);
        }
        if (Extra.isMap(claz)) {
            debug("<< AS MAP");
            return StorageAPI.STORE_MAP.store(info, (Map<?, ?>) data);
        }
        if (Extra.isSet(claz)) {
            debug(">> BY SET ");
            return StorageAPI.STORE_SET.restore(info, data);
        }
        Storable<Object> store = (Storable<Object>) getStore(claz);
        /* Se caso a Storable for nula ainda sim vai continuar
        if (store == null) {
            return data;
        }
         */



        Class<?> wrapper = Extra.getWrapper(claz);
        if (wrapper != null) {
            debug("<< AS "+ StorageAPI.getClassName(wrapper));
            return data;
        }

        if (info.isReference()) {
            Object key = StorageAPI.getKeyOfObject(data);
            debug("<< AS REFERENCE");
            StorageClassInfo classInfo = StorageAPI.getClassInfo(claz);
            classInfo.getCache().put(key, data);
            return StorageAPI.STORE_OBJECT.store(classInfo.getIndex(), key);
        }
        if (info.isInline()) {
            Object result = null;
            if (store != null) {
                result = store.store(data);
                debug("<< AS INLINE CUSTOM");
                if (result == null) {
                    debug("<< AS INLINE CUSTOM FAIL");
                } else {
                    debug("<< AS INLINE CUSTOM OK");
                }
            }
            if (result == null) {
                debug("<< AS AUTOMATIC INLINE");
                result = StorageAPI.STORE_INLINE.store(info, data);
            }
            return result;

        }
        try {
            Map<String, Object> map = new LinkedHashMap<>();
            if (store != null){
                store.store(map, data);
                if (!map.isEmpty()){
                    debug("<< AS STORABLE STORE");
                    return map;
                }
            }
            debug("<< AS OBJECT MAP");

            boolean saveType = false;

            if (info.getType() != claz|| info.isIndentifiable()) {
                saveType = true;
            }
            /*
            else{
                debug("Why not save type alias?");
                debug("<< Info Type "+ info.getType());
                debug("<< Class Type "+ claz);
                debug("<< Info indentifiable? "+ info.isIndentifiable());
            }*/
            if (saveType) {
                alias = StorageAPI.getAlias(claz);
                debug("<< AS TYPE ALIAS "+ alias);
                map.put(StorageAPI.STORE_KEY, alias);
            }
            List<Class<?>> classes = new ArrayList<>();
            while (!claz.equals(Object.class)) {
                classes.add(claz);
                claz = claz.getSuperclass();
            }
            Collections.reverse(classes);
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
                    debug("<< VARIABLE " + field.getName());
                    if (fieldValue == null) {
                        continue;
                    }

                    StorageInfo infoField = info.clone();
                    infoField.setField(field);
                    infoField.setType(field.getType());
                    infoField.updateByType();
                    infoField.updateByStorable();
                    infoField.updateByField();


                    debug("<< VARIABLE TYPE: " + StorageAPI.getClassName(fieldValue.getClass()));
                    Object fieldResult = StorageAPI.STORE_OBJECT.store(infoField, fieldValue);
                    if (fieldResult != null) {
                        if (infoField.isIndentifiable()) {
                            StorageAPI.getCacheOf(claz).put(fieldResult, data);
                        }
                        map.put(field.getName(), fieldResult);
                    }
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
