package net.eduard.api.lib.old;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.modules.WorldAPI;


/**
 * API de controle de {@link Location} e {@link World}
 * @version 1.0
 * @since 0.7
 * @deprecated Métodos adicionados em {@link WorldAPI} e {@link Mine}
 * @author Eduard
 *
 */
public abstract interface LocationSetup
{
  public default ArrayList<Location> createFloor(Location location, int size, Material material)
  {
    return createFloor(location, size, material, 0);
  }
  
  public  default ArrayList<Location> createFloor(Location location, int size, Material material, int data)
  {
    ArrayList<Location> locations = getBlocksSquared(location, size);
    for (Location location1 : locations) {
      location1.getBlock().setType(material);
      
      location1.getBlock().setData((byte)data);
    }
    return locations;
  }
  
  public  default ArrayList<Location> getBlocksSquared(Location location, int size) {
    return getBlocksBox(location, size, 0);
  }
  
  public default  ArrayList<Location> getBlocksBox(Location location, int size, int high)
  {
    return getBlocksBox(location, size, high, 0);
  }
  
  public default  ArrayList<Location> getBlocksBox(Location location, int size, int high, int low)
  {
    return getLocations(getHighLocation(getBlockLocation(location), high, size), 
      getLowLocation(getBlockLocation(location), low, size));
  }
  
  public  default boolean isSimiliarLocation(Location location1, Location location2) {
    return getBlockLocation(location1).equals(getBlockLocation(location2));
  }
  
  public default  Location getBlockLocation(Location location) {
    return new Location(location.getWorld(), (int)location.getX(), (int)location.getY(), 
      (int)location.getZ());
  }
  
  public default  ArrayList<Location> getLocations(ArrayList<Block> blocks) {
    ArrayList<Location> locations = new ArrayList<Location>();
    for (Block block : blocks) {
      locations.add(block.getLocation());
    }
    return locations;
  }
  
  public default  ArrayList<Block> getBlocks(ArrayList<Location> locations) {
    ArrayList<Block> blocks = new ArrayList<Block>();
    for (Location location : locations) {
      blocks.add(location.getBlock());
    }
    return blocks;
  }
  
  public  default ArrayList<Location> getLocations(Block block1, Block block2) {
    return getLocations(block1.getLocation(), block2.getLocation());
  }
  
  public  default ArrayList<Location> getLocations(Location location1, Location location2)
  {
    Location min = getMinLocation(location1, location2);
    Location max = getMaxLocation(location1, location2);
    ArrayList<Location> locations = new ArrayList<Location>();
    for (double x = min.getX(); x <= max.getX(); x += 1.0D) {
      for (double y = min.getY(); y <= max.getY(); y += 1.0D) {
        for (double z = min.getZ(); z <= max.getZ(); z += 1.0D) {
          Location loc = new Location(min.getWorld(), x, y, z);
          if (!locations.contains(loc)) {
            locations.add(loc);
          }
        }
      }
    }
    return locations;
  }
  
  public default  Location getMinLocation(Location loc1, Location loc2) {
    double x = Math.min(loc1.getX(), loc2.getX());
    double y = Math.min(loc1.getY(), loc2.getY());
    double z = Math.min(loc1.getZ(), loc2.getZ());
    return new Location(loc1.getWorld(), x, y, z);
  }
  
  public  default Location getMaxLocation(Location loc1, Location loc2)
  {
    double x = Math.max(loc1.getX(), loc2.getX());
    double y = Math.max(loc1.getY(), loc2.getY());
    double z = Math.max(loc1.getZ(), loc2.getZ());
    return new Location(loc1.getWorld(), x, y, z);
  }
  
  public default  Location getHighLocation(Location loc, int high, int size)
  {
    loc.add(size, high, size);
    return loc;
  }
  
  public default  Location getLowLocation(Location loc, int low, int size)
  {
    loc.subtract(size, low, size);
    return loc;
  }
}
