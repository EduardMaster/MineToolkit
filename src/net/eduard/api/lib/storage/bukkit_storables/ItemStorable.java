package net.eduard.api.lib.storage.bukkit_storables;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.game.Item;
import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.Storable;

/**
 * Precisa arrumar
 * @author Eduard
 *
 */
public class ItemStorable implements Storable {

	/**
	 * id:data-qnt;enchId-enchData,enchId-enchData;nome;descriao1,descricao2
	 */
	@Override
	public Object restore(Object object) {
		if (object instanceof String) {
			String text = (String) object;

			try {
				String[] split = text.split(";");
				String[] splitData = split[0].split("-");
				Integer qnt = Mine.toInt(splitData[1]);
				String[] splitInfo = splitData[0].split(":");
				Integer id = Mine.toInt(splitInfo[0]);
				short data = Mine.toShort(splitInfo[1]);
				Item item = new Item();
				item.setId(id);
				item.setData(data);
				item.setAmount(qnt);
				if (split.length > 0) {
					if (split[1].contains(",")) {
						String[] enchs = split[1].split(",");
						for (String enchant : enchs) {
							String[] ench = enchant.split("-");
							Integer ench_id = Mine.toInt(ench[0]);
							Integer ench_level = Mine.toInt(ench[1]);
							item.getEnchants().put(ench_id, ench_level);
						}
					} else {
						if (!split[1].equals(" ")) {
							String[] ench = split[1].split("-");
							Integer ench_id = Mine.toInt(ench[0]);
							Integer ench_level = Mine.toInt(ench[1]);
							item.getEnchants().put(ench_id, ench_level);
						}

					}
				}
				if (split.length > 1) {
					String nome = split[2];
					if (!nome.equals(" ")) {
						item.setName(Extra.toChatMessage(nome));
					}
				}
				if (split.length > 2) {
					List<String> lista = new ArrayList<>();
					String descricao = split[3];
					if (descricao.contains(",")) {
						String[] lore = descricao.split(",");
						for (String line : lore) {
							lista.add(Extra.toChatMessage(line));
						}
					} else {
						if (!descricao.equals(" ")) {
							lista.add(descricao);
						}

					}
					item.setLore(lista);
				}
				return item;

			} catch (Exception e) {
				e.printStackTrace();
				return new Item(1);
			}

		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Object store(Object object) {
		if (object instanceof ItemStack) {
			ItemStack item = (ItemStack) object;
			StringBuilder textao = new StringBuilder();
			textao.append(item.getTypeId() + ":" + item.getDurability() + "-" + item.getAmount() + ";");
			ItemMeta meta = item.getItemMeta();
			if (meta != null) {

				if (meta.hasEnchants()) {
					boolean first = true;
					for (Entry<Enchantment, Integer> enchant : item.getItemMeta().getEnchants().entrySet()) {
						if (!first) {
							textao.append(",");
						} else
							first = false;
						textao.append(enchant.getKey().getId());
						textao.append("-");
						textao.append(enchant.getValue());
					}
				} else {
					textao.append(" ");
				}
				textao.append(";");
				if (item.getItemMeta().hasDisplayName()) {
					textao.append(item.getItemMeta().getDisplayName());
				} else {
					textao.append(" ");
				}
				textao.append(";");
				if (meta.hasLore()) {
					boolean first = true;
					for (String line : meta.getLore()) {
						if (!first) {
							textao.append(",");
						} else
							first = false;
						textao.append(line);
					}

				} else {
					textao.append(" ");
				}
				textao.append(";");
			}
			return textao.toString();
		}
		return null;
	}

}
