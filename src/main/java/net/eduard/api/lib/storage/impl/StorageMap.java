package net.eduard.api.lib.storage.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.eduard.api.lib.storage.StorageAPI;
import net.eduard.api.lib.storage.api.StorageBase;
import net.eduard.api.lib.storage.api.StorageInfo;
import net.eduard.api.lib.storage.references.ReferenceMap;

final public class StorageMap extends StorageBase<Map<?,?> , Object> {

    public  StorageMap(){}

    @Override
    public Map<?,?> restore(StorageInfo info, Object data) {
        StorageInfo mapInfoKey = info.clone();
        mapInfoKey.setType(info.getMapKey());
        mapInfoKey.updateByType();
        mapInfoKey.updateByStoreClass();

        StorageInfo mapInfoValue = info.clone();
        mapInfoValue.setType(info.getMapValue());
        mapInfoValue.updateByType();
        mapInfoValue.updateByStoreClass();
        mapInfoValue.updateByField();

        if (info.isReference()) {
            if (data instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> oldMap = (Map<String, Object>) data;
                Map<Object, Integer> newMap = new HashMap<>();
                for (Entry<String, Object> entry : oldMap.entrySet()) {
                    newMap.put(StorageAPI.STORE_OBJECT.restore(mapInfoKey,entry.getKey()), StorageAPI.getObjectIdByReference(entry.getValue().toString()));
                }
                Map<Object, Object> mapinha = new HashMap<>();
                StorageAPI.newReference(new ReferenceMap(newMap, mapinha));
                debug("Restoring referenced map");
                return mapinha;
            }
            return null;
        }
        Map<Object, Object> newMap = new HashMap<>();
        if (data instanceof Map) {

            Map<?, ?> map = (Map<?, ?>) data;
            for (Entry<?, ?> entry : map.entrySet()) {

                Object key = StorageAPI.STORE_OBJECT.restore(mapInfoKey,entry.getKey());
                Object value = StorageAPI.STORE_OBJECT.restore(mapInfoValue,entry.getValue());
                debug("^^ " + key + " " + value);
                newMap.put(key, value);
            }
        }
        return newMap;
    }

    @Override
    public Object store(StorageInfo info, Map<?,?> data) {
        StorageInfo mapInfoKey = info.clone();
        mapInfoKey.setType(info.getMapKey());
        mapInfoKey.updateByType();
        mapInfoKey.updateByStoreClass();

        StorageInfo mapInfoValue = info.clone();
        mapInfoValue.setType(info.getMapValue());
        mapInfoValue.updateByType();
        mapInfoValue.updateByStoreClass();
        mapInfoValue.updateByField();

        Map<String, Object> newMap = new HashMap<>();
        for (Entry<?, ?> entry : data.entrySet()) {
            String key = StorageAPI.STORE_OBJECT.store(mapInfoKey,entry.getKey()).toString();

            Object value = StorageAPI.STORE_OBJECT.store(mapInfoValue,entry.getValue());
            newMap.put(key, value);
        }
        return newMap;
    }

}
