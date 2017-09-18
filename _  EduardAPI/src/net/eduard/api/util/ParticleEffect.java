package net.eduard.api.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.eduard.api.minecraft.reflection.Reflections;

public enum ParticleEffect {
	  HUGE_EXPLOSION(
			    "hugeexplosion"), 
			  LARGE_EXPLODE(
			    "largeexplode"), 
			  FIREWORKS_SPARK(
			    "fireworksSpark"), 
			  BUBBLE(
			    "bubble"), 
			  SUSPEND(
			    "suspend"), 
			  DEPTH_SUSPEND(
			    "depthSuspend"), 
			  TOWN_AURA(
			    "townaura"), 
			  CRIT(
			    "crit"), 
			  MAGIC_CRIT(
			    "magicCrit"), 
			  SMOKE(
			    "smoke"), 
			  MOB_SPELL(
			    "mobSpell"), 
			  MOB_SPELL_AMBIENT(
			    "mobSpellAmbient"), 
			  SPELL(
			    "spell"), 
			  INSTANT_SPELL(
			    "instantSpell"), 
			  WITCH_MAGIC(
			    "witchMagic"), 
			  NOTE(
			    "note"), 
			  PORTAL(
			    "portal"), 
			  ENCHANTMENT_TABLE(
			    "enchantmenttable"), 
			  EXPLODE(
			    "explode"), 
			  FLAME(
			    "flame"), 
			  LAVA(
			    "lava"), 
			  FOOTSTEP(
			    "footstep"), 
			  SPLASH(
			    "splash"), 
			  WAKE(
			    "wake"), 
			  LARGE_SMOKE(
			    "largesmoke"), 
			  CLOUD(
			    "cloud"), 
			  RED_DUST(
			    "reddust"), 
			  SNOWBALL_POOF(
			    "snowballpoof"), 
			  DRIP_WATER(
			    "dripWater"), 
			  DRIP_LAVA(
			    "dripLava"), 
			  SNOW_SHOVEL(
			    "snowshovel"), 
			  SLIME(
			    "slime"), 
			  HEART(
			    "heart"), 
			  ANGRY_VILLAGER(
			    "angryVillager"), 
			  HAPPY_VILLAGER(
			    "happyVillager");

			  private static final double MAX_RANGE = 16.0D;
			  private static Constructor<?> packetPlayOutWorldParticles;
			  private static Method getHandle;
			  private static Field playerConnection;
			  private static Method sendPacket;
			  private static final Map<String, ParticleEffect> NAME_MAP;
			  private final String name;
			  
			  public static double cos(double i)
			  {
			    return Math.cos(i);
			  }

			  public static double sin(double i) {
			    return Math.sin(i);
			  }
			  public static Plugin getPlugin(){
				  return JavaPlugin.getProvidingPlugin(ParticleEffect.class);
			  }

			 

			  static { NAME_MAP = new HashMap<>();

			    for (ParticleEffect p : values())
			      NAME_MAP.put(p.name, p);
			    try {
			      packetPlayOutWorldParticles = Reflections.getConstructor(Reflections.PacketType.PLAY_OUT_WORLD_PARTICLES.getPacket(), new Class[] { String.class, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, 
			        Float.TYPE, Float.TYPE, Integer.TYPE });
			      getHandle = Reflections.getMethod("CraftPlayer", Reflections.SubPackageType.ENTITY, "getHandle", new Class[0]);
			      playerConnection = Reflections.getField("EntityPlayer", Reflections.PackageType.MINECRAFT_SERVER, "playerConnection");
			      sendPacket = Reflections.getMethod(playerConnection.getType(), "sendPacket", new Class[] { Reflections.getClass("Packet", Reflections.PackageType.MINECRAFT_SERVER) });
			    } catch (Exception e) {
			      e.printStackTrace();
			    }
			  }

			  private ParticleEffect(String name)
			  {
			    this.name = name;
			  }

			  public String getName()
			  {
			    return this.name;
			  }

			  public static ParticleEffect fromName(String name)
			  {
			    if (name != null)
			      for (Map.Entry<?,?> e : NAME_MAP.entrySet())
			        if (((String)e.getKey()).equalsIgnoreCase(name))
			          return (ParticleEffect)e.getValue();
			    return null;
			  }

	private static final class PacketInstantiationException extends RuntimeException
	  {
	    private static final long serialVersionUID = 3203085387160737484L;

