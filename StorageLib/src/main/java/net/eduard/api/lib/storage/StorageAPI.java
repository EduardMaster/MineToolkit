package net.eduard.api.lib.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import net.eduard.api.lib.modules.Extra;
import net.eduard.storage.api.StorageClassInfo;
import net.eduard.storage.api.StorageInfo;
import net.eduard.storage.impl.*;
import net.eduard.storage.references.ReferenceBase;
import net.eduard.storage.storables.TimeStampStorable;
import net.eduard.storage.storables.UUIDStorable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;

/**
 * Sistema automatico de Armazenamento em Mapas que podem ser usados em JSON,
 * SQL, YAML<br>
 *
 * @version 1.4
 */
public final class StorageAPI {



    private StorageAPI() {
    }

    public static final StorageObject STORE_OBJECT = new StorageObject();
    public static final StorageSet STORE_SET = new StorageSet();
    public static final StorageList STORE_LIST = new StorageList();
    public static final StorageEnum STORE_ENUM = new StorageEnum();
    public static final StorageInline STORE_INLINE = new StorageInline();
    public static final StorageMap STORE_MAP = new StorageMap();
    public static final StorageArray STORE_ARRAY = new StorageArray();
    private static boolean debug = true;
    public static final String STORE_KEY = "=";
    private static final Map<Class<?>, StorageClassInfo> classInfoByClass = new LinkedHashMap<>();
    private static final Map<String, StorageClassInfo> classInfoByAlias = new LinkedHashMap<>();
    private static final List<ReferenceBase<?>> references = new ArrayList<>();


    public static String getClassName(Class<?> claz) {
        try {
            return claz.getSimpleName();
        } catch (Error error) {
            try {
                return claz.getName();
            } catch (Error error2) {
                try {
                    return claz.getTypeName();
                } catch (Error error3) {
                    return claz.getCanonicalName();
                }
            }
        }
    }

    public static StorageClassInfo getClassInfo(Class<?> clz) {
        if (clz == null) return null;
        StorageClassInfo classInfo = classInfoByClass.get(clz);
        if (classInfo == null) {
            classInfo = new StorageClassInfo(clz);
            classInfoByClass.put(clz, classInfo);
            classInfoByAlias.put(classInfo.getAlias(), classInfo);
        }
        return classInfo;
    }

    public static Map<Object, Object> getCacheOf(Class<?> clz) {
        return getClassInfo(clz).getCache();

    }

    public static Object getObjectByKey(Class<?> clz, Object key) {
        return getCacheOf(clz).get(key);
    }

    public static Object getKeyOfObject(Object object) {
        return getClassInfo(object.getClass()).getPrimary(object);
    }

    public static boolean isStorable(Class<?> claz) {
        return Storable.class.isAssignableFrom(claz);
    }


    /**
     * Atualiza as Referencias criadas e usadas na config
     */
    public static void updateReferences() {
        Iterator<ReferenceBase<?>> loop = references.iterator();
        while (loop.hasNext()) {
            ReferenceBase<?> next = loop.next();
            next.update();
            loop.remove();
        }
    }

    public static void newReference(ReferenceBase<?> reference) {
        references.add(reference);
        debug("++ REFERENCE");
    }


    public static Object store(Class<?> claz, Object object) {
        autoRegisterClass(claz);
        StorageInfo info = new StorageInfo(claz);
        info.updateByType();
        info.updateByStorable();
        return STORE_OBJECT.store(info, object);
    }

    public static Object storeField(Field field, Object object) {
        autoRegisterClass(field.getType());
        StorageInfo info = new StorageInfo(field.getType());
        info.setField(field);
        info.updateByType();
        info.updateByStorable();
        info.updateByField();
        return STORE_OBJECT.store(info, object);
    }

    public static String storeInline(Class<?> claz, Object object) {
        autoRegisterClass(claz);
        StorageInfo info = new StorageInfo(claz);
        info.updateByType();
        info.updateByStorable();
        info.setInline(true);
        return "" + STORE_OBJECT.store(info, object);
    }

    public static Object restore(Class<?> claz, Object object) {
        if (claz != null) {
            autoRegisterClass(claz);
        }
        StorageInfo info = new StorageInfo(claz);
        info.setIndentifiable(true);
        if (claz != null) {
            info.updateByType();
            info.updateByStorable();
        }
        return STORE_OBJECT.restore(info, object);
    }

    public static Object restoreField(Field field, Object object) {
        autoRegisterClass(field.getType());
        StorageInfo info = new StorageInfo(field);
        info.updateByType();
        info.updateByStorable();
        info.updateByField();
        return STORE_OBJECT.restore(info, object);
    }

    public static Object restoreInline(Class<?> claz, Object object) {
        if (claz != null) {
            autoRegisterClass(claz);
        }
        StorageInfo info = new StorageInfo(claz);
        info.updateByType();
        info.updateByStorable();
        info.setInline(true);
        return STORE_OBJECT.restore(info, object);

    }


    public static void registerStorable(Class<?> claz, Storable<?> storable) {
        StorageClassInfo info = getClassInfo(claz);
        info.setStorable(storable);
        log("ADDING CLASS " + info.getAlias());
    }

    public static Storable<?> autoRegisterClass(Class<?> claz) {
        return autoRegisterClass(claz, getClassName(claz));
    }

