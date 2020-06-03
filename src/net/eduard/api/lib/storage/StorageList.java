package net.eduard.api.lib.storage;


import java.util.*;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.references.ReferenceList;

public class StorageList extends StorageBase<List> {
    private Class<?> listType;

    public StorageList(StorageInfo info) {
        super(info);
        if (getField() != null) {
            setListType(Extra.getTypeKey(getField().getGenericType()));
            System.out.println("TIPO DA LISTA: "+getListType());
        } else
            setListType(String.class);

    }

    @Override
    public List restore(Object data) {
        StorageObject storage = new StorageObject(getInfo().clone());
        storage.setType(listType);
        storage.updateByType();
        storage.updateByStoreClass();
        storage.updateByField();
        debug(">> LIST RESTORATION");
        if (storage.isReference()) {
            debug("  IS REFERENCE LIST");
            if (data instanceof List) {
                List<?> oldList = (List<?>) data;
                List<Integer> newList = new ArrayList<>();
                for (Object item : oldList) {
                    newList.add((Integer) Integer.parseInt(item.toString()));
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
                newList.add(storage.restore(item));
            }
        } else if (data instanceof Map) {
            Map<?, ?> mapOld = (Map<?, ?>) data;
            for (Object item : mapOld.values()) {
                Object objeto = storage.restore(item);
                newList.add(objeto);
            }

        }
        return newList;

    }

    @Override
    public Object store(List data) {

        StorageObject storage = new StorageObject(getInfo().clone());
        storage.setType(listType);
        storage.updateByType();
        storage.updateByStoreClass();
        storage.updateByField();
        debug("<< LIST STORATION");
        List<Object> newList = new ArrayList<>();
        List<?> list = (List<?>) data;

        for (Object item : list) {
            Object dado = storage.store(item);
            System.out.println("TIPO DA LISTA2 "+dado.getClass());
            newList.add(dado);

        }
        /*
        if (getStore(getListType()) != null) {
            if (!storage.isInline() && !storage.isReference()) {
                Map<String, Object> map = new LinkedHashMap<>();
                for (int index = 1; index <= newList.size(); index++) {
                    map.put("" + index, newList.get(index - 1));
                }
                return map;
            }
        }
        */
        return newList;

    }

    public Class<?> getListType() {
        return listType;
    }

    public void setListType(Class<?> listType) {
        this.listType = listType;
    }
}