	    public PacketInstantiationException(String message)
	    {
	    }

	    public PacketInstantiationException(String message, Throwable cause)
	    {
	      super();
	    }
	  }

	  private static final class PacketSendingException extends RuntimeException
	  {
	    private static final long serialVersionUID = 3203085387160737484L;

	    public PacketSendingException(String message, Throwable cause)
	    {
	      super();
	    }
	  }
	 private static List<Player> getPlayers(Location center, double range)
	  {
	    List<Player> players = new ArrayList<>();
	    String name = center.getWorld().getName();
	    double squared = range * range;
	    for (Player p : Bukkit.getOnlinePlayers())
	      if ((p.getWorld().getName().equals(name)) && (p.getLocation().distanceSquared(center) <= squared))
	        players.add(p);
	    return players;
	  }

	  private static Object instantiatePacket(String name, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	  {
	    if (amount < 1)
	      throw new PacketInstantiationException("Amount cannot be lower than 1");
	    try {
	      return packetPlayOutWorldParticles.newInstance(new Object[] { name, Float.valueOf((float)center.getX()), Float.valueOf((float)center.getY()), Float.valueOf((float)center.getZ()), Float.valueOf(offsetX), Float.valueOf(offsetY), Float.valueOf(offsetZ), Float.valueOf(speed), Integer.valueOf(amount) });
	    } catch (Exception e) {
	      throw new PacketInstantiationException("Packet instantiation failed", e);
	    }
	  }

	  private static Object instantiateIconCrackPacket(int id, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	  {
	    return instantiatePacket("iconcrack_" + id, center, offsetX, offsetY, offsetZ, speed, amount);
	  }

	  private static Object instantiateBlockCrackPacket(int id, byte data, Location center, float offsetX, float offsetY, float offsetZ, int amount)
	  {
	    return instantiatePacket("blockcrack_" + id + "_" + data, center, offsetX, offsetY, offsetZ, 0.0F, amount);
	  }

	  private static Object instantiateBlockDustPacket(int id, byte data, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	  {
	    return instantiatePacket("blockdust_" + id + "_" + data, center, offsetX, offsetY, offsetZ, speed, amount);
	  }

	  private static void sendPacket(Player p, Object packet)
	  {
	    try
	    {
	      sendPacket.invoke(playerConnection.get(getHandle.invoke(p, new Object[0])), new Object[] { packet });
	    } catch (Exception e) {
	      throw new PacketSendingException("Failed to send a packet to player '" + p.getName() + "'", e);
	    }
	  }

	  private static void sendPacket(Collection<Player> players, Object packet)
	  {
	    for (Player p : players)
	      sendPacket(p, packet);
	  }
	  public void display(Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player players)
	  {
	    sendPacket(Arrays.asList(
	      new Player[] { players }), instantiatePacket(this.name, center, offsetX, offsetY, offsetZ, speed, amount));
	  }

	  public void display(Location center, double range, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	  {
	    if (range > MAX_RANGE)
	      throw new IllegalArgumentException("Range cannot exceed the maximum value of 16");
	    sendPacket(getPlayers(center, range), instantiatePacket(this.name, center, offsetX, offsetY, offsetZ, speed, amount));
	  }

	  public void display(Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	  {
	    display(center, MAX_RANGE, offsetX, offsetY, offsetZ, speed, amount);
	  }

	  public static void displayIconCrack(Location center, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player[] players)
	  {
	    sendPacket(Arrays.asList(players), instantiateIconCrackPacket(id, center, offsetX, offsetY, offsetZ, speed, amount));
	  }

	  public static void displayIconCrack(Location center, double range, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	  {
	    if (range > MAX_RANGE)
	      throw new IllegalArgumentException("Range has to be lower/equal the maximum of 16");
	    sendPacket(getPlayers(center, range), instantiateIconCrackPacket(id, center, offsetX, offsetY, offsetZ, speed, amount));
	  }

	  public static void displayIconCrack(Location center, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	  {
	    displayIconCrack(center, MAX_RANGE, id, offsetX, offsetY, offsetZ, speed, amount);
	  }

	  public static void displayBlockCrack(Location center, int id, byte data, float offsetX, float offsetY, float offsetZ, int amount, Player[] players)
	  {
	    sendPacket(Arrays.asList(players), instantiateBlockCrackPacket(id, data, center, offsetX, offsetY, offsetZ, amount));
	  }

	  public static void displayBlockCrack(Location center, double range, int id, byte data, float offsetX, float offsetY, float offsetZ, int amount)
	  {
	    if (range > MAX_RANGE)
	      throw new IllegalArgumentException("Range has to be lower/equal the maximum of 16");
	    sendPacket(getPlayers(center, range), instantiateBlockCrackPacket(id, data, center, offsetX, offsetY, offsetZ, amount));
	  }

	  public static void displayBlockCrack(Location center, int id, byte data, float offsetX, float offsetY, float offsetZ, int amount)
	  {
	    displayBlockCrack(center, MAX_RANGE, id, data, offsetX, offsetY, offsetZ, amount);
	  }

	  public static void displayBlockDust(Location center, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player[] players)
	  {
	    sendPacket(Arrays.asList(players), instantiateBlockDustPacket(id, data, center, offsetX, offsetY, offsetZ, speed, amount));
	  }

	  public static void displayBlockDust(Location center, double range, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	  {
	    if (range > MAX_RANGE)
	      throw new IllegalArgumentException("Range has to be lower/equal the maximum of 16");
	    sendPacket(getPlayers(center, range), instantiateBlockDustPacket(id, data, center, offsetX, offsetY, offsetZ, speed, amount));
	  }

	  public static void displayBlockDust(Location center, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	  {
	    displayBlockDust(center, MAX_RANGE, id, data, offsetX, offsetY, offsetZ, speed, amount);
	  }
	 public static void coneEffect(Location loc) {
		    new BukkitRunnable()
		    {
		      double phi = 0.0D;

		      @Override
			public void run() { this.phi += 0.3926990816987241D;

		        for (double t = 0.0D; t <= 6.283185307179586D; t += 0.1963495408493621D) {
		          for (double i = 0.0D; i <= 1.0D; i += 1.0D) {
		            double x = 0.4D * (6.283185307179586D - t) * 0.5D * cos(t + this.phi + i * 3.141592653589793D);
		            double y = 0.5D * t;
		            double z = 0.4D * (6.283185307179586D - t) * 0.5D * sin(t + this.phi + i * 3.141592653589793D);
		            loc.add(x, y, z);
		            ParticleEffect.HEART.display(loc, 0.0F, 0.0F, 0.0F, 0.0F, 1);
		            loc.subtract(x, y, z);
		          }
		        }

		        if (this.phi > 31.415926535897931D)
		          cancel();
		      }
		    }
		    .runTaskTimer(getPlugin(), 0L, 3L);
		  }

		  public static void agualaEffect(Location loc) {
		    new BukkitRunnable() {
		      double phi = 0.0D;

		      @Override
			public void run() { this.phi += 0.3141592653589793D;
		        for (double t = 0.0D; t <= 15.707963267948966D; t += 0.07853981633974483D) {
		          double r = 1.2D;
		          double x = r * cos(t) * sin(this.phi);
		          double y = r * cos(this.phi) + 1.2D;
		          double z = r * sin(t) * sin(this.phi);
		          loc.add(x, y, z);
		          ParticleEffect.DRIP_WATER.display(loc, 0.0F, 0.0F, 0.0F, 0.0F, 1);

		          loc.subtract(x, y, z);
		        }

		        if (this.phi > 3.141592653589793D)
		          cancel();
		      }
		    }
		    .runTaskTimer(getPlugin(), 0L, 1L);
		  }

		  public static void fireBenderEffect(Location loc) {
		    new BukkitRunnable() {
		      double phi = 0.0D;

		      @Override
			public void run() { this.phi += 0.3141592653589793D;
		        for (double t = 0.0D; t <= 15.707963267948966D; t += 0.07853981633974483D) {
		          double r = 1.2D;
		          double x = r * cos(t) * sin(this.phi);
		          double y = r * cos(this.phi) + 1.2D;
		          double z = r * sin(t) * sin(this.phi);
		          loc.add(x, y, z);
		          ParticleEffect.FLAME.display(loc, 0.0F, 0.0F, 0.0F, 0.0F, 1);

		          loc.subtract(x, y, z);
		        }

		        if (this.phi > 3.141592653589793D)
		          cancel();
		      }
		    }
		    .runTaskTimer(getPlugin(), 0L, 1L);
		  }
}
