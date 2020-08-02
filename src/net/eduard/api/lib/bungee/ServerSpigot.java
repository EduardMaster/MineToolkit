package net.eduard.api.lib.bungee;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServerSpigot implements Serializable {

    private int max;
    private String name;
    private int type;
    private int count;
    private int state;
    private String host;
    private int port;
    private List<String> players = new ArrayList<>();

    public ServerSpigot(String name) {
        this.name = name;
    }

    public void setState(ServerState state) {
        this.state = state.getValue();
    }


    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }


    public boolean isState(ServerState state) {
        return this.state == state.getValue();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public boolean canConnect() {
        return isOnline() && !isFull() && !isRestarting() && !isGameStarted();
    }


    public boolean isRestarting() {
        return isState(ServerState.RESTARTING);
    }

    public boolean isGameStarted() {
        return isState(ServerState.IN_GAME);
    }

    public boolean isOffline() {
        return isState(ServerState.OFFLINE);
    }

    public boolean isDisabled() {
        return isState(ServerState.DISABLED);
    }

    public boolean isOnline() {
        return !isOffline() && !isDisabled();
    }

    public boolean isFull() {
        return players.size() == max;
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}

