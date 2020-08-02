package net.eduard.api.lib.storage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.java_storables.TimeStampStorable;
import net.eduard.api.lib.storage.java_storables.UUIDStorable;
import net.eduard.api.lib.storage.references.ReferenceBase;

/**
 * Sistema automatico de Armazenamento em Mapas que podem ser usados em JSON,
 * SQL, YAML<br>
 *
 * @author Eduard
 * @version 1.3
 * @since Lib v1.0
 */
public final class StorageAPI {

    private StorageAPI() {

    }

    static final StorageObject STORE_OBJECT = new StorageObject();
    static final StorageList STORE_LIST = new StorageList();
    static final StorageEnum STORE_ENUM = new StorageEnum();
    static final StorageInline STORE_INLINE = new StorageInline();
    static final StorageMap STORE_MAP = new StorageMap();
    static final StorageArray STORE_ARRAY = new StorageArray();

    private static boolean debug = true;
    public static final String STORE_KEY = "=";
    public static final String REFER_KEY = "@";
    private static final Map<Class<?>, Storable<?>> storages = new LinkedHashMap<>();
    private static final Map<Class<?>, String> aliases = new LinkedHashMap<>();
    private static final Map<Integer, Object> objects = new LinkedHashMap<>();
    private static final List<ReferenceBase<?>> references = new ArrayList<>();

    public static Map<Class<?>, Storable<?>> getStorages() {
        return storages;
    }

    static boolean isStorable(Class<?> claz) {
        return Storable.class.isAssignableFrom(claz);
    }

    public static Object getObjectById(int id) {
        return objects.get(id);
    }

    private static final Random random = new Random();

    private static int randomId() {
        return random.nextInt(100000);
    }

