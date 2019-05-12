package net.eduard.api.lib.storage.bukkit_storables;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.Storable;

//@StorageAttributes(auto=false)
public class ItemStackStorable implements Storable {

	@Override
	public Object newInstance() {
		return new ItemStack(Material.STONE);
	}

	@Override
	public Object restore(Map<String, Object> map) {
		int id = Extra.toInt(map.get("id"));
		int amount = Extra.toInt(map.get("amount"));
		int data = Extra.toInt(map.get("data"));
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(id, amount, (short) data);
		String name = Extra.toChatMessage((String) map.get("name"));
		if (!name.isEmpty()) {
			Mine.setName(item, name);
		}
		@SuppressWarnings("unchecked")
		List<String> lore = Extra.toMessages((List<Object>) map.get("lore"));
		if (!lore.isEmpty()) {
			Mine.setLore(item, lore);
		}
		String enchants = (String) map.get("enchants");
		if (!enchants.isEmpty()) {
			if (enchants.contains(", ")) {
				String[] split = enchants.split(", ");
				for (String enchs : split) {
					String[] sub = enchs.split("-");
					@SuppressWarnings("deprecation")
					Enchantment ench = Enchantment.getById(Extra.toInt(sub[0]));
					Integer level = Extra.toInt(sub[1]);
					item.addUnsafeEnchantment(ench, level);

				}
			} else {
				String[] split = enchants.split("-");
				@SuppressWarnings("deprecation")
				Enchantment ench = Enchantment.getById(Extra.toInt(split[0]));
				Integer level = Extra.toInt(split[1]);
				item.addUnsafeEnchantment(ench, level);

			}
		}
		return item;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void store(Map<String, Object> map, Object object) {
		if (object instanceof ItemStack) {
			ItemStack item = (ItemStack) object;
			map.remove("durability");
			map.remove("meta");
			map.remove("type");
			map.remove("handle");
			map.put("id", item.getTypeId());
			map.put("data", item.getDurability());
			map.put("amount", item.getAmount());
			map.put("name", Mine.getName(item));
			map.put("lore", Extra.toConfigMessages(Mine.getLore(item)));
			String enchants = "";
			if (item.getItemMeta().hasEnchants()) {
				StringBuilder str = new StringBuilder();
				int id = 0;
				try {
					for (Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
						if (id > 0)
							str.append(", ");
						Enchantment enchantment = entry.getKey();
						str.append(enchantment.getId() + "-" + entry.getValue());
						id++;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				enchants = str.toString();
			}
			map.put("enchants", enchants);
		}

	};

}
