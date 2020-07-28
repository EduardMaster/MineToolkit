package net.eduard.api.lib.storage.storation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class ObjectStoration implements Storation {

    ObjectStoration(){
        StorationAPI.getInstance().register(Object.class, this);
    }
    @Override
    public boolean acceptClass(Class clz) {
        for (Constructor constructor : clz.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object store(Object data) {
        Class<?> clz = data.getClass();
        Map<String, Object> map = new HashMap<>();

        for (Field field : clz.getDeclaredFields()) {
            try {
                map.put(field.getName(), StorationAPI.getInstance().store(field.get(data)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    @Override
    public Object restore(Class clz, Object data) {


        Map<String, Object> map = (Map<String, Object>) data;
        try {
            Object instance = clz.newInstance();
            for (Field field : clz.getDeclaredFields()) {
                try {
                    field.set(data, StorationAPI.getInstance().restore(field.getType(),field.get(data)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }


        return null;
    }
}
