package net.eduard.api.lib.storage.storation;


import java.util.LinkedHashMap;
import java.util.Map;

final public class StorationAPI {

    private static final StorationAPI storation = new StorationAPI();


    public static StorationAPI getInstance() {
        return storation;
    }

    StorationAPI() {

        new BasicTypeStoration();
        new ObjectStoration();

    }

    private Storation<?> getStoration(Class<?> clz) {
        Storation<?> storationDesign = storations.get(clz);

        if (storationDesign == null) {
            for (Storation<?> storationMod : storations.values()) {
                if (storationMod.acceptClass(clz)) return storationMod;
            }

        }
        return null;
    }

    public Object store(Object object) {
        if (object == null) return null;
        Class<?> clz = object.getClass();
        Storation storationDesign = getStoration(clz);
        if (storationDesign != null) {
            return storationDesign.store(object);
        }


        return null;
    }

    public Object restore(Class<?> clz, Object data) {
        if (data == null) return null;

        Storation storationDesign = getStoration(clz);
        if (storationDesign != null) {
            return storationDesign.restore(clz, data);
        }


        return null;
    }

    public void register(Class<?> clz, Storation<?> storationDesign) {
        storations.put(clz, storationDesign);
    }


    private Map<Class<?>, Storation<?>> storations = new LinkedHashMap<>();
}
