package net.eduard.api.server;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.eduard.api.lib.config.Config;
import net.eduard.api.lib.database.DBManager;
import net.eduard.api.lib.database.HybridTypes;
import net.eduard.api.lib.database.SQLManager;
import net.eduard.api.lib.kotlin.ModuleResolverKt;
import net.eduard.api.lib.plugin.IPluginInstance;
import net.eduard.api.lib.plugin.PluginSettings;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

@Setter
@Getter
public class EduardBungeePlugin extends Plugin implements IPluginInstance {

    public boolean getBoolean(@NotNull String key){
        return getConfigs().getBoolean(key);
    }

    public  int getInt(@NotNull String key){
        return getConfigs().getInt(key);
    }

    public double getDouble(@NotNull String key){
        return getConfigs().getDouble(key);
    }

    @NotNull
    public String message(@NotNull String key){
        return getMessages().message(key);
    }

    @NotNull
    public List<String> getMessages(@NotNull String key){
        return  getMessages().getMessages(key);
    }


    @NotNull
    public  String getString(@NotNull String key) {
        return getConfigs().message(key);
    }

    public boolean getStarted() {
        return started;
    }

    private boolean started = false;
    private boolean isFree= false;

    public boolean isFree() {
        return isFree;
    }

    private Config configs = null;
    public Config getConfig(){
        return configs;
    }

    private Config messages = null;
    private String prefix = null;
    private Config storage = null;
    private SQLManager sqlManager = null;
    private DBManager dbManager = null;
    private PluginSettings settings = null;

    public Config getMessages() {
        return messages;
    }

    public Config getConfigs() {
        return configs;
    }

    public Config getStorage() {
        return storage;
    }

    public DBManager getDbManager() {
        return dbManager;
    }

    public PluginSettings getSettings() {
        return settings;
    }

    public SQLManager getSqlManager() {
        return sqlManager;
    }


    public String getPrefix() {
        if (prefix == null){
           prefix= "["+getPluginName()+"] ";
        }
        return prefix;
    }

    @NotNull
    public String getPluginName() {
        return getPlugin().getDescription().getName();
    }

    @NotNull
    public File getPluginFolder() {
        return getPlugin().getDataFolder();
    }



    public void registerEvents(Listener events) {
        ProxyServer.getInstance().getPluginManager()
                .registerListener(this, events);
    }

    public void registerCommand(Command comand) {
        ProxyServer.getInstance().getPluginManager().registerCommand(this, comand);
    }
    public void console(@NotNull String message) {
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent(message));
    }


    public Plugin getPlugin() {
        return this;
    }

    @Override
    public String getSystemName() {
        return getDescription().getName();
    }


    public void log(@NotNull String message) {
        console("§b"+getPrefix() +"§a"+ message);
    }

    public void error(@NotNull String message) {
        console("§e"+getPrefix() +"§c"+ message);
    }


    public void onLoad() {
        val loadingHybrids = HybridTypes.INSTANCE;

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
    public void onActivation(){

    }
    public void onEnable() {
        if (!started) onLoad();
        ModuleResolverKt.resolvePut(this);
    }
    public void onDisable() {
        ModuleResolverKt.resolveTake(this);
    }
    public void save() {

    }

    public void reload() {

    }

    public void configDefault() {

    }
    public void unregisterTasks() {

    }

    public void unregisterServices() {

    }

    public void unregisterListeners() {

    }

    public void unregisterCommands() {

    }

    public void unregisterStorableClasses() {

    }



}
