package net.eduard.api.command.api;

import net.eduard.api.EduardAPIMain;
import net.eduard.api.lib.manager.CommandManager;
import net.eduard.api.lib.modules.Mine;
import net.eduard.api.server.EduardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

public class ApiRestartEduardAPICommand extends CommandManager {
    public ApiRestartEduardAPICommand() {
        super("restarteduardapi");


    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("§eDescarregando plugins do Eduard");
        List<String> lista = new ArrayList<>();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin instanceof EduardPlugin) {
                Mine.runCommand("plugman unload " + plugin.getName());
                lista.add(plugin.getName());
            }
        }


        sender.sendMessage("§ePlugins do Eduard descarregaados pelo PlugMan");
        sender.sendMessage("§fReiniciando EduardAPI");

        Mine.runCommand("plugman unload EduardAPI");
        Mine.runCommand("plugman load EduardAPI");

        sender.sendMessage("§fReiniciando plugins do Eduard");
        for (String plNome : lista){
            Mine.runCommand("plugman load "+plNome);
        }
        sender.sendMessage("§aTodos os plugins foram recarregados com os novos jars");

        return true;

    }

    private String loadPlugin(String pluginName) {
        Plugin targetPlugin = null;
        String msg = "";
        File pluginDir = new File("plugins");
        if (!pluginDir.isDirectory()) {
            return pre + red + "Plugin directory not found!";
        }
        PluginLoader loader = ((JavaPlugin) EduardAPIMain.getPlugin(EduardAPIMain.class)).getPluginLoader();
        File pluginFile = new File(pluginDir, pluginName + ".jar");
        if (!pluginFile.isFile()) {
            for (File file : pluginDir.listFiles()) {
                try {
                    if (file.getName().endsWith(".jar")) {
                        PluginDescriptionFile pdf =loader
                               . getPluginDescription(file);
                        if (pdf.getName().equalsIgnoreCase(pluginName)) {
                            pluginFile = file;
                            msg = "(via search) ";
                            break;
                        }
                    }
                } catch (InvalidDescriptionException e) {
                    return pre + red + "Couldn't find file and failed to search descriptions!";
                }
            }
        }
        try {
            Bukkit.getServer().getPluginManager().loadPlugin(pluginFile);
            targetPlugin = getPlugin(pluginName);
            Bukkit.getServer().getPluginManager().enablePlugin(targetPlugin);
            return pre + green + getPlugin(pluginName) + " loaded " + msg + "and enabled!";
        } catch (UnknownDependencyException e) {
            return pre + red + "File exists, but is missing a dependency!";
        } catch (InvalidPluginException e) {
            System.out.println("Tried to load invalid Plugin.\n");
            return pre + red + "File exists, but isn't a loadable plugin file!";
        } catch (InvalidDescriptionException e) {
        }
        return pre + red + "Plugin exists, but has an invalid description!";
    }

    private Plugin getPlugin(String pluginName) {

        for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
            if (plugin.getDescription().getName().equalsIgnoreCase(pluginName)) {
                return plugin;
            }
        }
        return null;
    }

    private final String pre = "[Loader]", red = "§c", green = "§a";

    private String unloadPlugin(String pl) {

        PluginManager pm = Bukkit.getServer().getPluginManager();
        SimplePluginManager spm = (SimplePluginManager) pm;
        SimpleCommandMap cmdMap = null;
        List<Plugin> plugins = null;
        Map<String, Plugin> names = null;
        Map<String, Command> commands = null;
        Map<Event, SortedSet<RegisteredListener>> listeners = null;
        boolean reloadlisteners = true;
        if (spm != null) {
            try {
                Field pluginsField = spm.getClass().getDeclaredField("plugins");
                pluginsField.setAccessible(true);
                plugins = (List<Plugin>) pluginsField.get(spm);

                Field lookupNamesField = spm.getClass().getDeclaredField("lookupNames");
                lookupNamesField.setAccessible(true);
                names = (Map) lookupNamesField.get(spm);
                try {
                    Field listenersField = spm.getClass().getDeclaredField("listeners");
                    listenersField.setAccessible(true);
                    listeners = (Map) listenersField.get(spm);
                } catch (Exception e) {
                    reloadlisteners = false;
                }
                Field commandMapField = spm.getClass().getDeclaredField("commandMap");
                commandMapField.setAccessible(true);
                cmdMap = (SimpleCommandMap) commandMapField.get(spm);

                Field knownCommandsField = cmdMap.getClass().getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                commands = (Map) knownCommandsField.get(cmdMap);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                return pre + red + "Failed to unload plugin!";
            }
        }
        String tp = "";


        for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
            if (plugin.getDescription().getName().equalsIgnoreCase(pl)) {
                pm.disablePlugin(plugin);
                tp = tp + plugin.getName() + " ";
                if ((plugins != null) && (plugins.contains(plugin))) {
                    plugins.remove(plugin);
                }
                if ((names != null) && (names.containsKey(pl))) {
                    names.remove(pl);
                }
                if ((listeners != null) && (reloadlisteners)) {
                    for (SortedSet<RegisteredListener> set : listeners.values()) {
                        Iterator<RegisteredListener> it2 = set.iterator();
                        while (it2.hasNext()) {
                            RegisteredListener value = (RegisteredListener) it2.next();
                            if (value.getPlugin() == plugin) {
                                it2.remove();
                            }
                        }
                    }
                }
                Iterator<Map.Entry<String, Command>> it3 = commands.entrySet().iterator();
                if (cmdMap != null) {

                    while (it3.hasNext()) {
                        Map.Entry<String, Command> entry = (Map.Entry) it3.next();
                        if ((entry.getValue() instanceof PluginCommand)) {
                            PluginCommand c = (PluginCommand) entry.getValue();
                            if (c.getPlugin() == plugin) {
                                c.unregister(cmdMap);
                                it3.remove();
                            }
                        }
                    }
                }
            }
        }
        return pre + green + tp + "has been unloaded and disabled!";
    }
}
