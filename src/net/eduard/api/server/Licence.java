package net.eduard.api.server;

import net.eduard.api.lib.config.BukkitConfig;
import net.eduard.api.lib.config.BungeeConfig;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Sistema de verificacao de Compra do Plugin para Bungee ou Bukkit
 *
 * @author Eduard
 * @version 1.0
 */
public class Licence {

    private static boolean debug = true;

    private static final String site = "http://eduard.com.br/license/?";

    private static PluginActivationStatus test(String plugin, String owner, String key) {
        try {
            String tag = "[" + plugin + "] ";
            String link = site + "key=" + key + "&plugin=" + plugin + "&owner=" + owner;
            URLConnection connect = new URL(link).openConnection();
            connect.setConnectTimeout(5000);
            connect.setReadTimeout(5000);

            connect.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            Scanner scan = new Scanner(connect.getInputStream());
            StringBuilder b = new StringBuilder();
            while (scan.hasNext()) {
                String text = scan.next();
                b.append(text);
            }
            scan.close();
            try {
                return PluginActivationStatus.valueOf(b.toString().toUpperCase().replace(" ", "_"));
            } catch (Exception ex) {
                if (debug) {
                    ex.printStackTrace();
                    System.out.println(tag + "Verificando pelo link: " + link);
                }
                return PluginActivationStatus.ERROR;
            }
        } catch (IOException ex) {
            if (debug) {
                ex.printStackTrace();
            }
            return PluginActivationStatus.SITE_OFF;
        } catch (Exception e) {
            if (debug) {
                e.printStackTrace();
            }
            return PluginActivationStatus.ERROR;
        }
    }

    enum PluginActivationStatus {

        INVALID_KEY("§cNao foi encontrado esta Licensa no Sistema."),
        WRONG_KEY("§cNao possui esta Licensa no Sistema."),
        KEY_TO_WRONG_PLUGIN("§cA Licensa usada nao serve para este plugin."),
        KEY_TO_WRONG_OWNER("§cA Licensa usada nao serve para este Dono"),
        INVALID_IP("§cEste IP usado nao corresponde ao IP da Licensa"),
        ERROR("§cO plugin nao ativou pois deu algum tipo de erro ao receber a resposta do Site"),
        SITE_OFF("§eO Sistema de licença nao respondeu, §aplugin ativado para testes", true),
        PLUGIN_ACTIVATED("§aPlugin ativado com sucesso, Licensa permitida.", true),
        FOR_TEST("§aO plugin foi liberado para testes no PC do Eduard", true);


        private String message;
        private boolean active;

        PluginActivationStatus() {

        }

        PluginActivationStatus(String message) {
            setMessage(message);
        }

        PluginActivationStatus(String message, boolean active) {
            setMessage(message);
            setActive(active);
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }

    public static class BukkitTester {

        public static void test(JavaPlugin plugin, Runnable activation) {

            String pluginName = plugin.getName();
            String tag = "§b[" + plugin.getName() + "] §f";
            Bukkit.getConsoleSender().sendMessage(tag + "§eFazendo autenticacao do Plugin no site");
            BukkitConfig config = new BukkitConfig("license.yml", plugin);
            config.add("key", "INSIRA_KEY");
            config.add("owner", "INSIRA_Dono");
            config.saveDefault();
            String key = config.getString("key");
            String owner = config.getString("owner");
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->

            {
                PluginActivationStatus result = Licence.test(pluginName, owner, key);

                Bukkit.getConsoleSender().sendMessage(tag + result.getMessage());
                if (!result.isActive()) {
                    Bukkit.getPluginManager().disablePlugin(plugin);
                } else {
                    activation.run();
                }

            });

        }

    }

    public static class BungeeTester {

        public static void test(Plugin plugin, Runnable activation) {
            String pluginName = plugin.getDescription().getName();
            BungeeCord.getInstance().getConsole()
                    .sendMessage(new TextComponent("§aAutenticando o plugin " + pluginName));
            BungeeConfig config = new BungeeConfig("license.yml", plugin);
            config.add("key", "INSIRA_KEY");
            config.add("owner", "INSIRA_Dono");
            config.saveConfig();
            String key = config.getString("key");
            String owner = config.getString("owner");
            String tag = "§b[" + plugin.getDescription().getName() + "] §f";
            BungeeCord.getInstance().getScheduler().runAsync(plugin, () -> {

                PluginActivationStatus result = Licence.test(pluginName, owner, key);
                BungeeCord.getInstance().getConsole().sendMessage(new TextComponent(tag + result.getMessage()));
                if (!result.isActive()) {
                    BungeeCord.getInstance().getPluginManager().unregisterListeners(plugin);
                    BungeeCord.getInstance().getPluginManager().unregisterCommands(plugin);
                } else {
                    activation.run();
                }

            });

        }
    }

}
