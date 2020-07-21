package net.eduard.api;

import net.eduard.api.lib.plugin.HybridPlugin;
import net.eduard.api.lib.plugin.IPluginInstance;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

public class EduardAPIBunngeMain extends Plugin implements IPluginInstance {

    static{
        new LibraryLoader(new File("libs/")).loadLibraries();
    }

    private HybridPlugin api;

    @Override
    public void onLoad() {
        api = new EduardAPIBungee(this);
        api.onLoad();
    }

    public void onEnable() {
        api.onEnable();
    }

    public void onDisable() {
        api.onDisable();
    }

    @Override
    public Plugin getPlugin() {
        return this;
    }
}
