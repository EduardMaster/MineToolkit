
package net.eduard.api.lib.old;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

/**
 * Classe criada no intuito de facilitar a criação de Menus, Scoreboard, Items e com alguns métodos para facilitar outras coisas<br>
 * Não existia EduardAPI ainda, foi minha primeira classe API que criei <br>
 * Criada desde 2015/07/16
 * @version 1.0
 * @since 0.4
 * @author Eduard
 * @deprecated Não use mais esta classe pois criei outras classes melhores com mais métodos e facilidades
 *
 */
@Deprecated
public interface Eduard
{ 
	static Eduard getEduard() {
		
		return new Eduard() {
		};
	}

	default JavaPlugin getInstance() {
		return JavaPlugin.getProvidingPlugin(getClass());
	}

	default void setPermission( String cmd , String perm ) {

		getInstance().getCommand( cmd ).setPermission( perm );
	}

	default void setPermissions( Map< String , String > perms ) {

		for ( Entry< String , String > perm : perms.entrySet() ) {
			setPermission( perm.getKey() , perm.getValue() );
		}

	}

	default void setError( String cmd ) {

		getInstance().getCommand( cmd ).setPermissionMessage( getMessage( "no-perm" ) );
	}

	default void setErrors( String[] cmds ) {

		for ( String cmd : cmds ) {
			setError( cmd );
		}
	}

	default void setCommand( String cmd , CommandExecutor ex ) {

		getInstance().getCommand( cmd ).setExecutor( ex );
	}

	default void setCommands( Map< String , CommandExecutor > cmds ) {

		for ( Entry< String , CommandExecutor > cmd : cmds.entrySet() ) {
			setCommand( cmd.getKey() , cmd.getValue() );
		}
	}

	default void setEvent( Listener reg ) {

		Bukkit.getPluginManager().registerEvents( reg , getInstance() );
	}

	default void setEvents( Listener[] regs ) {

		for ( Listener reg : regs ) {
			setEvent( reg );
		}
	}

	default void saveConfig() {

		getInstance().saveConfig();
	}

	default void saveDefault() {

		getInstance().getConfig().options().copyDefaults( true );
		saveConfig();
	}

	default void reloadConfig() {

		getInstance().reloadConfig();
	}

	default void saveDefaultConfig() {

		getInstance().saveDefaultConfig();
	}

	default boolean hasLocation( String name ) {

		return hasLocation( getInstance().getConfig() , name );
	}

	default void setLocation( String name , Location loc ) {

		setLocation( getInstance().getConfig() , name , loc );
		saveConfig();
	}

	default Location getLocation( String name ) {

		return getLocation( getInstance().getConfig() , name );
	}

	default void add( String name , Object value ) {

		getInstance().getConfig().addDefault( name , value );
	}

	default void set( String name , Object value ) {

		getInstance().getConfig().set( name , value );
	}

	default Object get( String name ) {

		return getInstance().getConfig().get( name );
	}

	default boolean has( String name ) {

		return getInstance().getConfig().contains( name );
	}

	default String getMessage( String name ) {

		return ChatColor.translateAlternateColorCodes( '&' , getString( name ) );
	}

	default String colorToString( String name ) {

		return name.replace( '�' , '&' );
	}

	default String getString( String name ) {

		return getInstance().getConfig().getString( name );
	}

	default int getInt( String name ) {

		return getInstance().getConfig().getInt( name );
	}

	default double getDouble( String name ) {

		return getInstance().getConfig().getDouble( name );
	}

	default float getFloat( String name ) {

		return ( float ) getDouble( name );
	}

	default long getLong( String name ) {

		return getInstance().getConfig().getLong( name );
	}

	default boolean getBoolean( String name ) {

		return getInstance().getConfig().getBoolean( name );
	}

	default ConfigurationSection getSection( String name ) {

		return getInstance().getConfig().getConfigurationSection( name );
	}

	default Set< String > getKeys( String name ) {

		return getSection( name ).getKeys( false );
	}

	default Map< String , Object > getValues( String name ) {

		return getSection( name ).getValues( false );
	}

	default Player getPlayer( String name ) {

		return Bukkit.getPlayerExact( name );
	}

	default World getWorld( String name ) {

		return Bukkit.getWorld( name );
	}

	default boolean exist( Player p ) {

		return Arrays.asList( Bukkit.getOfflinePlayers() ).contains( ( OfflinePlayer ) p );
	}

	default boolean isOnline( Player p ) {

		return p != null;
	}

	default int random( int x , int y ) {

		int min , max;
		min = Math.min( x , y );
		max = Math.max( x , y );
		Random random = new Random();
		int numberRandom = min + random.nextInt( ( max - min ) + 1 );
		return numberRandom;
	}

	default boolean chance( double chance ) {

		Random random = new Random();
		return random.nextDouble() <= chance;
	}

	default void effect( Location loc , Effect effect , int data , int radius ) {

		loc.getWorld().playEffect( loc , effect , data , radius );
	}

