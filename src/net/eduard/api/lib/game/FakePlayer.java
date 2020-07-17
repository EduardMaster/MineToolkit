package net.eduard.api.lib.game;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Representa um Jogador Offline, implementa @{@link OfflinePlayer}
 *
 * @author Eduard
 * @version 1.2
 */
public class FakePlayer implements OfflinePlayer {


    private String name;
    private UUID id;
    private Player playerCache = null;


    public void setIdByName() {
        try {
            this.id = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public void setIdByNameLowerCase() {
        try {
            this.id = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name.toLowerCase()).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void fixUUID() {
        Player player = getPlayer();
        if (player != null) {
            this.id = player.getUniqueId();
        } else {
            setIdByName();
        }
    }


    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FakePlayer{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    public FakePlayer(String name) {
        this.name = name;
        fixUUID();
    }

    public FakePlayer(String name, UUID id) {
        setName(name);
        this.setId(id);
    }


    public FakePlayer(OfflinePlayer player) {
        this(player.getName(), player.getUniqueId());
    }

    public FakePlayer() {
        this("Eduard");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FakePlayer that = (FakePlayer) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public void setOp(boolean op) {

    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> mapaNovo = new HashMap<>();
        mapaNovo.put("name", this.name);
        mapaNovo.put("uuid", this.getUniqueId());
        return mapaNovo;
    }

    @Override
    public Location getBedSpawnLocation() {
        return null;
    }

    @Override
    public long getFirstPlayed() {
        return 0;
    }

    @Override
    public long getLastPlayed() {
        return 0;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Player getPlayer() {
        if (playerCache != null && playerCache.isOnline()) {
            return playerCache;
        }

        if (id != null)
            playerCache = Bukkit.getPlayer(id);
        if (playerCache == null && name != null)
            playerCache = Bukkit.getPlayer(name);

        return playerCache;
    }


    public UUID getUniqueId() {
        return this.id;
    }


    public boolean hasPlayedBefore() {
        return true;
    }


    public boolean isBanned() {
        return false;
    }


    public boolean isOnline() {
        return getPlayer() != null;
    }


    public boolean isWhitelisted() {

        return false;
    }


    public void setBanned(boolean bol) {


    }


    public void setWhitelisted(boolean bol) {


    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
