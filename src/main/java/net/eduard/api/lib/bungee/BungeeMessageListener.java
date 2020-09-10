package net.eduard.api.lib.bungee;


import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class BungeeMessageListener implements Listener {
    private BungeeController controller;

    public BungeeMessageListener(BungeeController bungeeController) {
        setController(bungeeController);
    }

    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        if (event.getTag().equals(BungeeAPI.getChannel())) {
            ByteArrayInputStream arrayIn = new ByteArrayInputStream(event.getData());
            DataInputStream data = new DataInputStream(arrayIn);

            try {
                String server = data.readUTF();
                String tag = data.readUTF();
                String line = data.readUTF();
                controller.receiveMessage(server, tag, line);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public BungeeController getController() {
        return controller;
    }

    public void setController(BungeeController controller) {
        this.controller = controller;
    }
}