	default void effect( Location loc , Effect effect , int data ) {

		loc.getWorld().playEffect( loc , effect , data );
	}

	default void effect( Location loc , Effect effect ) {

		effect( loc , effect , 0 );
	}

	default void sound( Location loc , Sound sound , float volume , float pitch ) {

		loc.getWorld().playSound( loc , sound , volume , pitch );
	}

	default void sound( Location loc , Sound sound , float volume ) {

		sound( loc , sound , volume , 1 );
	}

	default void sound( Location loc , Sound sound ) {

		sound( loc , sound , 2 );
	}

	default void potion( LivingEntity ent , PotionEffectType potion , int seconds ) {

		potion( ent , potion , seconds , 1 );
	}

	default void potion( LivingEntity ent , PotionEffectType potion , int seconds , int force ) {

		potion( ent , potion , seconds , force , true );
	}

	default void potion( LivingEntity ent , PotionEffectType potion , int seconds , int force ,
		boolean ambient ) {

		ent.addPotionEffect( new PotionEffect( potion , 20 * seconds , force , ambient ) );
	}

	default boolean hasMeta( ItemStack item ) {

		return item.hasItemMeta();
	}

	default boolean hasName( ItemStack item ) {

		return item.getItemMeta().hasDisplayName();
	}

	default String getName( ItemStack item ) {

		return item.getItemMeta().getDisplayName();
	}

	default ItemStack setName( String name , ItemStack item ) {

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName( name );
		item.setItemMeta( meta );
		return item;
	}

	default ItemStack addLore( String message , ItemStack item ) {

		ItemMeta meta = item.getItemMeta();
		if ( meta.getLore() == null ) {
			meta.setLore( Arrays.asList( message ) );
		} else {
			meta.getLore().add( message );
		}
		item.setItemMeta( meta );
		return item;
	}

	default ItemStack setLore( List< String > lores , ItemStack item ) {

		ItemMeta meta = item.getItemMeta();
		meta.setLore( lores );
		item.setItemMeta( meta );
		return item;
	}

	default ItemStack clearLore( ItemStack item ) {

		ItemMeta meta = item.getItemMeta();
		if ( meta.getLore() == null ) {
			meta.setLore( new ArrayList< String >() );
		} else {
			meta.getLore().clear();
		}
		item.setItemMeta( meta );
		return item;
	}

	default ItemStack set( Inventory inv , int slot , ItemStack item ) {

		inv.setItem( slot , item );
		return item;
	}

	default ItemStack add( Inventory inv , ItemStack item ) {

		inv.addItem( item );
		return item;
	}

	default ItemStack add( Enchantment ench , ItemStack item ) {

		item.addEnchantment( ench , 1 );
		return item;
	}

	default ItemStack add( Enchantment ench , int lvl , ItemStack item ) {

		item.addEnchantment( ench , lvl );
		return item;
	}

	default ItemStack item( Material type , String name ) {

		return item( type , ( short ) 0 , name );
	}

	default ItemStack item( Material type , String name , String... lores ) {

		return item( type , 1 , ( short ) 0 , name , lores );
	}

	default ItemStack item( Material type , String name , List< String > lores ) {

		return item( type , 1 , ( short ) 0 , name , lores );
	}

	default ItemStack item( Material type , short data ) {

		return item( type , 1 , data );
	}

	default ItemStack item( Material type , short data , String name ) {

		return item( type , 1 , data , name );
	}

	default ItemStack item( Material type ) {

		return item( type , 1 );
	}

	default ItemStack item( Material type , int amount , String name ) {

		return item( type , amount , ( short ) 0 , name );
	}

	default ItemStack item( Material type , int amount ) {

		return item( type , amount , ( short ) 0 );
	}

	default ItemStack item( Material type , int amount , short data ) {

		return item( type , amount , data , null );
	}

	default ItemStack item( Material type , int amount , short data , String name ) {

		List< String > lista = null;
		return item( type , amount , data , name , lista );
	}

	default ItemStack
		item( Material type , int amount , short data , String name , String... lores ) {

		return item( type , amount , data , name , Arrays.asList( lores ) );
	}

	default ItemStack item( Material type , int amount , short data , String name ,
		List< String > lores ) {

		ItemStack item = new ItemStack( type , amount , data );
		if ( name != null ) {
			setName( name , item );
		}
		if ( lores != null ) {
			setLore( lores , item );
		}
		return item;
	}

	default void setLocation( FileConfiguration config , String name , Location loc ) {

		config.set( name + ".world" , loc.getWorld().getName() );
		config.set( name + ".x" , loc.getX() );
		config.set( name + ".y" , loc.getY() );
		config.set( name + ".z" , loc.getZ() );
		config.set( name + ".yaw" , loc.getYaw() );
		config.set( name + ".pitch" , loc.getPitch() );
	}

