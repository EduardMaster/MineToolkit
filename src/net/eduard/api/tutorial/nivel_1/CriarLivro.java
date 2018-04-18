package net.eduard.api.tutorial.nivel_1;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class CriarLivro {

	public static void criar(Player p) {

		ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta) item.getItemMeta();
		meta.setAuthor("Servidor");
		meta.setTitle("§bServidor §4Regras");
		meta.setPages("§aAEwaewawe", "");
		item.setItemMeta(meta);
		p.getInventory().addItem(item);

	}
}
