package net.eduard.api.test;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.manager.EventsManager;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

/**
 * Testando skin customizada
 * @author Eduard
 *
 */
public class TesteAlterandoSkin extends EventsManager{
	@EventHandler
	public void event(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		final EntityPlayer entityPlayer = ((CraftPlayer) p).getHandle();
		final GameProfile playerProfile = entityPlayer.getProfile();
//		playerProfile.getProperties().clear();
//		String uuid = Extra.getPlayerUUIDByName(newSkin);
//		System.out.println(uuid);
//		JsonObject skinProperty = Extra.getSkinProperty(uuid);
//		System.out.println(skinProperty);
//		String name = skinProperty.get("name").getAsString();
//		String skin = skinProperty.get("value").getAsString();
//		String signature = skinProperty.get("signature").getAsString();
		String texture = "eyJ0aW1lc3RhbXAiOjE1NTAwNTM0NDczOTMsInByb2ZpbGVJZCI6IjkyMjcyZWFkZmQzYzQyNWE4MDk4MTAzNGE4ZDRhMzFmIiwicHJvZmlsZU5hbWUiOiJFZHVhcmRLaWxsZXJQcm8iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzY1MzEyZTdlNGFlNmI4YThiN2VjMWUzM2QzZWI3MDJiMDU4NzcyYWZiMjI0ZjljNTM3NGI5ZGE1ZDExNWVkZmMifX19";
		String

		signature = "gVqmx93qn9xdM4RnU7vIsSv9AEUqB+qA+XrzXegTUdrLHPsKo7uOngOewxE7s4r21tsjlMJq5svxUACRJ25MZQXPZd2Dv1nxsnyevvzjYS2h/HsrspuC5sc6+rGwroT0oIbQqb/RZyuivAtWULBmdUGn2VwvdxVY3PYDPzicsZ90IczOLCJHNlVh1lDbAu57w5diShByWG4Lf+cdbcuxyjVenMPLisxr8pBkXY59HlCqMqya7e8LhOdU9lmwxZflb3LwpamLvwxeTP8w5UhA6bnLFFamSOH3o76KO/2xr2tNODTEPRV7+mTHBHWd3epGOAAWkk5akFbS8EXwioPBfzPw9O3T3xZGvx9BpTMWH6Bt0kyzyrkEbmEfEUUbVjwLnPvQeGDkTq92CGRmI5091pCcvjhDF32lul6hbYS5p8S93Dgu8g5xTP4fzUebjCLMr0CsACcB7/hSHK8M7G3mjEdyOC749r2rCC8dlYZlnuOlg07jWpTyv0RM9D57y+8MB6IRWAVrvAocM01ebsRIJSTEDovFPFh49qEL6yLfathCMIUc+XZsUR0IlUTZ13+4JETihCj9nH3phYP12bXI+2N7ZFX+Zg3QBzuTGgnhAU27Foyu5ZQurSZ/W5IgJg7oL1PtM7AG+uAMzwBZwZrXVOq153cmgJwmEXMbS++LhuE=";

		playerProfile.getProperties().put("textures", new Property("textures", texture, signature));
	}