	default Location getLocation( FileConfiguration config , String name ) {

		World w = Bukkit.getWorld( config.getString( name + ".world" ) );
		double x = config.getDouble( name + ".x" );
		double y = config.getDouble( name + ".y" );
		double z = config.getDouble( name + ".z" );
		double yaw = config.getDouble( name + ".yaw" );
		double pitch = config.getDouble( name + ".pitch" );
		return new Location( w , x , y , z , ( float ) yaw , ( float ) pitch );
	}

	default boolean hasLocation( FileConfiguration config , String name ) {

		return config.contains( name + ".world" );
	}

	default boolean isLocation( FileConfiguration config , String name ) {

		return ( config.contains( name + ".world" ) && config.contains( name + ".x" )
			&& config.contains( name + ".y" ) && config.contains( name + ".z" )
			&& config.contains( name + ".yaw" ) && config.contains( name + ".pitch" ) );
	}

	default boolean compareLocation( Location loc1 , Location loc2 ) {

		return ( locationToInt( loc1 ).equals( locationToInt( loc2 ) ) );
	}

	default Location locationToInt( Location loc ) {

		return new Location( loc.getWorld() , ( int ) loc.getX() , ( int ) loc.getY() ,
			( int ) loc.getZ() );
	}

	default List< Location > toLocations( List< Block > blocks ) {

		List< Location > locations = new ArrayList<>();
		for ( Block block : blocks ) {
			locations.add( block.getLocation() );
		}
		return locations;
	}

	default List< Block > toBlocks( List< Location > locations ) {

		List< Block > blocks = new ArrayList<>();
		for ( Location location : locations ) {
			blocks.add( location.getBlock() );
		}
		return blocks;
	}

	default Location getLocation( Location loc ) {

		return new Location( loc.getWorld() , loc.getX() , loc.getY() , loc.getZ() , loc.getYaw() ,
			loc.getPitch() );
	}

	default void setType( List< Location > locations , Material material ) {

		for ( Location block : locations ) {
			block.getBlock().setType( material );
		}
	}

	
	default void setType( List< Location > locations , int id ) {

		for ( Location block : locations ) {
			block.getBlock().setTypeId( id );
		}
	}

	default void itemFloor( Location loc , int size , Material material ) {

		List< Location > locations = blocksSquared( loc , size );
		setType( locations , material );
	}

	default List< Location > blocksSquared( Location loc , int size ) {

		return blocksBox( loc , size , 0 );
	}

	default List< Location > blocksBox( Location loc , int size , int high ) {

		return blocksBox( loc , size , high , 0 );
	}

	default List< Location > blocksBox( Location loc , int size , int high , int low ) {

		return getLocations( getHighLocation( getLocation( loc ) , high , size ) ,
			getLowLocation( getLocation( loc ) , low , size ) );
	}

	default List< Block > getBlocks( Block block1 , Block block2 ) {

		return getBlocks( block1.getLocation() , block2.getLocation() );
	}

	default List< Block > getBlocks( Location loc1 , Location loc2 ) {

		List< Block > blocks = new ArrayList<>();
		for ( Location block : getLocations( loc1 , loc2 ) ) {
			blocks.add( block.getBlock() );
		}
		return blocks;
	}

	default List< Location > getLocations( Block block1 , Block block2 ) {

		return getLocations( block1.getLocation() , block2.getLocation() );
	}

	default List< Location > getLocations( Location loc1 , Location loc2 ) {

		Location min = getMinLocation( loc1 , loc2 );
		Location max = getMaxLocation( loc1 , loc2 );
		return getLocs( min , max );
	}

	default List< Location > getLocs( Location min , Location max ) {

		List< Location > locs = new ArrayList<>();
		for ( double x = min.getX(); x <= max.getX(); x++ ) {
			for ( double y = min.getY(); y <= max.getY(); y++ ) {
				for ( double z = min.getZ(); z <= max.getZ(); z++ ) {
					Location loc = new Location( min.getWorld() , x , y , z );
					if ( !locs.contains( loc ) ) {
						locs.add( loc );
					}
				}
			}
		}
		return locs;
	}

	default Location getMinLocation( Location loc1 , Location loc2 ) {

		double x = Math.min( loc1.getX() , loc2.getX() );
		double y = Math.min( loc1.getY() , loc2.getY() );
		double z = Math.min( loc1.getZ() , loc2.getZ() );
		return new Location( loc1.getWorld() , x , y , z );
	}

	default Location getMaxLocation( Location loc1 , Location loc2 ) {

		double x = Math.max( loc1.getX() , loc2.getX() );
		double y = Math.max( loc1.getY() , loc2.getY() );
		double z = Math.max( loc1.getZ() , loc2.getZ() );
		return new Location( loc1.getWorld() , x , y , z );
	}

	default Location getHighLocation( Location loc , int high , int size ) {

		loc.add( size , high , size );
		return loc;
	}

	default Location getLowLocation( Location loc , int low , int size ) {

		loc.subtract( size , low , size );
		return loc;
	}

	public class Configs
	{

		private File file;

