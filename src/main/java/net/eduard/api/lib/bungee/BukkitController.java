package net.eduard.api.lib.bungee;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BukkitController implements ServerController<Plugin> {

    private Plugin plugin;

    private final BukkitMessageListener listener = new BukkitMessageListener(this);

    @Override
    public void sendMessage(String server, String tag, String line) {
        ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(arrayOut);
        try {
            out.writeUTF(server);
            out.writeUTF(tag);
            out.writeUTF(line);
            Bukkit.getServer().sendPluginMessage(plugin, BungeeAPI.getChannel(), arrayOut.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendMessage(String tag, String line) {
        ByteArrayOutputStream arrayOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(arrayOut);
        try {
            out.writeUTF("bungeecord");
            out.writeUTF(tag);
            out.writeUTF(line);
            Bukkit.getServer().sendPluginMessage(plugin, BungeeAPI.getChannel(), arrayOut.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public void register(Plugin pl) {
        setPlugin(pl);
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, BungeeAPI.getChannel());
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, BungeeAPI.getChannel(), listener);
        Bukkit.getConsoleSender().sendMessage("§eRegistrando sistema de conexao com Bungeecoard via Plugin Messaging");

    }

    @Override
    public void unregister() {
        Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin, BungeeAPI.getChannel(), listener);
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin, BungeeAPI.getChannel());

    }

    @Override
    public void receiveMessage(String server, String tag, String line) {
        for (ServerMessageHandler handler : BungeeAPI.getHandlers()) {
            handler.onMessage(server, tag, line);
        }

    }

    @Override
    public void connect(String player, String serverType, String subType, int teamSize) {
        sendMessage("connect", player + " " + serverType + " " + subType + " "+teamSize);

    }

    @Override
    public void setState(String server, ServerState state) {

        sendMessage("set-state", server + " " + state);
    }

}
