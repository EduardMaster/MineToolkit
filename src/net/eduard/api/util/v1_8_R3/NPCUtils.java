package net.eduard.api.util.v1_8_R3;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;

public class NPCUtils {
	public void createNPC(Player player, String name) {

		Location location = player.getLocation();
		MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer nmsWorld = ((CraftWorld) player.getWorld()).getHandle();
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "§e§l" + name);
		EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld));
		changeSkin(gameProfile);
		npc.setLocation(location.getX(), location.getY(), location.getZ(), player.getLocation().getYaw(),
				player.getLocation().getPitch());

		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
		connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, npc));
		connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));

	}

	private void changeSkin(GameProfile profile) {
		String texture = "";
		String signature = "";
		profile.getProperties().put("textures", new Property("textures", texture, signature));
	}
}
