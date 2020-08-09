package net.eduard.api.core;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.eduard.api.EduardAPI;
import net.eduard.api.lib.abstraction.Minecraft;
import net.eduard.api.lib.config.Config;
import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.modules.MineReflect;
import org.bukkit.entity.Player;


import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class PlayerSkin {
    private static final HashMap<String, PlayerSkin> skins = new HashMap<>();
    private static final Map<Player, PlayerSkin> skinUsed = new HashMap<>();
    private static final Config config = new Config(EduardAPI.getInstance(), "skins.yml");

    public static void saveSkins() {
        for (PlayerSkin skin : skins.values()) {
            config.set(skin.getPlayerName().toLowerCase(), skin);
        }
        config.saveConfig();
    }

    public PlayerSkin() {
    }

    public static void reloadSkins() {
        skins.clear();

        for (String subSecao : config.getKeys()) {
            PlayerSkin skin = config.get(subSecao, PlayerSkin.class);
            skins.put(skin.getPlayerName().toLowerCase(), skin);
        }
    }

    public static PlayerSkin getSkin(String playerName) {
        PlayerSkin skin = skins.get(playerName.toLowerCase());
        if (skin == null) {
            skin = new PlayerSkin(playerName);
            if (skin.exists())
                skins.put(playerName.toLowerCase(), skin);
        }

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


    private String playerName;
    private String playerUUID;
    private String name;
    private String signature;
    private String value;
    private static final String LINK_MOJANG_UUID = "https://api.mojang.com/users/rofiles/minecraft/";
    private static final String LINK_MOJANG_SKIN = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private static final String LINK_MCAPI_UUID = "https://mcapi.ca/name/uuid/";
    private static final String LINK_MCAPI_SKIN = "http://mcapi.ca/uuid/player/";

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
            GameProfile profile = (GameProfile) Extra.getMethodInvoke(handle, "getProfile");

            profile.getProperties().clear();
            try {
                profile.getProperties().put("textures", new Property(this.name, this.value, this.signature));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        System.out.println("Atualizando skin do "+player.getName());

        Minecraft.getInstance().respawnPlayer(player);
        Minecraft.getInstance().reloadPlayer(player);




        /*


         */


    }

    public void update() {
        JsonObject skinProperty = Extra.getSkinProperty(this.playerUUID);
        if (skinProperty !=null) {
            this.name = skinProperty.get("name").getAsString();
            this.value = skinProperty.get("value").getAsString();
            this.signature = skinProperty.get("signature").getAsString();
        }

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
