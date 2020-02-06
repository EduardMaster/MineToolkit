package net.eduard.api.server.kit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.click.PlayerInteract;
import net.eduard.api.lib.manager.CooldownManager;
import net.eduard.api.lib.modules.KitType;
import net.eduard.api.lib.storage.StorageAttributes;

public class KitAbility extends CooldownManager {

	private String name;
	private double price;
	private boolean showOnGui = true;
	private boolean enabled = true;
	private String disabled = "§6Habilidade desativada temporariamente!";
	private boolean activeCooldownOnPvP;
	private int times = 1;
	private ItemStack icon;
	@NotCopyable
	private transient PlayerInteract click;
	@NotCopyable
	private transient Map<Player, Integer> timesUsed = new HashMap<>();
	@StorageAttributes(reference = true)
	private List<KitAbility> kits = new ArrayList<>();
	private transient List<Player> players = new ArrayList<>();

	

	public List<KitAbility> getKits() {
		return kits;
	}

	public void setKits(List<KitAbility> kits) {
		this.kits = kits;
	}

	public KitAbility() {
		this("", KitType.DEFAULT);

	}

	public KitAbility(String name) {
		this(name, KitType.DEFAULT);

	}

	public KitAbility(String name, KitType type) {
		if (name.isEmpty()) {
			setName(getClass().getSimpleName());
		} else {
			setName(name);
		}
		REQUIRE_PERMISSION = name.toLowerCase().replace(" ", "");
	}

	public ItemStack add(ItemStack item) {
		GIVE_ITEMS.add(Mine.setName(item, "§b" + name));
		return item;
	}

	public ItemStack add(Material material) {
		return add(material, 0);
	}

	public ItemStack add(Material material, int data) {
		return add(new ItemStack(material, 1, (short) data));
	}

	@Override
	public boolean cooldown(Player player) {
		if (!enabled) {
			Mine.send(player, disabled);
			return false;
		}
		if (onCooldown(player)) {
			sendOnCooldown(player);
			return false;
		}
		int x = 0;
		if (timesUsed.containsKey(player)) {
			x = timesUsed.get(player);
		}
		x++;
		if (x == times) {
			setOnCooldown(player);
			sendStartCooldown(player);
			timesUsed.remove(player);
		} else {
			timesUsed.put(player, x);
		}
		return true;
	}

	@EventHandler
	public void event(EntityDamageByEntityEvent e) {
		if (activeCooldownOnPvP) {
			if (e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				if (hasKit(p)) {
					setOnCooldown(p);
				}
			}
		}
	}

	public ItemStack getIcon() {
		return icon;
	}

	public String getName() {
		return name;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public double getPrice() {
		return price;
	}

	public int getTimes() {
		return times;
	}

	public boolean hasKit(Player player) {
		return players.contains(player);
	}

	public boolean isActiveCooldownOnPvP() {
		return activeCooldownOnPvP;
	}

	public KitAbility setActiveCooldownOnPvP(boolean activeCooldownOnPvP) {
		this.activeCooldownOnPvP = activeCooldownOnPvP;
		return this;
	}

	public KitAbility setIcon(ItemStack icon) {
		this.icon = icon;
		return this;
	}

	public KitAbility setIcon(Material material, int data, String... lore) {
		icon = new ItemStack(material);
		Mine.setName(icon, "§6Kit " + name);
		Mine.setLore(icon, lore);
		Mine.addEnchant(icon, Enchantment.DURABILITY, 10);
		return this;
	}

	public KitAbility setIcon(Material material, String... lore) {
		return setIcon(material, 0, lore);
	}

	public KitAbility setName(String name) {
		this.name = name;
		return this;
	}

	public KitAbility setPlayers(ArrayList<Player> players) {
		this.players = players;
		return this;
	}

	public KitAbility setPrice(double price) {
		this.price = price;
		return this;
	}

	public KitAbility setTimes(int times) {
		this.times = times;
		return this;
	}

	public boolean isShowOnGui() {
		return showOnGui;
	}

	public void setShowOnGui(boolean showOnGui) {
		this.showOnGui = showOnGui;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public PlayerInteract getClick() {
		return click;
	}

	public void setClick(PlayerInteract click) {
		this.click = click;
	}

}