    public static int newId() {
        int id;
        do {
            id = randomId();
        } while (objects.containsKey(id));
        return id;
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

    public static void newReference(ReferenceBase<?> refer) {
        references.add(refer);
        debug("<<----->> REFERENCE LINKED");
    }

    public static int getIdByObject(Object object) {
        for (Entry<Integer, Object> entry : objects.entrySet()) {
            if (entry.getValue().equals(object)) {
                return entry.getKey();
            }
        }
        int id = newId();
        objects.put(id, object);
        debug("<<>>" + object.getClass().getSimpleName() + "@" + id);
        return id;
    }

    public static Object store(Class<?> claz, Object object) {
        autoRegisterClass(claz);
        StorageInfo info = new StorageInfo(claz);
        info.updateByType();
        info.updateByStoreClass();
        return STORE_OBJECT.store(info, object);
    }

    public static String storeInline(Class<?> claz, Object object) {
        autoRegisterClass(claz);
        StorageInfo info = new StorageInfo(claz);

        info.updateByType();
        info.updateByStoreClass();
        info.setInline(true);


        return ""+ STORE_OBJECT.store(info, object);
    }

    public static Object restore(Class<?> claz, Object object) {
        if (claz != null) {
            autoRegisterClass(claz);
        }
        StorageInfo info = new StorageInfo(claz);

        info.setIndentifiable(true);

        if (claz != null) {
            info.updateByType();
            info.updateByStoreClass();
        }
        return STORE_OBJECT.restore(info, object);
    }

    public static Object restoreInline(Class<?> claz, Object object) {
        if (claz != null) {
            autoRegisterClass(claz);
        }
        StorageInfo info = new StorageInfo(claz);
        if (claz != null) {
            info.updateByType();
            info.updateByStoreClass();
        }
        info.setInline(true);
        return STORE_OBJECT.restore(info, object);

    }



    public static void register(Class<? extends Storable<?>> claz) {

        autoRegisterClass(claz);
    }

    public static void register(Class<? extends Storable<?>> claz, String alias) {
        autoRegisterClass(claz, alias);
        registerAlias(claz, alias);
    }

    public static void register(Class<?> claz, Storable<?> storable) {
        storages.put(claz, storable);
        aliases.put(claz, claz.getSimpleName());
        debug("++ CLASS " + claz.getSimpleName());
    }

    public static void registerPackage(Class<?> clazzForPackage) {
        registerPackage(clazzForPackage, clazzForPackage.getPackage().getName());
    }

    public static void registerPackage(Class<?> clazzPlugin, String pack) {
        debug("<> PACKAGE " + pack);

        List<Class<?>> classes = Extra.getClasses(clazzPlugin, pack);
        for (Class<?> claz : classes) {
            autoRegisterClass(claz);
        }
    }

    public static Storable<?> autoRegisterClass(Class<?> claz) {

        return autoRegisterClass(claz, claz.getSimpleName());
    }

    public static Storable<?> autoRegisterClass(Class<?> claz, String alias) {
        Storable<?> store = null;
        try {
            if (Extra.isWrapper(claz)) {
                return null;
            } else if (Modifier.isAbstract(claz.getModifiers())) {
                debug("== ABSTRACT CLASS " + claz.getSimpleName());
            } else if (claz.isEnum()) {
                debug("== ENUM CLASS " + claz.getSimpleName());
            } else if (claz.isAnonymousClass()) {
                debug("== ANONYMOUS CLASS " + claz.getSimpleName());
            } else if (claz.isInterface()) {
                debug("== INTERFACE " + claz.getSimpleName());
            } else if (isStorable(claz)) {
                store = (Storable<?>) claz.newInstance();
                register(claz, store);
            } else {
                debug("== AUTO CLASS " + claz.getSimpleName());
                registerAlias(claz, alias);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return store;
    }

    /**
     * Registra todas classes internas com StorageAPI#autoRegisterClass
     *
     * @param claz Classe com classes internas
     */
    public static void registerClasses(Class<?> claz) {
        debug("<> CLASSES " + claz.getName());
        for (Class<?> anotherClass : claz.getDeclaredClasses()) {
            autoRegisterClass(anotherClass);
        }
    }

    public static void registerObject(int id, Object object) {
        objects.put(id, object);
        debug("+ OBJECT " + getAlias(object.getClass()) + "@" + id);
    }

    public static void unregisterObjectById(int id) {
        objects.remove(id);
        debug("- OBJECT @" + id);
    }

    public static void unregisterObject(Object object) {
        objects.remove(object);
        debug("- OBJECT " + getAlias(object.getClass()));
    }

    public static void unregisterStorable(Class<?> claz) {
        storages.remove(claz);
        debug("- CLASS " + claz.getName());

    }


    public static Object transform(Object object, Class<?> type) throws Exception {
        String fieldTypeName = Extra.toTitle(type.getSimpleName());
        Object value = Extra.getMethodInvoke(Extra.class, "to" + fieldTypeName, Extra.getParameters(Object.class), object);
        if (value instanceof String) {
            value = Extra.toChatMessage((String) value);
        }
        return value;
    }

    public static int getObjectIdByReference(String reference) {
        if (reference.contains(StorageAPI.REFER_KEY)) {
            return Extra.toInt(reference.split(REFER_KEY)[1]);
        }
        return Extra.toInt(reference);
    }

    public static Storable<?> getStore(Class<?> claz) {
        if (claz == null)
            return null;

        Storable<?> store = storages.get(claz);


        if (store == null) {
            for (Entry<Class<?>, Storable<?>> entry : storages.entrySet()) {
                Class<?> loopClass = entry.getKey();
                if (loopClass.isAssignableFrom(claz)) {
                    store = entry.getValue();
                } else if (claz.isAssignableFrom(loopClass)) {
                    store = entry.getValue();
                }

            }
        }
        return store;

    }

    public static String getAlias(Class<?> claz) {
        return aliases.getOrDefault(claz, getClassName(claz));

    }

    public static String getClassName(Class<?> claz) {
        try {

            return claz.getSimpleName();
        } catch (Error err) {
            return claz.toString();
        }
    }

    public static Class<?> getClassByAlias(String alias) {
        for (Entry<Class<?>, String> entry : aliases.entrySet()) {
            if (entry.getValue().equals(alias)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static boolean isRegistred(Class<?> claz) {
        return storages.containsKey(claz);
    }

    public static Map<Class<?>, String> getAliases() {
        return aliases;
    }

    static {
        StorageAPI.register(UUID.class, new UUIDStorable());
        StorageAPI.register(Timestamp.class, new TimeStampStorable());
    }

    public static void debug(String msg) {
        if (debug)
            System.out.println("[Storage] " + msg);
    }

    public static void registerAlias(Class<?> claz, String alias) {
        aliases.put(claz, alias);
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        StorageAPI.debug = debug;
    }

    private static Gson gson;

    public static void startGson() {
        GsonBuilder b = new GsonBuilder()

                .serializeSpecialFloatingPointValues()
                .enableComplexMapKeySerialization();
        storages.forEach((key, value) -> {
            if (value instanceof JsonSerializer && value instanceof JsonDeserializer) {
                b.registerTypeAdapter(key, value);
            }
        });
        gson = b.create();


    }

    public static <E> E loadGson(File arquivo, Class<E> clz) {
        try {
            return gson.fromJson(new String(Files.readAllBytes(arquivo.toPath())), clz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveGson(File arquivo, Object data) {
        try {
            Files.write(arquivo.toPath(), gson.toJson(data).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
