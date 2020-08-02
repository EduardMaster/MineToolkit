package net.eduard.api.lib.bungee;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;


import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class BukkitMessageListener implements PluginMessageListener {

    public BukkitMessageListener(BukkitController bukkitController) {
        setController(bukkitController);
    }

    private BukkitController controller;

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals(BungeeAPI.getChannel())) {
            ByteArrayInputStream arrayIn = new ByteArrayInputStream(message);

            DataInputStream data = new DataInputStream(arrayIn);
            try {
                ObjectInputStream objStream = new ObjectInputStream(arrayIn);
                String server = objStream.readUTF();
                String tag = objStream.readUTF();
                if (tag.equals("server-update")){

                    ServerSpigot spigot = (ServerSpigot) objStream.readObject();
                    BungeeAPI.getServers().
                            put(spigot.getName().toLowerCase(),spigot);


                }else {
                    String line = data.readUTF();
                    controller.receiveMessage(server, tag, line);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public BukkitController getController() {
        return controller;
    }

    public void setController(BukkitController controller) {
        this.controller = controller;
    }

}
