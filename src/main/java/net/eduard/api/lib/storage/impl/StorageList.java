package net.eduard.api.lib.storage.impl;


import java.util.*;

import net.eduard.api.lib.storage.StorageAPI;
import net.eduard.api.lib.storage.api.StorageBase;
import net.eduard.api.lib.storage.api.StorageInfo;
import net.eduard.api.lib.storage.references.ReferenceList;

final public class StorageList extends StorageBase<List<?>, Object> {
    public StorageList() {
    }

    @Override
    public List<?> restore(StorageInfo info, Object data) {
        StorageInfo listInfo = info.clone();
        listInfo.setType(info.getListType());
        listInfo.updateByType();
        listInfo.updateByStorable();
        listInfo.updateByField();
        debug(">> LIST TYPE: " + listInfo.getAlias());

        if (listInfo.isReference()) {
            debug(">> BY REFERENCE LIST");
            if (data instanceof List) {
                List<?> oldList = (List<?>) data;
                List<Object> newList = new ArrayList<>();
                List<Object> realList = new ArrayList<>();
                debug(">> LIST SIZE "+ oldList.size());
                for (Object item : oldList) {
                    Object key = StorageAPI.STORE_OBJECT.restore(listInfo, item);

                    debug(">> ITEM KEY "+ key );

                    Object object = StorageAPI.getObjectByKey(listInfo.getType(), key);
                    if (object != null) {
                        debug(">> ITEM REFERENCE EXISTS");
                        realList.add(object);
                    } else {
                        debug(">> ITEM REFERENCE NOT LOEADED");
                        newList.add(key);
                    }
                }

                StorageAPI.newReference(new ReferenceList(listInfo, newList, realList));
                return realList;
            }
            return null;

        }
        List<Object> newList = new ArrayList<>();
        if (data instanceof List) {
            List<?> listOld = (List<?>) data;
            debug(">> LIST BY LIST");
            for (Object item : listOld) {
                newList.add(StorageAPI.STORE_OBJECT.restore(listInfo, item));
            }
        } else if (data instanceof Map) {
            Map<?, ?> mapOld = (Map<?, ?>) data;
            debug(">> LIST BY MAP");
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
        if (listInfo.getType() == null) {
            listInfo.setType(String.class);
        }
        listInfo.updateByType();
        listInfo.updateByStorable();
        listInfo.updateByField();
        List<Object> newList = new ArrayList<>();
        debug("<< LIST TYPE: " + listInfo.getAlias());
        for (Object item : data) {
            try {
                debug("<< AS ITEM FROM LIST");
                Object dado = StorageAPI.STORE_OBJECT.store(listInfo, item);
                newList.add(dado);
            }catch (Exception ex){
                ex.printStackTrace();
                debug("<< SAVE ITEM FROM LIST FAIL");
            }
        }
        return newList;

    }

}
