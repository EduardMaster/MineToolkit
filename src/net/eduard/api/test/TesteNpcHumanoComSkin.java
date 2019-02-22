package net.eduard.api.test;

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
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class TesteNpcHumanoComSkin {
	public static void npcBolado(Player p) {
		Location location = p.getLocation();
//		String texture = "";
//		String signature = "";
		CraftPlayer cp = (CraftPlayer) p;
		EntityPlayer ep = cp.getHandle();
		PlayerConnection pc = ep.playerConnection;
		CraftWorld cw = (CraftWorld) cp.getWorld();
		WorldServer ws = cw.getHandle();
		CraftServer sc = ((CraftServer) Bukkit.getServer());
//		DedicatedPlayerList ds = sc.getHandle();
		MinecraftServer ms = sc.getServer();
		String texture = "eyJ0aW1lc3RhbXAiOjE1NTAwNTM0NDczOTMsInByb2ZpbGVJZCI6IjkyMjcyZWFkZmQzYzQyNWE4MDk4MTAzNGE4ZDRhMzFmIiwicHJvZmlsZU5hbWUiOiJFZHVhcmRLaWxsZXJQcm8iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzY1MzEyZTdlNGFlNmI4YThiN2VjMWUzM2QzZWI3MDJiMDU4NzcyYWZiMjI0ZjljNTM3NGI5ZGE1ZDExNWVkZmMifX19";
		String

		signature = "gVqmx93qn9xdM4RnU7vIsSv9AEUqB+qA+XrzXegTUdrLHPsKo7uOngOewxE7s4r21tsjlMJq5svxUACRJ25MZQXPZd2Dv1nxsnyevvzjYS2h/HsrspuC5sc6+rGwroT0oIbQqb/RZyuivAtWULBmdUGn2VwvdxVY3PYDPzicsZ90IczOLCJHNlVh1lDbAu57w5diShByWG4Lf+cdbcuxyjVenMPLisxr8pBkXY59HlCqMqya7e8LhOdU9lmwxZflb3LwpamLvwxeTP8w5UhA6bnLFFamSOH3o76KO/2xr2tNODTEPRV7+mTHBHWd3epGOAAWkk5akFbS8EXwioPBfzPw9O3T3xZGvx9BpTMWH6Bt0kyzyrkEbmEfEUUbVjwLnPvQeGDkTq92CGRmI5091pCcvjhDF32lul6hbYS5p8S93Dgu8g5xTP4fzUebjCLMr0CsACcB7/hSHK8M7G3mjEdyOC749r2rCC8dlYZlnuOlg07jWpTyv0RM9D57y+8MB6IRWAVrvAocM01ebsRIJSTEDovFPFh49qEL6yLfathCMIUc+XZsUR0IlUTZ13+4JETihCj9nH3phYP12bXI+2N7ZFX+Zg3QBzuTGgnhAU27Foyu5ZQurSZ/W5IgJg7oL1PtM7AG+uAMzwBZwZrXVOq153cmgJwmEXMbS++LhuE=";
		GameProfile profile = new GameProfile(UUID.randomUUID(), "EduBolado");
		EntityPlayer npc = new EntityPlayer(ms, ws, profile, new PlayerInteractManager(ws));
		profile.getProperties().put("textures", new Property("textures", texture, signature));
		npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

		pc.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, npc));
		pc.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
		profile = ((CraftPlayer) p).getProfile();
		profile.getProperties().put("textures", new Property("textures", texture, signature));
	}

}
