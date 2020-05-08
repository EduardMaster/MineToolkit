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
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Sistema automatico de Armazenamento em Mapas que podem ser usados em JSON,
 * SQL, YAML<br>
 *
 * @author Eduard
 * @version 1.3
 * @since Lib v1.0
 */
public class StorageAPI {


    private static boolean debug = true;
    public static String STORE_KEY = "=";
    public static String REFER_KEY = "@";
    private static Map<Class<?>, Storable> storages = new LinkedHashMap<>();
    private static Map<Class<?>, String> aliases = new LinkedHashMap<>();
    private static Map<Integer, Object> objects = new LinkedHashMap<>();
    private static List<ReferenceBase> references = new ArrayList<>();

    public static boolean isStorable(Object object) {

        return object instanceof Storable;
    }

    public static boolean isStorable(Class<?> claz) {
        return Storable.class.isAssignableFrom(claz);
    }

    public static Object getObjectById(int id) {
        return objects.get(id);
    }

    private static int randomId() {
        return new Random().nextInt(100000);
    }

    public synchronized static int newId() {
        int id = 0;
        do {
            id = randomId();
        } while (objects.containsKey(id));
        return id;
    }

    /**
     * Atualiza as Referencias criadas e usadas na config
     */
    public static void updateReferences() {
        Iterator<ReferenceBase> loop = references.iterator();
        while (loop.hasNext()) {
            ReferenceBase next = loop.next();
            next.update();
            loop.remove();
        }
    }

    public synchronized static void newReference(ReferenceBase refer) {
        references.add(refer);
        debug("<<----->> REFERENCE LINKED");
    }

    public synchronized static int getIdByObject(Object object) {
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

        StorageObject storeSystem = new StorageObject(new StorageInfo(claz));
        storeSystem.updateByType();
        storeSystem.updateByStoreClass();
        return storeSystem.store(object);
    }

    public static Object restore(Class<?> claz, Object object) {
        StorageObject storeSystem = new StorageObject(new StorageInfo(claz));
        storeSystem.setIndentifiable(true);

        if (claz != null) {
            storeSystem.updateByType();
            storeSystem.updateByStoreClass();
        }
        return storeSystem.restore(object);
    }

    public synchronized static void register(Class<? extends Storable> claz) {

        autoRegisterClass(claz);
    }

    public synchronized static void register(Class<? extends Storable> claz, String alias) {
        autoRegisterClass(claz, alias);
        registerAlias(claz, alias);
    }

    public synchronized static void register(Class<?> claz, Storable storable) {
        storages.put(claz, storable);
        aliases.put(claz, claz.getSimpleName());
        debug("++ CLASS " + claz.getSimpleName());
    }

    public synchronized static void registerPackage(Class<?> clazzForPackage) {
        registerPackage(clazzForPackage, clazzForPackage.getPackage().getName());
    }

    public synchronized static void registerPackage(Class<?> clazzPlugin, String pack) {
        debug("<> PACKAGE " + pack);

        List<Class<?>> classes = Extra.getClasses(clazzPlugin, pack);
        for (Class<?> claz : classes) {
            autoRegisterClass(claz);
        }
    }

    public synchronized static Storable autoRegisterClass(Class<?> claz) {
        return autoRegisterClass(claz, claz.getSimpleName());
    }

