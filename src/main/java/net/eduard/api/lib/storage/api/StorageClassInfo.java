package net.eduard.api.lib.storage.api;

import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.StorageAPI;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class StorageClassInfo {

    public StorageClassInfo(Class<?> aClass) {
        setCurrentClass(aClass);
    }

    public Object getPrimary(Object instance) {
        try {

            System.out.println("Index: " + getIndex());

            return getIndex()
                    .getField()
                    .get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Class<?> currentClass;
    private Storable<?> storable;
    private String alias;
    private StorageInfo index;

    private final Map<Object, Object> cache = new HashMap<>();

    public Class<?> getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(Class<?> currentClass) {
        this.currentClass = currentClass;
    }

    public StorageInfo getIndex() {
        if (index != null) return index;
        // Busca o Index field das classes supers
        Class<?> clz = getCurrentClass();
        while (!clz.equals(Object.class)) {
            for (Field field : clz.getDeclaredFields()) {
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
                StorageInfo info = new StorageInfo(field);

                info.updateByType();
                info.updateByStorable();
                info.updateByField();
                if (info.isIndentifiable()) {
                    index = info;
                    break;
                }
            }
            clz = clz.getSuperclass();
        }


        return index;
    }

    public String getAlias() {
        if (alias == null) {
            alias = StorageAPI.getClassName(getCurrentClass());
        }
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Map<Object, Object> getCache() {
        return cache;
    }

    public Storable<?> getStorable() {
        return storable;
    }

    public void setStorable(Storable<?> storable) {
        this.storable = storable;
    }
}
