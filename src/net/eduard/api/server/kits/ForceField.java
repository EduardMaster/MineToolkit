package net.eduard.api.server.kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;



import net.eduard.api.lib.click.PlayerClick;
import net.eduard.api.lib.click.PlayerClickEffect;
import net.eduard.api.server.kit.KitAbility;


public class ForceField extends KitAbility{
	
	@Override
	public void run() {
		for (Player player : force){
			field(player);
		}
		
	}
	public double damage = 3;
	public double range = 5;
	public int effectSeconds  = 5;
	
	public void field(Player player){
		for (Entity entity: player.getNearbyEntities(range, range, range)){
			if (entity instanceof LivingEntity){
				LivingEntity livingEntity = (LivingEntity) entity;
				livingEntity.damage(damage, player);
			}
		}
	}
	public void force(Player player){
	
		for (LivingEntity entity: 	player.getWorld().getLivingEntities()){
			if (entity.getLocation().distance(player.getLocation())<range){
				entity.damage(damage, player);
			}
		}
	}
	public static ArrayList<Player> force = new ArrayList<>();
	
	public ForceField() {
		setIcon(Material.NETHER_FENCE, "�fAtive Force Field nos Inimigos");
		add(Material.NETHER_FENCE);
		setMessage("�6Force field desativado!");
		setTime(30);
		setClick(new PlayerClick(Material.NETHER_FENCE,new PlayerClickEffect() {
			
			@Override
			public void onClick(Player player, Block block, ItemStack item) {
				// TODO Auto-generated method stub
				if (hasKit(player)){
					if (cooldown(player)){
						force.add(player);
						asyncDelay(new Runnable() {
							
							@Override
							public void run() {
								force.remove(player);
								player.sendMessage(getMessage());
							}
						}, effectSeconds*20);
					}
				}
			}
		}));
	}
	@Override
	public void register(Plugin plugin) {
		setPlugin(plugin);
		asyncTimer();
		super.register(plugin);
	}

}
