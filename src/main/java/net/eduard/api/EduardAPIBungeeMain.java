package net.eduard.api;

import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

/**
 * Para fazer plugins usando esta dependencia , lembre-se de colocar depends: [EduardAPI]
 * em vez de depend: [EduardAPI] na bungee.yml
 * @author Eduard
 */
public class EduardAPIBungeeMain extends Plugin {
    static{
        new LibraryLoader(new File("libs/")).loadLibraries();
    }

    public EduardAPIBungee bungee;
    @Override
    public void onEnable() {
        bungee = new EduardAPIBungee(this);
        bungee.onEnable();

    }

    @Override
    public void onDisable() {
        bungee.onDisable();
    }
}
