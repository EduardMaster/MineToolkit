package net.eduard.api.core;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.eduard.api.EduardAPI;
import net.eduard.api.lib.config.Config;
import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.modules.MineReflect;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.StorageAPI;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerSkin implements Storable {
    private static ArrayList<PlayerSkin> skins = new ArrayList<>();
    private static Map<Player, PlayerSkin> skinUsed = new HashMap<>();
    private static Config config = new Config(EduardAPI.getInstance(), "skins.yml");

    public static void saveSkins() {
        for (PlayerSkin skin : skins) {
            config.set(skin.getPlayerName().toLowerCase(), skin);
        }
        config.saveConfig();
    }

    public PlayerSkin() {
    }

    public static void reloadSkins() {
        skins.clear();
        StorageAPI.register(PlayerSkin.class);
        for (String subSecao : config.getKeys()) {
            PlayerSkin skin = (PlayerSkin) config.get(subSecao);
            skins.add(skin);
        }
    }

    public static PlayerSkin getSkin(String playerName) {
        for (PlayerSkin skin : skins) {
            if (skin.getPlayerName().equalsIgnoreCase(playerName
            )) {
                return skin;
            }
        }
        PlayerSkin skin = new PlayerSkin(playerName);
        if (skin.exists())
            skins.add(skin);

        return skin;
    }

    public static void change(Player player, String skinName) {
        PlayerSkin skin = getSkin(skinName);
        String defName = EduardAPI.getInstance().getString("custom-skin");
        if (defName.isEmpty()) {
            defName = "EduardKIllerPro";
        }
        if (!skin.exists()) {
            skin = getSkin(defName);
        }
        skin.update(player);
        skinUsed.put(player, skin);
    }

    public boolean exists() {
        return this.playerUUID != null;
    }

    public static boolean setSkin(GameProfile profile, UUID uuid) {
        try {

            URLConnection connection = (URLConnection) new URL(String
                    .format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", "" + uuid))
                    .openConnection();
//	        if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
            String reply = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
            String skin = reply.split("\"value\":\"")[1].split("\"")[0];
            String signature = reply.split("\"signature\":\"")[1].split("\"")[0];
            profile.getProperties().put("textures", new Property("textures", skin, signature));
//	            return true;
//	        } else {
//	            System.out.println("Connection could not be opened (Response code " + connection.getResponseCode() + ", " + connection.getResponseMessage() + ")");
//	            return false;
//	        }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private String playerName;
    private String playerUUID;
    private String name;
    private String signature;
    private String value;
    public static final String LINK_MOJANG_UUID = "https://api.mojang.com/users/rofiles/minecraft/";
    public static final String LINK_MOJANG_SKIN = "https://sessionserver.mojang.com/session/minecraft/profile/";
    public static final String LINK_MCAPI_UUID = "https://mcapi.ca/name/uuid/";
    public static final String LINK_MCAPI_SKIN = "http://mcapi.ca/uuid/player/";

    public PlayerSkin(String playerName) {
        this.playerName = playerName;
        this.playerUUID = Extra.getPlayerUUIDByName(playerName);
        if (this.playerUUID != null) {
            update();
        }

    }

    public void update(Player player) {
        if (this.playerUUID == null) return;

        try {
            Object handle = MineReflect.getHandle(player);
            GameProfile profile  = (GameProfile) Extra.getResult(handle,"getProfile");

            profile.getProperties().clear();
            try {
                profile.getProperties().put("textures", new Property(this.name, this.value, this.signature));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }



//
        //  Minecraft.newInstance().respawnPlayer(player);
      /*
        Location loc = player.getLocation();

        player.teleport(Bukkit.getWorld("world_nether").getSpawnLocation());
        player.teleport(loc);


       */
    }

    public void update() {
        JsonObject skinProperty = Extra.getSkinProperty(this.playerUUID);
        this.name = skinProperty.get("name").getAsString();
        this.value = skinProperty.get("value").getAsString();
        this.signature = skinProperty.get("signature").getAsString();

    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
    }
}
