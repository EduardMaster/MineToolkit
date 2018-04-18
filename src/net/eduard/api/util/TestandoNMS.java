package net.eduard.api.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

import net.eduard.api.setup.Mine;
import net.eduard.api.util.v1_8_R3.ActionBarUtil;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class TestandoNMS  implements CommandExecutor {
	

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
		if (cmd.getName().equalsIgnoreCase("sendmesnsagem")) {
			if (sender instanceof Player) {

				Player p = (Player) sender;

				if (args.length == 0) {
					p.sendMessage("§cPor favor,use /sendmesnsagem <mensagem>");
				} else {

					String msg = Mine.getText(0, args);
					ActionBarUtil.sendActionBarMessage(p, msg);

				}
			}

		}
		if (cmd.getName().equalsIgnoreCase("setnpc")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				TestandoNMS.getNewNpc(p);
				p.sendMessage("§aNpc setado com sucesso");
			}
		}
		return false;
	}


	public static void ativar() {
		Bukkit.getConsoleSender().sendMessage("§e[NMS] Habilitado");
		Mine.command("sendmesnsagem", new TestandoNMS(), null, null);
		Mine.command("setnpc", new TestandoNMS(), null, null);
	}
	
	public static Class<?> getNMSClass(String nmsClassString) throws ClassNotFoundException {
	    String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
	    String name = "net.minecraft.server." + version + nmsClassString;
	    Class<?> nmsClass = Class.forName(name);
	    return nmsClass;
	}

	public static Class<?> getNMSArray(String nmsClassString) throws ClassNotFoundException {
	    String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
	    String name = "[Lnet.minecraft.server." + version + nmsClassString + ";";
	    Class<?> nmsClass = Class.forName(name);
	    return nmsClass;
	}
	public static void getNewNpc(Player p) {
		try {
			Object servidor = Bukkit.getServer().getClass().getDeclaredMethod("getServer").invoke(Bukkit.getServer());
			World world = p.getWorld();
			Object mundo = world.getClass().getDeclaredMethod("getHandle").invoke(world);
			GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "TESTONA");
			Class<?> cplayerInteractPlaye = TestandoNMS.getNMSClass("PlayerInteractManager");
			Object interact = cplayerInteractPlaye.getDeclaredConstructor(TestandoNMS.getNMSClass("World")).newInstance(mundo);
			Class<?> centityPlayer = TestandoNMS.getNMSClass("EntityPlayer");
			Constructor<?> constructor = centityPlayer.getDeclaredConstructor(TestandoNMS.getNMSClass("MinecraftServer"), TestandoNMS.getNMSClass("WorldServer"),
					gameProfile.getClass(), cplayerInteractPlaye);
			Object npc = constructor.newInstance(servidor, mundo, gameProfile, interact);
			List<Object> lista = new ArrayList<>();
			lista.add(npc);
			System.out.println("NPC: " + npc.getClass());
//			System.out.println("" + npc.getClass());
			Method setLocation = TestandoNMS.getNMSClass("Entity").getDeclaredMethod("setLocation", double.class, double.class, double.class,
					float.class, float.class);
			setLocation.invoke(npc, p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), 0F, 0F);
			Class<?> cpacote = TestandoNMS.getNMSClass("PacketPlayOutPlayerInfo");
			Class<?> cenumPlayerInfo = TestandoNMS.getNMSClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
			Object enumer = cenumPlayerInfo.getField("ADD_PLAYER").get(cenumPlayerInfo);
			System.out.println("ENUMER: " + enumer.getClass());
//			System.out.println("" + enumer);
			Class<?> cEntityPlayer = TestandoNMS.getNMSClass("EntityPlayer");
			for (Constructor<?> construtor : cpacote.getDeclaredConstructors()) {
				System.out.println(""+construtor);
			}
			Constructor<?> pacoteConstructor = cpacote.getDeclaredConstructors()[2];
//			System.out.println("" + pacoteConstructor);
			Object entidadeArray = Array.newInstance(cEntityPlayer, 10);
//			Array.set
			System.out.println("ARRAY: " + entidadeArray.getClass());
			Array.set(entidadeArray, 0, npc);
			Object pacote = pacoteConstructor.newInstance(enumer, entidadeArray);
//			Lists.newArrayList(npc)
//			new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, Lists.newArrayList(npc));
			sendPacket(pacote, p);
			Object pacote2 = TestandoNMS.getNMSClass("PacketPlayOutNamedEntitySpawn")
					.getDeclaredConstructor(TestandoNMS.getNMSClass("EntityHuman")).newInstance(npc);
			sendPacket(pacote2, p);
			
			
			
			
			// centityPlayer.getDeclaredConstructor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
		// WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
		// EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, new
		// GameProfile(UUID.randomUUID(), "NC"), new PlayerInteractManager(nmsWorld));
		//// npc.setLocation(0.0, 70.0, 0.0, 0F, 0F);
		// npc.setLocation(p.getLocation().getX(), p.getLocation().getY(),
		// p.getLocation().getZ(), 0F, 0F);
		//
		// PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
		//// connection.sendPacket(new
		// PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, npc));
		// connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
	}

	public static void sendPacket(Object pacote, Player player) {
		try {
			Object getHandle = player.getClass().getDeclaredMethod("getHandle").invoke(player);
			Object playerConnection = getHandle.getClass().getDeclaredField("playerConnection").get(getHandle);
			playerConnection.getClass().getDeclaredMethod("sendPacket", TestandoNMS.getNMSClass("Packet"))
					.invoke(playerConnection, pacote);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	public static void makeRespawn(Player player) {
		try {
			// Object packet = Extra.getNew(claz_pPlayInClientCommand,
			// Extra.getValue(claz_mEnumClientCommand, "PERFORM_RESPAWN"));
			// Extra.getResult(getConnection(player), "a", packet);
			PacketPlayInClientCommand pacote = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
			CraftPlayer craftplayer = (CraftPlayer) player;
			craftplayer.getHandle().playerConnection.a(pacote);

			Constructor<PacketPlayInClientCommand> contructor = PacketPlayInClientCommand.class.getDeclaredConstructor(EnumClientCommand.class);
			PacketPlayInClientCommand objectPacote = contructor.newInstance(EnumClientCommand.class.getDeclaredField("ENUM_CLIENT_COMMAND"));
			PlayerConnection conexao = craftplayer.getHandle().playerConnection;
			Method metodo = conexao.getClass().getDeclaredMethod("a", PacketPlayInClientCommand.class);
			metodo.setAccessible(true);
			metodo.invoke(conexao, objectPacote);
			
			
		} catch (Exception ex) {
			try {
				// Object packet = Extra.getNew(claz_pPlayInClientCommand,
				// Extra.getValue(claz_mEnumClientCommand2, "PERFORM_RESPAWN"));
				// Extra.getResult(getConnection(player), "a", packet);
				PacketPlayInClientCommand pacote = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
				CraftPlayer craftplayer = (CraftPlayer) player;
				craftplayer.getHandle().playerConnection.a(pacote);

				Constructor<PacketPlayInClientCommand> contructor = PacketPlayInClientCommand.class.getDeclaredConstructor(EnumClientCommand.class);
				PacketPlayInClientCommand objectPacote = contructor.newInstance(EnumClientCommand.class.getDeclaredField("ENUM_CLIENT_COMMAND"));
				PlayerConnection conexao = craftplayer.getHandle().playerConnection;
				Method metodo = conexao.getClass().getDeclaredMethod("a", PacketPlayInClientCommand.class);
				metodo.setAccessible(true);
				metodo.invoke(conexao, objectPacote);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