    public static Storable<?> autoRegisterClass(Class<?> claz, String alias) {
        Storable<?> store = null;
        try {
            if (Extra.isWrapper(claz)) {
                return null;
            } else if (Modifier.isAbstract(claz.getModifiers())) {
                debug("== ABSTRACT CLASS " + alias);
            } else if (claz.isEnum()) {
                debug("== ENUM CLASS " + alias);
            } else if (claz.isAnonymousClass()) {
                debug("== ANONYMOUS CLASS " + alias);
            } else if (claz.isInterface()) {
                debug("== INTERFACE " + alias);
            } else if (isStorable(claz)) {
                store = (Storable<?>) claz.newInstance();
                registerStorable(claz, store);
            } else {
                debug("++ AUTO CLASS " + alias);
                registerAlias(claz, alias);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return store;
    }

    /**
     * Desregistra uma classe
     *
     * @param clz Classe
     */
    public static void unregisterStorable(Class<?> clz) {
        StorageClassInfo info = classInfoByClass.get(clz);
        if (info != null) {
            classInfoByClass.remove(clz);
            classInfoByAlias.remove(info.getAlias());
            log("REMOVING CLASS " + info.getAlias());
        }
    }


    public static void unregisterPlugin(Class<?> pluginClass) {
        StorageAPI.log("- CLASSES FROM PLUGIN " + pluginClass);
        ClassLoader loader = pluginClass.getClassLoader();
        Iterator<Entry<String, StorageClassInfo>> it = classInfoByAlias.entrySet().iterator();
        String alias = getClassName(pluginClass);
        int amount = 0;
        while (it.hasNext()) {
            Entry<String, StorageClassInfo> entry = it.next();
            Class<?> classe = entry.getValue().getCurrentClass();

            if (classe.getClassLoader() == loader) {
                StorageClassInfo info = entry.getValue();
                info.getCache().clear();
                classInfoByClass.remove(info.getCurrentClass());
                it.remove();
                amount++;
            }
        }

        StorageAPI.log("REMOVING CLASSES WITH SAME LOADER OF: " + alias + " AMOUNT: " + amount);
    }


    public static Object transform(Object object, Class<?> type) throws Exception {
        String fieldTypeName = Extra.toTitle(getClassName(type));
        Object value = Extra.getMethodInvoke(Extra.class, "to" + fieldTypeName, Extra.getParametersTypes(Object.class), object);
        if (value instanceof String) {
            value = Extra.toChatMessage((String) value);
        }
        return value;
    }

    public static Storable<?> getStore(Class<?> clz) {
        if (clz == null)
            return null;
        StorageClassInfo mainInfo = getClassInfo(clz);
        Storable<?> store = mainInfo.getStorable();
        if (store == null && (!clz.isEnum()) && !Extra.isWrapper(clz)) {
            debug("GETTING STORABLE FROM MAP FOR CLASS: " + StorageAPI.getClassName(clz));
            for (Entry<Class<?>, StorageClassInfo> entry : classInfoByClass.entrySet()) {
                Class<?> loopClass = entry.getKey();
                StorageClassInfo info = entry.getValue();
                if (loopClass.equals(clz)) continue;
                if (loopClass.isAssignableFrom(clz) && info.getStorable() != null) {
                    store = info.getStorable();
                    debug("requiredClass extends LoopClass: " + StorageAPI.getClassName(loopClass));
                } else if (clz.isAssignableFrom(loopClass) && info.getStorable() != null) {
                    store = info.getStorable();
                    debug("LoopClass extends requiredClass: " + StorageAPI.getClassName(loopClass));
                }
            }
        }
        mainInfo.setStorable(store);
        return store;

    }

    public static String getAlias(Class<?> clz) {
        return getClassInfo(clz).getAlias();
    }

    public static Class<?> getClassByAlias(String alias) {
        return classInfoByAlias.get(alias).getCurrentClass();
    }

    public static boolean isRegistred(Class<?> claz) {
        return classInfoByClass.containsKey(claz);
    }

    static {
        StorageAPI.registerStorable(UUID.class, new UUIDStorable());
        StorageAPI.registerStorable(Timestamp.class, new TimeStampStorable());
    }

    public static void debug(String msg) {
        int limit = 120;
        if (msg.length() > limit) {
            msg = msg.substring(0, limit) + "...";
        }
        if (debug)
            console("[Storage] " + msg);
    }

    public static void log(String msg) {
        int limit = 120;
        if (msg.length() > limit) {
            msg = msg.substring(0, limit) + "...";
        }
        console("[StorageAPI] " + msg);
    }

    public static void console(String msg) {
        System.out.println(msg);
    }

    public static void registerAlias(Class<?> claz, String alias) {
        getClassInfo(claz).setAlias(alias);
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        StorageAPI.debug = debug;
    }

    private static Gson gson;

    public static void startGson() {
        GsonBuilder builder = new GsonBuilder()
                .serializeSpecialFloatingPointValues()
                .enableComplexMapKeySerialization();
        classInfoByClass.forEach((key, value) -> {
            Storable<?> storable = value.getStorable();
            if (storable != null) {
                if (storable instanceof JsonSerializer && storable instanceof JsonDeserializer) {
                    builder.registerTypeAdapter(key, storable);
                }
            }
        });
        gson = builder.create();
    }

    public static Gson getGson() {
        return gson;
    }

    public static <E> E loadGson(File file, Class<E> clz) {
        try {
            return gson.fromJson(new String(Files.readAllBytes(file.toPath())), clz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveGson(File file, Object data) {
        try {
            Files.write(file.toPath(), gson.toJson(data).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
