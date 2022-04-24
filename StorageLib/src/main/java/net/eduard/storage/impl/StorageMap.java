package net.eduard.storage.impl;

import net.eduard.api.lib.storage.StorageAPI;
import net.eduard.storage.api.StorageBase;
import net.eduard.storage.api.StorageInfo;
import net.eduard.storage.references.ReferenceMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

final public class StorageMap extends StorageBase<Map<?, ?>, Object> {

    public StorageMap() {
    }

    @Override
    public Map<?, ?> restore(StorageInfo info, Object data) {
        StorageInfo mapInfoKey = info.clone();
        mapInfoKey.setType(info.getMapKey());
        mapInfoKey.updateByType();
        mapInfoKey.updateByStorable();
        mapInfoKey.updateByField();
        mapInfoKey.setInline(true);

        StorageInfo mapInfoValue = info.clone();
        mapInfoValue.setType(info.getMapValue());
        mapInfoValue.updateByType();
        mapInfoValue.updateByStorable();
        mapInfoValue.updateByField();

        if (info.isReference() || info.isReferenceMapValue()) {

            if (data instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<Object, Object> oldMap = (Map<Object, Object>) data;
                Map<Object, Object> newMap = new HashMap<>();
                for (Entry<Object, Object> entry : oldMap.entrySet()) {
                    Object entryKey = StorageAPI.STORE_OBJECT.restore(mapInfoKey, entry.getKey());
                    Object entryValue = StorageAPI.STORE_OBJECT.restore(mapInfoValue, entry.getValue());
                    newMap.put(entryKey, entryValue);
                }
                Map<Object, Object> realMap = new HashMap<>();
                StorageAPI.newReference(new ReferenceMap(info, mapInfoKey, mapInfoValue, newMap, realMap));
                debug("Restoring referenced map");
                return realMap;
            }
            return null;
        }
        Map<Object, Object> newMap = new HashMap<>();
        if (data instanceof Map) {

            Map<?, ?> map = (Map<?, ?>) data;
            for (Entry<?, ?> entry : map.entrySet()) {

                Object key = StorageAPI.STORE_OBJECT.restore(mapInfoKey, entry.getKey());
                Object value = StorageAPI.STORE_OBJECT.restore(mapInfoValue, entry.getValue());
                debug("^^ " + key + " " + value);
                newMap.put(key, value);
            }
        }
        return newMap;
    }

    @Override
    public Object store(StorageInfo info, Map<?, ?> data) {
        StorageInfo mapInfoKey = info.clone();
        mapInfoKey.setType(info.getMapKey());
        mapInfoKey.updateByType();
        mapInfoKey.updateByStorable();
        mapInfoKey.updateByField();
        mapInfoKey.setInline(true);
        mapInfoKey.setReference(false);

        StorageInfo mapInfoValue = info.clone();
        mapInfoValue.setType(info.getMapValue());
        mapInfoValue.updateByType();
        mapInfoValue.updateByStorable();
        mapInfoValue.updateByField();
        debug("<< MAP KEY TYPE: " + mapInfoKey.getAlias());
        debug("<< MAP VALUE TYPE: " + mapInfoValue.getAlias());
        Map<String, Object> newMap = new HashMap<>();
        for (Entry<?, ?> entry : data.entrySet()) {
            if (entry.getKey() == null) {
                System.out.println("Chave do HashMap NULL");
                continue;
            }
            if (entry.getKey() instanceof Enum) {
                // System.out.println("Enum: "+entry.getKey());
            }
            debug("<< KEY FROM MAP");
            String key = StorageAPI.STORE_OBJECT.store(mapInfoKey, entry.getKey()).toString();
            debug("<< VALUE FROM MAP");
            Object value = StorageAPI.STORE_OBJECT.store(mapInfoValue, entry.getValue());
            newMap.put(key, value);
        }
        return newMap;
    }

}