		private FileConfiguration config;
		
		public Configs( String name ){
			file = new File(  getEduard().getInstance().getDataFolder() , name );
			reloadConfig();
		}

		public File getFile() {

			return file;
		}

		public FileConfiguration getConfig() {

			return config;
		}

	
		public void reloadConfig() {

			config = YamlConfiguration.loadConfiguration( file );
			InputStream imputStream = getEduard().getInstance().getResource( file.getName() );
			if ( imputStream != null ) {
				YamlConfiguration imputConfig = YamlConfiguration.loadConfiguration( imputStream );
				config.setDefaults( imputConfig );
			}
		}

		public void saveDefaultConfig() {

			 getEduard().getInstance().saveResource( file.getName() , true );
		}

		public void saveConfig() {

			try {
				config.save( file );
			} catch ( IOException ex ) {
			}
		}

		public void saveDefault() {

			config.options().copyDefaults( true );
			saveConfig();
		}

		public boolean hasLocation( String name ) {

			return getEduard().hasLocation( config , name );
		}

		public void setLocation( Location loc , String name ) {

			getEduard().setLocation( config , name , loc );
			saveConfig();
		}

		public Location getLocation( String name ) {

			return getEduard().getLocation( config , name );
		}

		public void add( String name , Object value ) {

			config.addDefault( name , value );
		}

		public void set( String name , Object value ) {

			config.set( name , value );
		}

		public Object get( String name ) {

			return config.get( name );
		}

		public boolean has( String name ) {

			return config.contains( name );
		}

		public String getMessage( String name ) {

			return ChatColor.translateAlternateColorCodes( '&' , config.getString( name ) );
		}

		public String getString( String name ) {

			return config.getString( name );
		}

		public int getInt( String name ) {

			return config.getInt( name );
		}

		public double getDouble( String name ) {

			return config.getDouble( name );
		}

		public float getFloat( String name ) {

			return ( float ) getDouble( name );
		}

		public long getLong( String name ) {

			return config.getLong( name );
		}

		public boolean getBoolean( String name ) {

			return config.getBoolean( name );
		}

		public ConfigurationSection getSection( String name ) {

			return config.getConfigurationSection( name );
		}

		public Set< String > getKeys( String name ) {

			return getSection( name ).getKeys( false );
		}

		public Map< String , Object > getValues( String name ) {

			return getSection( name ).getValues( false );
		}

	}

	public class Minigame
	{

		private String name;

		private String arenas;

		public Minigame(){

			name = "";
			arenas = "Arenas";
		}

		public HashMap< String , String > players = new HashMap<>();

		public List< Player > getPlayers() {

			List< Player > player = new ArrayList<>();
			for ( String p : players.keySet() ) {
				player.add( Bukkit.getPlayerExact( p ) );
			}
			return player;
		}

		public List< Player > getPlayers( String arena ) {

			List< Player > player = new ArrayList<>();
			for ( Entry< String , String > p : players.entrySet() ) {
				if ( p.getValue().equals( arena ) ) {
					player.add( Bukkit.getPlayerExact( p.getKey() ) );
				}
			}
			return player;
		}

		public List< String > getArenas() {

			Configs arenaPasta = new Configs( arenas );
			arenaPasta.getFile().mkdirs();
			return Arrays.asList( Arrays.asList( arenaPasta.getFile().list() ).toString()
				.replace( ".yml" , "" ).split( ", " ) );
		}

		public List< String > getEnabledArenas() {

			ArrayList< String > list = new ArrayList< String >();
			for ( String essaArena : getArenas() ) {
				if ( getArena( essaArena ).enable() ) {
					list.add( essaArena );
				}
			}
			return list;
		}

		public void setLobby( Player p ) {

			getEduard().setLocation( name + "." + "Lobby" , p.getLocation() );
			getEduard().saveConfig();
		}

		public boolean hasLobby() {

			return getEduard().hasLocation( name + "." + "Lobby" );

		}

		public Location getLobby() {

			return getEduard().getLocation( name + "." + "Lobby" );
		}

		public Arena getArena( String name ) {

			return new Arena( name );
		}

		public class Arena
		{

			private Configs config;

			public Arena( String name ){

				config = new Configs( arenas + "/" + name + ".yml" );
			}

			public void create() {

				enable( false );
			}

			public void delete() {

				config.getFile().delete();
			}

			public boolean exist() {

				return config.getFile().exists();
			}

			public void enable( boolean value ) {

				set( "enable" , value );
				config.saveConfig();
			}

			public boolean enable() {

				return config.getBoolean( "enable" );
			}

			public void set( String name , Object value ) {

				config.set( name , value );
			}

			public Object get( String name ) {

				return config.get( name );
			}

			public boolean has( String name ) {

				return config.has( name );
			}

			public void set( Location loc , String name ) {

				config.setLocation( loc , name );
			}

			public Location getLocation( String name ) {

				return config.getLocation( name );
			}

			public boolean hasLocation( String name ) {

				return config.hasLocation( name );
			}

