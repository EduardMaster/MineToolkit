package net.eduard.api.tutorial.nivel_3;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.setup.Mine;

public class KitsConfiguraveis {

	/**
	 * 277:0-1,0-1;1-1,
	 */
	@SuppressWarnings({ "unused", "null", "deprecation" })
	public void a() {
		YamlConfiguration config = null;

		ConfigurationSection itensSecao = config.getConfigurationSection("kits");
		// for ( String kitNome : itensSecao.getKeys(false)) {

		List<String> itens = itensSecao.getStringList("nomekit");

		for (String conteudo : itens) {
			if (conteudo.contains(",")) {
				String[] splitInicial = conteudo.split(",");

				String itemInfo = splitInicial[0];

				String encantamentos = splitInicial[1];

				ItemStack item = null;

				if (encantamentos.contains(";")) {

					String[] splitEncantamentos = encantamentos.split(";");

					for (String encantamento : splitEncantamentos) {

						String[] splitEncantamento = encantamento.split("-");

						Enchantment ench = Enchantment.getById(Integer.valueOf(splitEncantamento[0]));
						Integer level = Integer.valueOf(splitEncantamento[1]);
						item.addUnsafeEnchantment(ench, level);
						Mine.addEnchant(item, ench, level);

					}

				}
			}
			//additem
		}

		// }

	}
}
