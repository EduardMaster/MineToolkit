package net.eduard.api.kits;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import me.confuser.barapi.BarAPI;
import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.game.Ability;


public class MadMan extends Ability{

	public MadMan() {
		setIcon(Material.REDSTONE, "§fEnfraqueça seus inimigos");
		
	}
	 private HashMap<Player, Double> madman = new HashMap<>();
	@Override
	@SuppressWarnings("deprecation")
	public void run() {
		
		 DecimalFormat dm = new DecimalFormat("##");
		      for (Player p : Mine.getPlayers())
		      {
		        if (hasKit(p)) {
		          for (Entity e : p.getNearbyEntities(20.0D, 20.0D, 20.0D)) {
		            if ((e instanceof Player))
		            {
		              if (this.madman.containsKey(e))
		              {
		                this.madman.put((Player)e, Double.valueOf(this.madman.get(e).doubleValue() + 0.01D));
		              }
		              else
		              {
		                this.madman.put((Player)e, Double.valueOf(0.01D));
		                ((Player)e).sendMessage("\u00A7bTem um madman por perto!");
		              }
		              BarAPI.setMessage((Player)e, dm.format(this.madman.get(e).doubleValue() * 100.0D) + "% \u00A76<< \u00A7bEfeito do madman");
		            }
		          }
		        }
		        if (this.madman.containsKey(p))
		        {
		          boolean hasMadMan = false;
		          for (Entity e : p.getNearbyEntities(20.0D, 20.0D, 20.0D)) {
		            if ((e instanceof Player))
		            {
		              Player mad = (Player)e;
		              if (hasKit(mad))
		              {
		                hasMadMan = true;
		                break;
		              }
		            }
		          }
		          if (!hasMadMan)
		            if (this.madman.get(p).doubleValue() - 0.2D <= 0.0D)
		            {
		              this.madman.remove(p);
		              BarAPI.setMessage(p, "\u00A73Efeito do madman passou!");
		              BarAPI.removeBar(p);
		            }
		            else
		            {
		              this.madman.put(p, Double.valueOf(this.madman.get(p).doubleValue() - 0.2D));
		              BarAPI.setMessage(p, dm.format(this.madman.get(p).doubleValue() * 100.0D) + "% \u00A76<< \u00A7bEfeito do madman");
		            }
		        }
		      }
		
	}
	public static Map<Player, Float> charge = new HashMap<>();
	
	public double damage = 6;
	
	@Override
	public void register(Plugin plugin) {
		timer(20, this);
		super.register(plugin);
	}
	
	
	@EventHandler
	public void event(PlayerMoveEvent e){
		
	}


	  @EventHandler
	  public void event(EntityDamageEvent event)
	  {
	    if ((event.getEntity() instanceof Player))
	    {
	      Player p = (Player)event.getEntity();
	      if (this.madman.containsKey(p))
	        event.setDamage(event.getDamage() + event.getDamage() * this.madman.get(p).doubleValue());
	    }
	  }
}