			public void setLocation( Location loc , String name ) {

				config.setLocation( loc , name );
			}

		}

	}

	public class Time
	{

		private int id = -1;

		private int time = -1;

		public int getTime() {

			return time;
		}

		public void setTime( int time ) {

			this.time = time;
		}

		public Time( Runnable run , long ticks , int times ){

			if ( times < 0 ) {
				time = 100000;
			} else {
				time = times;
			}
			id = new BukkitRunnable() {

				public void run() {

					if ( times == time ) {
						time--;
						run.run();
					} else {
						if ( time < 1 ) {
							stop();
						} else {
							time--;
							run.run();
						}
					}
				}
			}.runTaskTimer(  getEduard().getInstance() , ticks , ticks ).getTaskId();
		}

		public Time( Runnable run , int seconds , int times ){

			this( run , seconds * 20L , times );
		}

		public Time( Runnable run , int times ){

			this( run , 1 , times );
		}

		public Time( Runnable run ){

			this( run , 1 );
		}

		public void stop() {

			if ( id != -1 ) {
				Bukkit.getScheduler().cancelTask( id );
			}
		}

	}

	public class Cooldown
	{

		private HashMap< String , Integer > time = new HashMap<>();

		private HashMap< String , Long > id = new HashMap<>();

		public boolean has( Player p ) {

			if ( id.containsKey( p.getUniqueId().toString() ) ) { return getTime( p ) > 0; }
			return false;
		}

		public void start( Player p , int seconds ) {

			if ( !has( p ) ) {
				id.put( p.getUniqueId().toString() , System.currentTimeMillis() );
				time.put( p.getUniqueId().toString() , seconds );
			}
		}

		public void remove( Player p ) {

			id.remove( p.getUniqueId().toString() );
			time.remove( p.getUniqueId().toString() );
		}

		public long getTime( Player p ) {

			Long last = id.get( p.getUniqueId().toString() );
			Integer delay = time.get( p.getUniqueId().toString() );
			delay *= 1000;
			long wait = ( last - ( System.currentTimeMillis() - delay ) );
			return wait / 1000;
		}
	}

	public class Cooldowns
	{

		private HashMap< String , Time > time = new HashMap<>();

		public int getTime( Player p ) {

			if ( has( p ) ) { return time.get( p.getUniqueId().toString() ).getTime(); }
			return -1;
		}

		public boolean has( Player p ) {

			return time.containsKey( p.getUniqueId().toString() );
		}

		public void start( Player p , int seconds ) {

			start( p , seconds , ( Runnable ) null );
		}

		public void start( Player p , int seconds , String message ) {

			start( p , seconds , new Runnable() {

				public void run() {

					p.sendMessage( message );
				}
			} );
		}

		public void start( Player p , int seconds , Runnable end ) {

			if ( !has( p ) ) {
				Time time = new Time( new Runnable() {

					public void run() {

						if ( getTime( p ) == 0 ) {
							if ( end != null ) {
								end.run();
							}
							stop( p );
						}
					}
				} , seconds );
				this.time.put( p.getUniqueId().toString() , time );
			}
		}

		public void stop( Player p ) {

			if ( has( p ) ) {
				String name = p.getUniqueId().toString();
				time.get( name ).stop();
				time.remove( p.getUniqueId().toString() );
			}
		}
	}

	public class Delays
	{

		private HashMap< String , Time > time = new HashMap<>();

		public boolean has( Player p ) {

			return time.containsKey( p.getUniqueId().toString() );
		}

		public void start( Player p , int seconds ) {

			start( p , seconds , ( Runnable ) null );
		}

		public void start( Player p , int seconds , String message ) {

			start( p , seconds , new Runnable() {

				public void run() {

					p.sendMessage( message );
				}
			} );
		}

		public void start( Player p , int seconds , Runnable end ) {

			if ( has( p ) ) { return; }
			Time time = new Time( new Runnable() {

				public void run() {

					if ( end != null ) {
						end.run();
					}
					stop( p );
				}
			} , seconds , 1 );
			this.time.put( p.getUniqueId().toString() , time );

		}

		public void stop( Player p ) {

			if ( has( p ) ) {
				String name = p.getUniqueId().toString();
				time.get( name ).stop();
				time.remove( p.getUniqueId().toString() );
			}
		}
	}

	public class Timers
	{

		private Time time;

		private ITimer game;

		private int[] numbers;

		public Timers(){

			setGame( new ITimer() {

				@Override
				public void normalRun() {

					Bukkit.broadcastMessage( "�6Normal Run: �e" + getTime() );
				}

				@Override
				public void lastRun() {

					Bukkit.broadcastMessage( "�6Last Run: �b" + getTime() );
				}

				@Override
				public void finalRun() {

					Bukkit.broadcastMessage( "�6Final Run: �eGame Over" );
				}
			} );
			setNumbers( 1 , 2 , 3 , 4 , 5 , 10 , 20 , 30 , 60 );
		}

