package net.eduard.api;

import net.eduard.api.lib.plugin.HybridPlugin;
import net.eduard.api.lib.plugin.IPluginInstance;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.File;

public class EduardAPIMain extends JavaPlugin implements IPluginInstance {

    static{
        new LibraryLoader(new File("libs/")).loadLibraries();
    }

    private HybridPlugin api;


    public EduardAPIMain getPlugin() {
        return this;
    }

    public void onLoad() {
        api = new EduardAPI(this);
        api.onLoad();

    }

    public void onEnable() {
        api.onEnable();
    }

    public void onDisable() {
        api.onDisable();
    }


}
