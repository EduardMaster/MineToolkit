package net.eduard.api;

import net.eduard.api.lib.config.Config;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class EduAPI extends JavaPlugin {
    private static EduAPI instance;
    private LibraryLoader libraryLoader;
    private File librariesFolder;
    private Config configs;
    private Config messages;

    public Config getConfigs(){
        return configs;
    }

    public void log(String msg){
        System.out.println("[EduardAPI] "+msg);
    }

    public boolean getBoolean(String path){
        return configs.getBoolean(path);
    }
    public String message(String path){
        return messages.message(path);
    }
    public Config getMessages(){
        return messages;
    }

    public String getString (String path){
        return configs.message(path);
    }

    private EduardAPI plugin;

    public FileConfiguration getConfig() {
        return null;
    }

    public void onEnable(){
        instance = this;
        librariesFolder = new File(getDataFolder(),"libs/");
        librariesFolder.mkdirs();
        libraryLoader = new LibraryLoader( librariesFolder);
        configs = new Config(this,"config.yml");
        messages = new Config(this,"messages.yml");
        plugin =    new EduardAPI(this);
        plugin.onEnable();
    }

    public void onDisable(){

        plugin.onDisable();
    }
}
