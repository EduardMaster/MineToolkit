package net.eduard.api.lib.game;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Jogador Off Ficticio<br>
 * nome;id
 * 
 * @author Eduard
 * @version 1.2
 *
 */
public class FakePlayer implements OfflinePlayer, Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private UUID id;


	public void setName(String name) {
		this.name = name;
	}

	public FakePlayer(String name) {
		this.name = name;
		fixUUID();
	}
	public static void main(String[] args) {
		try {
			byte[] bites1 = ("OfflinePlayer:" + "EduardKillerPro").getBytes("UTF-8");
			byte[] bites2 = ("OfflinePlayer:" + "EduardKillerPro".toLowerCase()).getBytes("UTF-8");
			UUID id1 = UUID.nameUUIDFromBytes(bites1);
			UUID id2 = UUID.nameUUIDFromBytes(bites2);
			System.out.println(id1);
			System.out.println(id2);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setIdByName() {
		try {
			this.id = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void setIdByNameLowerCase() {
		try {
			this.id = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name.toLowerCase()).getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void fixUUID(){
		Player player = getPlayer();
		if (player!=null){
			this.id = player.getUniqueId();
		}else{
		 setIdByName();
		}
	}

	@Override
	public String toString() {
		return "FakePlayer [name=" + name + ", id=" + id + "]";
	}

	public FakePlayer(String name, UUID id) {
		this(name);
		this.setId(id);
	}

	public FakePlayer(UUID id) {
		this(null, id);
	}

	public FakePlayer(OfflinePlayer player) {
		this(player.getName(), player.getUniqueId());
	}

	public FakePlayer() {
		// TODO Auto-generated constructor stub
	}
	public void log(String msg) {
//		System.out.println(msg);
	}

	@Override
	public int hashCode() {
		log("Metodo Hashcode sendo feito");
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		log("Metodo equals sendo feito");
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FakePlayer other = (FakePlayer) obj;
	
		if (other.getName() != null && getName() != null) {
			return other.getName().equalsIgnoreCase(this.getName());
		}
		if (other.getId() != null && this.getId() != null) {
			return other.getId().equals(this.getId());
		}
		return false;
	}
	@Override
	public boolean isOp() {
		return false;
	}

	@Override
	public void setOp(boolean op) {

	}

	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> mapaNovo = new HashMap<>();
		mapaNovo.put("name", this.name);
		mapaNovo.put("uuid", this.getUniqueId());
		return mapaNovo;
	}

	@Override
	public Location getBedSpawnLocation() {
		return null;
	}

	@Override
	public long getFirstPlayed() {
		return 0;
	}

	@Override
	public long getLastPlayed() {
		return 0;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Player getPlayer() {
		Player player = null;
		if (id != null)
			player = Bukkit.getPlayer(id);
		if (player != null)
			return player;
		if (name != null) {
			player = Bukkit.getPlayer(name);
		}
		return player;
	}

	@Override
	public UUID getUniqueId() {
		return this.id;
	}

	@Override
	public boolean hasPlayedBefore() {
		return true;
	}

	@Override
	public boolean isBanned() {
		return false;
	}

	@Override
	public boolean isOnline() {
		return getPlayer() != null;
	}

	@Override
	public boolean isWhitelisted() {

		return false;
	}

	@Deprecated
	public void setBanned(boolean bol) {


	}

	@Override
	public void setWhitelisted(boolean bol) {


	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

}