		public int getTime() {

			return time.getTime();
		}

		public void setTime( int seconds ) {

			time.setTime( seconds );
		}

		public boolean started() {

			return getTime() > 0;
		}

		public void start( ITimer game , int seconds ) {

			if ( game != null ) {
				setGame( game );
			}
			time = new Time( new Runnable() {

				public void run() {

					if ( getTime() == 0 ) {
						getGame().finalRun();
					} else {
						for ( int id : getNumbers() ) {
							if ( id == getTime() ) {
								getGame().normalRun();
							}
						}
					}
				}
			} , 1 , seconds );
		}

		public void stop() {

			if ( time != null ) {
				time.stop();
			}
		}

		public int[] getNumbers() {

			return numbers;
		}

		public void setNumbers( int... numbers ) {

			this.numbers = numbers;
		}

		public ITimer getGame() {

			return game;
		}

		public void setGame( ITimer game ) {

			this.game = game;
		}

	}

	public class Fireworks
	{

		private Firework firework;

		private FireworkMeta meta;

		private List< FireworkEffect > effects;

		public void reload() {

			firework.setFireworkMeta( meta );
		}

		public Fireworks( Location loc ){

			firework = loc.getWorld().spawn( loc , Firework.class );
			meta = firework.getFireworkMeta();
			FireworkMeta meta = firework.getFireworkMeta();
			effects = meta.getEffects();
		}

		public List< FireworkEffect > getEffects() {

			return effects;
		}

		public Firework getFirework() {

			return firework;
		}

		public void setPower( int power ) {

			meta.setPower( power );
			reload();
		}

		public void item( FireworkType type , Color firstColor , Color lastColor ) {

			item( FireworkEffect.Type.valueOf( type.name() ) , true , true , firstColor , lastColor );
		}

		public void item( FireworkEffect.Type type , boolean flicker , boolean trail ,
			Color firstColor , Color lastColor ) {

			item( FireworkEffect.builder().with( type ).flicker( flicker ).trail( trail )
				.withColor( firstColor ).withFade( lastColor ).build() );
		}

		public void item( FireworkEffect effect ) {

			meta.addEffect( effect );
			reload();
		}
	}

	
	public class CraftNormal
	{

		private ShapelessRecipe recipe;

		private ItemStack result;

		public CraftNormal( ItemStack result ){

			setResult( result );
			setRecipe( new ShapelessRecipe( result ) );
		}
		
		public void add( Material ingredient , int data ) {

			recipe.addIngredient( ingredient , data );
		}

		public void remove( Material ingredient , int data ) {

			recipe.removeIngredient( ingredient , data );
		}

		public ShapelessRecipe getRecipe() {

			return recipe;
		}

		public void setRecipe( ShapelessRecipe recipe ) {

			this.recipe = recipe;
		}

		public ItemStack getResult() {

			return result;
		}

		public void setResult( ItemStack result ) {

			this.result = result;
		}

	}

	
	public class CraftExtra
	{

		private ShapedRecipe recipe;

		private ItemStack result;

		private Material[] items = new Material[9];

		private int[] datas = new int[9];

		public CraftExtra( ItemStack result ){

			setResult( result );
			setRecipe( new ShapedRecipe( result ) );
			for ( int x = 0; x < datas.length; x++ ) {
				datas[x] = 0;
			}
		}

		public void set( int slot , Material material ) {

			set( slot , material , 0 );

		}

		public void set( int slot , Material material , int data ) {

			if ( ( slot < 1 ) || ( slot > 9 ) ) { return; }
			items[slot - 1] = material;
			datas[slot - 1] = data;

		}

		public ShapedRecipe getRecipe() {

			try {
				recipe.shape( ( items[0] == null ? " " : "A" ) + ( items[1] == null ? " " : "B" )
					+ ( items[2] == null ? " " : "C" ) , ( items[3] == null ? " " : "D" )
					+ ( items[4] == null ? " " : "E" ) + ( items[5] == null ? " " : "F" ) ,
					( items[6] == null ? " " : "G" ) + ( items[7] == null ? " " : "H" )
						+ ( items[8] == null ? " " : "I" ) );

				char shape = 'A';
				for ( int x = 0; x < items.length; x++ ) {
					if ( items[x] == null ) {
						shape++;
						continue;
					}
					recipe.setIngredient( shape , items[x] , datas[x] );
					shape++;
				}

			} catch ( Exception e ) {
				e.printStackTrace();
			}

			return recipe;
		}

		public void setRecipe( ShapedRecipe recipe ) {

			this.recipe = recipe;
		}

		public ItemStack getResult() {

			return result;
		}

		public void setResult( ItemStack result ) {

			this.result = result;
		}
	}

	public enum FireworkType
	{
		BALL , BALL_LARGE , BURST , CREEPER , STAR
	}

	public interface IPlayer
	{

		void run( Player p );

	}

	public interface ITimer
	{

		void finalRun();

		void lastRun();

