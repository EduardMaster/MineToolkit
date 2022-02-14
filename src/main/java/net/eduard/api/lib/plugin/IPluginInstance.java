package net.eduard.api.lib.plugin;


import java.io.File;

public interface IPluginInstance {

    Object getPlugin();

    String getSystemName();

    File getPluginFolder();
}
