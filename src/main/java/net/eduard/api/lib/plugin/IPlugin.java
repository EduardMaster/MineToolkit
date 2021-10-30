package net.eduard.api.lib.plugin;

import lombok.val;
import net.eduard.api.lib.config.Config;
import net.eduard.api.lib.database.DBManager;
import net.eduard.api.lib.database.SQLManager;
import net.eduard.api.lib.storage.StorageAPI;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public interface IPlugin extends IPluginInstance {
    boolean getStarted();

    void setStarted(boolean flag);

    @NotNull
    Config getConfigs();

    void setConfigs(@NotNull Config config);

    @NotNull
    Config getStorage();

    void setStorage(@NotNull Config storage);

    @NotNull
    Config getMessages();

    void setMessages(@NotNull Config var1);

    @NotNull
    DBManager getDbManager();

    void setDbManager(@NotNull DBManager var1);

    @NotNull
    SQLManager getSqlManager();

    void setSqlManager(@NotNull SQLManager var1);

    @NotNull
    PluginSettings getSettings();

    void setSettings(@NotNull PluginSettings var1);

    default void onLoad() {
        val currentInstance = this;
        if (!currentInstance.getStarted()) {
            currentInstance.setDbManager(new DBManager());
            currentInstance.setConfigs(new Config(currentInstance, "config.yml"));
            currentInstance.setMessages(new Config(currentInstance, "messages.yml"));
            currentInstance.setStorage(new Config(currentInstance, "storage.yml"));
            currentInstance.setSettings(new PluginSettings());
            currentInstance.getConfigs().add("settings", currentInstance.getSettings());
            currentInstance.getConfigs().add("database", currentInstance.getDbManager());
            currentInstance.getConfigs().saveConfig();
            currentInstance.setSettings(currentInstance.getConfigs().get("settings", PluginSettings.class));
            currentInstance.setDbManager(currentInstance.getConfigs().get("database", DBManager.class));
            currentInstance.setSqlManager(new SQLManager(currentInstance.getDbManager()));
          //  currentInstance.setStorageManager(new StorageManager(currentInstance.getSqlManager()));
            currentInstance.setStarted(true);
           // currentInstance.getStorageManager().setType(currentInstance.getSettings().getStoreType());
            if (currentInstance.getDbManager().isEnabled()) {
                currentInstance.getDbManager().openConnection();
            }
        }
    }

    // fazer depois
    default void backup(){
        /*

          settings.lastBackup = Extra.getNow()

        try {
            val simpleDateFormat = SimpleDateFormat("dd-MM-YYYY HH-mm-ss")
            val pasta = File(
                pluginFolder,
                "/backup/" + simpleDateFormat.format(System.currentTimeMillis()) + "/"
            )
            pasta.mkdirs()
            if (storage.existConfig() && storage.keys.isNotEmpty()) {
                Files.copy(storage.file.toPath(), Paths.get(pasta.path, storage.name))
            }
            if (configs.existConfig() && storage.keys.isNotEmpty()) {
                Files.copy(configs.file.toPath(), Paths.get(pasta.path, configs.name))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
         */
    }

    default void unregisterStorableClasses(){
        StorageAPI.unregisterPlugin(getClass());
    }

    @NotNull
    default String getSystemName(){
        return getPluginName();
    }

    @NotNull
    String getPluginName();

    @NotNull
    File getPluginFolder();

    void log(@NotNull String text);

    void error(@NotNull String error);


    default void console(@NotNull String message) {
        System.out.println(message);
    }

    void onEnable();

    void onDisable();

    default void reload(){

    }

    default void configDefault(){

    }

    default void save(){

    }

    default void onActivation(){

    }

    void unregisterTasks();

    void unregisterListeners();

    void unregisterServices();

    void unregisterCommands();

    default boolean getBoolean(@NotNull String key){
        return getConfigs().getBoolean(key);
    }

    default  int getInt(@NotNull String key){
        return getConfigs().getInt(key);
    }

    default double getDouble(@NotNull String key){
     return getConfigs().getDouble(key);
    }


    @NotNull
    default String message(@NotNull String key){
        return getMessages().message(key);
    }

    @NotNull
    default List<String> getMessages(@NotNull String key){
        return  getMessages().getMessages(key);
    }


    @NotNull
    default String getString(@NotNull String key) {
        return getConfigs().message(key);
    }

    default void deleteOldBackups(){
        /*

         val pasta = File(pluginFolder, "/backup/")
        pasta.mkdirs()
        val lista = listOf(*pasta.listFiles()!!)
        lista.filter { it.lastModified() + TimeUnit.DAYS.
        toMillis(1) <= System.currentTimeMillis() }
            .forEach {
                Extra.deleteFolder(it)
                if (it.exists())
                    it.delete()
            }

         */
    }

}
