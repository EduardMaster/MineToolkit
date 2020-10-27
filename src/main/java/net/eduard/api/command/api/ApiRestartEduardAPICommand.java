package net.eduard.api.command.api;

import net.eduard.api.EduardAPI;
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
import java.util.logging.Level;

public class ApiRestartEduardAPICommand extends CommandManager {
    public ApiRestartEduardAPICommand() {
        super("restarteduardapi");


    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("§eDescarregando plugins do Eduard");
        List<String> lista = new ArrayList<>();
        for (Plugin pl : Bukkit.getPluginManager().getPlugins()) {
            if (pl instanceof EduardPlugin) {
                Mine.makeCommand("plugman unload " + pl.getName());
                lista.add(pl.getName());
            }
        }


        sender.sendMessage("§ePlugins do Eduard descarregaados pelo PlugMan");
        sender.sendMessage("§fReiniciando EduardAPI");

        Mine.makeCommand("plugman unload EduardAPI");
        Mine.makeCommand("plugman load EduardAPI");

        sender.sendMessage("§fReiniciando plugins do Eduard");
        for (String plNome : lista){
            Mine.makeCommand("plugman load "+plNome);
        }
        sender.sendMessage("§aTodos os plugins foram recarregados com os novos jars");

        return true;

    }

    private String loadPlugin(String pl) {
        Plugin targetPlugin = null;
        String msg = "";
        File pluginDir = new File("plugins");
        if (!pluginDir.isDirectory()) {
            return pre + red + "Plugin directory not found!";
        }
        PluginLoader loader = ((JavaPlugin) EduardAPIMain.getPlugin(EduardAPIMain.class)).getPluginLoader();
        File pluginFile = new File(pluginDir, pl + ".jar");
        if (!pluginFile.isFile()) {
            for (File f : pluginDir.listFiles()) {
                try {
                    if (f.getName().endsWith(".jar")) {

                        PluginDescriptionFile pdf =loader
                               . getPluginDescription(f);
                        if (pdf.getName().equalsIgnoreCase(pl)) {
                            pluginFile = f;
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
            targetPlugin = getPlugin(pl);
            Bukkit.getServer().getPluginManager().enablePlugin(targetPlugin);
            return pre + green + getPlugin(pl) + " loaded " + msg + "and enabled!";
        } catch (UnknownDependencyException e) {
            return pre + red + "File exists, but is missing a dependency!";
        } catch (InvalidPluginException e) {
            System.out.println("Tried to load invalid Plugin.\n");
            return pre + red + "File exists, but isn't a loadable plugin file!";
        } catch (InvalidDescriptionException e) {
        }
        return pre + red + "Plugin exists, but has an invalid description!";
    }

    private Plugin getPlugin(String p) {

        for (Plugin pl : Bukkit.getServer().getPluginManager().getPlugins()) {
            if (pl.getDescription().getName().equalsIgnoreCase(p)) {
                return pl;
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


        for (Plugin p : Bukkit.getServer().getPluginManager().getPlugins()) {
            if (p.getDescription().getName().equalsIgnoreCase(pl)) {
                pm.disablePlugin(p);
                tp = tp + p.getName() + " ";
                if ((plugins != null) && (plugins.contains(p))) {
                    plugins.remove(p);
                }
                if ((names != null) && (names.containsKey(pl))) {
                    names.remove(pl);
                }
                if ((listeners != null) && (reloadlisteners)) {
                    for (SortedSet<RegisteredListener> set : listeners.values()) {
                        Iterator<RegisteredListener> it2 = set.iterator();
                        while (it2.hasNext()) {
                            RegisteredListener value = (RegisteredListener) it2.next();
                            if (value.getPlugin() == p) {
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
                            if (c.getPlugin() == p) {
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
