package net.eduard.api.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.API;
import net.eduard.api.game.Sounds;
import net.eduard.api.manager.CMD;
import net.eduard.api.setup.ExtraAPI;
import net.eduard.api.setup.ItemAPI;

public class SoupCommand extends CMD {

	public int recoverAmount = 7;
	public boolean recoverFood = false;
	public boolean hasHungry = true;
	public List<String> sign = new ArrayList<>();
	public Sounds sound = Sounds.create(Sound.BURP);
	public String message = "§aPlaca de sopa criada!";
	public ItemStack soup = ItemAPI.newItem(Material.MUSHROOM_SOUP, "§6Sopa");

	public SoupCommand() {
		super("soup", "sopas");
	}
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (API.onlyPlayer(sender)) {
			Player p = (Player) sender;
			ItemAPI.fill(p.getInventory(), soup);
		}
		return true;
	}
	@EventHandler
	public void event(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getMaterial() == Material.MUSHROOM_SOUP
				& e.getAction().name().contains("RIGHT")) {
			boolean remove = false;
			e.setCancelled(true);
			int value = recoverAmount;
			if (p.getHealth() < p.getMaxHealth()) {

				double calc = p.getHealth() + value;
				p.setHealth(calc >= p.getMaxHealth() ? p.getMaxHealth() : calc);
				remove = true;
			}
			if (recoverFood) {
				if (p.getFoodLevel() < 20) {
					int calc = value + p.getFoodLevel();
					p.setFoodLevel(calc >= 20 ? 20 : calc);
					p.setSaturation(p.getSaturation() + 5);
					remove = true;
				}
			}
			if (remove) {
				e.setUseItemInHand(Result.DENY);
				p.getItemInHand().setType(Material.BOWL);
				sound.create(p);
			}

		}else if (e.getAction().name().contains("BLOCK")) {
			if (e.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) e.getClickedBlock().getState();
				if (sign.getLine(0).equalsIgnoreCase(
						ExtraAPI.toText(this.sign.get(0)))) {
					Inventory inv = API
							.newInventory("§8Placa de Sopas", 6*9);
					ItemAPI.fill(inv, soup);
					p.openInventory(inv);
				}
			}
		}
	}

	@EventHandler
	public void event(FoodLevelChangeEvent e) {

		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (!hasHungry) {
				if (e.getFoodLevel() < 20) {
					e.setFoodLevel(20);
					p.setExhaustion(0);
					p.setSaturation(20);
				}
			}
		}

	}
	@EventHandler
	public void event(SignChangeEvent e) {

		Player p = e.getPlayer();
		if (e.getLine(0).toLowerCase().contains("soup")) {
			int id = 0;
			for (String text : sign) {
				e.setLine(id, ExtraAPI.toText(text));
				id++;
			}
			p.sendMessage(message);
		}
	}
}
