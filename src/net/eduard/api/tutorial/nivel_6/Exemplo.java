package net.eduard.api.tutorial.nivel_6;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class Exemplo {
	
	public static Mercado mercadao = new Mercado();
	public static void abrir(Player p) {
		Inventory inv = Bukkit.createInventory(null, 6*9, "Cancelar Vendas");
		int id = 0;
		for (ProdutoMercado produto : mercadao.getProdutos()) {
			if (produto.getDono().equals(p.getName())) {
				inv.setItem(id, produto.getIconeDevolucao());
				id++;
			}
		}
		
		p.openInventory(inv);
		
	}
	@EventHandler
	public void inventory(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
//			Player p = (Player) e.getWhoClicked();
			
			if (e.getInventory().getName().equals("Cancelar Vendas")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null)return;
				for (ProdutoMercado produto : mercadao.getProdutos()) {
					if (produto.getIconeDevolucao().equals(e.getCurrentItem())) {
						
						mercadao.getProdutos().remove(produto);
						break;
					}
					
				}
				
				
			}
			
		}
	}
	
	

}
