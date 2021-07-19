package net.eduard.api;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Classe inicial do EduardAPI que faz carregamento do Koltin para o restante funcionar e em breve também a baixar o kotlin se não tiver*
 * @author Eduard
 */
public class EduardAPIMain extends JavaPlugin {
    static {
        new LibraryLoader(new File("libs/")).loadLibraries();
    }

    public EduardAPI eduardAPI;


    @Override
    public void onLoad() {
        eduardAPI = new EduardAPI(this);
        eduardAPI.onLoad();

    }

    @Override
    public void onEnable() {
        if (eduardAPI == null) {
            eduardAPI = new EduardAPI(this);
            eduardAPI.onLoad();
        }
        eduardAPI.onEnable();

    }

    @Override
    public void onDisable() {
        eduardAPI.onDisable();
    }
}
