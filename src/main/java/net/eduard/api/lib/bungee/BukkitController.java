package net.eduard.api.lib.bungee;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
            Player online = getFirstPlayer();
            // Não usar Bukkit.getServer().sendData()
            // porque essa porra envia 1 requisição para cada jogador online
            // isso significa se tiver 10 on , vai mandar essa Mensagem de bytes , 10 vezes no Bungeecord
            if (online == null) return;
            online.sendPluginMessage(plugin, BungeeAPI.getChannel(), arrayOut.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player getFirstPlayer() {
        try {
            return Bukkit.getOnlinePlayers().iterator().next();
        } catch (Exception exception) {
            return null;
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
            Player online = getFirstPlayer();
            if (online == null) return;
            online.sendPluginMessage(plugin, BungeeAPI.getChannel(), arrayOut.toByteArray());
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
        sendMessage("connect", player + " " + serverType + " " + subType + " " + teamSize);

    }

    @Override
    public void setState(String server, ServerState state) {
        sendMessage("set-state", server + " " + state);
    }

}