		void normalRun();

	}

	public class Scoreboards
	{

		private Scoreboard score;

		private Objective scoreObj;

		private HashMap< Integer , String > scoreSlots;

		private int scoreSize;

		private int scoreNameSize;

		private String scoreName;

		private String scoreNameColor;

		private int scoreNameId;

		public Scoreboards(){

			scoreSlots = new HashMap<>();
			score = Bukkit.getScoreboardManager().getNewScoreboard();
			scoreObj = score.registerNewObjective( getName( "�6�lSimples ScoreBoard" ) , "dummy" );
			scoreObj.setDisplaySlot( DisplaySlot.SIDEBAR );
			setName( "�6�lSimples ScoreBoard" );
			update();
		}

		private void update() {

			setNameSize( 16 , "�f�l" );
			new Time( new Runnable() {

				public void run() {

					String text = ChatColor.stripColor( scoreName );
					if ( text.length() > scoreNameSize ) {
						text = text + getFake( 5 );
						scoreNameId++;
						String message = "";
						if ( scoreNameId > text.length() ) {
							scoreNameId = 0;
							message = ( text.substring( 0 , scoreNameSize ) );
						} else if ( scoreNameId > ( text.length() - scoreNameSize ) ) {
							String frontText =
								text.substring( 0 , scoreNameId + ( scoreNameSize - text.length() ) );
							message =
								( text.substring( 0 + scoreNameId , text.length() ) + frontText );
						} else {
							message =
								( text.substring( 0 + scoreNameId , scoreNameSize + scoreNameId ) );
						}
						scoreObj.setDisplayName( scoreNameColor + message );
					}
				}

			} , 5L , -1 );

		}

		public Scoreboard getScore() {

			return score;
		}

		public void setNameSize( int nameSize , String colorBeforeName ) {

			if ( nameSize < 5 ) {
				nameSize = 5;
			}
			if ( ( nameSize + colorBeforeName.length() ) > 32 ) {
				nameSize -= colorBeforeName.length();
			}
			scoreNameSize = nameSize;
			scoreNameColor = colorBeforeName;
		}

		public void setName( String name ) {

			scoreObj.setDisplayName( getName( 32 , name ) );
			scoreName = name;
			scoreNameId = 0;
		}

		public void show( Player p ) {

			p.setScoreboard( score );
		}

		public void setSize( int size ) {

			if ( ( size > 15 ) | ( size < 1 ) ) {
				size = 15;
			}
			scoreSize = size;
			for ( int x = 1; x < 15; x++ ) {
				removeSlot( x );
			}
			scoreSlots.clear();
			for ( int x = 1; x <= size; x++ ) {
				setFake( x );
			}
		}

		public String getFake( int id ) {

			String name = "";
			for ( int x = 1; x <= id; x++ ) {
				name = name + " ";
			}
			return name;
		}

		public void setFake( int id ) {

			setSlot( id , getFake( id ) );
		}

		public void add( String name ) {

			List< Integer > time = new ArrayList<>();
			for ( Integer list : scoreSlots.keySet() ) {
				if ( scoreSlots.get( list ).startsWith( " " ) ) {
					time.add( list );
				}
			}
			int id = 0;
			for ( Integer x : time ) {
				if ( id < x ) {
					id = x;
				}
			}

			if ( id == 0 ) {
				if ( ( scoreSlots.keySet().size() + 1 ) < scoreSize ) {
					setSlot( scoreSlots.keySet().size() + 1 , name );
				}

			} else {
				setSlot( id , name );
			}

		}

	
		public void removeSlot( int id ) {

			if ( scoreSlots.containsKey( id ) ) {
				score.resetScores( Bukkit.getOfflinePlayer( scoreSlots.get( id ) ) );
				scoreSlots.remove( id );
			}
		}

		
		public void setSlot( int id , String name ) {

			if ( ( id > scoreSize ) || ( id < 1 ) ) { return; }
			name = getName( name );
			removeSlot( id );
			scoreSlots.put( id , name );
			scoreObj.getScore( Bukkit.getOfflinePlayer( name ) ).setScore( id );
		}

		public String getName( String name ) {

			return name.length() > 16 ? name.substring( 0 , 16 ) : name;
		}

		public String getName( int size , String name ) {

			return name.length() > size ? name.substring( 0 , size ) : name;
		}

	}

	public class GUI
	{

		private static class Event implements Listener
		{