	@EventHandler
	public void event(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.getItemInHand() == null)
			return;
		if (p.getItemInHand().getType() == Material.TNT) {
			final EntityPlayer entityPlayer = ((CraftPlayer) p).getHandle();
			final GameProfile playerProfile = entityPlayer.getProfile();
//			playerProfile.getProperties().clear();
//			String uuid = Extra.getPlayerUUIDByName(newSkin);
//			System.out.println(uuid);
//			JsonObject skinProperty = Extra.getSkinProperty(uuid);
//			System.out.println(skinProperty);
//			String name = skinProperty.get("name").getAsString();
//			String skin = skinProperty.get("value").getAsString();
//			String signature = skinProperty.get("signature").getAsString();
			String texture = "eyJ0aW1lc3RhbXAiOjE1NTAwNTM0NDczOTMsInByb2ZpbGVJZCI6IjkyMjcyZWFkZmQzYzQyNWE4MDk4MTAzNGE4ZDRhMzFmIiwicHJvZmlsZU5hbWUiOiJFZHVhcmRLaWxsZXJQcm8iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzY1MzEyZTdlNGFlNmI4YThiN2VjMWUzM2QzZWI3MDJiMDU4NzcyYWZiMjI0ZjljNTM3NGI5ZGE1ZDExNWVkZmMifX19";
			String

			signature = "gVqmx93qn9xdM4RnU7vIsSv9AEUqB+qA+XrzXegTUdrLHPsKo7uOngOewxE7s4r21tsjlMJq5svxUACRJ25MZQXPZd2Dv1nxsnyevvzjYS2h/HsrspuC5sc6+rGwroT0oIbQqb/RZyuivAtWULBmdUGn2VwvdxVY3PYDPzicsZ90IczOLCJHNlVh1lDbAu57w5diShByWG4Lf+cdbcuxyjVenMPLisxr8pBkXY59HlCqMqya7e8LhOdU9lmwxZflb3LwpamLvwxeTP8w5UhA6bnLFFamSOH3o76KO/2xr2tNODTEPRV7+mTHBHWd3epGOAAWkk5akFbS8EXwioPBfzPw9O3T3xZGvx9BpTMWH6Bt0kyzyrkEbmEfEUUbVjwLnPvQeGDkTq92CGRmI5091pCcvjhDF32lul6hbYS5p8S93Dgu8g5xTP4fzUebjCLMr0CsACcB7/hSHK8M7G3mjEdyOC749r2rCC8dlYZlnuOlg07jWpTyv0RM9D57y+8MB6IRWAVrvAocM01ebsRIJSTEDovFPFh49qEL6yLfathCMIUc+XZsUR0IlUTZ13+4JETihCj9nH3phYP12bXI+2N7ZFX+Zg3QBzuTGgnhAU27Foyu5ZQurSZ/W5IgJg7oL1PtM7AG+uAMzwBZwZrXVOq153cmgJwmEXMbS++LhuE=";
			
			playerProfile.getProperties().put("textures", new Property("textures", texture, signature));

			final PacketPlayOutPlayerInfo removePlayerInfo = new PacketPlayOutPlayerInfo(
					EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) p).getHandle());
			final PacketPlayOutPlayerInfo addPlayerInfo = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER,
					((CraftPlayer) p).getHandle());
			final PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(
					new int[] { entityPlayer.getId() });
			final PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);
			final PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entityPlayer.getId(),
					entityPlayer.getDataWatcher(), true);
			final PacketPlayOutEntityHeadRotation headRotation = new PacketPlayOutEntityHeadRotation(entityPlayer,
					(byte) MathHelper.d(entityPlayer.getHeadRotation() * 256.0f / 360.0f));
			for (Player jogador : Mine.getPlayers()) {
				((CraftPlayer) jogador).getHandle().playerConnection.sendPacket(removePlayerInfo);
				((CraftPlayer) jogador).getHandle().playerConnection.sendPacket(addPlayerInfo);
			}
			
			Location local = p.getLocation();
//			p.setHealth(0);
//			p.spigot().respawn();
			World w = Bukkit.getWorld("world_nether");
			p.teleport(w.getSpawnLocation());
			p.teleport(local);
//			((CraftPlayer) p).getHandle().playerConnection.sendPacket(destroy);
//			((CraftPlayer) p).getHandle().playerConnection.sendPacket(spawn);
//			((CraftPlayer) p).getHandle().playerConnection.sendPacket(metadata);
//			((CraftPlayer) p).getHandle().playerConnection.sendPacket(headRotation);
			p.sendMessage("§aFuncionando");
//			respawnPlayer(player);
		}

	}
}
