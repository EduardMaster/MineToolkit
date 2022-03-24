package net.eduard.api.lib.abstraction;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Eduard
 */
final public class Minecraft_v1_12_R1 extends Minecraft {
    private static final Map<String, Object> particles = new HashMap<>();

    @Override
    public void setBlock(@NotNull Block block, @NotNull Chunk chunk, @NotNull Material material, int data, boolean updateLightning) {

    }

    public Minecraft_v1_12_R1() {
        if (particles.isEmpty())
            for (EnumParticle particle : EnumParticle.values()) {
                particles.put(particle.b().toLowerCase(), particle);
            }

    }



    @Override
    public void forceOpen(@NotNull Chest chest, @NotNull Player player) {

    }

    @Override
    public void canAttackMelee(@NotNull Creature creature, @NotNull String classEntityName, int priority) {
    }

    @Override
    public void canTarget(@NotNull Creature creature, @NotNull String classEntityName, int priority) {
    }

    @Override
    public void removeGoals(@NotNull Creature creature) {

    }

    @Override
    public void removeTargetGoals(@NotNull Creature creature) {

    }

    @Override
    public void sendPacket(Object packet, Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet<?>) packet);
    }

    @Override
    public void setHeadSkin(ItemStack head, String texture, String signature) {


    }

    public void setInventoryName(Inventory inventory, String name) {


    }


    private static final Set<String> blockedMessage = new HashSet<>();

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
        if (particula.d() > 0) {

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
                .a(new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN));
    }

    @Override
    public void setPlayerSkin(Player player, String newSkin) {
        final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        final GameProfile playerProfile = entityPlayer.getProfile();
		playerProfile.getProperties().clear();
        String uuid = "";// Extra.getPlayerUUIDByName(newSkin);


        JsonObject skinProperty = null;//Extra.getSkinProperty(uuid);

        String name = skinProperty.get("name").getAsString();
        String skin = skinProperty.get("value").getAsString();
        String signature = skinProperty.get("signature").getAsString();
        try {
            playerProfile.getProperties().put("textures", new Property(name, skin, signature));
        } catch (Exception e) {
            e.printStackTrace();
        }


        respawnPlayer(player);
        reloadPlayer(player);

    }


    @Override
    public void setPlayerName(Player player, String newName) {

        final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        final GameProfile playerProfile = entityPlayer.getProfile();



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
        addToTab(player);

    }

    @Override
    public void respawnPlayer(Player player) {
        final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        final PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(new int[]{entityPlayer.getId()});
        final PacketPlayOutPlayerInfo removePlayerInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
                ((CraftPlayer) player).getHandle());
        final PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);
        final PacketPlayOutPlayerInfo addPlayerInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
                ((CraftPlayer) player).getHandle());
        final PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entityPlayer.getId(),
                entityPlayer.getDataWatcher(), true);
        final PacketPlayOutEntityHeadRotation headRotation = new PacketPlayOutEntityHeadRotation(entityPlayer,
                (byte) MathHelper.d(entityPlayer.getHeadRotation() * 256.0f / 360.0f));

        sendPacketsToOthers(player, removePlayerInfo,destroy, spawn,addPlayerInfo, metadata, headRotation);

    }



    public void reloadPlayer(Player player){
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(
                new PacketPlayOutPlayerInfo(
                        PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) player).getHandle())
        );

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) player).getHandle())
        );
        ((CraftServer) Bukkit.getServer()).getHandle().moveToWorld(
                ((CraftPlayer) player).getHandle(), ((CraftPlayer) player)
                        .getHandle().dimension, true, player.getLocation(),
                true);
    }

    @Override
    public void removeFromTab(Player playerRemoved) {
        final PacketPlayOutPlayerInfo removePlayerInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
                ((CraftPlayer) playerRemoved).getHandle());
        sendPacketsToAll(removePlayerInfo);

    }

    @Override
    public void addToTab(Player playerToAdd) {
        final PacketPlayOutPlayerInfo addPlayerInfo =

                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
                        ((CraftPlayer) playerToAdd).getHandle());
        sendPacketsToAll(addPlayerInfo);

    }

    public void updateDisplayName(Player player) {
        final PacketPlayOutPlayerInfo updateDisplayName = new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, ((CraftPlayer) player).getHandle());
        sendPacketsToAll(updateDisplayName);
    }

    @Override
    public Object getItemNBT(ItemStack item) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbt = null;
        if ((nbt = nmsItem.getTag()) == null)
            nmsItem.setTag(nbt);

        return nbt;
    }

    @Override
    public ItemStack setItemNBT(ItemStack item, Object nbt) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        nmsItem.setTag((NBTTagCompound) nbt);
        CraftItemStack craftMirror = CraftItemStack.asCraftMirror(nmsItem);
        return craftMirror;
    }

    @Override
    public void sendActionBar(Player player, String message) {
        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(new ChatComponentText(message),ChatMessageType.GAME_INFO);
        sendPacket(packetPlayOutChat, player);
    }


    @Override
    public void disableAI(Entity entity) {

       // MineReflect.disableAI(entity);

    }

    @NotNull
    @Override
    public Collection<Player> getPlayers() {
        return null;
    }

    @Override
    public void setTabList(@NotNull Player player, @NotNull String header, @NotNull String footer) {

    }

    @Override
    public void followLocation(@NotNull Creature creature, @NotNull Location location, double speed, int priority) {

    }

    @Override
    public void followTarget(@NotNull Creature creature, double speed, int priority) {

    }

}
