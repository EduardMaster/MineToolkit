package net.eduard.api.lib.abstraction;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EnumClientCommand;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInClientCommand;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;

/**
 * @author Eduard
 */
public class Minecraft_v1_7_R4 extends Minecraft {

    public void sendPacket(Object packet, Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet) packet);
    }

    @Override
    public void removeFromTab(Player playerRemoved) {

        final PacketPlayOutPlayerInfo removePlayerInfo = PacketPlayOutPlayerInfo
                .removePlayer(((CraftPlayer) playerRemoved).getHandle());

        sendPacketsToOthers(playerRemoved, removePlayerInfo);
    }

    @Override
    public void addToTab(Player playerToAdd) {
        final PacketPlayOutPlayerInfo addPlayerInfo = PacketPlayOutPlayerInfo
                .addPlayer(((CraftPlayer) playerToAdd).getHandle());
        final PacketPlayOutPlayerInfo updatePlayerInfo = PacketPlayOutPlayerInfo
                .updateDisplayName(((CraftPlayer) playerToAdd).getHandle());
        sendPacketsToOthers(playerToAdd, addPlayerInfo, updatePlayerInfo);

    }

    @Override
    public Object getItemNBT(ItemStack item) {

        return null;
    }

    @Override
    public ItemStack setItemNBT(ItemStack item, Object nbt) {

        return item;
    }

    @Override
    public void respawnPlayer(Player playerToRespawn) {
        final EntityPlayer entityPlayer = ((CraftPlayer) playerToRespawn).getHandle();
        final PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(new int[]{entityPlayer.getId()});
        final PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);
        final PacketPlayOutPlayerInfo addPlayerInfo = PacketPlayOutPlayerInfo
                .addPlayer(((CraftPlayer) playerToRespawn).getHandle());
        final PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entityPlayer.getId(),
                entityPlayer.getDataWatcher(), true);
        final PacketPlayOutEntityHeadRotation headRotation = new PacketPlayOutEntityHeadRotation(entityPlayer,
                (byte) MathHelper.d(entityPlayer.getHeadRotation() * 256.0f / 360.0f));
        final PacketPlayOutPlayerInfo removePlayerInfo = PacketPlayOutPlayerInfo
                .removePlayer(((CraftPlayer) playerToRespawn).getHandle());

        sendPacketsToOthers(playerToRespawn, removePlayerInfo, addPlayerInfo, destroy, spawn, metadata, headRotation);

    }

    @Override
    public void performRespawn(Player player) {
        ((CraftPlayer) player).getHandle().playerConnection
                .a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));

    }

    @Override
    public void setHeadSkin(ItemStack head, String texture, String signature) {

    }

    @Override
    public void setPlayerName(Player player, String newName) {

    }

    @Override
    public void setPlayerSkin(Player player, String newSkin) {


    }

    @Override
    public void sendActionBar(Player player, String message) {


    }

    @Override
    public void sendParticle(Player player, String name, Location location, int amount, float xOffset, float yOffset, float zOffset, float speed) {

    }

}