			@EventHandler
			public void OpenGui( PlayerInteractEvent e ) {

				Player p = e.getPlayer();
				ItemStack item = e.getItem();
				Action action = e.getAction();
				if ( item == null ) return;
				for ( GUI gui : GUI.all_gui ) {
					if ( gui.getItemKey() == null ) {
						continue;
					}
					boolean itemType = false;
					if ( gui.isByMaterial() ) {
						if ( gui.getItemKey().getType() == item.getType() ) {
							itemType = true;
						}
					} else {
						if ( gui.getItemKey().equals( item ) ) {
							itemType = true;
						}
					}
					if ( itemType ) {
						boolean actionType = false;
						if ( gui.getAction() == ActionGUI.RIGHT ) {
							if ( action == Action.RIGHT_CLICK_AIR
								|| action == Action.RIGHT_CLICK_BLOCK ) {
								actionType = true;
							}
						} else if ( gui.getAction() == ActionGUI.LEFT ) {
							if ( action == Action.LEFT_CLICK_AIR
								|| action == Action.LEFT_CLICK_BLOCK ) {
								actionType = true;
							}
						} else if ( gui.getAction() == ActionGUI.BOTH ) {
							if ( action != Action.PHYSICAL ) {
								actionType = true;
							}
						}
						if ( actionType ) {
							e.setCancelled( true );
							p.openInventory( gui.getGUI() );
							return;
						}
					}
				}
			}

		
			@EventHandler
			public void InteractGui( InventoryClickEvent e ) {

				if ( !( e.getWhoClicked() instanceof Player ) ) return;
				Player p = ( Player ) e.getWhoClicked();
				if ( e.getCurrentItem() == null ) return;
				for ( GUI gui : GUI.all_gui ) {
					if ( gui.getGUI().getTitle().equals( e.getInventory().getTitle() ) ) {
						if ( gui.effect.containsKey( e.getSlot() ) ) {
							if ( gui.getItemKey().equals( e.getCurrentItem() ) ) return;
							if ( gui.getGUI().getSize() < e.getRawSlot() ) {
								p.updateInventory();
								return;
							}
							if ( gui.getGUI().getItem( e.getSlot() ).equals( e.getCurrentItem() ) ) {
								IconEffect slotEffect = gui.effect.get( e.getSlot() );
								if ( slotEffect != null ) {
									slotEffect.run( p );
								}
							}

						}
						e.setCancelled( true );
						return;
					}
				}
			}

			@EventHandler
			public void OpenGui( InventoryOpenEvent e ) {

				if ( !( e.getPlayer() instanceof Player ) ) return;
				Inventory inv = e.getInventory();
				Player p = ( Player ) e.getPlayer();
				for ( GUI gui : GUI.all_gui ) {
					if ( inv.getTitle().equals( gui.getGUI().getTitle() ) ) {
						p.playSound( p.getLocation() , gui.getOpenSound() , 2 , 1 );
						return;
					}
				}
			}
		}

		public static List< GUI > all_gui = new ArrayList<>();

		public HashMap< Integer , IconEffect > effect = new HashMap<>();

		private Inventory inv;

		private ActionGUI action;

		private Sound openSound;

		private ItemStack itemKey;

		private boolean byMaterial;
		
		public Inventory getGUI() {

			return inv;
		}

		private void setGUI( Inventory invGUI ) {

			this.inv = invGUI;
		}
		public static void enableGUI() {

			Bukkit.getPluginManager().registerEvents(
				new GUI.Event() , getEduard().getInstance() );
		}

		public GUI( String name , int size ){

			if ( name.isEmpty() ) name = "No empty Name";
			if ( size % 9 > 0 ) {
				size = 9;
			} else if ( size < 0 | size > 9 * 6 ) {
				size = 9;
			}
			setGUI( Bukkit.createInventory( null , size , name ) );
			GUI remove = null;
			for ( GUI gui : all_gui ) {
				if ( gui.getGUI().getTitle().equals( name ) ) {
					remove = gui;
				}
			}
			if ( remove != null ) {
				all_gui.remove( remove );
			}
			all_gui.add( this );
			setAction( ActionGUI.BOTH );
			setOpenSound( Sound.HORSE_ARMOR );
			setByMaterial( false );
		}

		public void addIcon( ItemStack icon ) {

			getGUI().addItem( icon );
		}

		public void addIcon( ItemStack icon , IconEffect run ) {

			addIcon( icon );
			int id = getGUI().first( icon );
			effect.put( id , run );
		}

		public void setIcon( int slot , ItemStack icon ) {

			getGUI().setItem( slot , icon );
		}

		public void setIcon( int slot , ItemStack icon , IconEffect run ) {

			getGUI().setItem( slot , icon );
			effect.put( slot , run );
		}

		public ItemStack getItemKey() {

			return itemKey;
		}

		public void setItemKey( ItemStack itemKey ) {

			this.itemKey = itemKey;
		}

		public ActionGUI getAction() {

			return action;
		}

		public void setAction( ActionGUI action ) {

			this.action = action;
		}

		public boolean isByMaterial() {

			return byMaterial;
		}

		public void setByMaterial( boolean byMaterial ) {

			this.byMaterial = byMaterial;
		}

		public Sound getOpenSound() {

			return openSound;
		}

		public void setOpenSound( Sound openSound ) {

			this.openSound = openSound;
		}

		public interface IconEffect
		{

			void run( Player p );
		}

		public enum ActionGUI
		{
			RIGHT , LEFT , BOTH
		}
	}

}
