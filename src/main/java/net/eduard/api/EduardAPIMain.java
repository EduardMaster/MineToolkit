package net.eduard.api;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * @author Eduard
 */
public class EduardAPIMain extends JavaPlugin {
    static{
        new LibraryLoader(new File("libs/")).loadLibraries();
    }

    public EduardAPI bungee;
    @Override
    public void onEnable() {
        bungee = new EduardAPI(this);
        bungee.onEnable();

    }

    @Override
    public void onDisable() {
        bungee.onDisable();
    }
}
