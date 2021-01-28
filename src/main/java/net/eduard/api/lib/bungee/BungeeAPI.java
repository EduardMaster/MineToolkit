package net.eduard.api.lib.bungee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BungeeAPI {
    BungeeAPI() {
    }

    private final static String channel = "bukkit:bungee";
    private final static Map<String, ServerSpigot> servers = new HashMap<>();
    private final static List<ServerMessageHandler> handlers = new ArrayList<>();
    private static ServerController controller;


    public static ServerSpigot getServer(String serverName) {
        ServerSpigot server = servers.get(serverName.toLowerCase());
        if (server == null) {
            server = new ServerSpigot(serverName);
            servers.put(serverName.toLowerCase(), server);
        }
        return server;
    }

    public static int getPlayersAmount() {
        int amount = 0;
        for (ServerSpigot server : servers.values()) {
            amount+=server.getCount();
        }
        return amount;
    }

    public static int getPlayersAmount(String serverType) {
        int amount = 0;
        for (ServerSpigot server : servers.values()) {
            if (server.getType().equalsIgnoreCase(serverType)) {
                amount += server.getCount();
            }
        }
        return amount;
    }

    public static int getPlayersAmount(String serverType, int teamSize) {
        int amount = 0;
        for (ServerSpigot server : servers.values()) {
            if (!server.getSubType().equalsIgnoreCase("lobby")
                    && server.getType().equalsIgnoreCase(serverType)
                    && server.getTeamSize() == teamSize) {
                amount += server.getCount();
            }
        }
        return amount;
    }

    public static int getPlayersAmount(String serverType, String subType) {
        int amount = 0;
        for (ServerSpigot server : servers.values()) {
            if (server.getType().equalsIgnoreCase(serverType) &&
                    subType.equalsIgnoreCase(server.getSubType())) {
                amount += server.getCount();
            }
        }
        return amount;
    }

    static {
        try {
            Class.forName("org.bukkit.Bukkit");
            controller = new BukkitController();
        } catch (Exception e) {
            controller = new BungeeController();
        }
    }

    public static BukkitController getBukkit() {
        return (BukkitController) controller;
    }

    public static BungeeController getBungee() {
        return (BungeeController) controller;
    }

    public static String getChannel() {
        return channel;
    }

    public static void register(ServerMessageHandler receiver) {
        handlers.add(receiver);
    }

    public static List<ServerMessageHandler> getHandlers() {
        return handlers;
    }

    public static ServerController getController() {
        return controller;
    }

    public static Map<String, ServerSpigot> getServers() {
        return servers;
    }


}
