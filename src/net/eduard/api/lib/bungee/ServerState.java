package net.eduard.api.lib.bungee;

public enum ServerState {
    RESTARTING(3),
    IN_GAME(2),
    ONLINE(1),
    OFFLINE(0),
    DISABLED(-1);
    private final int value;

    public int getValue() {
        return value;
    }

    ServerState(int value) {
        this.value = value;
    }

}
