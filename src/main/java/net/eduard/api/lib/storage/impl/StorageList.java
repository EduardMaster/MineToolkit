package net.eduard.api.lib.storage.impl;


import java.util.*;

import net.eduard.api.lib.storage.StorageAPI;
import net.eduard.api.lib.storage.api.StorageBase;
import net.eduard.api.lib.storage.api.StorageInfo;
import net.eduard.api.lib.storage.references.ReferenceList;

final public class StorageList extends StorageBase<List<?>, Object> {
    public  StorageList(){}

    @Override
    public List<?> restore(StorageInfo info, Object data) {
        StorageInfo listInfo = info.clone();
        listInfo.setType(info.getListType());
        listInfo.updateByType();
        listInfo.updateByStoreClass();
        listInfo.updateByField();

        debug(">> LIST RESTORATION");
        if (listInfo.isReference()) {
            debug("  IS REFERENCE LIST");
            if (data instanceof List) {
                List<?> oldList = (List<?>) data;
                List<Integer> newList = new ArrayList<>();
                for (Object item : oldList) {

                    newList.add(StorageAPI.getObjectIdByReference(item.toString()));
                }
                List<Object> list = new ArrayList<>();
                StorageAPI.newReference(new ReferenceList(newList, list));
                return list;
            }
            return null;

        }
        List<Object> newList = new ArrayList<>();
        if (data instanceof List) {
            List<?> listOld = (List<?>) data;
            for (Object item : listOld) {
                newList.add(StorageAPI.STORE_OBJECT.restore(listInfo, item));
            }
        } else if (data instanceof Map) {
            Map<?, ?> mapOld = (Map<?, ?>) data;
            debug("RESTORING LIST BY MAP");
            for (Object item : mapOld.values()) {
                Object objeto = StorageAPI.STORE_OBJECT.restore(listInfo, item);
                newList.add(objeto);
            }

        }
        return newList;

    }

    public Object store(StorageInfo info, List<?> data) {
        StorageInfo listInfo = info.clone();
        listInfo.setType(info.getListType());
        if (listInfo.getType() == null){
            listInfo.setType(String.class);
        }
        listInfo.updateByType();
        listInfo.updateByStoreClass();
        listInfo.updateByField();

        debug("<< LIST STORATION");
        List<Object> newList = new ArrayList<>();

        for (Object item : data) {
            Object dado = StorageAPI.STORE_OBJECT.store(listInfo, item);
            newList.add(dado);
        }
        return newList;

    }

}
