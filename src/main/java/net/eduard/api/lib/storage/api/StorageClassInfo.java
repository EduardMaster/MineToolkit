package net.eduard.api.lib.storage.api;

import net.eduard.api.lib.storage.Storable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class StorageClassInfo {

    public StorageClassInfo(Class<? > aClass){
        setCurrentClass(aClass);
    }

    public Object getPrimary(Object instance){
        try {
            return getIndex().getField().get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    private Class<?> currentClass;
    private Storable<?> storable;
    private String alias;
    private StorageInfo index;

    private final Map<Object,Object> cache = new HashMap<>();

    public Class<?> getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(Class<?> currentClass) {
        this.currentClass = currentClass;
    }

    public StorageInfo getIndex() {
        if (index == null){
            for (Field field : getCurrentClass().getDeclaredFields()){
                StorageInfo info = new StorageInfo(field);
                info.updateByType();
                info.updateByStorable();
                info.updateByField();
                if (info.isIndentifiable()){
                    index = info;
                }
            }
        }
        return index;
    }

    public String getAlias() {
        if (alias == null){
            alias = getCurrentClass().getSimpleName();
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
