package net.eduard.api.lib.abstraction;

import java.lang.reflect.Field;
import java.util.*;

import net.eduard.api.lib.modules.MineReflect;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.eduard.api.lib.modules.Extra;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

/**
 * @author Eduard
 */
public class Minecraft_v1_8_R3 extends Minecraft {
    private static Map<String, Object> particles = new HashMap<>();

    public Minecraft_v1_8_R3() {
        if (particles.isEmpty())
            for (EnumParticle particle : EnumParticle.values()) {
                particles.put(particle.b().toLowerCase(), particle);
            }

    }


    @Override
    public void sendPacket(Object packet, Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet<?>) packet);
    }

    @Override
    public void setHeadSkin(ItemStack head, String texture, String signature) {


    }


    private static Set<String> blockedMessage = new HashSet<>();

    @Override
    public void sendParticle(Player player, String name, Location location, int amount, float xOffset, float yOffset, float zOffset, float speed) {
        float x = (float) location.getX();
        float y = (float) location.getY();
        float z = (float) location.getZ();

        Object result = particles.get(name.toLowerCase());
        if (result == null) {
            String text = "Particle not exist in this version " + name;
            if (blockedMessage.contains(text)) return;
            System.out.println(text);
            blockedMessage.add(text);
            return;
        }
        EnumParticle particula = (EnumParticle) result;
        if (particula.f()) {

            String text = "Particle not sent because need arguments: " + name;
            if (blockedMessage.contains(text)) return;
            System.out.println(text);
            blockedMessage.add(text);
        } else {
            Object packet = new PacketPlayOutWorldParticles(particula, true, x, y, z, xOffset, yOffset, zOffset, speed, amount);
            sendPacket(player, packet);
        }


    }

    @Override
    public void performRespawn(Player player) {
        ((CraftPlayer) player).getHandle().playerConnection
                .a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
    }

    @Override
    public void setPlayerSkin(Player player, String newSkin) {
        final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        final GameProfile playerProfile = entityPlayer.getProfile();
//		playerProfile.getProperties().clear();
        String uuid = Extra.getPlayerUUIDByName(newSkin);


        JsonObject skinProperty = Extra.getSkinProperty(uuid);

        String name = skinProperty.get("name").getAsString();
        String skin = skinProperty.get("value").getAsString();
        String signature = skinProperty.get("signature").getAsString();
        try {
            playerProfile.getProperties().put("textures", new Property(name, skin, signature));
        } catch (Exception e) {
            e.printStackTrace();
        }


        respawnPlayer(player);

    }


    @Override
    public void setPlayerName(Player player, String newName) {

        final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        final GameProfile playerProfile = entityPlayer.getProfile();

        removeFromTab(player);

        try {
            final Field field = playerProfile.getClass().getDeclaredField("name");
            field.setAccessible(true);
            field.set(playerProfile, newName);
            field.setAccessible(false);
            entityPlayer.getClass().getDeclaredField("displayName").set(entityPlayer, newName);
            entityPlayer.getClass().getDeclaredField("listName").set(entityPlayer, newName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        respawnPlayer(player);

    }

    @Override
    public void respawnPlayer(Player player) {
        final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        final PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(new int[]{entityPlayer.getId()});
        final PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);
        final PacketPlayOutPlayerInfo addPlayerInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER,
                ((CraftPlayer) player).getHandle());
        final PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entityPlayer.getId(),
                entityPlayer.getDataWatcher(), true);
        final PacketPlayOutEntityHeadRotation headRotation = new PacketPlayOutEntityHeadRotation(entityPlayer,
                (byte) MathHelper.d(entityPlayer.getHeadRotation() * 256.0f / 360.0f));
        final PacketPlayOutPlayerInfo removePlayerInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER,
                ((CraftPlayer) player).getHandle());
        sendPacketsToOthers(player, removePlayerInfo, addPlayerInfo, destroy, spawn, metadata, headRotation);

    }

    @Override
    public void removeFromTab(Player playerRemoved) {
        final PacketPlayOutPlayerInfo removePlayerInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER,
                ((CraftPlayer) playerRemoved).getHandle());
        sendPacketsToOthers(playerRemoved, removePlayerInfo);

    }

    @Override
    public void addToTab(Player playerToAdd) {
        final PacketPlayOutPlayerInfo addPlayerInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER,
                ((CraftPlayer) playerToAdd).getHandle());

        final PacketPlayOutPlayerInfo updatePlayerInfo = new PacketPlayOutPlayerInfo(
                EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, ((CraftPlayer) playerToAdd).getHandle());
        sendPacketsToOthers(playerToAdd, addPlayerInfo, updatePlayerInfo);
    }

    @Override
    public Object getItemNBT(ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbt = null;
        if ((nbt = nmsItem.getTag()) == null)
            nmsItem.setTag(nbt);

        return nbt;
    }

    @Override
    public ItemStack setItemNBT(ItemStack item, Object nbt) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        nmsItem.setTag((NBTTagCompound) nbt);
        CraftItemStack craftMirror = CraftItemStack.asCraftMirror(nmsItem);
        return craftMirror;
    }

    @Override
    public void sendActionBar(Player player, String message) {
        IChatBaseComponent chatBaseComponent = ChatSerializer.a("" + message);
        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(chatBaseComponent, (byte) 2);
        sendPacket(packetPlayOutChat, player);

    }

}
