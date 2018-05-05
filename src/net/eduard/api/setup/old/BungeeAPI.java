package net.eduard.api.setup.old;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
/**
 * Controlar o Bungee pelo Bukkit
 * @version 1.0
 * @since Lib v1.0
 * @author Eduard
 *
 */
@Deprecated
public final class BungeeAPI {
	public static Plugin getInstance() {
		return JavaPlugin.getProvidingPlugin(BungeeAPI.class);
	}
	public static void connectServer(Player player, String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);

		sendBungeeMessage(player, out);
	}
	public static void connectServer(String playerName, String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ConnectOther");
		out.writeUTF(playerName);
		out.writeUTF(server);
		sendBungeeMessage(out);
	}
	
	/**
	 * String server to send to, ALL to send to every server (except the one
	 * sending the plugin message), or ONLINE to send to every server that's
	 * online (except the one sending the plugin message)
	 * 
	 * @param subChannel
	 * @param server
	 * @param data
	 */
	public static void sendToServer(String subChannel,String server,
			Object... data) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Forward"); // So BungeeCord knows to forward it
		out.writeUTF(server);
		out.writeUTF(subChannel); // The channel name to check if this your data

		ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
		DataOutputStream msgout = new DataOutputStream(msgbytes);
		try {
			msgout.writeInt(data.length);
			for (int i = 0; i < data.length; i++) {
				msgout.writeUTF(data[i].toString());
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // You can do anything you want with msgout

		out.writeShort(msgbytes.toByteArray().length);
		out.write(msgbytes.toByteArray());

		sendBungeeMessage(out);
	}
	public static void sendToPlayer(String playerName, String subChannel,
			Object... data) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ForwardToPlayer"); // So BungeeCord knows to forward it
		out.writeUTF(playerName);
		out.writeUTF(subChannel); // The channel name to check if this your data

		ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
		DataOutputStream msgout = new DataOutputStream(msgbytes);
		try {
			msgout.writeInt(data.length);
			for (int i = 0; i < data.length; i++) {
				msgout.writeUTF(data[i].toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // You can do anything you want with msgout

		out.writeShort(msgbytes.toByteArray().length);
		out.write(msgbytes.toByteArray());

		sendBungeeMessage(out);
	}
	public static String[] getMessage(ByteArrayDataInput data) {
		short len = data.readShort();
		byte[] msgbytes = new byte[len];
		
		data.readFully(msgbytes);
		String[] result = null;
		DataInputStream msgin = new DataInputStream(
				new ByteArrayInputStream(msgbytes));
		try {
			result = new String[msgin.readInt()];
			for (int i = 0; i < result.length; i++) {
				String somedata = msgin.readUTF();
				result[i] = somedata;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = new String[0];
		}
		return result;
	}
	public static String getPlayerIP(ByteArrayDataInput data) {
		String ip = data.readUTF();
		int port = data.readInt();
		return ip + ":" + port;
	}
	public static Object[] getServerIp(ByteArrayDataInput data) {
		String serverName = data.readUTF();
		String ip = data.readUTF();
		int port = data.readUnsignedShort();
		return new Object[]{serverName, ip + ":" + port};
	}
	public static Object[] getServerPlayerCount(ByteArrayDataInput data) {
		String server = data.readUTF();
		int playercount = data.readInt();
		return new Object[]{server, playercount};
	}
	public static Object[] getServerPlayerList(ByteArrayDataInput data) {
		String server = data.readUTF();
		String[] playerList = data.readUTF().split(", ");
		return new Object[]{server, playerList};
	}
	public static Object[] getServerPlayerId(ByteArrayDataInput data) {
		String playerName = data.readUTF();
		String uuid = data.readUTF();
		return new Object[]{playerName, UUID.fromString(uuid)};
	}
	public static UUID getPlayerId(ByteArrayDataInput data) {
		String uuid = data.readUTF();
		return UUID.fromString(uuid);
	}
	public static String getPlayerServer(ByteArrayDataInput data) {
		return data.readUTF();
	}
	public static String[] getServers(ByteArrayDataInput data) {
		return data.readUTF().split(", ");
	}
	public static boolean isPlayerIp(String subChannel) {
		return subChannel.equals("IP");
	}
	public static boolean isServerPlayerCount(String subChannel) {
		return subChannel.equals("PlayerCount");
	}
	public static boolean isServerPlayerList(String subChannel) {
		return subChannel.equals("PlayerCount");
	}
	public static boolean isServers(String subChannel) {
		return subChannel.equals("GetServers");
	}
	public static boolean isPlayerServer(String subChannel) {
		return subChannel.equals("GetServer");
	}

	public boolean isServerIp(String subChannel) {
		return subChannel.equals("ServerIP");
	}
	public static boolean isServerPlayerId(String subChannel) {
		return subChannel.equals("UUIDOther");
	}
	public static boolean isPlayerId(String subChannel) {
		return subChannel.equals("UUID");
	}
	public static boolean isPlayerServerIp(String subChannel) {
		return subChannel.equals("ServerIP");
	}
	
	public static void sendMessage(String playerName, String message) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();

		out.writeUTF("Message");
		out.writeUTF(playerName);
		out.writeUTF(message);
		sendBungeeMessage(out);
	}
	public static void getPlayerCount(String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerCount");
		out.writeUTF(server);
		sendBungeeMessage(out);
	}
	public static void getServers() {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("GetServers");
		sendBungeeMessage(out);
	}
	public static void getPlayerList(String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerList");
		out.writeUTF(server);
		sendBungeeMessage(out);
	}
	public static void getPlayerServer(Player player) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("GetServer");
		sendBungeeMessage(player, out);
	}
	public static void sendBungeeMessage(Player player,
			ByteArrayDataOutput message) {
		player.sendPluginMessage(getInstance(), "BungeeCord",
				message.toByteArray());
	}
	public static void kickPlayer(String playerName, String reason) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("KickPlayer");
		out.writeUTF(playerName);
		out.writeUTF(reason);
		sendBungeeMessage(out);
	}
	public static void getServerIp(String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ServerIP");
		out.writeUTF(server);
		sendBungeeMessage(out);
	}
	public static void getPlayerUuid(Player player) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("UUID");
		sendBungeeMessage(player, out);
	}
	public static void getPlayerUuid(String playerName) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("UUIDOther");
		out.writeUTF(playerName);
		sendBungeeMessage(out);
	}
	public static void sendBungeeMessage(ByteArrayDataOutput message) {
		sendBungeeMessage(getFirstPlayer(), message);
	}
	public static void getRealIp(Player player) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("IP");

		player.sendPluginMessage(getInstance(), "BungeeCord",
				out.toByteArray());
	}
	public static Player getFirstPlayer() {
		return Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
	}
}
