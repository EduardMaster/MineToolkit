package net.eduard.api.lib.storage.references;

import java.lang.reflect.Field;

import net.eduard.api.lib.storage.StorageAPI;

public class ReferenceValue extends ReferenceBase<Integer> {


    public ReferenceValue(int id, Field field, Object instance) {
        super(field, instance);
        setRestore(id);
    }


    public void update() {
        try {

            getField().set(getInstance(), StorageAPI.getObjectById(getRestore()));

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