    public synchronized static Storable autoRegisterClass(Class<?> claz, String alias) {
        Storable store = null;
        try {
            if (Modifier.isAbstract(claz.getModifiers())) {
                debug("== ABSTRACT CLASS " + claz.getSimpleName());
            } else if (claz.isEnum()) {
                debug("== ENUM CLASS " + claz.getSimpleName());
            } else if (claz.isAnonymousClass()) {
                debug("== ANONYMOUS CLASS " + claz.getSimpleName());
            } else if (claz.isInterface()) {
                debug("== INTERFACE " + claz.getSimpleName());
            } else if (isStorable(claz)) {
                store = (Storable) claz.newInstance();
                register(claz, store);
            } else {
                debug("-- CLASS " + claz.getSimpleName());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return store;
    }

    public synchronized static void registerClasses(Class<?> claz) {
        debug("<> CLASSES " + claz.getName());
        for (Class<?> anotherClass : claz.getDeclaredClasses()) {
            autoRegisterClass(anotherClass);
        }
    }

    public synchronized static void registerObject(int id, Object object) {
        objects.put(id, object);
        debug("+ OBJECT " + getAlias(object.getClass()) + "@" + id);
    }

    public synchronized static void unregisterObjectById(int id) {
        objects.remove(id);
        debug("- OBJECT @" + id);
    }

    public synchronized static void unregisterObject(Object object) {
        objects.remove(object);
        debug("- OBJECT " + getAlias(object.getClass()));
    }

    public synchronized static void unregisterStorable(Class<?> claz) {
        storages.remove(claz);
        debug("- CLASS " + claz.getName());

    }

    public static synchronized void unregisterStorables(JavaPlugin plugin) {
        debug("- CLASSES FROM PLUGIN " + plugin.getName());
        Iterator<Class<?>> it = storages.keySet().iterator();
        int amount = 0;
        //System.out.println("§aLoader: "+plugin.getPluginLoader());
        //System.out.println("§aLoader2: "+plugin.getClass().getClassLoader());
        while (it.hasNext()) {
            Class<?> next = it.next();

            ClassLoader loader = next.getClassLoader();
            //System.out.println(next+" "+(loader==null));

            if (loader != null) {
                if (loader.equals(plugin.getClass().getClassLoader())) {
                    //System.out.println("§aClasse: "+next);
                    aliases.remove(next);
                    amount++;
                    it.remove();
                }
            }
        }
        debug("- CLASSES WITH SAME LOADER OF " + plugin.getName() + " : " + amount);
    }

    public static Object transform(Object object, Class<?> type) throws Exception {
        String fieldTypeName = Extra.toTitle(type.getSimpleName());
        Object value = Extra.getResult(Extra.class, "to" + fieldTypeName, Extra.getParameters(Object.class), object);
        if (value instanceof String) {
            value = Extra.toChatMessage((String) value);
        }
        return value;
    }

    public synchronized static Storable getStore(Class<?> claz) {
        if (claz == null)
            return null;

        Storable store = storages.get(claz);

        // System.out.println("claz trying to get store: "+claz);

        if (store == null) {
            for (Entry<Class<?>, Storable> entry : storages.entrySet()) {
                if (claz.equals(entry.getKey())) {
                    //System.out.println("Types equals "+entry.getKey()+"   "+claz);
                    return entry.getValue();
                }
                //   System.out.println("Buceta "+entry.getKey());
                if (entry.getKey().isAssignableFrom(claz)) {
                    //System.out.println("Entry key "+entry.getKey());
                    store = entry.getValue();
                } else if (claz.isAssignableFrom(entry.getKey())) {
                    store = entry.getValue();
                }

            }
        }
        return store;

    }

    public synchronized static String getAlias(Class<?> claz) {
        for (Entry<Class<?>, String> entry : aliases.entrySet()) {
            if (entry.getKey().equals(claz)) {
                return entry.getValue();
            }
        }
        for (Entry<Class<?>, String> entry : aliases.entrySet()) {
            if (entry.getKey().isAssignableFrom(claz)) {
                return entry.getValue();
            }
            if (claz.isAssignableFrom(entry.getKey())) {
                return entry.getValue();
            }
        }
        return claz.getSimpleName();

    }

    public synchronized static Class<?> getClassByAlias(String alias) {
        for (Entry<Class<?>, String> entry : aliases.entrySet()) {
            if (entry.getValue().equals(alias)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public synchronized static boolean isRegistred(Class<?> claz) {
        return storages.containsKey(claz);
    }

    public synchronized static Map<Class<?>, String> getAliases() {
        return aliases;
    }

    public static void setAliases(Map<Class<?>, String> aliases) {
        StorageAPI.aliases = aliases;
    }

//	public static Map<UUID, Object> getDatabase() {
//		return database;
//	}
//
//	public static void setDatabase(Map<UUID, Object> database) {
//		StorageAPI.database = database;
//	}

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
        storages.entrySet().forEach(entry -> {
            Class<?> key = entry.getKey();
            Storable value = entry.getValue();
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
